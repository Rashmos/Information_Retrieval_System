package org.apache.lucene.analysis.standard;

import java.io.Reader;
import java.util.Hashtable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;

public class StandardAnalyzer extends Analyzer
{
  private Hashtable stopTable;
  public static final String[] STOP_WORDS = { "a", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "s", "such", "t", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with" };

  public StandardAnalyzer()
  {
    this(STOP_WORDS);
  }

  public StandardAnalyzer(String[] paramArrayOfString)
  {
    this.stopTable = StopFilter.makeStopTable(paramArrayOfString);
  }

  public final TokenStream tokenStream(String paramString, Reader paramReader)
  {
    Object localObject = new StandardTokenizer(paramReader);
    localObject = new StandardFilter((TokenStream)localObject);
    localObject = new LowerCaseFilter((TokenStream)localObject);
    localObject = new StopFilter((TokenStream)localObject, this.stopTable);
    return localObject;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.standard.StandardAnalyzer
 * JD-Core Version:    0.6.2
 */