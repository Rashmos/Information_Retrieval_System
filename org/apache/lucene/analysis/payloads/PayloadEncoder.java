package org.apache.lucene.analysis.payloads;

import org.apache.lucene.index.Payload;

public abstract interface PayloadEncoder
{
  public abstract Payload encode(char[] paramArrayOfChar);

  public abstract Payload encode(char[] paramArrayOfChar, int paramInt1, int paramInt2);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.PayloadEncoder
 * JD-Core Version:    0.6.2
 */