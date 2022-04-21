package com.g.rpc.transport;

import com.g.rpc.entity.RPCRequest;
import com.g.rpc.entity.RPCResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CommonDecoder extends ReplayingDecoder {
    public static final int MAGIC_NUMBER = 250;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magicNumber = byteBuf.readInt();
        if(magicNumber != MAGIC_NUMBER){
            log.error("magic number not match {} - {}", magicNumber, MAGIC_NUMBER);
            return ;
        }
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if(packageCode == 0){
            packageClass = RPCRequest.class;
        }else if(packageCode == 1){
            packageClass = RPCResponse.class;
        }else{
            log.error("package code not match");
            return;
        }
        int serializerCode = byteBuf.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null){
            log.error("serializerCode not match {}" , serializerCode);
            return;
        }
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        log.info(String.valueOf(packageClass));
        Object obj = serializer.deserialize(bytes, packageClass);
        list.add(obj);
    }
}
