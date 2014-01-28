package org.apache.lucene.index;

final class Posting
{
  Term term;
  int freq;
  int[] positions;

  Posting(Term paramTerm, int paramInt)
  {
    this.term = paramTerm;
    this.freq = 1;
    this.positions = new int[1];
    this.positions[0] = paramInt;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.Posting
 * JD-Core Version:    0.6.2
 */