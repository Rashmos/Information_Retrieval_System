package org.apache.lucene.search;

import org.apache.lucene.document.Document;

final class HitDoc
{
  float score;
  int id;
  Document doc = null;
  HitDoc next;
  HitDoc prev;

  HitDoc(float paramFloat, int paramInt)
  {
    this.score = paramFloat;
    this.id = paramInt;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.HitDoc
 * JD-Core Version:    0.6.2
 */