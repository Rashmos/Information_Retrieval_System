package org.apache.lucene.queryParser;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

public class QueryParser
  implements QueryParserConstants
{
  Analyzer analyzer;
  String field;
  int phraseSlop = 0;
  private static final int CONJ_NONE = 0;
  private static final int CONJ_AND = 1;
  private static final int CONJ_OR = 2;
  private static final int MOD_NONE = 0;
  private static final int MOD_NOT = 10;
  private static final int MOD_REQ = 11;
  public QueryParserTokenManager token_source;
  public Token token;
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos;
  private Token jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  private final int[] jj_la1 = new int[15];
  private final int[] jj_la1_0 = { 384, 384, 3584, 3584, 32710528, 32706560, 20054016, 262144, 262144, 32768, 12582912, 32768, 524288, 32768, 32702464 };
  private final JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;
  private Vector jj_expentries = new Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  public static Query parse(String paramString1, String paramString2, Analyzer paramAnalyzer)
    throws ParseException
  {
    try
    {
      QueryParser localQueryParser = new QueryParser(paramString2, paramAnalyzer);
      return localQueryParser.parse(paramString1);
    }
    catch (TokenMgrError localTokenMgrError)
    {
      throw new ParseException(localTokenMgrError.getMessage());
    }
  }

  public QueryParser(String paramString, Analyzer paramAnalyzer)
  {
    this(new FastCharStream(new StringReader("")));
    this.analyzer = paramAnalyzer;
    this.field = paramString;
  }

  public Query parse(String paramString)
    throws ParseException, TokenMgrError
  {
    ReInit(new FastCharStream(new StringReader(paramString)));
    return Query(this.field);
  }

  public void setPhraseSlop(int paramInt)
  {
    this.phraseSlop = paramInt;
  }

  public int getPhraseSlop()
  {
    return this.phraseSlop;
  }

  private void addClause(Vector paramVector, int paramInt1, int paramInt2, Query paramQuery)
  {
    if (paramInt1 == 1)
    {
      BooleanClause localBooleanClause = (BooleanClause)paramVector.elementAt(paramVector.size() - 1);
      if (!localBooleanClause.prohibited)
        localBooleanClause.required = true;
    }
    if (paramQuery == null)
      return;
    boolean bool2 = paramInt2 == 10;
    boolean bool1 = paramInt2 == 11;
    if ((paramInt1 == 1) && (!bool2))
      bool1 = true;
    paramVector.addElement(new BooleanClause(paramQuery, bool1, bool2));
  }

  private Query getFieldQuery(String paramString1, Analyzer paramAnalyzer, String paramString2)
  {
    TokenStream localTokenStream = paramAnalyzer.tokenStream(paramString1, new StringReader(paramString2));
    Vector localVector = new Vector();
    while (true)
    {
      org.apache.lucene.analysis.Token localToken;
      try
      {
        localToken = localTokenStream.next();
      }
      catch (IOException localIOException)
      {
        localToken = null;
      }
      if (localToken == null)
        break;
      localVector.addElement(localToken.termText());
    }
    if (localVector.size() == 0)
      return null;
    if (localVector.size() == 1)
      return new TermQuery(new Term(paramString1, (String)localVector.elementAt(0)));
    PhraseQuery localPhraseQuery = new PhraseQuery();
    localPhraseQuery.setSlop(this.phraseSlop);
    for (int i = 0; i < localVector.size(); i++)
      localPhraseQuery.add(new Term(paramString1, (String)localVector.elementAt(i)));
    return localPhraseQuery;
  }

  private Query getRangeQuery(String paramString1, Analyzer paramAnalyzer, String paramString2, boolean paramBoolean)
  {
    TokenStream localTokenStream = paramAnalyzer.tokenStream(paramString1, new StringReader(paramString2));
    Term[] arrayOfTerm = new Term[2];
    for (int i = 0; i < 2; i++)
    {
      org.apache.lucene.analysis.Token localToken;
      try
      {
        localToken = localTokenStream.next();
      }
      catch (IOException localIOException)
      {
        localToken = null;
      }
      if (localToken != null)
      {
        String str = localToken.termText();
        if (!str.equalsIgnoreCase("NULL"))
          arrayOfTerm[i] = new Term(paramString1, str);
      }
    }
    return new RangeQuery(arrayOfTerm[0], arrayOfTerm[1], paramBoolean);
  }

  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    QueryParser localQueryParser = new QueryParser("field", new SimpleAnalyzer());
    Query localQuery = localQueryParser.parse(paramArrayOfString[0]);
    System.out.println(localQuery.toString("field"));
  }

  public final int Conjunction()
    throws ParseException
  {
    int i = 0;
    switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
    {
    case 7:
    case 8:
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 7:
        jj_consume_token(7);
        i = 1;
        break;
      case 8:
        jj_consume_token(8);
        i = 2;
        break;
      default:
        this.jj_la1[0] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      this.jj_la1[1] = this.jj_gen;
    }
    return i;
  }

  public final int Modifiers()
    throws ParseException
  {
    int i = 0;
    switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
    {
    case 9:
    case 10:
    case 11:
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 10:
        jj_consume_token(10);
        i = 11;
        break;
      case 11:
        jj_consume_token(11);
        i = 10;
        break;
      case 9:
        jj_consume_token(9);
        i = 10;
        break;
      default:
        this.jj_la1[2] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      this.jj_la1[3] = this.jj_gen;
    }
    return i;
  }

  public final Query Query(String paramString)
    throws ParseException
  {
    Vector localVector = new Vector();
    Object localObject = null;
    int j = Modifiers();
    Query localQuery = Clause(paramString);
    addClause(localVector, 0, j, localQuery);
    if (j == 0)
      localObject = localQuery;
    while (true)
    {
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 16:
      case 17:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
        break;
      case 13:
      case 14:
      case 15:
      case 18:
      case 19:
      default:
        this.jj_la1[4] = this.jj_gen;
        break;
      }
      int i = Conjunction();
      j = Modifiers();
      localQuery = Clause(paramString);
      addClause(localVector, i, j, localQuery);
    }
    if ((localVector.size() == 1) && (localObject != null))
      return localObject;
    BooleanQuery localBooleanQuery = new BooleanQuery();
    for (int k = 0; k < localVector.size(); k++)
      localBooleanQuery.add((BooleanClause)localVector.elementAt(k));
    return localBooleanQuery;
  }

  public final Query Clause(String paramString)
    throws ParseException
  {
    Token localToken = null;
    if (jj_2_1(2))
    {
      localToken = jj_consume_token(17);
      jj_consume_token(14);
      paramString = localToken.image;
    }
    Query localQuery;
    switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
    {
    case 16:
    case 17:
    case 20:
    case 21:
    case 22:
    case 23:
    case 24:
      localQuery = Term(paramString);
      break;
    case 12:
      jj_consume_token(12);
      localQuery = Query(paramString);
      jj_consume_token(13);
      break;
    case 13:
    case 14:
    case 15:
    case 18:
    case 19:
    default:
      this.jj_la1[5] = this.jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    return localQuery;
  }

  public final Query Term(String paramString)
    throws ParseException
  {
    Token localToken2 = null;
    Token localToken3 = null;
    int i = 0;
    int j = 0;
    int k = 0;
    boolean bool = false;
    Token localToken1;
    Object localObject;
    switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
    {
    case 17:
    case 20:
    case 21:
    case 24:
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 17:
        localToken1 = jj_consume_token(17);
        break;
      case 20:
        localToken1 = jj_consume_token(20);
        i = 1;
        break;
      case 21:
        localToken1 = jj_consume_token(21);
        j = 1;
        break;
      case 24:
        localToken1 = jj_consume_token(24);
        break;
      case 18:
      case 19:
      case 22:
      case 23:
      default:
        this.jj_la1[6] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 18:
        jj_consume_token(18);
        k = 1;
        break;
      default:
        this.jj_la1[7] = this.jj_gen;
      }
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 15:
        jj_consume_token(15);
        localToken2 = jj_consume_token(24);
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
        {
        case 18:
          jj_consume_token(18);
          k = 1;
          break;
        default:
          this.jj_la1[8] = this.jj_gen;
        }
        break;
      default:
        this.jj_la1[9] = this.jj_gen;
      }
      if (j != 0)
        localObject = new WildcardQuery(new Term(paramString, localToken1.image));
      else if (i != 0)
        localObject = new PrefixQuery(new Term(paramString, localToken1.image.substring(0, localToken1.image.length() - 1)));
      else if (k != 0)
        localObject = new FuzzyQuery(new Term(paramString, localToken1.image));
      else
        localObject = getFieldQuery(paramString, this.analyzer, localToken1.image);
      break;
    case 22:
    case 23:
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 22:
        localToken1 = jj_consume_token(22);
        bool = true;
        break;
      case 23:
        localToken1 = jj_consume_token(23);
        break;
      default:
        this.jj_la1[10] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 15:
        jj_consume_token(15);
        localToken2 = jj_consume_token(24);
        break;
      default:
        this.jj_la1[11] = this.jj_gen;
      }
      localObject = getRangeQuery(paramString, this.analyzer, localToken1.image.substring(1, localToken1.image.length() - 1), bool);
      break;
    case 16:
      localToken1 = jj_consume_token(16);
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 19:
        localToken3 = jj_consume_token(19);
        break;
      default:
        this.jj_la1[12] = this.jj_gen;
      }
      switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
      {
      case 15:
        jj_consume_token(15);
        localToken2 = jj_consume_token(24);
        break;
      default:
        this.jj_la1[13] = this.jj_gen;
      }
      localObject = getFieldQuery(paramString, this.analyzer, localToken1.image.substring(1, localToken1.image.length() - 1));
      if ((localToken3 != null) && ((localObject instanceof PhraseQuery)))
        try
        {
          int m = Float.valueOf(localToken3.image.substring(1)).intValue();
          ((PhraseQuery)localObject).setSlop(m);
        }
        catch (Exception localException1)
        {
        }
      break;
    case 18:
    case 19:
    default:
      this.jj_la1[14] = this.jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    if (localToken2 != null)
    {
      float f = 1.0F;
      try
      {
        f = Float.valueOf(localToken2.image).floatValue();
      }
      catch (Exception localException2)
      {
      }
      ((Query)localObject).setBoost(f);
    }
    return localObject;
  }

  private final boolean jj_2_1(int paramInt)
  {
    this.jj_la = paramInt;
    this.jj_lastpos = (this.jj_scanpos = this.token);
    boolean bool = !jj_3_1();
    jj_save(0, paramInt);
    return bool;
  }

  private final boolean jj_3_1()
  {
    if (jj_scan_token(17))
      return true;
    if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos))
      return false;
    if (jj_scan_token(14))
      return true;
    return (this.jj_la != 0) || (this.jj_scanpos != this.jj_lastpos);
  }

  public QueryParser(CharStream paramCharStream)
  {
    this.token_source = new QueryParserTokenManager(paramCharStream);
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 15; i++)
      this.jj_la1[i] = -1;
    for (int j = 0; j < this.jj_2_rtns.length; j++)
      this.jj_2_rtns[j] = new JJCalls();
  }

  public void ReInit(CharStream paramCharStream)
  {
    this.token_source.ReInit(paramCharStream);
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 15; i++)
      this.jj_la1[i] = -1;
    for (int j = 0; j < this.jj_2_rtns.length; j++)
      this.jj_2_rtns[j] = new JJCalls();
  }

  public QueryParser(QueryParserTokenManager paramQueryParserTokenManager)
  {
    this.token_source = paramQueryParserTokenManager;
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 15; i++)
      this.jj_la1[i] = -1;
    for (int j = 0; j < this.jj_2_rtns.length; j++)
      this.jj_2_rtns[j] = new JJCalls();
  }

  public void ReInit(QueryParserTokenManager paramQueryParserTokenManager)
  {
    this.token_source = paramQueryParserTokenManager;
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 15; i++)
      this.jj_la1[i] = -1;
    for (int j = 0; j < this.jj_2_rtns.length; j++)
      this.jj_2_rtns[j] = new JJCalls();
  }

  private final Token jj_consume_token(int paramInt)
    throws ParseException
  {
    Token localToken;
    if ((localToken = this.token).next != null)
      this.token = this.token.next;
    else
      this.token = (this.token.next = this.token_source.getNextToken());
    this.jj_ntk = -1;
    if (this.token.kind == paramInt)
    {
      this.jj_gen += 1;
      if (++this.jj_gc > 100)
      {
        this.jj_gc = 0;
        for (int i = 0; i < this.jj_2_rtns.length; i++)
          for (JJCalls localJJCalls = this.jj_2_rtns[i]; localJJCalls != null; localJJCalls = localJJCalls.next)
            if (localJJCalls.gen < this.jj_gen)
              localJJCalls.first = null;
      }
      return this.token;
    }
    this.token = localToken;
    this.jj_kind = paramInt;
    throw generateParseException();
  }

  private final boolean jj_scan_token(int paramInt)
  {
    if (this.jj_scanpos == this.jj_lastpos)
    {
      this.jj_la -= 1;
      if (this.jj_scanpos.next == null)
        this.jj_lastpos = (this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken());
      else
        this.jj_lastpos = (this.jj_scanpos = this.jj_scanpos.next);
    }
    else
    {
      this.jj_scanpos = this.jj_scanpos.next;
    }
    if (this.jj_rescan)
    {
      int i = 0;
      for (Token localToken = this.token; (localToken != null) && (localToken != this.jj_scanpos); localToken = localToken.next)
        i++;
      if (localToken != null)
        jj_add_error_token(paramInt, i);
    }
    return this.jj_scanpos.kind != paramInt;
  }

  public final Token getNextToken()
  {
    if (this.token.next != null)
      this.token = this.token.next;
    else
      this.token = (this.token.next = this.token_source.getNextToken());
    this.jj_ntk = -1;
    this.jj_gen += 1;
    return this.token;
  }

  public final Token getToken(int paramInt)
  {
    Token localToken = this.lookingAhead ? this.jj_scanpos : this.token;
    for (int i = 0; i < paramInt; i++)
      if (localToken.next != null)
        localToken = localToken.next;
      else
        localToken = localToken.next = this.token_source.getNextToken();
    return localToken;
  }

  private final int jj_ntk()
  {
    if ((this.jj_nt = this.token.next) == null)
      return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
    return this.jj_ntk = this.jj_nt.kind;
  }

  private void jj_add_error_token(int paramInt1, int paramInt2)
  {
    if (paramInt2 >= 100)
      return;
    if (paramInt2 == this.jj_endpos + 1)
    {
      this.jj_lasttokens[(this.jj_endpos++)] = paramInt1;
    }
    else if (this.jj_endpos != 0)
    {
      this.jj_expentry = new int[this.jj_endpos];
      for (int i = 0; i < this.jj_endpos; i++)
        this.jj_expentry[i] = this.jj_lasttokens[i];
      int j = 0;
      Enumeration localEnumeration = this.jj_expentries.elements();
      while (localEnumeration.hasMoreElements())
      {
        int[] arrayOfInt = (int[])localEnumeration.nextElement();
        if (arrayOfInt.length == this.jj_expentry.length)
        {
          j = 1;
          for (int k = 0; k < this.jj_expentry.length; k++)
            if (arrayOfInt[k] != this.jj_expentry[k])
            {
              j = 0;
              break;
            }
          if (j != 0)
            break;
        }
      }
      if (j == 0)
        this.jj_expentries.addElement(this.jj_expentry);
      if (paramInt2 != 0)
      {
        int tmp207_206 = paramInt2;
        this.jj_endpos = tmp207_206;
        this.jj_lasttokens[(tmp207_206 - 1)] = paramInt1;
      }
    }
  }

  public final ParseException generateParseException()
  {
    this.jj_expentries.removeAllElements();
    boolean[] arrayOfBoolean = new boolean[25];
    for (int i = 0; i < 25; i++)
      arrayOfBoolean[i] = false;
    if (this.jj_kind >= 0)
    {
      arrayOfBoolean[this.jj_kind] = true;
      this.jj_kind = -1;
    }
    for (int j = 0; j < 15; j++)
      if (this.jj_la1[j] == this.jj_gen)
        for (k = 0; k < 32; k++)
          if ((this.jj_la1_0[j] & 1 << k) != 0)
            arrayOfBoolean[k] = true;
    for (int k = 0; k < 25; k++)
      if (arrayOfBoolean[k] != 0)
      {
        this.jj_expentry = new int[1];
        this.jj_expentry[0] = k;
        this.jj_expentries.addElement(this.jj_expentry);
      }
    this.jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] arrayOfInt = new int[this.jj_expentries.size()][];
    for (int m = 0; m < this.jj_expentries.size(); m++)
      arrayOfInt[m] = ((int[])this.jj_expentries.elementAt(m));
    return new ParseException(this.token, arrayOfInt, QueryParserConstants.tokenImage);
  }

  public final void enable_tracing()
  {
  }

  public final void disable_tracing()
  {
  }

  private final void jj_rescan_token()
  {
    this.jj_rescan = true;
    for (int i = 0; i < 1; i++)
    {
      JJCalls localJJCalls = this.jj_2_rtns[i];
      do
      {
        if (localJJCalls.gen > this.jj_gen)
        {
          this.jj_la = localJJCalls.arg;
          this.jj_lastpos = (this.jj_scanpos = localJJCalls.first);
          switch (i)
          {
          case 0:
            jj_3_1();
          }
        }
        localJJCalls = localJJCalls.next;
      }
      while (localJJCalls != null);
    }
    this.jj_rescan = false;
  }

  private final void jj_save(int paramInt1, int paramInt2)
  {
    for (JJCalls localJJCalls = this.jj_2_rtns[paramInt1]; localJJCalls.gen > this.jj_gen; localJJCalls = localJJCalls.next)
      if (localJJCalls.next == null)
      {
        localJJCalls = localJJCalls.next = new JJCalls();
        break;
      }
    localJJCalls.gen = (this.jj_gen + paramInt2 - this.jj_la);
    localJJCalls.first = this.token;
    localJJCalls.arg = paramInt2;
  }

  static final class JJCalls
  {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.queryParser.QueryParser
 * JD-Core Version:    0.6.2
 */