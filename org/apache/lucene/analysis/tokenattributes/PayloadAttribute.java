package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.index.Payload;
import org.apache.lucene.util.Attribute;

public abstract interface PayloadAttribute extends Attribute
{
  public abstract Payload getPayload();

  public abstract void setPayload(Payload paramPayload);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tokenattributes.PayloadAttribute
 * JD-Core Version:    0.6.2
 */