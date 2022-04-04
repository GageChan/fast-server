package io.github.gagechan.server.http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.gagechan.server.config.AppConfigProperties;
import io.github.gagechan.server.ioc.UrlContainer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Route dispatcher.
 * @author GageChan
 * @version  : UrlDispatcher.java, v 0.1 2022年04月01 21:32 GageChan
 */
@Slf4j
public class RouteDispatcher extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                FullHttpRequest fullHttpRequest) throws Exception {
        HttpRequest request = new HttpRequest(fullHttpRequest);
        HttpResponse response = new HttpResponse(channelHandlerContext, request);
        String path = request.getPath();
        if (path.startsWith("/" + AppConfigProperties.getInstance().getStaticPrefix())) {
            response.renderStatic(path);
            response.send();
            return;
        }
        Class<?> controller = UrlContainer.getClazz(path);
        if (controller == null) {
            response.renderHtml("404");
        } else {
            try {
                Method method = controller.getMethod("doService", HttpRequest.class,
                    HttpResponse.class);
                method.invoke(controller.newInstance(), request, response);
            } catch (Throwable throwable) {
                log.error("executing controller occur error.", throwable);
                if (throwable instanceof InvocationTargetException) {
                    request.addAttribute("err",
                        ((InvocationTargetException) throwable).getTargetException().toString());
                } else {
                    request.addAttribute("err", throwable);
                }

                response.renderHtml("500");
            }
        }
        response.send();
    }

}
