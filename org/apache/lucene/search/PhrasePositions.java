package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermPositions;

final class PhrasePositions
{
  int doc;
  int position;
  int count;
  int offset;
  TermPositions tp;
  PhrasePositions next;

  PhrasePositions(TermPositions paramTermPositions, int paramInt)
    throws IOException
  {
    this.tp = paramTermPositions;
    this.offset = paramInt;
    next();
  }

  final void next()
    throws IOException
  {
    if (!this.tp.next())
    {
      this.tp.close();
      this.doc = 2147483647;
      return;
    }
    this.doc = this.tp.doc();
    this.position = 0;
  }

  final void firstPosition()
    throws IOException
  {
    this.count = this.tp.freq();
    nextPosition();
  }

  final boolean nextPosition()
    throws IOException
  {
    if (this.count-- > 0)
    {
      this.position = (this.tp.nextPosition() - this.offset);
      return true;
    }
    return false;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.PhrasePositions
 * JD-Core Version:    0.6.2
 */