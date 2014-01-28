package org.apache.lucene.analysis.standard;

import java.io.IOException;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public final class StandardFilter extends TokenFilter
  implements StandardTokenizerConstants
{
  private static final String APOSTROPHE_TYPE = StandardTokenizerConstants.tokenImage[2];
  private static final String ACRONYM_TYPE = StandardTokenizerConstants.tokenImage[3];

  public StandardFilter(TokenStream paramTokenStream)
  {
    this.input = paramTokenStream;
  }

  public final Token next()
    throws IOException
  {
    Token localToken = this.input.next();
    if (localToken == null)
      return null;
    String str1 = localToken.termText();
    String str2 = localToken.type();
    if ((str2 == APOSTROPHE_TYPE) && ((str1.endsWith("'s")) || (str1.endsWith("'S"))))
      return new Token(str1.substring(0, str1.length() - 2), localToken.startOffset(), localToken.endOffset(), str2);
    if (str2 == ACRONYM_TYPE)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; i < str1.length(); i++)
      {
        char c = str1.charAt(i);
        if (c != '.')
          localStringBuffer.append(c);
      }
      return new Token(localStringBuffer.toString(), localToken.startOffset(), localToken.endOffset(), str2);
    }
    return localToken;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.standard.StandardFilter
 * JD-Core Version:    0.6.2
 */