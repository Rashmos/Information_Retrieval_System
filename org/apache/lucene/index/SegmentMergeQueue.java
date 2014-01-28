package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.util.PriorityQueue;

final class SegmentMergeQueue extends PriorityQueue
{
  SegmentMergeQueue(int paramInt)
  {
    initialize(paramInt);
  }

  protected final boolean lessThan(Object paramObject1, Object paramObject2)
  {
    SegmentMergeInfo localSegmentMergeInfo1 = (SegmentMergeInfo)paramObject1;
    SegmentMergeInfo localSegmentMergeInfo2 = (SegmentMergeInfo)paramObject2;
    int i = localSegmentMergeInfo1.term.compareTo(localSegmentMergeInfo2.term);
    if (i == 0)
      return localSegmentMergeInfo1.base < localSegmentMergeInfo2.base;
    return i < 0;
  }

  final void close()
    throws IOException
  {
    while (top() != null)
      ((SegmentMergeInfo)pop()).close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentMergeQueue
 * JD-Core Version:    0.6.2
 */