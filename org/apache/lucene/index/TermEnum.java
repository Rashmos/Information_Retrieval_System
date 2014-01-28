package org.apache.lucene.index;

import java.io.IOException;

public abstract class TermEnum
{
  public abstract boolean next()
    throws IOException;

  public abstract Term term();

  public abstract int docFreq();

  public abstract void close()
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermEnum
 * JD-Core Version:    0.6.2
 */