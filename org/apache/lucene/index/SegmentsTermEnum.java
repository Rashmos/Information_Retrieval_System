package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.util.PriorityQueue;

class SegmentsTermEnum extends TermEnum
{
  private SegmentMergeQueue queue;
  private Term term;
  private int docFreq;

  SegmentsTermEnum(SegmentReader[] paramArrayOfSegmentReader, int[] paramArrayOfInt, Term paramTerm)
    throws IOException
  {
    this.queue = new SegmentMergeQueue(paramArrayOfSegmentReader.length);
    Object localObject;
    for (int i = 0; i < paramArrayOfSegmentReader.length; i++)
    {
      localObject = paramArrayOfSegmentReader[i];
      SegmentTermEnum localSegmentTermEnum;
      if (paramTerm != null)
        localSegmentTermEnum = (SegmentTermEnum)((SegmentReader)localObject).terms(paramTerm);
      else
        localSegmentTermEnum = (SegmentTermEnum)((SegmentReader)localObject).terms();
      SegmentMergeInfo localSegmentMergeInfo = new SegmentMergeInfo(paramArrayOfInt[i], localSegmentTermEnum, (SegmentReader)localObject);
      if ((localSegmentTermEnum.term() != null ? 1 : paramTerm == null ? localSegmentMergeInfo.next() : 0) != 0)
        this.queue.put(localSegmentMergeInfo);
      else
        localSegmentMergeInfo.close();
    }
    if ((paramTerm != null) && (this.queue.size() > 0))
    {
      localObject = (SegmentMergeInfo)this.queue.top();
      this.term = ((SegmentMergeInfo)localObject).termEnum.term();
      this.docFreq = ((SegmentMergeInfo)localObject).termEnum.docFreq();
    }
  }

  public final boolean next()
    throws IOException
  {
    SegmentMergeInfo localSegmentMergeInfo = (SegmentMergeInfo)this.queue.top();
    if (localSegmentMergeInfo == null)
    {
      this.term = null;
      return false;
    }
    this.term = localSegmentMergeInfo.term;
    this.docFreq = 0;
    while ((localSegmentMergeInfo != null) && (this.term.compareTo(localSegmentMergeInfo.term) == 0))
    {
      this.queue.pop();
      this.docFreq += localSegmentMergeInfo.termEnum.docFreq();
      if (localSegmentMergeInfo.next())
        this.queue.put(localSegmentMergeInfo);
      else
        localSegmentMergeInfo.close();
      localSegmentMergeInfo = (SegmentMergeInfo)this.queue.top();
    }
    return true;
  }

  public final Term term()
  {
    return this.term;
  }

  public final int docFreq()
  {
    return this.docFreq;
  }

  public final void close()
    throws IOException
  {
    this.queue.close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentsTermEnum
 * JD-Core Version:    0.6.2
 */