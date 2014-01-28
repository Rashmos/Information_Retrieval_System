package org.apache.lucene.store;

import java.util.Vector;

final class RAMInputStream extends InputStream
  implements Cloneable
{
  RAMFile file;
  int pointer = 0;

  public RAMInputStream(RAMFile paramRAMFile)
  {
    this.file = paramRAMFile;
    this.length = this.file.length;
  }

  public final void readInternal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    int j = this.pointer;
    while (i != 0)
    {
      int k = j / 1024;
      int m = j % 1024;
      int n = 1024 - m;
      int i1 = n >= i ? i : n;
      byte[] arrayOfByte = (byte[])this.file.buffers.elementAt(k);
      System.arraycopy(arrayOfByte, m, paramArrayOfByte, paramInt1, i1);
      paramInt1 += i1;
      j += i1;
      i -= i1;
    }
    this.pointer += paramInt2;
  }

  public final void close()
  {
  }

  public final void seekInternal(long paramLong)
  {
    this.pointer = ((int)paramLong);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.RAMInputStream
 * JD-Core Version:    0.6.2
 */