package org.apache.lucene.store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

final class FSInputStream extends InputStream
{
  Descriptor file = null;
  boolean isClone;

  public FSInputStream(File paramFile)
    throws IOException
  {
    this.file = new Descriptor(paramFile, "r");
    this.length = this.file.length();
  }

  protected final void readInternal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    synchronized (this.file)
    {
      long l = getFilePointer();
      if (l != this.file.position)
      {
        this.file.seek(l);
        this.file.position = l;
      }
      int i = 0;
      do
      {
        int j = this.file.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
        if (j == -1)
          throw new IOException("read past EOF");
        this.file.position += j;
        i += j;
      }
      while (i < paramInt2);
    }
  }

  public final void close()
    throws IOException
  {
    if (!this.isClone)
      this.file.close();
  }

  protected final void seekInternal(long paramLong)
    throws IOException
  {
  }

  protected final void finalize()
    throws IOException
  {
    close();
  }

  public Object clone()
  {
    FSInputStream localFSInputStream = (FSInputStream)super.clone();
    localFSInputStream.isClone = true;
    return localFSInputStream;
  }

  private class Descriptor extends RandomAccessFile
  {
    public long position;

    public Descriptor(File paramString, String arg3)
      throws IOException
    {
      super(str);
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.FSInputStream
 * JD-Core Version:    0.6.2
 */