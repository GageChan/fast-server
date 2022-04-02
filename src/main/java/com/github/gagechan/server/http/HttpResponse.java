package com.github.gagechan.server.http;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import com.github.gagechan.server.config.AppConfigProperties;

import cn.hutool.core.io.resource.ResourceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Http response.
 *
 * @author : GageChan
 * @version : HttpResponse.java, v 0.1 2022年04月01 20:15 GageChan
 */
@Slf4j
public class HttpResponse {
    /**
     * The constant CONTENT_TYPE_TEXT.
     */
    public final static String          CONTENT_TYPE_TEXT = "text/plain";
    /**
     * The constant CONTENT_TYPE_HTML.
     */
    public final static String          CONTENT_TYPE_HTML = "text/html";
    /**
     * The constant CONTENT_TYPE_XML.
     */
    public final static String          CONTENT_TYPE_XML  = "text/xml";
    /**
     * The constant CONTENT_TYPE_JSON.
     */
    public final static String          CONTENT_TYPE_JSON = "application/json";

    public final static String          CONTENT_IMAGE     = "image/png";

    private final ChannelHandlerContext ctx;
    private final HttpRequest           request;

    private final HttpResponseStatus    status            = HttpResponseStatus.OK;
    private String                      contentType       = CONTENT_TYPE_HTML;
    private final HttpHeaders           headers           = new DefaultHttpHeaders();
    private final Set<Cookie>           cookies           = new HashSet<>();
    private Object                      content           = Unpooled.EMPTY_BUFFER;

    /**
     * Instantiates a new Http response.
     *
     * @param ctx     the ctx
     * @param request the request
     */
    public HttpResponse(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    /**
     * Build json response.
     *
     * @param json the json
     */
    public void buildJsonResponse(String json) {
        buildResponse(CONTENT_TYPE_JSON, json);
    }

    /**
     * Build html response.
     *
     * @param html the html
     */
    public void buildHtmlResponse(String html) {
        buildResponse(CONTENT_TYPE_HTML, html);
    }

    /**
     * Build response.
     *
     * @param contentType the content type
     * @param data        the data
     */
    public void buildResponse(String contentType, String data) {
        this.contentType = contentType;
        this.content = Unpooled.copiedBuffer(data, StandardCharsets.UTF_8);
    }

    public HttpResponse renderHtml(String path) {
        AppConfigProperties config = AppConfigProperties.getInstance();
        String prefix = config.getTemplatePrefix();
        if (!prefix.endsWith("/")) {
            prefix = prefix + "/";
        }
        if (prefix.startsWith("/")) {
            prefix = prefix.substring(1);
        }
        String suffix = config.getTemplateSuffix();
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        String html = "<html><h1>Hello World!</h1></html>";
        try {
            html = ResourceUtil.readUtf8Str(prefix + path + suffix);
        } catch (Exception e) {
            log.error("read html {} occur err.", prefix + path + suffix, e);
        }
        buildHtmlResponse(html);
        return this;
    }

    public HttpResponse renderStatic(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        byte[] bytes = new byte[0];
        try {
            bytes = ResourceUtil.readBytes(path);
        } catch (Exception e) {
            log.error("read bytes {} occur err.", path, e);
        }
        if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg")
            || path.endsWith(".webp") || path.endsWith(".icon") || path.endsWith(".ico")) {
            this.contentType = CONTENT_IMAGE;
        }
        this.content = Unpooled.copiedBuffer(bytes);
        return this;
    }

    /**
     * Send.
     */
    public void send() {
        if (request != null && request.isKeepAlive()) {
            setKeepAlive();
            ctx.writeAndFlush(this.buildFullHttpResponse());
        } else {
            ctx.writeAndFlush(this.buildFullHttpResponse());
        }
    }

    private FullHttpResponse buildFullHttpResponse() {
        final ByteBuf byteBuf = (ByteBuf) content;
        final FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
            request.getProtocolVersion(), status, byteBuf);

        // headers
        final HttpHeaders httpHeaders = fullHttpResponse.headers();
        fillHeadersAndCookies(httpHeaders);
        httpHeaders.set(HttpHeaderNames.CONTENT_LENGTH.toString(), byteBuf.readableBytes());

        return fullHttpResponse;
    }

    private void fillHeadersAndCookies(HttpHeaders httpHeaders) {
        httpHeaders.set(HttpHeaderNames.CONTENT_TYPE.toString(), contentType);
        httpHeaders.set(HttpHeaderNames.CONTENT_ENCODING.toString(), StandardCharsets.UTF_8);
        for (Cookie cookie : cookies) {
            httpHeaders.add(HttpHeaderNames.SET_COOKIE.toString(),
                ServerCookieEncoder.LAX.encode(cookie));
        }
    }

    /**
     * Add header.
     *
     * @param name  the name
     * @param value the value
     */
    public void addHeader(String name, Object value) {
        headers.add(name, value);
    }

    /**
     * Add cookie http response.
     *
     * @param cookie the cookie
     * @return the http response
     */
    private HttpResponse addCookie(Cookie cookie) {
        cookies.add(cookie);
        return this;
    }

    /**
     * Add cookie http response.
     *
     * @param name  the name
     * @param value the value
     * @return the http response
     */
    public HttpResponse addCookie(String name, String value) {
        return addCookie(new DefaultCookie(name, value));
    }

    /**
     * Add cookie http response.
     *
     * @param name            the name
     * @param value           the value
     * @param maxAgeInSeconds the max age in seconds
     * @param path            the path
     * @param domain          the domain
     * @return the http response
     */
    private HttpResponse addCookie(String name, String value, int maxAgeInSeconds, String path,
                                   String domain) {
        Cookie cookie = new DefaultCookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath(path);
        return addCookie(cookie);
    }

    /**
     * Add cookie http response.
     *
     * @param name            the name
     * @param value           the value
     * @param maxAgeInSeconds the max age in seconds
     * @return the http response
     */
    public HttpResponse addCookie(String name, String value, int maxAgeInSeconds) {
        return addCookie(name, value, maxAgeInSeconds, "/", null);
    }

    /**
     * Sets keep alive.
     */
    public void setKeepAlive() {
        addHeader(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());
    }

}
