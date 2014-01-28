package org.apache.lucene.index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public final class Term
  implements Serializable
{
  String field;
  String text;

  public Term(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, true);
  }

  Term(String paramString1, String paramString2, boolean paramBoolean)
  {
    this.field = (paramBoolean ? paramString1.intern() : paramString1);
    this.text = paramString2;
  }

  public final String field()
  {
    return this.field;
  }

  public final String text()
  {
    return this.text;
  }

  public final boolean equals(Object paramObject)
  {
    if (paramObject == null)
      return false;
    Term localTerm = (Term)paramObject;
    return (this.field == localTerm.field) && (this.text.equals(localTerm.text));
  }

  public final int hashCode()
  {
    return this.field.hashCode() + this.text.hashCode();
  }

  public final int compareTo(Term paramTerm)
  {
    if (this.field == paramTerm.field)
      return this.text.compareTo(paramTerm.text);
    return this.field.compareTo(paramTerm.field);
  }

  final void set(String paramString1, String paramString2)
  {
    this.field = paramString1;
    this.text = paramString2;
  }

  public final String toString()
  {
    return "Term<" + this.field + ":" + this.text + ">";
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    this.field = this.field.intern();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.Term
 * JD-Core Version:    0.6.2
 */