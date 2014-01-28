package org.apache.lucene.analysis;

import java.io.Reader;

public final class SimpleAnalyzer extends Analyzer
{
  public final TokenStream tokenStream(String paramString, Reader paramReader)
  {
    return new LowerCaseTokenizer(paramReader);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.SimpleAnalyzer
 * JD-Core Version:    0.6.2
 */