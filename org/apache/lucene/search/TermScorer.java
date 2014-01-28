package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.TermDocs;

final class TermScorer extends Scorer
{
  private TermDocs termDocs;
  private byte[] norms;
  private float weight;
  private int doc;
  private final int[] docs = new int[''];
  private final int[] freqs = new int[''];
  private int pointer;
  private int pointerMax;
  private static final int SCORE_CACHE_SIZE = 32;
  private float[] scoreCache = new float[32];

  TermScorer(TermDocs paramTermDocs, byte[] paramArrayOfByte, float paramFloat)
    throws IOException
  {
    this.termDocs = paramTermDocs;
    this.norms = paramArrayOfByte;
    this.weight = paramFloat;
    for (int i = 0; i < 32; i++)
      this.scoreCache[i] = (Similarity.tf(i) * this.weight);
    this.pointerMax = this.termDocs.read(this.docs, this.freqs);
    if (this.pointerMax != 0)
    {
      this.doc = this.docs[0];
    }
    else
    {
      this.termDocs.close();
      this.doc = 2147483647;
    }
  }

  final void score(HitCollector paramHitCollector, int paramInt)
    throws IOException
  {
    for (int i = this.doc; i < paramInt; i = this.docs[this.pointer])
    {
      int j = this.freqs[this.pointer];
      float f = j < 32 ? this.scoreCache[j] : Similarity.tf(j) * this.weight;
      f *= Similarity.norm(this.norms[i]);
      paramHitCollector.collect(i, f);
      if (++this.pointer == this.pointerMax)
      {
        this.pointerMax = this.termDocs.read(this.docs, this.freqs);
        if (this.pointerMax != 0)
        {
          this.pointer = 0;
        }
        else
        {
          this.termDocs.close();
          this.doc = 2147483647;
          return;
        }
      }
    }
    this.doc = i;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TermScorer
 * JD-Core Version:    0.6.2
 */