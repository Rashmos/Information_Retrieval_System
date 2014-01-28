package org.apache.lucene.search;

import java.io.IOException;

abstract class Scorer
{
  abstract void score(HitCollector paramHitCollector, int paramInt)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Scorer
 * JD-Core Version:    0.6.2
 */