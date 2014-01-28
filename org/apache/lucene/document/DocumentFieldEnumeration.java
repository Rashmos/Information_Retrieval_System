package org.apache.lucene.document;

import java.util.Enumeration;

final class DocumentFieldEnumeration
  implements Enumeration
{
  DocumentFieldList fields;

  DocumentFieldEnumeration(Document paramDocument)
  {
    this.fields = paramDocument.fieldList;
  }

  public final boolean hasMoreElements()
  {
    return this.fields != null;
  }

  public final Object nextElement()
  {
    Field localField = this.fields.field;
    this.fields = this.fields.next;
    return localField;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.DocumentFieldEnumeration
 * JD-Core Version:    0.6.2
 */