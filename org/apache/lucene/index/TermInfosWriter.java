package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.OutputStream;

final class TermInfosWriter
{
  private FieldInfos fieldInfos;
  private OutputStream output;
  private Term lastTerm = new Term("", "");
  private TermInfo lastTi = new TermInfo();
  private int size = 0;
  static final int INDEX_INTERVAL = 128;
  private long lastIndexPointer = 0L;
  private boolean isIndex = false;
  private TermInfosWriter other = null;

  TermInfosWriter(Directory paramDirectory, String paramString, FieldInfos paramFieldInfos)
    throws IOException, SecurityException
  {
    initialize(paramDirectory, paramString, paramFieldInfos, false);
    this.other = new TermInfosWriter(paramDirectory, paramString, paramFieldInfos, true);
    this.other.other = this;
  }

  private TermInfosWriter(Directory paramDirectory, String paramString, FieldInfos paramFieldInfos, boolean paramBoolean)
    throws IOException
  {
    initialize(paramDirectory, paramString, paramFieldInfos, paramBoolean);
  }

  private void initialize(Directory paramDirectory, String paramString, FieldInfos paramFieldInfos, boolean paramBoolean)
    throws IOException
  {
    this.fieldInfos = paramFieldInfos;
    this.isIndex = paramBoolean;
    this.output = paramDirectory.createFile(paramString + (this.isIndex ? ".tii" : ".tis"));
    this.output.writeInt(0);
  }

  final void add(Term paramTerm, TermInfo paramTermInfo)
    throws IOException, SecurityException
  {
    if ((!this.isIndex) && (paramTerm.compareTo(this.lastTerm) <= 0))
      throw new IOException("term out of order");
    if (paramTermInfo.freqPointer < this.lastTi.freqPointer)
      throw new IOException("freqPointer out of order");
    if (paramTermInfo.proxPointer < this.lastTi.proxPointer)
      throw new IOException("proxPointer out of order");
    if ((!this.isIndex) && (this.size % 128 == 0))
      this.other.add(this.lastTerm, this.lastTi);
    writeTerm(paramTerm);
    this.output.writeVInt(paramTermInfo.docFreq);
    this.output.writeVLong(paramTermInfo.freqPointer - this.lastTi.freqPointer);
    this.output.writeVLong(paramTermInfo.proxPointer - this.lastTi.proxPointer);
    if (this.isIndex)
    {
      this.output.writeVLong(this.other.output.getFilePointer() - this.lastIndexPointer);
      this.lastIndexPointer = this.other.output.getFilePointer();
    }
    this.lastTi.set(paramTermInfo);
    this.size += 1;
  }

  private final void writeTerm(Term paramTerm)
    throws IOException
  {
    int i = stringDifference(this.lastTerm.text, paramTerm.text);
    int j = paramTerm.text.length() - i;
    this.output.writeVInt(i);
    this.output.writeVInt(j);
    this.output.writeChars(paramTerm.text, i, j);
    this.output.writeVInt(this.fieldInfos.fieldNumber(paramTerm.field));
    this.lastTerm = paramTerm;
  }

  private static final int stringDifference(String paramString1, String paramString2)
  {
    int i = paramString1.length();
    int j = paramString2.length();
    int k = i < j ? i : j;
    for (int m = 0; m < k; m++)
      if (paramString1.charAt(m) != paramString2.charAt(m))
        return m;
    return k;
  }

  final void close()
    throws IOException, SecurityException
  {
    this.output.seek(0L);
    this.output.writeInt(this.size);
    this.output.close();
    if (!this.isIndex)
      this.other.close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermInfosWriter
 * JD-Core Version:    0.6.2
 */