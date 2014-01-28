package org.apache.lucene.analysis.de;

import java.io.IOException;
import java.util.Hashtable;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public final class GermanStemFilter extends TokenFilter
{
  private Token token = null;
  private GermanStemmer stemmer = null;
  private Hashtable exclusions = null;

  public GermanStemFilter(TokenStream paramTokenStream)
  {
    this.input = paramTokenStream;
  }

  public GermanStemFilter(TokenStream paramTokenStream, Hashtable paramHashtable)
  {
    this(paramTokenStream);
    this.exclusions = paramHashtable;
  }

  public final Token next()
    throws IOException
  {
    if ((this.token = this.input.next()) == null)
      return null;
    if ((this.exclusions != null) && (this.exclusions.contains(this.token.termText())))
      return this.token;
    String str = this.stemmer.stem(this.token.termText());
    if (!str.equals(this.token.termText()))
      return new Token(str, this.token.startOffset(), this.token.endOffset(), this.token.type());
    return this.token;
  }

  public void setStemmer(GermanStemmer paramGermanStemmer)
  {
    if (paramGermanStemmer != null)
      this.stemmer = paramGermanStemmer;
  }

  public void setExclusionTable(Hashtable paramHashtable)
  {
    this.exclusions = paramHashtable;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.de.GermanStemFilter
 * JD-Core Version:    0.6.2
 */