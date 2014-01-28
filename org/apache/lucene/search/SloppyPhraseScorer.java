package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.util.PriorityQueue;

final class SloppyPhraseScorer extends PhraseScorer
{
  private int slop;

  SloppyPhraseScorer(TermPositions[] paramArrayOfTermPositions, int paramInt, byte[] paramArrayOfByte, float paramFloat)
    throws IOException
  {
    super(paramArrayOfTermPositions, paramArrayOfByte, paramFloat);
    this.slop = paramInt;
  }

  protected final float phraseFreq()
    throws IOException
  {
    this.pq.clear();
    int i = 0;
    for (PhrasePositions localPhrasePositions1 = this.first; localPhrasePositions1 != null; localPhrasePositions1 = localPhrasePositions1.next)
    {
      localPhrasePositions1.firstPosition();
      if (localPhrasePositions1.position > i)
        i = localPhrasePositions1.position;
      this.pq.put(localPhrasePositions1);
    }
    float f = 0.0F;
    int j = 0;
    do
    {
      PhrasePositions localPhrasePositions2 = (PhrasePositions)this.pq.pop();
      int k = localPhrasePositions2.position;
      int m = ((PhrasePositions)this.pq.top()).position;
      for (int n = k; n <= m; n = localPhrasePositions2.position)
      {
        k = n;
        if (!localPhrasePositions2.nextPosition())
        {
          j = 1;
          break;
        }
      }
      int i1 = i - k;
      if (i1 <= this.slop)
        f = (float)(f + 1.0D / (i1 + 1));
      if (localPhrasePositions2.position > i)
        i = localPhrasePositions2.position;
      this.pq.put(localPhrasePositions2);
    }
    while (j == 0);
    return f;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.SloppyPhraseScorer
 * JD-Core Version:    0.6.2
 */