package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.util.PriorityQueue;

abstract class PhraseScorer extends Scorer
{
  protected byte[] norms;
  protected float weight;
  protected PhraseQueue pq;
  protected PhrasePositions first;
  protected PhrasePositions last;

  PhraseScorer(TermPositions[] paramArrayOfTermPositions, byte[] paramArrayOfByte, float paramFloat)
    throws IOException
  {
    this.norms = paramArrayOfByte;
    this.weight = paramFloat;
    this.pq = new PhraseQueue(paramArrayOfTermPositions.length);
    for (int i = 0; i < paramArrayOfTermPositions.length; i++)
      this.pq.put(new PhrasePositions(paramArrayOfTermPositions[i], i));
    pqToList();
  }

  final void score(HitCollector paramHitCollector, int paramInt)
    throws IOException
  {
    while (this.last.doc < paramInt)
    {
      while (this.first.doc < this.last.doc)
      {
        do
          this.first.next();
        while (this.first.doc < this.last.doc);
        firstToLast();
        if (this.last.doc >= paramInt)
          return;
      }
      float f1 = phraseFreq();
      if (f1 > 0.0D)
      {
        float f2 = Similarity.tf(f1) * this.weight;
        f2 *= Similarity.norm(this.norms[this.first.doc]);
        paramHitCollector.collect(this.first.doc, f2);
      }
      this.last.next();
    }
  }

  protected abstract float phraseFreq()
    throws IOException;

  protected final void pqToList()
  {
    this.last = (this.first = null);
    while (this.pq.top() != null)
    {
      PhrasePositions localPhrasePositions = (PhrasePositions)this.pq.pop();
      if (this.last != null)
        this.last.next = localPhrasePositions;
      else
        this.first = localPhrasePositions;
      this.last = localPhrasePositions;
      localPhrasePositions.next = null;
    }
  }

  protected final void firstToLast()
  {
    this.last.next = this.first;
    this.last = this.first;
    this.first = this.first.next;
    this.last.next = null;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.PhraseScorer
 * JD-Core Version:    0.6.2
 */