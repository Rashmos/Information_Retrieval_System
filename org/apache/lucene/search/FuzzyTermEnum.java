package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public final class FuzzyTermEnum extends FilteredTermEnum
{
  double distance;
  boolean fieldMatch = false;
  boolean endEnum = false;
  Term searchTerm = null;
  String field = "";
  String text = "";
  int textlen;
  public static final double FUZZY_THRESHOLD = 0.5D;
  public static final double SCALE_FACTOR = 2.0D;
  private int[][] e = new int[0][0];

  public FuzzyTermEnum(IndexReader paramIndexReader, Term paramTerm)
    throws IOException
  {
    super(paramIndexReader, paramTerm);
    this.searchTerm = paramTerm;
    this.field = this.searchTerm.field();
    this.text = this.searchTerm.text();
    this.textlen = this.text.length();
    setEnum(paramIndexReader.terms(new Term(this.searchTerm.field(), "")));
  }

  protected final boolean termCompare(Term paramTerm)
  {
    if (this.field == paramTerm.field())
    {
      String str = paramTerm.text();
      int i = str.length();
      int j = editDistance(this.text, str, this.textlen, i);
      this.distance = (1.0D - j / Math.min(this.textlen, i));
      return this.distance > 0.5D;
    }
    this.endEnum = true;
    return false;
  }

  protected final float difference()
  {
    return (float)((this.distance - 0.5D) * 2.0D);
  }

  public final boolean endEnum()
  {
    return this.endEnum;
  }

  private static final int min(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1 < paramInt2 ? paramInt1 : paramInt2;
    return i < paramInt3 ? i : paramInt3;
  }

  private final int editDistance(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    if ((this.e.length <= paramInt1) || (this.e[0].length <= paramInt2))
      this.e = new int[Math.max(this.e.length, paramInt1 + 1)][Math.max(this.e.length, paramInt2 + 1)];
    int[][] arrayOfInt = this.e;
    if (paramInt1 == 0)
      return paramInt2;
    if (paramInt2 == 0)
      return paramInt1;
    for (int i = 0; i <= paramInt1; i++)
      arrayOfInt[i][0] = i;
    for (int j = 0; j <= paramInt2; j++)
      arrayOfInt[0][j] = j;
    for (i = 1; i <= paramInt1; i++)
    {
      int k = paramString1.charAt(i - 1);
      for (j = 1; j <= paramInt2; j++)
        if (k != paramString2.charAt(j - 1))
          arrayOfInt[i][j] = (min(arrayOfInt[(i - 1)][j], arrayOfInt[i][(j - 1)], arrayOfInt[(i - 1)][(j - 1)]) + 1);
        else
          arrayOfInt[i][j] = min(arrayOfInt[(i - 1)][j] + 1, arrayOfInt[i][(j - 1)] + 1, arrayOfInt[(i - 1)][(j - 1)]);
    }
    return arrayOfInt[paramInt1][paramInt2];
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
 * Qualified Name:     org.apache.lucene.search.FuzzyTermEnum
 * JD-Core Version:    0.6.2
 */