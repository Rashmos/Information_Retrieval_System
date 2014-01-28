package org.apache.lucene.search;

final class TopDocs
{
  int totalHits;
  ScoreDoc[] scoreDocs;

  TopDocs(int paramInt, ScoreDoc[] paramArrayOfScoreDoc)
  {
    this.totalHits = paramInt;
    this.scoreDocs = paramArrayOfScoreDoc;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TopDocs
 * JD-Core Version:    0.6.2
 */