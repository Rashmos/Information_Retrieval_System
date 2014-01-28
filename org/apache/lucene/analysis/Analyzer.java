package org.apache.lucene.analysis;

import java.io.Reader;

public abstract class Analyzer
{
  public TokenStream tokenStream(String paramString, Reader paramReader)
  {
    return tokenStream(paramReader);
  }

  /** @deprecated */
  public TokenStream tokenStream(Reader paramReader)
  {
    return tokenStream(null, paramReader);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.Analyzer
 * JD-Core Version:    0.6.2
 */