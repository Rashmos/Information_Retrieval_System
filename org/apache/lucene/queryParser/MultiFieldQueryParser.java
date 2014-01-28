package org.apache.lucene.queryParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class MultiFieldQueryParser extends QueryParser
{
  public static final int NORMAL_FIELD = 0;
  public static final int REQUIRED_FIELD = 1;
  public static final int PROHIBITED_FIELD = 2;

  public MultiFieldQueryParser(QueryParserTokenManager paramQueryParserTokenManager)
  {
    super(paramQueryParserTokenManager);
  }

  public MultiFieldQueryParser(CharStream paramCharStream)
  {
    super(paramCharStream);
  }

  public MultiFieldQueryParser(String paramString, Analyzer paramAnalyzer)
  {
    super(paramString, paramAnalyzer);
  }

  public static Query parse(String paramString, String[] paramArrayOfString, Analyzer paramAnalyzer)
    throws ParseException
  {
    BooleanQuery localBooleanQuery = new BooleanQuery();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      Query localQuery = QueryParser.parse(paramString, paramArrayOfString[i], paramAnalyzer);
      localBooleanQuery.add(localQuery, false, false);
    }
    return localBooleanQuery;
  }

  public static Query parse(String paramString, String[] paramArrayOfString, int[] paramArrayOfInt, Analyzer paramAnalyzer)
    throws ParseException
  {
    BooleanQuery localBooleanQuery = new BooleanQuery();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      Query localQuery = QueryParser.parse(paramString, paramArrayOfString[i], paramAnalyzer);
      int j = paramArrayOfInt[i];
      switch (j)
      {
      case 1:
        localBooleanQuery.add(localQuery, true, false);
        break;
      case 2:
        localBooleanQuery.add(localQuery, false, true);
        break;
      default:
        localBooleanQuery.add(localQuery, false, false);
      }
    }
    return localBooleanQuery;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.queryParser.MultiFieldQueryParser
 * JD-Core Version:    0.6.2
 */