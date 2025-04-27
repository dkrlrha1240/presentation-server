package org.network.netty.common;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;


public class PacketWriter {
	private final ByteArrayOutputStream baos;
	private final int header;
	private int length;

	public PacketWriter(final int header, final int size) {
		this.baos = new ByteArrayOutputStream(size);
		this.header = header;
		this.writeInt(header);
	}

	public PacketWriter(int header) {
		this(header, 32);
	}

	public int getHeader() {
		return this.header;
	}

	public int getLength() {
		return this.length;
	}

	public final byte[] getPacket() {
		ByteArrayOutputStream tmp = new ByteArrayOutputStream(32);
		byte[] bytes = intToByteArray(this.length);
		tmp.write(bytes[0]);
		tmp.write(bytes[1]);
		tmp.write(bytes[2]);
		tmp.write(bytes[3]);
		byte[] org = baos.toByteArray();
        for (byte b : org) {
            tmp.write(b);
        }
		return tmp.toByteArray();
	}

	public final void write(byte b) {
		length++;
		baos.write(b);
	}

	public final void write(int b) {
		this.write((byte) b);
	}

	public final void write(byte[] b) {
		for (int i = 0; i < b.length; i++)
			write(b[i]);
	}

	public final void writeBoolean(boolean bool) {
		this.write(bool ? 1 : 0);
	}

	public final void writeString(String s) {
        writeInt(s.getBytes(StandardCharsets.UTF_8).length);
        write(s.getBytes(StandardCharsets.UTF_8));
    }

	public final void writeInt(final int i) {
		if (i != -88888) {
			write((byte) (i & 0xFF));
			write((byte) ((i >>> 8) & 0xFF));
			write((byte) ((i >>> 16) & 0xFF));
			write((byte) ((i >>> 24) & 0xFF));
		}
	}

	public void writeInt(long i) {
		write((byte) (i & 0xFF));
		write((byte) ((i >>> 8) & 0xFF));
		write((byte) ((i >>> 16) & 0xFF));
		write((byte) ((i >>> 24) & 0xFF));
	}

	public final void writeLong(final long l) {
		write((byte) (l & 0xFF));
		write((byte) ((l >>> 8) & 0xFF));
		write((byte) ((l >>> 16) & 0xFF));
		write((byte) ((l >>> 24) & 0xFF));
		write((byte) ((l >>> 32) & 0xFF));
		write((byte) ((l >>> 40) & 0xFF));
		write((byte) ((l >>> 48) & 0xFF));
		write((byte) ((l >>> 56) & 0xFF));
	}

	public final void writeDouble(double d) {
		writeLong(Double.doubleToLongBits(d));
	}
	
	public static byte[] intToByteArray(int i)
	{
		  byte[] result = new byte[4];
		  result[0] = (byte) (i >> 24);
		  result[1] = (byte) (i >> 16);
		  result[2] = (byte) (i >> 8);
		  result[3] = (byte) (i /*>> 0*/);
		  return result;
	}
}
