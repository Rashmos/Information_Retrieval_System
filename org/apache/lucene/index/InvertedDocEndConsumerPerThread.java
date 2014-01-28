package org.apache.lucene.index;

abstract class InvertedDocEndConsumerPerThread
{
  abstract void startDocument();

  abstract InvertedDocEndConsumerPerField addField(DocInverterPerField paramDocInverterPerField, FieldInfo paramFieldInfo);

  abstract void finishDocument();

  abstract void abort();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.InvertedDocEndConsumerPerThread
 * JD-Core Version:    0.6.2
 */