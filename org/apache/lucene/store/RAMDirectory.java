package org.apache.lucene.store;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public final class RAMDirectory extends Directory
{
  Hashtable files = new Hashtable();

  public final String[] list()
  {
    String[] arrayOfString = new String[this.files.size()];
    int i = 0;
    Enumeration localEnumeration = this.files.keys();
    while (localEnumeration.hasMoreElements())
      arrayOfString[(i++)] = ((String)localEnumeration.nextElement());
    return arrayOfString;
  }

  public final boolean fileExists(String paramString)
  {
    RAMFile localRAMFile = (RAMFile)this.files.get(paramString);
    return localRAMFile != null;
  }

  public final long fileModified(String paramString)
    throws IOException
  {
    RAMFile localRAMFile = (RAMFile)this.files.get(paramString);
    return localRAMFile.lastModified;
  }

  public final long fileLength(String paramString)
  {
    RAMFile localRAMFile = (RAMFile)this.files.get(paramString);
    return localRAMFile.length;
  }

  public final void deleteFile(String paramString)
  {
    this.files.remove(paramString);
  }

  public final void renameFile(String paramString1, String paramString2)
  {
    RAMFile localRAMFile = (RAMFile)this.files.get(paramString1);
    this.files.remove(paramString1);
    this.files.put(paramString2, localRAMFile);
  }

  public final OutputStream createFile(String paramString)
  {
    RAMFile localRAMFile = new RAMFile();
    this.files.put(paramString, localRAMFile);
    return new RAMOutputStream(localRAMFile);
  }

  public final InputStream openFile(String paramString)
  {
    RAMFile localRAMFile = (RAMFile)this.files.get(paramString);
    return new RAMInputStream(localRAMFile);
  }

  public final Lock makeLock(String paramString)
  {
    return new Lock()
    {
      private final String val$name;

      public boolean obtain()
        throws IOException
      {
        synchronized (RAMDirectory.this.files)
        {
          if (!RAMDirectory.this.fileExists(this.val$name))
          {
            RAMDirectory.this.createFile(this.val$name).close();
            bool = true;
            return bool;
          }
          boolean bool = false;
          return bool;
        }
      }

      public void release()
      {
        RAMDirectory.this.deleteFile(this.val$name);
      }
    };
  }

  public final void close()
  {
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.RAMDirectory
 * JD-Core Version:    0.6.2
 */