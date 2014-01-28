package org.apache.lucene.document;

import java.util.Enumeration;

public final class Document
{
  DocumentFieldList fieldList = null;

  public final void add(Field paramField)
  {
    this.fieldList = new DocumentFieldList(paramField, this.fieldList);
  }

  public final Field getField(String paramString)
  {
    for (DocumentFieldList localDocumentFieldList = this.fieldList; localDocumentFieldList != null; localDocumentFieldList = localDocumentFieldList.next)
      if (localDocumentFieldList.field.name().equals(paramString))
        return localDocumentFieldList.field;
    return null;
  }

  public final String get(String paramString)
  {
    Field localField = getField(paramString);
    if (localField != null)
      return localField.stringValue();
    return null;
  }

  public final Enumeration fields()
  {
    return new DocumentFieldEnumeration(this);
  }

  public final String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Document<");
    for (DocumentFieldList localDocumentFieldList = this.fieldList; localDocumentFieldList != null; localDocumentFieldList = localDocumentFieldList.next)
    {
      localStringBuffer.append(localDocumentFieldList.field.toString());
      if (localDocumentFieldList.next != null)
        localStringBuffer.append(" ");
    }
    localStringBuffer.append(">");
    return localStringBuffer.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.Document
 * JD-Core Version:    0.6.2
 */