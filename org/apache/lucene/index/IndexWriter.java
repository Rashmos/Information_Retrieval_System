package org.apache.lucene.index;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.Lock.With;
import org.apache.lucene.store.OutputStream;
import org.apache.lucene.store.RAMDirectory;

public final class IndexWriter
{
  private Directory directory;
  private Analyzer analyzer;
  private SegmentInfos segmentInfos = new SegmentInfos();
  private final Directory ramDirectory = new RAMDirectory();
  private Lock writeLock;
  public int maxFieldLength = 10000;
  public int mergeFactor = 10;
  public int maxMergeDocs = 2147483647;
  public PrintStream infoStream = null;

  public IndexWriter(String paramString, Analyzer paramAnalyzer, boolean paramBoolean)
    throws IOException
  {
    this(FSDirectory.getDirectory(paramString, paramBoolean), paramAnalyzer, paramBoolean);
  }

  public IndexWriter(File paramFile, Analyzer paramAnalyzer, boolean paramBoolean)
    throws IOException
  {
    this(FSDirectory.getDirectory(paramFile, paramBoolean), paramAnalyzer, paramBoolean);
  }

  public IndexWriter(Directory paramDirectory, Analyzer paramAnalyzer, boolean paramBoolean)
    throws IOException
  {
    this.directory = paramDirectory;
    this.analyzer = paramAnalyzer;
    Lock localLock = this.directory.makeLock("write.lock");
    if (!localLock.obtain())
      throw new IOException("Index locked for write: " + localLock);
    this.writeLock = localLock;
    synchronized (this.directory)
    {
      new Lock.With(paramBoolean)
      {
        private final boolean val$create;

        public Object doBody()
          throws IOException
        {
          if (this.val$create)
            IndexWriter.this.segmentInfos.write(IndexWriter.this.directory);
          else
            IndexWriter.this.segmentInfos.read(IndexWriter.this.directory);
          return null;
        }
      }
      .run();
    }
  }

  public final synchronized void close()
    throws IOException
  {
    flushRamSegments();
    this.ramDirectory.close();
    this.writeLock.release();
    this.writeLock = null;
    this.directory.close();
  }

  protected final void finalize()
    throws IOException
  {
    if (this.writeLock != null)
    {
      this.writeLock.release();
      this.writeLock = null;
    }
  }

  public final synchronized int docCount()
  {
    int i = 0;
    for (int j = 0; j < this.segmentInfos.size(); j++)
    {
      SegmentInfo localSegmentInfo = this.segmentInfos.info(j);
      i += localSegmentInfo.docCount;
    }
    return i;
  }

  public final void addDocument(Document paramDocument)
    throws IOException
  {
    DocumentWriter localDocumentWriter = new DocumentWriter(this.ramDirectory, this.analyzer, this.maxFieldLength);
    String str = newSegmentName();
    localDocumentWriter.addDocument(str, paramDocument);
    synchronized (this)
    {
      this.segmentInfos.addElement(new SegmentInfo(str, 1, this.ramDirectory));
      maybeMergeSegments();
    }
  }

  private final synchronized String newSegmentName()
  {
    return "_" + Integer.toString(this.segmentInfos.counter++, 36);
  }

  public final synchronized void optimize()
    throws IOException
  {
    flushRamSegments();
    while ((this.segmentInfos.size() > 1) || ((this.segmentInfos.size() == 1) && ((SegmentReader.hasDeletions(this.segmentInfos.info(0))) || (this.segmentInfos.info(0).dir != this.directory))))
    {
      int i = this.segmentInfos.size() - this.mergeFactor;
      mergeSegments(i < 0 ? 0 : i);
    }
  }

  public final synchronized void addIndexes(Directory[] paramArrayOfDirectory)
    throws IOException
  {
    optimize();
    for (int i = 0; i < paramArrayOfDirectory.length; i++)
    {
      SegmentInfos localSegmentInfos = new SegmentInfos();
      localSegmentInfos.read(paramArrayOfDirectory[i]);
      for (int j = 0; j < localSegmentInfos.size(); j++)
        this.segmentInfos.addElement(localSegmentInfos.info(j));
    }
    optimize();
  }

  private final void flushRamSegments()
    throws IOException
  {
    int i = this.segmentInfos.size() - 1;
    int j = 0;
    while ((i >= 0) && (this.segmentInfos.info(i).dir == this.ramDirectory))
    {
      j += this.segmentInfos.info(i).docCount;
      i--;
    }
    if ((i < 0) || (j + this.segmentInfos.info(i).docCount > this.mergeFactor) || (this.segmentInfos.info(this.segmentInfos.size() - 1).dir != this.ramDirectory))
      i++;
    if (i >= this.segmentInfos.size())
      return;
    mergeSegments(i);
  }

  private final void maybeMergeSegments()
    throws IOException
  {
    for (long l = this.mergeFactor; l <= this.maxMergeDocs; l *= this.mergeFactor)
    {
      int i = this.segmentInfos.size();
      int j = 0;
      do
      {
        SegmentInfo localSegmentInfo = this.segmentInfos.info(i);
        if (localSegmentInfo.docCount >= l)
          break;
        j += localSegmentInfo.docCount;
        i--;
      }
      while (i >= 0);
      if (j < l)
        break;
      mergeSegments(i + 1);
    }
  }

  private final void mergeSegments(int paramInt)
    throws IOException
  {
    String str = newSegmentName();
    int i = 0;
    if (this.infoStream != null)
      this.infoStream.print("merging segments");
    SegmentMerger localSegmentMerger = new SegmentMerger(this.directory, str);
    Vector localVector = new Vector();
    for (int j = paramInt; j < this.segmentInfos.size(); j++)
    {
      ??? = this.segmentInfos.info(j);
      if (this.infoStream != null)
        this.infoStream.print(" " + ((SegmentInfo)???).name + " (" + ((SegmentInfo)???).docCount + " docs)");
      SegmentReader localSegmentReader = new SegmentReader((SegmentInfo)???);
      localSegmentMerger.add(localSegmentReader);
      if ((localSegmentReader.directory == this.directory) || (localSegmentReader.directory == this.ramDirectory))
        localVector.addElement(localSegmentReader);
      i += ((SegmentInfo)???).docCount;
    }
    if (this.infoStream != null)
    {
      this.infoStream.println();
      this.infoStream.println(" into " + str + " (" + i + " docs)");
    }
    localSegmentMerger.merge();
    this.segmentInfos.setSize(paramInt);
    this.segmentInfos.addElement(new SegmentInfo(str, i, this.directory));
    synchronized (this.directory)
    {
      new Lock.With(localVector)
      {
        private final Vector val$segmentsToDelete;

        public Object doBody()
          throws IOException
        {
          IndexWriter.this.segmentInfos.write(IndexWriter.this.directory);
          IndexWriter.this.deleteSegments(this.val$segmentsToDelete);
          return null;
        }
      }
      .run();
    }
  }

  private final void deleteSegments(Vector paramVector)
    throws IOException
  {
    Vector localVector = new Vector();
    deleteFiles(readDeleteableFiles(), localVector);
    for (int i = 0; i < paramVector.size(); i++)
    {
      SegmentReader localSegmentReader = (SegmentReader)paramVector.elementAt(i);
      if (localSegmentReader.directory == this.directory)
        deleteFiles(localSegmentReader.files(), localVector);
      else
        deleteFiles(localSegmentReader.files(), localSegmentReader.directory);
    }
    writeDeleteableFiles(localVector);
  }

  private final void deleteFiles(Vector paramVector, Directory paramDirectory)
    throws IOException
  {
    for (int i = 0; i < paramVector.size(); i++)
      paramDirectory.deleteFile((String)paramVector.elementAt(i));
  }

  private final void deleteFiles(Vector paramVector1, Vector paramVector2)
    throws IOException
  {
    for (int i = 0; i < paramVector1.size(); i++)
    {
      String str = (String)paramVector1.elementAt(i);
      try
      {
        this.directory.deleteFile(str);
      }
      catch (IOException localIOException)
      {
        if (this.directory.fileExists(str))
        {
          if (this.infoStream != null)
            this.infoStream.println(localIOException.getMessage() + "; Will re-try later.");
          paramVector2.addElement(str);
        }
      }
    }
  }

  private final Vector readDeleteableFiles()
    throws IOException
  {
    Vector localVector = new Vector();
    if (!this.directory.fileExists("deletable"))
      return localVector;
    InputStream localInputStream = this.directory.openFile("deletable");
    try
    {
      for (int i = localInputStream.readInt(); i > 0; i--)
        localVector.addElement(localInputStream.readString());
    }
    finally
    {
      localInputStream.close();
    }
    return localVector;
  }

  private final void writeDeleteableFiles(Vector paramVector)
    throws IOException
  {
    OutputStream localOutputStream = this.directory.createFile("deleteable.new");
    try
    {
      localOutputStream.writeInt(paramVector.size());
      for (int i = 0; i < paramVector.size(); i++)
        localOutputStream.writeString((String)paramVector.elementAt(i));
    }
    finally
    {
      localOutputStream.close();
    }
    this.directory.renameFile("deleteable.new", "deletable");
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexWriter
 * JD-Core Version:    0.6.2
 */