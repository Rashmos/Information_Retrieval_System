package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

public abstract interface TypeAttribute extends Attribute
{
  public abstract String type();

  public abstract void setType(String paramString);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tokenattributes.TypeAttribute
 * JD-Core Version:    0.6.2
 */