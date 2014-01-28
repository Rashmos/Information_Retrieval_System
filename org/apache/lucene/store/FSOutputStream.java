package org.apache.lucene.store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

final class FSOutputStream extends OutputStream
{
  RandomAccessFile file = null;

  public FSOutputStream(File paramFile)
    throws IOException
  {
    this.file = new RandomAccessFile(paramFile, "rw");
  }

  public final void flushBuffer(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    this.file.write(paramArrayOfByte, 0, paramInt);
  }

  public final void close()
    throws IOException
  {
    super.close();
    this.file.close();
  }

  public final void seek(long paramLong)
    throws IOException
  {
    super.seek(paramLong);
    this.file.seek(paramLong);
  }

  public final long length()
    throws IOException
  {
    return this.file.length();
  }

  protected final void finalize()
    throws IOException
  {
    this.file.close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.FSOutputStream
 * JD-Core Version:    0.6.2
 */