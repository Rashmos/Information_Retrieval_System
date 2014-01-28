package org.apache.lucene.store;

import java.io.IOException;

public abstract class Directory
{
  public abstract String[] list()
    throws IOException, SecurityException;

  public abstract boolean fileExists(String paramString)
    throws IOException, SecurityException;

  public abstract long fileModified(String paramString)
    throws IOException, SecurityException;

  public abstract void deleteFile(String paramString)
    throws IOException, SecurityException;

  public abstract void renameFile(String paramString1, String paramString2)
    throws IOException, SecurityException;

  public abstract long fileLength(String paramString)
    throws IOException, SecurityException;

  public abstract OutputStream createFile(String paramString)
    throws IOException, SecurityException;

  public abstract InputStream openFile(String paramString)
    throws IOException, SecurityException;

  public abstract Lock makeLock(String paramString);

  public abstract void close()
    throws IOException, SecurityException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.Directory
 * JD-Core Version:    0.6.2
 */