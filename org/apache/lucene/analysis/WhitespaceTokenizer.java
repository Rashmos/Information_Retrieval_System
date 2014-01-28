package org.apache.lucene.analysis;

import java.io.Reader;

public class WhitespaceTokenizer extends CharTokenizer
{
  public WhitespaceTokenizer(Reader paramReader)
  {
    super(paramReader);
  }

  protected boolean isTokenChar(char paramChar)
  {
    return !Character.isWhitespace(paramChar);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.WhitespaceTokenizer
 * JD-Core Version:    0.6.2
 */