package org.apache.lucene.analysis;

import java.io.Reader;
import java.util.Hashtable;

public final class StopAnalyzer extends Analyzer
{
  private Hashtable stopTable;
  public static final String[] ENGLISH_STOP_WORDS = { "a", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "s", "such", "t", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with" };

  public StopAnalyzer()
  {
    this.stopTable = StopFilter.makeStopTable(ENGLISH_STOP_WORDS);
  }

  public StopAnalyzer(String[] paramArrayOfString)
  {
    this.stopTable = StopFilter.makeStopTable(paramArrayOfString);
  }

  public final TokenStream tokenStream(String paramString, Reader paramReader)
  {
    return new StopFilter(new LowerCaseTokenizer(paramReader), this.stopTable);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.StopAnalyzer
 * JD-Core Version:    0.6.2
 */