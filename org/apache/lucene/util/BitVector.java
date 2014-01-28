package org.apache.lucene.util;

import java.io.IOException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.OutputStream;

public final class BitVector
{
  public byte[] bits;
  private int size;
  private int count = -1;
  private static final byte[] BYTE_COUNTS = { 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8 };

  public BitVector(int paramInt)
  {
    this.size = paramInt;
    this.bits = new byte[(this.size >> 3) + 1];
  }

  public final void set(int paramInt)
  {
    int tmp7_6 = (paramInt >> 3);
    byte[] tmp7_1 = this.bits;
    tmp7_1[tmp7_6] = ((byte)(tmp7_1[tmp7_6] | 1 << (paramInt & 0x7)));
    this.count = -1;
  }

  public final void clear(int paramInt)
  {
    int tmp7_6 = (paramInt >> 3);
    byte[] tmp7_1 = this.bits;
    tmp7_1[tmp7_6] = ((byte)(tmp7_1[tmp7_6] & (1 << (paramInt & 0x7) ^ 0xFFFFFFFF)));
    this.count = -1;
  }

  public final boolean get(int paramInt)
  {
    return (this.bits[(paramInt >> 3)] & 1 << (paramInt & 0x7)) != 0;
  }

  public final int size()
  {
    return this.size;
  }

  public final int count()
  {
    if (this.count == -1)
    {
      int i = 0;
      int j = this.bits.length;
      for (int k = 0; k < j; k++)
        i += BYTE_COUNTS[(this.bits[k] & 0xFF)];
      this.count = i;
    }
    return this.count;
  }

  public final void write(Directory paramDirectory, String paramString)
    throws IOException
  {
    OutputStream localOutputStream = paramDirectory.createFile(paramString);
    try
    {
      localOutputStream.writeInt(size());
      localOutputStream.writeInt(count());
      localOutputStream.writeBytes(this.bits, this.bits.length);
    }
    finally
    {
      localOutputStream.close();
    }
  }

  public BitVector(Directory paramDirectory, String paramString)
    throws IOException
  {
    InputStream localInputStream = paramDirectory.openFile(paramString);
    try
    {
      this.size = localInputStream.readInt();
      this.count = localInputStream.readInt();
      this.bits = new byte[(this.size >> 3) + 1];
      localInputStream.readBytes(this.bits, 0, this.bits.length);
    }
    finally
    {
      localInputStream.close();
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.BitVector
 * JD-Core Version:    0.6.2
 */