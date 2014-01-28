package org.apache.lucene.index;

import java.io.IOException;

public abstract interface TermPositions extends TermDocs
{
  public abstract int nextPosition()
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermPositions
 * JD-Core Version:    0.6.2
 */