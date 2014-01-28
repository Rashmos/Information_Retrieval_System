package org.apache.lucene.analysis;

import java.io.IOException;
import java.io.Reader;

public abstract class Tokenizer extends TokenStream
{
  protected Reader input;

  public void close()
    throws IOException
  {
    this.input.close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.Tokenizer
 * JD-Core Version:    0.6.2
 */