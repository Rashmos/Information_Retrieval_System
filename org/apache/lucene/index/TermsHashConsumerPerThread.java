package org.apache.lucene.index;

import java.io.IOException;

abstract class TermsHashConsumerPerThread
{
  abstract void startDocument()
    throws IOException;

  abstract DocumentsWriter.DocWriter finishDocument()
    throws IOException;

  public abstract TermsHashConsumerPerField addField(TermsHashPerField paramTermsHashPerField, FieldInfo paramFieldInfo);

  public abstract void abort();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermsHashConsumerPerThread
 * JD-Core Version:    0.6.2
 */