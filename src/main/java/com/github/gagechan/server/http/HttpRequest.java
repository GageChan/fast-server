package com.github.gagechan.server.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Http request.
 * @author  : GageChan
 * @version  : HttpRequest.java, v 0.1 2022年04月01 20:14 GageChan
 */
@Slf4j
public class HttpRequest {

    /**
     * The Request.
     */
    private final FullHttpRequest     request;
    /**
     * The Path.
     */
    private final String              path;
    /**
     * The Method.
     */
    private final HttpMethod          method;
    /**
     * The Headers.
     */
    private final Map<String, String> headers    = new HashMap<>();
    /**
     * The Params.
     */
    private final Map<String, Object> params     = new HashMap<>();
    /**
     * The Cookies.
     */
    private final Map<String, Cookie> cookies    = new HashMap<>();

    /**
     * The Attributes.
     */
    private final Map<String, Object> attributes = new HashMap<>();

    private byte[]                    body;

    /**
     * Instantiates a new Http request.
     *
     * @param request the request
    */
    public HttpRequest(FullHttpRequest request) {
        this.request = request;
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        this.path = decoder.rawPath();
        this.method = request.method();
        parseHeaders();
        parseParam(decoder);
    }

    private void parseParam(QueryStringDecoder decoder) {
        HttpMethod httpMethod = request.method();
        if (httpMethod.equals(HttpMethod.GET)) {
            parseGetParam(decoder);
        } else if (httpMethod.equals(HttpMethod.POST)) {
            parsePostParam();
        }
    }

    private void parseGetParam(QueryStringDecoder decoder) {
        for (Map.Entry<String, List<String>> entry : decoder.parameters().entrySet()) {
            List<String> values = entry.getValue();
            this.params.put(entry.getKey(), values.size() > 0 ? values.get(0) : null);
        }
    }

    private void parsePostParam() {
        if (this.headers.getOrDefault("Content-Type", HttpResponse.CONTENT_TYPE_HTML)
            .equals(HttpResponse.CONTENT_TYPE_JSON)) {
            ByteBuf content = request.content();
            body = new byte[content.readableBytes()];
            content.readBytes(body);
            return;
        }
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
        for (InterfaceHttpData data : decoder.getBodyHttpDatas()) {
            final InterfaceHttpData.HttpDataType dataType = data.getHttpDataType();
            if (dataType == InterfaceHttpData.HttpDataType.Attribute) {
                //普通参数
                Attribute attribute = (Attribute) data;
                try {
                    this.params.put(attribute.getName(), attribute.getValue());
                } catch (IOException e) {
                    log.error("parse post param err.", e);
                }
            } else {
                log.error("can not parse param, {}", data);
            }
        }
    }

    private void parseHeaders() {
        for (Map.Entry<String, String> entry : request.headers()) {
            this.headers.put(entry.getKey(), entry.getValue());
        }

        String cookieString = this.headers.get(HttpHeaderNames.COOKIE.toString());
        if (StrUtil.isNotBlank(cookieString)) {
            final Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(cookieString);
            for (Cookie cookie : cookies) {
                this.cookies.put(cookie.name(), cookie);
            }
        }
    }

    /**
    * Gets attribute.
    *
    * @param key the key
    * @return the attribute
    */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
    * Add attribute http request.
    *
    * @param key the key
    * @param value the value
    * @return the http request
    */
    public HttpRequest addAttribute(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    /**
    * Is keep alive boolean.
    *
    * @return the boolean
    */
    public boolean isKeepAlive() {
        final String connectionHeader = getHeader(HttpHeaderNames.CONNECTION.toString());
        // 无论任何版本Connection为close时都关闭连接
        if (HttpHeaderValues.CLOSE.toString().equalsIgnoreCase(connectionHeader)) {
            return false;
        }

        // HTTP/1.0只有Connection为Keep-Alive时才会保持连接
        if (HttpVersion.HTTP_1_0.equals(getProtocolVersion())) {
            if (!HttpHeaderValues.KEEP_ALIVE.toString().equalsIgnoreCase(connectionHeader)) {
                return false;
            }
        }
        // HTTP/1.1默认打开Keep-Alive
        return true;
    }

    /**
    * Gets protocol version.
    *
    * @return the protocol version
    */
    public HttpVersion getProtocolVersion() {
        return request.protocolVersion();
    }

    /**
     * Gets path.
     *
     * @return the path
    */
    public String getPath() {
        return path;
    }

    /**
    * Get body byte [ ].
    *
    * @return the byte [ ]
    */
    public byte[] getBody() {
        return body;
    }

    /**
     * Gets header.
     *
     * @param key the key
    * @return the header
    */
    public String getHeader(String key) {
        return headers.get(key);
    }

    /**
     * Gets params.
     *
     * @param key the key
    * @return the params
    */
    public Object getParam(String key) {
        return params.get(key);
    }

    /**
     * Gets cookie.
     *
     * @param key the key
    * @return the cookie
    */
    public Cookie getCookie(String key) {
        return cookies.get(key);
    }

    /**
     * Method http method.
     *
     * @return the http method
    */
    public HttpMethod method() {
        return method;
    }
}
