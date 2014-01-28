package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.util.BitVector;

final class SegmentMergeInfo
{
  Term term;
  int base;
  SegmentTermEnum termEnum;
  SegmentReader reader;
  SegmentTermPositions postings;
  int[] docMap = null;

  SegmentMergeInfo(int paramInt, SegmentTermEnum paramSegmentTermEnum, SegmentReader paramSegmentReader)
    throws IOException
  {
    this.base = paramInt;
    this.reader = paramSegmentReader;
    this.termEnum = paramSegmentTermEnum;
    this.term = paramSegmentTermEnum.term();
    this.postings = new SegmentTermPositions(paramSegmentReader);
    if (this.reader.deletedDocs != null)
    {
      BitVector localBitVector = this.reader.deletedDocs;
      int i = this.reader.maxDoc();
      this.docMap = new int[i];
      int j = 0;
      for (int k = 0; k < i; k++)
        if (localBitVector.get(k))
          this.docMap[k] = -1;
        else
          this.docMap[k] = (j++);
    }
  }

  final boolean next()
    throws IOException
  {
    if (this.termEnum.next())
    {
      this.term = this.termEnum.term();
      return true;
    }
    this.term = null;
    return false;
  }

  final void close()
    throws IOException
  {
    this.termEnum.close();
    this.postings.close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentMergeInfo
 * JD-Core Version:    0.6.2
 */