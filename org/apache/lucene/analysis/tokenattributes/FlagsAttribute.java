package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

public abstract interface FlagsAttribute extends Attribute
{
  public abstract int getFlags();

  public abstract void setFlags(int paramInt);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tokenattributes.FlagsAttribute
 * JD-Core Version:    0.6.2
 */