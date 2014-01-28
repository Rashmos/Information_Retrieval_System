package org.apache.lucene.analysis;

import java.io.IOException;

public final class PorterStemFilter extends TokenFilter
{
  private PorterStemmer stemmer = new PorterStemmer();

  public PorterStemFilter(TokenStream paramTokenStream)
  {
    this.input = paramTokenStream;
  }

  public final Token next()
    throws IOException
  {
    Token localToken = this.input.next();
    if (localToken == null)
      return null;
    String str = this.stemmer.stem(localToken.termText);
    if (str != localToken.termText)
      localToken.termText = str;
    return localToken;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.PorterStemFilter
 * JD-Core Version:    0.6.2
 */