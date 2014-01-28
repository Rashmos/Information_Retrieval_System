package org.apache.lucene.search;

import org.apache.lucene.util.PriorityQueue;

final class PhraseQueue extends PriorityQueue
{
  PhraseQueue(int paramInt)
  {
    initialize(paramInt);
  }

  protected final boolean lessThan(Object paramObject1, Object paramObject2)
  {
    PhrasePositions localPhrasePositions1 = (PhrasePositions)paramObject1;
    PhrasePositions localPhrasePositions2 = (PhrasePositions)paramObject2;
    if (localPhrasePositions1.doc == localPhrasePositions2.doc)
      return localPhrasePositions1.position < localPhrasePositions2.position;
    return localPhrasePositions1.doc < localPhrasePositions2.doc;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.PhraseQueue
 * JD-Core Version:    0.6.2
 */