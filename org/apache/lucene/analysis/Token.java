package org.apache.lucene.analysis;

public final class Token
{
  String termText;
  int startOffset;
  int endOffset;
  String type = "word";

  public Token(String paramString, int paramInt1, int paramInt2)
  {
    this.termText = paramString;
    this.startOffset = paramInt1;
    this.endOffset = paramInt2;
  }

  public Token(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    this.termText = paramString1;
    this.startOffset = paramInt1;
    this.endOffset = paramInt2;
    this.type = paramString2;
  }

  public final String termText()
  {
    return this.termText;
  }

  public final int startOffset()
  {
    return this.startOffset;
  }

  public final int endOffset()
  {
    return this.endOffset;
  }

  public final String type()
  {
    return this.type;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.Token
 * JD-Core Version:    0.6.2
 */