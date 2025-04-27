package org.network.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NettyDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < 4) { // 읽을 수 있는 데이터가 최소 크기보다 작으면 대기
			return;
		}
		int packetLength = in.readInt(); // 패킷 길이 읽기
		if(in.readableBytes() < packetLength) { // 데이터 무결성 검증
			in.resetReaderIndex();
			return;
		}
		in.markReaderIndex();
		byte[] decode = new byte[packetLength];
		in.readBytes(decode); // 데이터를 바이트 배열로 읽기
		in.markReaderIndex();
		out.add(decode); // 디코딩된 데이터 추가
	}
}

