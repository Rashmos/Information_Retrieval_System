package org.apache.lucene.analysis.compound.hyphenation;

import java.util.ArrayList;

public abstract interface PatternConsumer
{
  public abstract void addClass(String paramString);

  public abstract void addException(String paramString, ArrayList<Object> paramArrayList);

  public abstract void addPattern(String paramString1, String paramString2);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.PatternConsumer
 * JD-Core Version:    0.6.2
 */