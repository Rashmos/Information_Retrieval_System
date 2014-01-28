package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

public abstract interface OffsetAttribute extends Attribute
{
  public abstract int startOffset();

  public abstract void setOffset(int paramInt1, int paramInt2);

  public abstract int endOffset();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tokenattributes.OffsetAttribute
 * JD-Core Version:    0.6.2
 */