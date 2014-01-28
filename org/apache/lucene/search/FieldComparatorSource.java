package org.apache.lucene.search;

import java.io.IOException;
import java.io.Serializable;

public abstract class FieldComparatorSource
  implements Serializable
{
  public abstract FieldComparator newComparator(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FieldComparatorSource
 * JD-Core Version:    0.6.2
 */