package io.github.gagechan.server.http;

import io.github.gagechan.server.annotation.Bean;
import io.github.gagechan.server.config.AppConfigProperties;
import io.github.gagechan.server.ioc.BeanPostProcess;

import cn.hutool.core.date.DateUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Web app launcher.
 * @author GageChan
 * @version  : WebAppLauncher.java, v 0.1 2022年04月01 21:31 GageChan
 */
@Slf4j
@Bean
public class WebAppLauncher implements BeanPostProcess {

    /**
    * Start.
    */
    public void start() {
        int port = AppConfigProperties.getInstance().getPort();
        long start = System.currentTimeMillis();

        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            final ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).option(ChannelOption.SO_BACKLOG, 1024)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpServerCodec())
                            .addLast(new HttpObjectAggregator(65536))
                            .addLast(new RouteDispatcher());
                    }
                });

            final Channel ch = b.bind(port).sync().channel();
            log.info("***** Welcome To {} on port [{}], starting spend {}ms *****",
                AppConfigProperties.getInstance().getApplicationName(), port,
                DateUtil.spendMs(start));
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("start web app was failure", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void postProcess() {
        Thread thread = new Thread(this::start, "bootstrap-thread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
