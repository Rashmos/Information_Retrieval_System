package org.apache.lucene.document;

final class DocumentFieldList
{
  Field field;
  DocumentFieldList next;

  DocumentFieldList(Field paramField, DocumentFieldList paramDocumentFieldList)
  {
    this.field = paramField;
    this.next = paramDocumentFieldList;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.DocumentFieldList
 * JD-Core Version:    0.6.2
 */