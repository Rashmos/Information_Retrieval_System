package org.apache.lucene.analysis.de;

import java.io.File;
import java.io.Reader;
import java.util.Hashtable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public final class GermanAnalyzer extends Analyzer
{
  private String[] GERMAN_STOP_WORDS = { "einer", "eine", "eines", "einem", "einen", "der", "die", "das", "dass", "daß", "du", "er", "sie", "es", "was", "wer", "wie", "wir", "und", "oder", "ohne", "mit", "am", "im", "in", "aus", "auf", "ist", "sein", "war", "wird", "ihr", "ihre", "ihres", "als", "für", "von", "mit", "dich", "dir", "mich", "mir", "mein", "sein", "kein", "durch", "wegen", "wird" };
  private Hashtable stoptable = new Hashtable();
  private Hashtable excltable = new Hashtable();

  public GermanAnalyzer()
  {
    this.stoptable = StopFilter.makeStopTable(this.GERMAN_STOP_WORDS);
  }

  public GermanAnalyzer(String[] paramArrayOfString)
  {
    this.stoptable = StopFilter.makeStopTable(paramArrayOfString);
  }

  public GermanAnalyzer(Hashtable paramHashtable)
  {
    this.stoptable = paramHashtable;
  }

  public GermanAnalyzer(File paramFile)
  {
    this.stoptable = WordlistLoader.getWordtable(paramFile);
  }

  public void setStemExclusionTable(String[] paramArrayOfString)
  {
    this.excltable = StopFilter.makeStopTable(paramArrayOfString);
  }

  public void setStemExclusionTable(Hashtable paramHashtable)
  {
    this.excltable = paramHashtable;
  }

  public void setStemExclusionTable(File paramFile)
  {
    this.excltable = WordlistLoader.getWordtable(paramFile);
  }

  public final TokenStream tokenStream(String paramString, Reader paramReader)
  {
    Object localObject = new StandardTokenizer(paramReader);
    localObject = new StandardFilter((TokenStream)localObject);
    localObject = new StopFilter((TokenStream)localObject, this.stoptable);
    localObject = new GermanStemFilter((TokenStream)localObject, this.excltable);
    localObject = new LowerCaseFilter((TokenStream)localObject);
    return localObject;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.de.GermanAnalyzer
 * JD-Core Version:    0.6.2
 */