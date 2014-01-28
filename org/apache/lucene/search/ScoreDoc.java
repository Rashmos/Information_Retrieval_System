package org.apache.lucene.search;

final class ScoreDoc
{
  float score;
  int doc;

  ScoreDoc(int paramInt, float paramFloat)
  {
    this.doc = paramInt;
    this.score = paramFloat;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.ScoreDoc
 * JD-Core Version:    0.6.2
 */