package org.apache.lucene.analysis;

import java.io.IOException;

public abstract class TokenFilter extends TokenStream
{
  protected TokenStream input;

  public void close()
    throws IOException
  {
    this.input.close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.TokenFilter
 * JD-Core Version:    0.6.2
 */