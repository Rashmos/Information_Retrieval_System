package org.apache.lucene.analysis.standard;

public class Token
{
  public int kind;
  public int beginLine;
  public int beginColumn;
  public int endLine;
  public int endColumn;
  public String image;
  public Token next;
  public Token specialToken;

  public final String toString()
  {
    return this.image;
  }

  public static final Token newToken(int paramInt)
  {
    switch (paramInt)
    {
    }
    return new Token();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.standard.Token
 * JD-Core Version:    0.6.2
 */