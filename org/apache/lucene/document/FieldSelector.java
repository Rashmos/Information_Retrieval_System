package org.apache.lucene.document;

import java.io.Serializable;

public abstract interface FieldSelector extends Serializable
{
  public abstract FieldSelectorResult accept(String paramString);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.FieldSelector
 * JD-Core Version:    0.6.2
 */