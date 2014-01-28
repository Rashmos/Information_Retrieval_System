package org.apache.lucene.index;

import java.io.IOException;

abstract class InvertedDocConsumerPerThread
{
  abstract void startDocument()
    throws IOException;

  abstract InvertedDocConsumerPerField addField(DocInverterPerField paramDocInverterPerField, FieldInfo paramFieldInfo);

  abstract DocumentsWriter.DocWriter finishDocument()
    throws IOException;

  abstract void abort();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.InvertedDocConsumerPerThread
 * JD-Core Version:    0.6.2
 */