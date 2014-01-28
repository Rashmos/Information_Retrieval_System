package org.apache.lucene.analysis.standard;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;
import org.apache.lucene.analysis.Tokenizer;

public class StandardTokenizer extends Tokenizer
  implements StandardTokenizerConstants
{
  public StandardTokenizerTokenManager token_source;
  public Token token;
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  private final int[] jj_la1 = new int[1];
  private final int[] jj_la1_0 = { 255 };
  private Vector jj_expentries = new Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public StandardTokenizer(Reader paramReader)
  {
    this(new FastCharStream(paramReader));
    this.input = paramReader;
  }

  public final org.apache.lucene.analysis.Token next()
    throws ParseException, IOException
  {
    Token localToken = null;
    switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
    {
    case 1:
      localToken = jj_consume_token(1);
      break;
    case 2:
      localToken = jj_consume_token(2);
      break;
    case 3:
      localToken = jj_consume_token(3);
      break;
    case 4:
      localToken = jj_consume_token(4);
      break;
    case 5:
      localToken = jj_consume_token(5);
      break;
    case 6:
      localToken = jj_consume_token(6);
      break;
    case 7:
      localToken = jj_consume_token(7);
      break;
    case 0:
      localToken = jj_consume_token(0);
      break;
    default:
      this.jj_la1[0] = this.jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    if (localToken.kind == 0)
      return null;
    return new org.apache.lucene.analysis.Token(localToken.image, localToken.beginColumn, localToken.endColumn, StandardTokenizerConstants.tokenImage[localToken.kind]);
  }

  public StandardTokenizer(CharStream paramCharStream)
  {
    this.token_source = new StandardTokenizerTokenManager(paramCharStream);
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 1; i++)
      this.jj_la1[i] = -1;
  }

  public void ReInit(CharStream paramCharStream)
  {
    this.token_source.ReInit(paramCharStream);
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 1; i++)
      this.jj_la1[i] = -1;
  }

  public StandardTokenizer(StandardTokenizerTokenManager paramStandardTokenizerTokenManager)
  {
    this.token_source = paramStandardTokenizerTokenManager;
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 1; i++)
      this.jj_la1[i] = -1;
  }

  public void ReInit(StandardTokenizerTokenManager paramStandardTokenizerTokenManager)
  {
    this.token_source = paramStandardTokenizerTokenManager;
    this.token = new Token();
    this.jj_ntk = -1;
    this.jj_gen = 0;
    for (int i = 0; i < 1; i++)
      this.jj_la1[i] = -1;
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
      return this.token;
    }
    this.token = localToken;
    this.jj_kind = paramInt;
    throw generateParseException();
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
    Token localToken = this.token;
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

  public final ParseException generateParseException()
  {
    this.jj_expentries.removeAllElements();
    boolean[] arrayOfBoolean = new boolean[14];
    for (int i = 0; i < 14; i++)
      arrayOfBoolean[i] = false;
    if (this.jj_kind >= 0)
    {
      arrayOfBoolean[this.jj_kind] = true;
      this.jj_kind = -1;
    }
    for (int j = 0; j < 1; j++)
      if (this.jj_la1[j] == this.jj_gen)
        for (k = 0; k < 32; k++)
          if ((this.jj_la1_0[j] & 1 << k) != 0)
            arrayOfBoolean[k] = true;
    for (int k = 0; k < 14; k++)
      if (arrayOfBoolean[k] != 0)
      {
        this.jj_expentry = new int[1];
        this.jj_expentry[0] = k;
        this.jj_expentries.addElement(this.jj_expentry);
      }
    int[][] arrayOfInt = new int[this.jj_expentries.size()][];
    for (int m = 0; m < this.jj_expentries.size(); m++)
      arrayOfInt[m] = ((int[])this.jj_expentries.elementAt(m));
    return new ParseException(this.token, arrayOfInt, StandardTokenizerConstants.tokenImage);
  }

  public final void enable_tracing()
  {
  }

  public final void disable_tracing()
  {
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.standard.StandardTokenizer
 * JD-Core Version:    0.6.2
 */