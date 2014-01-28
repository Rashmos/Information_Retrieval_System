package org.apache.lucene.store;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import org.apache.lucene.util.Constants;

public final class FSDirectory extends Directory
{
  private static final Hashtable DIRECTORIES = new Hashtable();
  private File directory = null;
  private int refCount;

  public static FSDirectory getDirectory(String paramString, boolean paramBoolean)
    throws IOException
  {
    return getDirectory(new File(paramString), paramBoolean);
  }

  public static FSDirectory getDirectory(File paramFile, boolean paramBoolean)
    throws IOException
  {
    paramFile = new File(paramFile.getCanonicalPath());
    FSDirectory localFSDirectory1;
    synchronized (DIRECTORIES)
    {
      localFSDirectory1 = (FSDirectory)DIRECTORIES.get(paramFile);
      if (localFSDirectory1 == null)
      {
        localFSDirectory1 = new FSDirectory(paramFile, paramBoolean);
        DIRECTORIES.put(paramFile, localFSDirectory1);
      }
      else if (paramBoolean)
      {
        localFSDirectory1.create();
      }
    }
    synchronized (localFSDirectory1)
    {
      localFSDirectory1.refCount += 1;
    }
    return localFSDirectory1;
  }

  private FSDirectory(File paramFile, boolean paramBoolean)
    throws IOException
  {
    this.directory = paramFile;
    if (paramBoolean)
      create();
    if (!this.directory.isDirectory())
      throw new IOException(paramFile + " not a directory");
  }

  private synchronized void create()
    throws IOException
  {
    if ((!this.directory.exists()) && (!this.directory.mkdir()))
      throw new IOException("Cannot create directory: " + this.directory);
    String[] arrayOfString = this.directory.list();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      File localFile = new File(this.directory, arrayOfString[i]);
      if (!localFile.delete())
        throw new IOException("couldn't delete " + arrayOfString[i]);
    }
  }

  public final String[] list()
    throws IOException
  {
    return this.directory.list();
  }

  public final boolean fileExists(String paramString)
    throws IOException
  {
    File localFile = new File(this.directory, paramString);
    return localFile.exists();
  }

  public final long fileModified(String paramString)
    throws IOException
  {
    File localFile = new File(this.directory, paramString);
    return localFile.lastModified();
  }

  public static final long fileModified(File paramFile, String paramString)
    throws IOException
  {
    File localFile = new File(paramFile, paramString);
    return localFile.lastModified();
  }

  public final long fileLength(String paramString)
    throws IOException
  {
    File localFile = new File(this.directory, paramString);
    return localFile.length();
  }

  public final void deleteFile(String paramString)
    throws IOException
  {
    File localFile = new File(this.directory, paramString);
    if (!localFile.delete())
      throw new IOException("couldn't delete " + paramString);
  }

  public final synchronized void renameFile(String paramString1, String paramString2)
    throws IOException
  {
    File localFile1 = new File(this.directory, paramString1);
    File localFile2 = new File(this.directory, paramString2);
    if ((localFile2.exists()) && (!localFile2.delete()))
      throw new IOException("couldn't delete " + paramString2);
    if (!localFile1.renameTo(localFile2))
      throw new IOException("couldn't rename " + paramString1 + " to " + paramString2);
  }

  public final OutputStream createFile(String paramString)
    throws IOException
  {
    return new FSOutputStream(new File(this.directory, paramString));
  }

  public final InputStream openFile(String paramString)
    throws IOException
  {
    return new FSInputStream(new File(this.directory, paramString));
  }

  public final Lock makeLock(String paramString)
  {
    File localFile = new File(this.directory, paramString);
    return new Lock()
    {
      private final File val$lockFile;

      public boolean obtain()
        throws IOException
      {
        if (Constants.JAVA_1_1)
          return true;
        return this.val$lockFile.createNewFile();
      }

      public void release()
      {
        if (Constants.JAVA_1_1)
          return;
        this.val$lockFile.delete();
      }

      public String toString()
      {
        return "Lock@" + this.val$lockFile;
      }
    };
  }

  public final synchronized void close()
    throws IOException
  {
    if (--this.refCount <= 0)
      synchronized (DIRECTORIES)
      {
        DIRECTORIES.remove(this.directory);
      }
  }

  public String toString()
  {
    return "FSDirectory@" + this.directory;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.FSDirectory
 * JD-Core Version:    0.6.2
 */