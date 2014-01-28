package org.apache.lucene.search;

import org.apache.lucene.util.PriorityQueue;

final class HitQueue extends PriorityQueue
{
  HitQueue(int paramInt)
  {
    initialize(paramInt);
  }

  protected final boolean lessThan(Object paramObject1, Object paramObject2)
  {
    ScoreDoc localScoreDoc1 = (ScoreDoc)paramObject1;
    ScoreDoc localScoreDoc2 = (ScoreDoc)paramObject2;
    if (localScoreDoc1.score == localScoreDoc2.score)
      return localScoreDoc1.doc > localScoreDoc2.doc;
    return localScoreDoc1.score < localScoreDoc2.score;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.HitQueue
 * JD-Core Version:    0.6.2
 */