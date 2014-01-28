package org.apache.lucene.search;

import java.io.IOException;

final class BooleanScorer extends Scorer
{
  private int currentDoc;
  private SubScorer scorers = null;
  private BucketTable bucketTable = new BucketTable(this);
  private int maxCoord = 1;
  private float[] coordFactors = null;
  private int requiredMask = 0;
  private int prohibitedMask = 0;
  private int nextMask = 1;

  final void add(Scorer paramScorer, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    if ((paramBoolean1) || (paramBoolean2))
    {
      if (this.nextMask == 0)
        throw new IndexOutOfBoundsException("More than 32 required/prohibited clauses in query.");
      i = this.nextMask;
      this.nextMask <<= 1;
    }
    else
    {
      i = 0;
    }
    if (!paramBoolean2)
      this.maxCoord += 1;
    if (paramBoolean2)
      this.prohibitedMask |= i;
    else if (paramBoolean1)
      this.requiredMask |= i;
    this.scorers = new SubScorer(paramScorer, paramBoolean1, paramBoolean2, this.bucketTable.newCollector(i), this.scorers);
  }

  private final void computeCoordFactors()
    throws IOException
  {
    this.coordFactors = new float[this.maxCoord];
    for (int i = 0; i < this.maxCoord; i++)
      this.coordFactors[i] = Similarity.coord(i, this.maxCoord);
  }

  final void score(HitCollector paramHitCollector, int paramInt)
    throws IOException
  {
    if (this.coordFactors == null)
      computeCoordFactors();
    while (this.currentDoc < paramInt)
    {
      this.currentDoc = Math.min(this.currentDoc + 1024, paramInt);
      for (SubScorer localSubScorer = this.scorers; localSubScorer != null; localSubScorer = localSubScorer.next)
        localSubScorer.scorer.score(localSubScorer.collector, this.currentDoc);
      this.bucketTable.collectHits(paramHitCollector);
    }
  }

  static final class Collector extends HitCollector
  {
    private BooleanScorer.BucketTable bucketTable;
    private int mask;

    public Collector(int paramInt, BooleanScorer.BucketTable paramBucketTable)
    {
      this.mask = paramInt;
      this.bucketTable = paramBucketTable;
    }

    public final void collect(int paramInt, float paramFloat)
    {
      BooleanScorer.BucketTable localBucketTable = this.bucketTable;
      int i = paramInt & 0x3FF;
      BooleanScorer.Bucket localBucket = localBucketTable.buckets[i];
      if (localBucket == null)
      {
        void tmp39_36 = new BooleanScorer.Bucket();
        localBucket = tmp39_36;
        localBucketTable.buckets[i] = tmp39_36;
      }
      if (localBucket.doc != paramInt)
      {
        localBucket.doc = paramInt;
        localBucket.score = paramFloat;
        localBucket.bits = this.mask;
        localBucket.coord = 1;
        localBucket.next = localBucketTable.first;
        localBucketTable.first = localBucket;
      }
      else
      {
        localBucket.score += paramFloat;
        localBucket.bits |= this.mask;
        localBucket.coord += 1;
      }
    }
  }

  static final class BucketTable
  {
    public static final int SIZE = 1024;
    public static final int MASK = 1023;
    final BooleanScorer.Bucket[] buckets = new BooleanScorer.Bucket[1024];
    BooleanScorer.Bucket first = null;
    private BooleanScorer scorer;

    public BucketTable(BooleanScorer paramBooleanScorer)
    {
      this.scorer = paramBooleanScorer;
    }

    public final void collectHits(HitCollector paramHitCollector)
    {
      int i = this.scorer.requiredMask;
      int j = this.scorer.prohibitedMask;
      float[] arrayOfFloat = this.scorer.coordFactors;
      for (BooleanScorer.Bucket localBucket = this.first; localBucket != null; localBucket = localBucket.next)
        if (((localBucket.bits & j) == 0) && ((localBucket.bits & i) == i))
          paramHitCollector.collect(localBucket.doc, localBucket.score * arrayOfFloat[localBucket.coord]);
      this.first = null;
    }

    public final int size()
    {
      return 1024;
    }

    public HitCollector newCollector(int paramInt)
    {
      return new BooleanScorer.Collector(paramInt, this);
    }
  }

  static final class Bucket
  {
    int doc = -1;
    float score;
    int bits;
    int coord;
    Bucket next;
  }

  static final class SubScorer
  {
    public Scorer scorer;
    public boolean required = false;
    public boolean prohibited = false;
    public HitCollector collector;
    public SubScorer next;

    public SubScorer(Scorer paramScorer, boolean paramBoolean1, boolean paramBoolean2, HitCollector paramHitCollector, SubScorer paramSubScorer)
    {
      this.scorer = paramScorer;
      this.required = paramBoolean1;
      this.prohibited = paramBoolean2;
      this.collector = paramHitCollector;
      this.next = paramSubScorer;
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanScorer
 * JD-Core Version:    0.6.2
 */