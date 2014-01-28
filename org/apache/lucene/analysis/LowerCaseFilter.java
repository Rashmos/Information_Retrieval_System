package org.apache.lucene.analysis;

import java.io.IOException;

public final class LowerCaseFilter extends TokenFilter
{
  public LowerCaseFilter(TokenStream paramTokenStream)
  {
    this.input = paramTokenStream;
  }

  public final Token next()
    throws IOException
  {
    Token localToken = this.input.next();
    if (localToken == null)
      return null;
    localToken.termText = localToken.termText.toLowerCase();
    return localToken;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.LowerCaseFilter
 * JD-Core Version:    0.6.2
 */