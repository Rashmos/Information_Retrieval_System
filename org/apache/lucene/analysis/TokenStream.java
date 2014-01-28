package org.apache.lucene.analysis;

import java.io.IOException;

public abstract class TokenStream
{
  public abstract Token next()
    throws IOException;

  public void close()
    throws IOException
  {
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.TokenStream
 * JD-Core Version:    0.6.2
 */