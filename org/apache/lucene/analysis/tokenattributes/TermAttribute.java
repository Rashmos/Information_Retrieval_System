package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

public abstract interface TermAttribute extends Attribute
{
  public abstract String term();

  public abstract void setTermBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract void setTermBuffer(String paramString);

  public abstract void setTermBuffer(String paramString, int paramInt1, int paramInt2);

  public abstract char[] termBuffer();

  public abstract char[] resizeTermBuffer(int paramInt);

  public abstract int termLength();

  public abstract void setTermLength(int paramInt);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tokenattributes.TermAttribute
 * JD-Core Version:    0.6.2
 */