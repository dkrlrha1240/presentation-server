package org.network.netty.common;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class PacketReader {
	private final byte[] bf;
	private int position = 0;
	private final int length;
	private final int header;
	
	public PacketReader(byte[] bytes) {
		this.bf = bytes;
		this.length = bf.length;
		this.position = 0;
		this.header = readInt();
	}
	
	public final int getHeader() {
		return this.header;
	}

	
	public final byte[] getBytes() {
		return bf;
	}
	
	public final byte readByte() {
		if(position >= length) {
			return -1;
		}
		return bf[position++];
	}
	
	public final boolean readBoolean() {
		return readByte() == 1;
	}
	
	public final int readInt() {
        final byte byte1 = readByte();
        final byte byte2 = readByte();
        final byte byte3 = readByte();
        final byte byte4 = readByte();
        byte[] bytes = {byte4, byte3, byte2, byte1};
        return ByteBuffer.wrap(bytes).getInt();
	}
	
	public final long readLong() {
        final byte byte1 = readByte();
        final byte byte2 = readByte();
        final byte byte3 = readByte();
        final byte byte4 = readByte();
        final byte byte5 = readByte();
        final byte byte6 = readByte();
        final byte byte7 = readByte();
        final byte byte8 = readByte();
        byte[] bytes = {byte8, byte7, byte6, byte5, byte4, byte3, byte2, byte1};
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
	}
	
	public final String readString() {
		int n = readInt();
        byte[] ret = new byte[n];
        for (int x = 0; x < n; x++) {
            ret[x] = readByte();
        }
        return new String(ret, StandardCharsets.UTF_8);
	}

	
	
}
