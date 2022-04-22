package com.g.rpc.transport;

import com.g.rpc.entity.RPCRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CommonEncoder extends MessageToByteEncoder {
    public static final int MAGIC_NUMBER = 250;
    private final CommonSerializer seriablizer;

    public CommonEncoder (CommonSerializer seriablizer){
        this.seriablizer = seriablizer;
    }

    /**
     * 编码格式: MAGIC_NUMBER : 4 bytes | Packge_TYPE : 4 bytes | Serializer_TYPE : 4 bytes | Data_Length : 4 bytes | Data_Bytes: Data.length
     * @param channelHandlerContext
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUMBER);
        if(msg instanceof RPCRequest){
            out.writeInt(0);
        }else{
            out.writeInt(1);
        }
        out.writeInt(seriablizer.getCode());
        byte[] bytes = seriablizer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
