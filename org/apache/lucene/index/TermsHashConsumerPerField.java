package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.document.Fieldable;

abstract class TermsHashConsumerPerField
{
  abstract boolean start(Fieldable[] paramArrayOfFieldable, int paramInt)
    throws IOException;

  abstract void finish()
    throws IOException;

  abstract void skippingLongTerm()
    throws IOException;

  abstract void start(Fieldable paramFieldable);

  abstract void newTerm(RawPostingList paramRawPostingList)
    throws IOException;

  abstract void addTerm(RawPostingList paramRawPostingList)
    throws IOException;

  abstract int getStreamCount();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermsHashConsumerPerField
 * JD-Core Version:    0.6.2
 */