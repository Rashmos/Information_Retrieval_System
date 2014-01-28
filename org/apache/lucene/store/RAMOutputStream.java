package org.apache.lucene.store;

import java.io.IOException;
import java.util.Vector;

final class RAMOutputStream extends OutputStream
{
  RAMFile file;
  int pointer = 0;

  public RAMOutputStream(RAMFile paramRAMFile)
  {
    this.file = paramRAMFile;
  }

  public final void flushBuffer(byte[] paramArrayOfByte, int paramInt)
  {
    int i = this.pointer / 1024;
    int j = this.pointer % 1024;
    int k = 1024 - j;
    int m = k >= paramInt ? paramInt : k;
    if (i == this.file.buffers.size())
      this.file.buffers.addElement(new byte[1024]);
    byte[] arrayOfByte = (byte[])this.file.buffers.elementAt(i);
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, j, m);
    if (m < paramInt)
    {
      int n = m;
      m = paramInt - m;
      i++;
      if (i == this.file.buffers.size())
        this.file.buffers.addElement(new byte[1024]);
      arrayOfByte = (byte[])this.file.buffers.elementAt(i);
      System.arraycopy(paramArrayOfByte, n, arrayOfByte, 0, m);
    }
    this.pointer += paramInt;
    if (this.pointer > this.file.length)
      this.file.length = this.pointer;
    this.file.lastModified = System.currentTimeMillis();
  }

  public final void close()
    throws IOException
  {
    super.close();
  }

  public final void seek(long paramLong)
    throws IOException
  {
    super.seek(paramLong);
    this.pointer = ((int)paramLong);
  }

  public final long length()
    throws IOException
  {
    return this.file.length;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.RAMOutputStream
 * JD-Core Version:    0.6.2
 */