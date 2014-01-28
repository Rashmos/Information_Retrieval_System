package org.apache.lucene.store;

import java.io.IOException;

public abstract class OutputStream
{
  static final int BUFFER_SIZE = 1024;
  private final byte[] buffer = new byte[1024];
  private long bufferStart = 0L;
  private int bufferPosition = 0;

  public final void writeByte(byte paramByte)
    throws IOException
  {
    if (this.bufferPosition >= 1024)
      flush();
    this.buffer[(this.bufferPosition++)] = paramByte;
  }

  public final void writeBytes(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    for (int i = 0; i < paramInt; i++)
      writeByte(paramArrayOfByte[i]);
  }

  public final void writeInt(int paramInt)
    throws IOException
  {
    writeByte((byte)(paramInt >> 24));
    writeByte((byte)(paramInt >> 16));
    writeByte((byte)(paramInt >> 8));
    writeByte((byte)paramInt);
  }

  public final void writeVInt(int paramInt)
    throws IOException
  {
    while ((paramInt & 0xFFFFFF80) != 0)
    {
      writeByte((byte)(paramInt & 0x7F | 0x80));
      paramInt >>>= 7;
    }
    writeByte((byte)paramInt);
  }

  public final void writeLong(long paramLong)
    throws IOException
  {
    writeInt((int)(paramLong >> 32));
    writeInt((int)paramLong);
  }

  public final void writeVLong(long paramLong)
    throws IOException
  {
    while ((paramLong & 0xFFFFFF80) != 0L)
    {
      writeByte((byte)(int)(paramLong & 0x7F | 0x80));
      paramLong >>>= 7;
    }
    writeByte((byte)(int)paramLong);
  }

  public final void writeString(String paramString)
    throws IOException
  {
    int i = paramString.length();
    writeVInt(i);
    writeChars(paramString, 0, i);
  }

  public final void writeChars(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = paramInt1 + paramInt2;
    for (int j = paramInt1; j < i; j++)
    {
      int k = paramString.charAt(j);
      if ((k >= 1) && (k <= 127))
      {
        writeByte((byte)k);
      }
      else if (((k >= 128) && (k <= 2047)) || (k == 0))
      {
        writeByte((byte)(0xC0 | k >> 6));
        writeByte((byte)(0x80 | k & 0x3F));
      }
      else
      {
        writeByte((byte)(0xE0 | k >>> 12));
        writeByte((byte)(0x80 | k >> 6 & 0x3F));
        writeByte((byte)(0x80 | k & 0x3F));
      }
    }
  }

  protected final void flush()
    throws IOException
  {
    flushBuffer(this.buffer, this.bufferPosition);
    this.bufferStart += this.bufferPosition;
    this.bufferPosition = 0;
  }

  protected abstract void flushBuffer(byte[] paramArrayOfByte, int paramInt)
    throws IOException;

  public void close()
    throws IOException
  {
    flush();
  }

  public final long getFilePointer()
    throws IOException
  {
    return this.bufferStart + this.bufferPosition;
  }

  public void seek(long paramLong)
    throws IOException
  {
    flush();
    this.bufferStart = paramLong;
  }

  public abstract long length()
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.OutputStream
 * JD-Core Version:    0.6.2
 */