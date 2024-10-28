package cn.xiangstudy.websocket.t1;

import cn.xiangstudy.utils.DateUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * @author zhangxiang
 * @date 2024-10-28 14:58
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channelGroup.forEach(ch -> {
            if(channel != ch){
                ch.writeAndFlush("[客户端]" + channel.remoteAddress() + "--->" + s);
            }else {
                ch.writeAndFlush("[我的]" + "--->" + s);
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Date now = new Date();
        String nowStr = DateUtils.dateToStr(now, "yyyy-MM-dd HH:mm:ss");
        channelGroup.writeAndFlush("[客户端]" + nowStr + channel.remoteAddress() + "上线");
        channelGroup.add(channel);
        System.out.println("[客户端]" + nowStr + channel.remoteAddress() + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Date now = new Date();
        String nowStr = DateUtils.dateToStr(now, "yyyy-MM-dd HH:mm:ss");
        channelGroup.writeAndFlush("[客户端]" + nowStr + channel.remoteAddress() + "离线");
        System.out.println("[客户端]" + nowStr + channel.remoteAddress() + "离线");
        System.out.println("剩余个数：" + channelGroup.size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

