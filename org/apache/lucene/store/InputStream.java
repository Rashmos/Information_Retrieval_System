package org.apache.lucene.store;

import java.io.IOException;

public abstract class InputStream
  implements Cloneable
{
  static final int BUFFER_SIZE = 1024;
  private byte[] buffer;
  private char[] chars;
  private long bufferStart = 0L;
  private int bufferLength = 0;
  private int bufferPosition = 0;
  protected long length;

  public final byte readByte()
    throws IOException
  {
    if (this.bufferPosition >= this.bufferLength)
      refill();
    return this.buffer[(this.bufferPosition++)];
  }

  public final void readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 1024)
    {
      for (int i = 0; i < paramInt2; i++)
        paramArrayOfByte[(i + paramInt1)] = readByte();
    }
    else
    {
      long l = getFilePointer();
      seekInternal(l);
      readInternal(paramArrayOfByte, paramInt1, paramInt2);
      this.bufferStart = (l + paramInt2);
      this.bufferPosition = 0;
      this.bufferLength = 0;
    }
  }

  public final int readInt()
    throws IOException
  {
    return (readByte() & 0xFF) << 24 | (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 8 | readByte() & 0xFF;
  }

  public final int readVInt()
    throws IOException
  {
    int i = readByte();
    int j = i & 0x7F;
    for (int k = 7; (i & 0x80) != 0; k += 7)
    {
      i = readByte();
      j |= (i & 0x7F) << k;
    }
    return j;
  }

  public final long readLong()
    throws IOException
  {
    return readInt() << 32 | readInt() & 0xFFFFFFFF;
  }

  public final long readVLong()
    throws IOException
  {
    int i = readByte();
    long l = i & 0x7F;
    for (int j = 7; (i & 0x80) != 0; j += 7)
    {
      i = readByte();
      l |= (i & 0x7F) << j;
    }
    return l;
  }

  public final String readString()
    throws IOException
  {
    int i = readVInt();
    if ((this.chars == null) || (i > this.chars.length))
      this.chars = new char[i];
    readChars(this.chars, 0, i);
    return new String(this.chars, 0, i);
  }

  public final void readChars(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = paramInt1 + paramInt2;
    for (int j = paramInt1; j < i; j++)
    {
      int k = readByte();
      if ((k & 0x80) == 0)
        paramArrayOfChar[j] = ((char)(k & 0x7F));
      else if ((k & 0xE0) != 224)
        paramArrayOfChar[j] = ((char)((k & 0x1F) << 6 | readByte() & 0x3F));
      else
        paramArrayOfChar[j] = ((char)((k & 0xF) << 12 | (readByte() & 0x3F) << 6 | readByte() & 0x3F));
    }
  }

  protected final void refill()
    throws IOException
  {
    long l1 = this.bufferStart + this.bufferPosition;
    long l2 = l1 + 1024L;
    if (l2 > this.length)
      l2 = this.length;
    this.bufferLength = ((int)(l2 - l1));
    if (this.bufferLength == 0)
      throw new IOException("read past EOF");
    if (this.buffer == null)
      this.buffer = new byte[1024];
    readInternal(this.buffer, 0, this.bufferLength);
    this.bufferStart = l1;
    this.bufferPosition = 0;
  }

  protected abstract void readInternal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void close()
    throws IOException;

  public final long getFilePointer()
  {
    return this.bufferStart + this.bufferPosition;
  }

  public final void seek(long paramLong)
    throws IOException
  {
    if ((paramLong >= this.bufferStart) && (paramLong < this.bufferStart + this.bufferLength))
    {
      this.bufferPosition = ((int)(paramLong - this.bufferStart));
    }
    else
    {
      this.bufferStart = paramLong;
      this.bufferPosition = 0;
      this.bufferLength = 0;
      seekInternal(paramLong);
    }
  }

  protected abstract void seekInternal(long paramLong)
    throws IOException;

  public final long length()
  {
    return this.length;
  }

  public Object clone()
  {
    InputStream localInputStream = null;
    try
    {
      localInputStream = (InputStream)super.clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
    }
    if (this.buffer != null)
    {
      localInputStream.buffer = new byte[1024];
      System.arraycopy(this.buffer, 0, localInputStream.buffer, 0, this.bufferLength);
    }
    localInputStream.chars = null;
    return localInputStream;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.InputStream
 * JD-Core Version:    0.6.2
 */