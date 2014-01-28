package org.apache.lucene.analysis;

import java.io.IOException;
import java.util.Hashtable;

public final class StopFilter extends TokenFilter
{
  private Hashtable table;

  public StopFilter(TokenStream paramTokenStream, String[] paramArrayOfString)
  {
    this.input = paramTokenStream;
    this.table = makeStopTable(paramArrayOfString);
  }

  public StopFilter(TokenStream paramTokenStream, Hashtable paramHashtable)
  {
    this.input = paramTokenStream;
    this.table = paramHashtable;
  }

  public static final Hashtable makeStopTable(String[] paramArrayOfString)
  {
    Hashtable localHashtable = new Hashtable(paramArrayOfString.length);
    for (int i = 0; i < paramArrayOfString.length; i++)
      localHashtable.put(paramArrayOfString[i], paramArrayOfString[i]);
    return localHashtable;
  }

  public final Token next()
    throws IOException
  {
    for (Token localToken = this.input.next(); localToken != null; localToken = this.input.next())
      if (this.table.get(localToken.termText) == null)
        return localToken;
    return null;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.StopFilter
 * JD-Core Version:    0.6.2
 */