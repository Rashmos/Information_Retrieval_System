package org.apache.lucene.analysis;

import java.io.Reader;

public final class LowerCaseTokenizer extends LetterTokenizer
{
  public LowerCaseTokenizer(Reader paramReader)
  {
    super(paramReader);
  }

  protected char normalize(char paramChar)
  {
    return Character.toLowerCase(paramChar);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.LowerCaseTokenizer
 * JD-Core Version:    0.6.2
 */