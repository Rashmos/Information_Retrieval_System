package org.apache.lucene.search;

import java.io.IOException;
import java.util.Vector;
import org.apache.lucene.document.Document;

public final class Hits
{
  private Query query;
  private Searcher searcher;
  private Filter filter = null;
  private int length;
  private Vector hitDocs = new Vector();
  private HitDoc first;
  private HitDoc last;
  private int numDocs = 0;
  private int maxDocs = 200;

  Hits(Searcher paramSearcher, Query paramQuery, Filter paramFilter)
    throws IOException
  {
    this.query = paramQuery;
    this.searcher = paramSearcher;
    this.filter = paramFilter;
    getMoreDocs(50);
  }

  private final void getMoreDocs(int paramInt)
    throws IOException
  {
    if (this.hitDocs.size() > paramInt)
      paramInt = this.hitDocs.size();
    int i = paramInt * 2;
    TopDocs localTopDocs = this.searcher.search(this.query, this.filter, i);
    this.length = localTopDocs.totalHits;
    ScoreDoc[] arrayOfScoreDoc = localTopDocs.scoreDocs;
    float f = 1.0F;
    if ((this.length > 0) && (arrayOfScoreDoc[0].score > 1.0F))
      f = 1.0F / arrayOfScoreDoc[0].score;
    int j = arrayOfScoreDoc.length < this.length ? arrayOfScoreDoc.length : this.length;
    for (int k = this.hitDocs.size(); k < j; k++)
      this.hitDocs.addElement(new HitDoc(arrayOfScoreDoc[k].score * f, arrayOfScoreDoc[k].doc));
  }

  public final int length()
  {
    return this.length;
  }

  public final Document doc(int paramInt)
    throws IOException
  {
    HitDoc localHitDoc1 = hitDoc(paramInt);
    remove(localHitDoc1);
    addToFront(localHitDoc1);
    if (this.numDocs > this.maxDocs)
    {
      HitDoc localHitDoc2 = this.last;
      remove(this.last);
      localHitDoc2.doc = null;
    }
    if (localHitDoc1.doc == null)
      localHitDoc1.doc = this.searcher.doc(localHitDoc1.id);
    return localHitDoc1.doc;
  }

  public final float score(int paramInt)
    throws IOException
  {
    return hitDoc(paramInt).score;
  }

  private final HitDoc hitDoc(int paramInt)
    throws IOException
  {
    if (paramInt >= this.length)
      throw new IndexOutOfBoundsException("Not a valid hit number: " + paramInt);
    if (paramInt >= this.hitDocs.size())
      getMoreDocs(paramInt);
    return (HitDoc)this.hitDocs.elementAt(paramInt);
  }

  private final void addToFront(HitDoc paramHitDoc)
  {
    if (this.first == null)
      this.last = paramHitDoc;
    else
      this.first.prev = paramHitDoc;
    paramHitDoc.next = this.first;
    this.first = paramHitDoc;
    paramHitDoc.prev = null;
    this.numDocs += 1;
  }

  private final void remove(HitDoc paramHitDoc)
  {
    if (paramHitDoc.doc == null)
      return;
    if (paramHitDoc.next == null)
      this.last = paramHitDoc.prev;
    else
      paramHitDoc.next.prev = paramHitDoc.prev;
    if (paramHitDoc.prev == null)
      this.first = paramHitDoc.next;
    else
      paramHitDoc.prev.next = paramHitDoc.next;
    this.numDocs -= 1;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Hits
 * JD-Core Version:    0.6.2
 */