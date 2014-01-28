package org.apache.lucene.analysis;

import java.io.Reader;

public class LetterTokenizer extends CharTokenizer
{
  public LetterTokenizer(Reader paramReader)
  {
    super(paramReader);
  }

  protected boolean isTokenChar(char paramChar)
  {
    return Character.isLetter(paramChar);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.LetterTokenizer
 * JD-Core Version:    0.6.2
 */