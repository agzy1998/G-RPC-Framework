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
     * @param o
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(MAGIC_NUMBER);
        if(o instanceof RPCRequest){
            byteBuf.writeInt(0);
        }else{
            byteBuf.writeInt(1);
        }
        byteBuf.writeInt(seriablizer.getCode());
        byte[] bytes = seriablizer.serialize(o);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
