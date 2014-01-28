package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public class WildcardTermEnum extends FilteredTermEnum
{
  Term searchTerm;
  String field = "";
  String text = "";
  String pre = "";
  int preLen = 0;
  boolean fieldMatch = false;
  boolean endEnum = false;
  public static final char WILDCARD_STRING = '*';
  public static final char WILDCARD_CHAR = '?';

  public WildcardTermEnum(IndexReader paramIndexReader, Term paramTerm)
    throws IOException
  {
    super(paramIndexReader, paramTerm);
    this.searchTerm = paramTerm;
    this.field = this.searchTerm.field();
    this.text = this.searchTerm.text();
    int i = this.text.indexOf('*');
    int j = this.text.indexOf('?');
    int k = i;
    if (k == -1)
      k = j;
    else if (j >= 0)
      k = Math.min(k, j);
    this.pre = this.searchTerm.text().substring(0, k);
    this.preLen = this.pre.length();
    this.text = this.text.substring(this.preLen);
    setEnum(paramIndexReader.terms(new Term(this.searchTerm.field(), this.pre)));
  }

  protected final boolean termCompare(Term paramTerm)
  {
    if (this.field == paramTerm.field())
    {
      String str = paramTerm.text();
      if (str.startsWith(this.pre))
        return wildcardEquals(this.text, 0, str, this.preLen);
    }
    this.endEnum = true;
    return false;
  }

  public final float difference()
  {
    return 1.0F;
  }

  public final boolean endEnum()
  {
    return this.endEnum;
  }

  public static final boolean wildcardEquals(String paramString1, int paramInt1, String paramString2, int paramInt2)
  {
    int i = paramInt1;
    int j = paramInt2;
    while (true)
    {
      int k = j >= paramString2.length() ? 1 : 0;
      int m = i >= paramString1.length() ? 1 : 0;
      int n;
      if (k != 0)
      {
        n = 1;
        int i1 = i;
        while ((i1 < paramString1.length()) && (n != 0))
        {
          int i2 = paramString1.charAt(i1);
          if ((i2 != 63) && (i2 != 42))
            n = 0;
          else
            i1++;
        }
        if (n != 0)
          return true;
      }
      if ((k != 0) || (m != 0))
        break;
      if (paramString1.charAt(i) != '?')
        if (paramString1.charAt(i) == '*')
        {
          i++;
          for (n = paramString2.length(); n >= j; n--)
            if (wildcardEquals(paramString1, i, paramString2, n))
              return true;
        }
        else
        {
          if (paramString1.charAt(i) != paramString2.charAt(j))
            break;
        }
      i++;
      j++;
    }
    return false;
  }

  public void close()
    throws IOException
  {
    super.close();
    this.searchTerm = null;
    this.field = null;
    this.text = null;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.WildcardTermEnum
 * JD-Core Version:    0.6.2
 */