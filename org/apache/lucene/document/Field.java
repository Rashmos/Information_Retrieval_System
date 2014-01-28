package org.apache.lucene.document;

import java.io.Reader;

public final class Field
{
  private String name = "body";
  private String stringValue = null;
  private Reader readerValue = null;
  private boolean isStored = false;
  private boolean isIndexed = true;
  private boolean isTokenized = true;

  public static final Field Keyword(String paramString1, String paramString2)
  {
    return new Field(paramString1, paramString2, true, true, false);
  }

  public static final Field UnIndexed(String paramString1, String paramString2)
  {
    return new Field(paramString1, paramString2, true, false, false);
  }

  public static final Field Text(String paramString1, String paramString2)
  {
    return new Field(paramString1, paramString2, true, true, true);
  }

  public static final Field UnStored(String paramString1, String paramString2)
  {
    return new Field(paramString1, paramString2, false, true, true);
  }

  public static final Field Text(String paramString, Reader paramReader)
  {
    return new Field(paramString, paramReader);
  }

  public String name()
  {
    return this.name;
  }

  public String stringValue()
  {
    return this.stringValue;
  }

  public Reader readerValue()
  {
    return this.readerValue;
  }

  public Field(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramString1 == null)
      throw new IllegalArgumentException("name cannot be null");
    if (paramString2 == null)
      throw new IllegalArgumentException("value cannot be null");
    this.name = paramString1.intern();
    this.stringValue = paramString2;
    this.isStored = paramBoolean1;
    this.isIndexed = paramBoolean2;
    this.isTokenized = paramBoolean3;
  }

  Field(String paramString, Reader paramReader)
  {
    if (paramString == null)
      throw new IllegalArgumentException("name cannot be null");
    if (paramReader == null)
      throw new IllegalArgumentException("value cannot be null");
    this.name = paramString.intern();
    this.readerValue = paramReader;
  }

  public final boolean isStored()
  {
    return this.isStored;
  }

  public final boolean isIndexed()
  {
    return this.isIndexed;
  }

  public final boolean isTokenized()
  {
    return this.isTokenized;
  }

  public final String toString()
  {
    if ((this.isStored) && (this.isIndexed) && (!this.isTokenized))
      return "Keyword<" + this.name + ":" + this.stringValue + ">";
    if ((this.isStored) && (!this.isIndexed) && (!this.isTokenized))
      return "Unindexed<" + this.name + ":" + this.stringValue + ">";
    if ((this.isStored) && (this.isIndexed) && (this.isTokenized) && (this.stringValue != null))
      return "Text<" + this.name + ":" + this.stringValue + ">";
    if ((!this.isStored) && (this.isIndexed) && (this.isTokenized) && (this.readerValue != null))
      return "Text<" + this.name + ":" + this.readerValue + ">";
    return super.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.Field
 * JD-Core Version:    0.6.2
 */