package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.util.PriorityQueue;

final class ExactPhraseScorer extends PhraseScorer
{
  ExactPhraseScorer(TermPositions[] paramArrayOfTermPositions, byte[] paramArrayOfByte, float paramFloat)
    throws IOException
  {
    super(paramArrayOfTermPositions, paramArrayOfByte, paramFloat);
  }

  protected final float phraseFreq()
    throws IOException
  {
    for (PhrasePositions localPhrasePositions = this.first; localPhrasePositions != null; localPhrasePositions = localPhrasePositions.next)
    {
      localPhrasePositions.firstPosition();
      this.pq.put(localPhrasePositions);
    }
    pqToList();
    int i = 0;
    do
    {
      while (this.first.position < this.last.position)
      {
        do
          if (!this.first.nextPosition())
            return i;
        while (this.first.position < this.last.position);
        firstToLast();
      }
      i++;
    }
    while (this.last.nextPosition());
    return i;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.ExactPhraseScorer
 * JD-Core Version:    0.6.2
 */