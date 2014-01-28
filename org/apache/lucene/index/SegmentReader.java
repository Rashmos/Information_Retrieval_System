package org.apache.lucene.index;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.Lock.With;
import org.apache.lucene.util.BitVector;

final class SegmentReader extends IndexReader
{
  private boolean closeDirectory = false;
  private String segment;
  FieldInfos fieldInfos;
  private FieldsReader fieldsReader;
  TermInfosReader tis;
  BitVector deletedDocs = null;
  private boolean deletedDocsDirty = false;
  InputStream freqStream;
  InputStream proxStream;
  private Hashtable norms = new Hashtable();

  SegmentReader(SegmentInfo paramSegmentInfo, boolean paramBoolean)
    throws IOException
  {
    this(paramSegmentInfo);
    this.closeDirectory = paramBoolean;
  }

  SegmentReader(SegmentInfo paramSegmentInfo)
    throws IOException
  {
    super(paramSegmentInfo.dir);
    this.segment = paramSegmentInfo.name;
    this.fieldInfos = new FieldInfos(this.directory, this.segment + ".fnm");
    this.fieldsReader = new FieldsReader(this.directory, this.segment, this.fieldInfos);
    this.tis = new TermInfosReader(this.directory, this.segment, this.fieldInfos);
    if (hasDeletions(paramSegmentInfo))
      this.deletedDocs = new BitVector(this.directory, this.segment + ".del");
    this.freqStream = this.directory.openFile(this.segment + ".frq");
    this.proxStream = this.directory.openFile(this.segment + ".prx");
    openNorms();
  }

  final synchronized void doClose()
    throws IOException
  {
    if (this.deletedDocsDirty)
    {
      synchronized (this.directory)
      {
        new Lock.With(this.directory.makeLock("commit.lock"))
        {
          public Object doBody()
            throws IOException
          {
            SegmentReader.this.deletedDocs.write(SegmentReader.this.directory, SegmentReader.this.segment + ".tmp");
            SegmentReader.this.directory.renameFile(SegmentReader.this.segment + ".tmp", SegmentReader.this.segment + ".del");
            return null;
          }
        }
        .run();
      }
      this.deletedDocsDirty = false;
    }
    this.fieldsReader.close();
    this.tis.close();
    if (this.freqStream != null)
      this.freqStream.close();
    if (this.proxStream != null)
      this.proxStream.close();
    closeNorms();
    if (this.closeDirectory)
      this.directory.close();
  }

  static final boolean hasDeletions(SegmentInfo paramSegmentInfo)
    throws IOException
  {
    return paramSegmentInfo.dir.fileExists(paramSegmentInfo.name + ".del");
  }

  final synchronized void doDelete(int paramInt)
    throws IOException
  {
    if (this.deletedDocs == null)
      this.deletedDocs = new BitVector(maxDoc());
    this.deletedDocsDirty = true;
    this.deletedDocs.set(paramInt);
  }

  final Vector files()
    throws IOException
  {
    Vector localVector = new Vector(16);
    localVector.addElement(this.segment + ".fnm");
    localVector.addElement(this.segment + ".fdx");
    localVector.addElement(this.segment + ".fdt");
    localVector.addElement(this.segment + ".tii");
    localVector.addElement(this.segment + ".tis");
    localVector.addElement(this.segment + ".frq");
    localVector.addElement(this.segment + ".prx");
    if (this.directory.fileExists(this.segment + ".del"))
      localVector.addElement(this.segment + ".del");
    for (int i = 0; i < this.fieldInfos.size(); i++)
    {
      FieldInfo localFieldInfo = this.fieldInfos.fieldInfo(i);
      if (localFieldInfo.isIndexed)
        localVector.addElement(this.segment + ".f" + i);
    }
    return localVector;
  }

  public final TermEnum terms()
    throws IOException
  {
    return this.tis.terms();
  }

  public final TermEnum terms(Term paramTerm)
    throws IOException
  {
    return this.tis.terms(paramTerm);
  }

  public final synchronized Document document(int paramInt)
    throws IOException
  {
    if (isDeleted(paramInt))
      throw new IllegalArgumentException("attempt to access a deleted document");
    return this.fieldsReader.doc(paramInt);
  }

  public final synchronized boolean isDeleted(int paramInt)
  {
    return (this.deletedDocs != null) && (this.deletedDocs.get(paramInt));
  }

  public final TermDocs termDocs()
    throws IOException
  {
    return new SegmentTermDocs(this);
  }

  public final TermPositions termPositions()
    throws IOException
  {
    return new SegmentTermPositions(this);
  }

  public final int docFreq(Term paramTerm)
    throws IOException
  {
    TermInfo localTermInfo = this.tis.get(paramTerm);
    if (localTermInfo != null)
      return localTermInfo.docFreq;
    return 0;
  }

  public final int numDocs()
  {
    int i = maxDoc();
    if (this.deletedDocs != null)
      i -= this.deletedDocs.count();
    return i;
  }

  public final int maxDoc()
  {
    return this.fieldsReader.size();
  }

  public final byte[] norms(String paramString)
    throws IOException
  {
    Norm localNorm = (Norm)this.norms.get(paramString);
    if (localNorm == null)
      return null;
    if (localNorm.bytes == null)
    {
      byte[] arrayOfByte = new byte[maxDoc()];
      norms(paramString, arrayOfByte, 0);
      localNorm.bytes = arrayOfByte;
    }
    return localNorm.bytes;
  }

  final void norms(String paramString, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    InputStream localInputStream = normStream(paramString);
    if (localInputStream == null)
      return;
    try
    {
      localInputStream.readBytes(paramArrayOfByte, paramInt, maxDoc());
    }
    finally
    {
      localInputStream.close();
    }
  }

  final InputStream normStream(String paramString)
    throws IOException
  {
    Norm localNorm = (Norm)this.norms.get(paramString);
    if (localNorm == null)
      return null;
    InputStream localInputStream = (InputStream)localNorm.in.clone();
    localInputStream.seek(0L);
    return localInputStream;
  }

  private final void openNorms()
    throws IOException
  {
    for (int i = 0; i < this.fieldInfos.size(); i++)
    {
      FieldInfo localFieldInfo = this.fieldInfos.fieldInfo(i);
      if (localFieldInfo.isIndexed)
        this.norms.put(localFieldInfo.name, new Norm(this.directory.openFile(this.segment + ".f" + localFieldInfo.number)));
    }
  }

  private final void closeNorms()
    throws IOException
  {
    synchronized (this.norms)
    {
      Enumeration localEnumeration = this.norms.elements();
      while (localEnumeration.hasMoreElements())
      {
        Norm localNorm = (Norm)localEnumeration.nextElement();
        localNorm.in.close();
      }
    }
  }

  private static class Norm
  {
    public InputStream in;
    public byte[] bytes;

    public Norm(InputStream paramInputStream)
    {
      this.in = paramInputStream;
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentReader
 * JD-Core Version:    0.6.2
 */