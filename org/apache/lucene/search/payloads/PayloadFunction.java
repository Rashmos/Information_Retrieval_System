package org.apache.lucene.search.payloads;

import java.io.Serializable;

public abstract class PayloadFunction
  implements Serializable
{
  public abstract float currentScore(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2);

  public abstract float docScore(int paramInt1, String paramString, int paramInt2, float paramFloat);

  public abstract int hashCode();

  public abstract boolean equals(Object paramObject);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.payloads.PayloadFunction
 * JD-Core Version:    0.6.2
 */