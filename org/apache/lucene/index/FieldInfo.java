package org.apache.lucene.index;

final class FieldInfo
{
  String name;
  boolean isIndexed;
  int number;

  FieldInfo(String paramString, boolean paramBoolean, int paramInt)
  {
    this.name = paramString;
    this.isIndexed = paramBoolean;
    this.number = paramInt;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FieldInfo
 * JD-Core Version:    0.6.2
 */