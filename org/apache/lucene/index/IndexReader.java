package org.apache.lucene.index;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.Lock.With;

public abstract class IndexReader
{
  Directory directory;
  private Lock writeLock;

  protected IndexReader(Directory paramDirectory)
  {
    this.directory = paramDirectory;
  }

  public static IndexReader open(String paramString)
    throws IOException
  {
    return open(FSDirectory.getDirectory(paramString, false));
  }

  public static IndexReader open(File paramFile)
    throws IOException
  {
    return open(FSDirectory.getDirectory(paramFile, false));
  }

  public static IndexReader open(Directory paramDirectory)
    throws IOException
  {
    synchronized (paramDirectory)
    {
      IndexReader localIndexReader = (IndexReader)new Lock.With(paramDirectory)
      {
        private final Directory val$directory;

        public Object doBody()
          throws IOException
        {
          SegmentInfos localSegmentInfos = new SegmentInfos();
          localSegmentInfos.read(this.val$directory);
          if (localSegmentInfos.size() == 1)
            return new SegmentReader(localSegmentInfos.info(0), true);
          SegmentReader[] arrayOfSegmentReader = new SegmentReader[localSegmentInfos.size()];
          for (int i = 0; i < localSegmentInfos.size(); i++)
            arrayOfSegmentReader[i] = new SegmentReader(localSegmentInfos.info(i), i == localSegmentInfos.size() - 1);
          return new SegmentsReader(this.val$directory, arrayOfSegmentReader);
        }
      }
      .run();
      return localIndexReader;
    }
  }

  public static long lastModified(String paramString)
    throws IOException
  {
    return lastModified(new File(paramString));
  }

  public static long lastModified(File paramFile)
    throws IOException
  {
    return FSDirectory.fileModified(paramFile, "segments");
  }

  public static long lastModified(Directory paramDirectory)
    throws IOException
  {
    return paramDirectory.fileModified("segments");
  }

  public static boolean indexExists(String paramString)
  {
    return new File(paramString, "segments").exists();
  }

  public static boolean indexExists(File paramFile)
  {
    return new File(paramFile, "segments").exists();
  }

  public static boolean indexExists(Directory paramDirectory)
    throws IOException
  {
    return paramDirectory.fileExists("segments");
  }

  public abstract int numDocs();

  public abstract int maxDoc();

  public abstract Document document(int paramInt)
    throws IOException;

  public abstract boolean isDeleted(int paramInt);

  public abstract byte[] norms(String paramString)
    throws IOException;

  public abstract TermEnum terms()
    throws IOException;

  public abstract TermEnum terms(Term paramTerm)
    throws IOException;

  public abstract int docFreq(Term paramTerm)
    throws IOException;

  public TermDocs termDocs(Term paramTerm)
    throws IOException
  {
    TermDocs localTermDocs = termDocs();
    localTermDocs.seek(paramTerm);
    return localTermDocs;
  }

  public abstract TermDocs termDocs()
    throws IOException;

  public TermPositions termPositions(Term paramTerm)
    throws IOException
  {
    TermPositions localTermPositions = termPositions();
    localTermPositions.seek(paramTerm);
    return localTermPositions;
  }

  public abstract TermPositions termPositions()
    throws IOException;

  public final synchronized void delete(int paramInt)
    throws IOException
  {
    if (this.writeLock == null)
    {
      Lock localLock = this.directory.makeLock("write.lock");
      if (!localLock.obtain())
        throw new IOException("Index locked for write: " + localLock);
      this.writeLock = localLock;
    }
    doDelete(paramInt);
  }

  abstract void doDelete(int paramInt)
    throws IOException;

  public final int delete(Term paramTerm)
    throws IOException
  {
    TermDocs localTermDocs = termDocs(paramTerm);
    if (localTermDocs == null)
      return 0;
    int i = 0;
    try
    {
      while (localTermDocs.next())
      {
        delete(localTermDocs.doc());
        i++;
      }
    }
    finally
    {
      localTermDocs.close();
    }
    return i;
  }

  public final synchronized void close()
    throws IOException
  {
    doClose();
    if (this.writeLock != null)
    {
      this.writeLock.release();
      this.writeLock = null;
    }
  }

  abstract void doClose()
    throws IOException;

  protected final void finalize()
    throws IOException
  {
    if (this.writeLock != null)
    {
      this.writeLock.release();
      this.writeLock = null;
    }
  }

  public static boolean isLocked(Directory paramDirectory)
    throws IOException
  {
    return paramDirectory.fileExists("write.lock");
  }

  public static boolean isLocked(String paramString)
    throws IOException
  {
    return new File(paramString, "write.lock").exists();
  }

  public static void unlock(Directory paramDirectory)
    throws IOException
  {
    paramDirectory.deleteFile("write.lock");
    paramDirectory.deleteFile("commit.lock");
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexReader
 * JD-Core Version:    0.6.2
 */