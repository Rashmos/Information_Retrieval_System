package org.apache.lucene.queryParser;

import java.io.IOException;
import java.io.PrintStream;

public class QueryParserTokenManager
  implements QueryParserConstants
{
  public PrintStream debugStream = System.out;
  static final long[] jjbitVec0 = { -2L, -1L, -1L, -1L };
  static final long[] jjbitVec2 = { 0L, 0L, -1L, -1L };
  static final int[] jjnextStates = { 26, 29, 30, 33, 34, 31, 27, 20, 21, 23, 24, 29, 30, 31, 28, 32, 35, 15, 16, 0, 1 };
  public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, null, null, "+", "-", "(", ")", ":", "^", null, null, "~", null, null, null, null, null, null };
  public static final String[] lexStateNames = { "Boost", "DEFAULT" };
  public static final int[] jjnewLexState = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, 1 };
  static final long[] jjtoToken = { 33554305L };
  static final long[] jjtoSkip = { 64L };
  private CharStream input_stream;
  private final int[] jjrounds = new int[37];
  private final int[] jjstateSet = new int[74];
  protected char curChar;
  int curLexState = 1;
  int defaultLexState = 1;
  int jjnewStateCnt;
  int jjround;
  int jjmatchedPos;
  int jjmatchedKind;

  public void setDebugStream(PrintStream paramPrintStream)
  {
    this.debugStream = paramPrintStream;
  }

  private final int jjStopStringLiteralDfa_1(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    }
    return -1;
  }

  private final int jjStartNfa_1(int paramInt, long paramLong)
  {
    return jjMoveNfa_1(jjStopStringLiteralDfa_1(paramInt, paramLong), paramInt + 1);
  }

  private final int jjStopAtPos(int paramInt1, int paramInt2)
  {
    this.jjmatchedKind = paramInt2;
    this.jjmatchedPos = paramInt1;
    return paramInt1 + 1;
  }

  private final int jjStartNfaWithStates_1(int paramInt1, int paramInt2, int paramInt3)
  {
    this.jjmatchedKind = paramInt2;
    this.jjmatchedPos = paramInt1;
    try
    {
      this.curChar = this.input_stream.readChar();
    }
    catch (IOException localIOException)
    {
      return paramInt1 + 1;
    }
    return jjMoveNfa_1(paramInt3, paramInt1 + 1);
  }

  private final int jjMoveStringLiteralDfa0_1()
  {
    switch (this.curChar)
    {
    case '(':
      return jjStopAtPos(0, 12);
    case ')':
      return jjStopAtPos(0, 13);
    case '+':
      return jjStopAtPos(0, 10);
    case '-':
      return jjStopAtPos(0, 11);
    case ':':
      return jjStopAtPos(0, 14);
    case '^':
      return jjStopAtPos(0, 15);
    case '~':
      return jjStartNfaWithStates_1(0, 18, 18);
    }
    return jjMoveNfa_1(0, 0);
  }

  private final void jjCheckNAdd(int paramInt)
  {
    if (this.jjrounds[paramInt] != this.jjround)
    {
      this.jjstateSet[(this.jjnewStateCnt++)] = paramInt;
      this.jjrounds[paramInt] = this.jjround;
    }
  }

  private final void jjAddStates(int paramInt1, int paramInt2)
  {
    do
      this.jjstateSet[(this.jjnewStateCnt++)] = jjnextStates[paramInt1];
    while (paramInt1++ != paramInt2);
  }

  private final void jjCheckNAddTwoStates(int paramInt1, int paramInt2)
  {
    jjCheckNAdd(paramInt1);
    jjCheckNAdd(paramInt2);
  }

  private final void jjCheckNAddStates(int paramInt1, int paramInt2)
  {
    do
      jjCheckNAdd(jjnextStates[paramInt1]);
    while (paramInt1++ != paramInt2);
  }

  private final void jjCheckNAddStates(int paramInt)
  {
    jjCheckNAdd(jjnextStates[paramInt]);
    jjCheckNAdd(jjnextStates[(paramInt + 1)]);
  }

  private final int jjMoveNfa_1(int paramInt1, int paramInt2)
  {
    int i = 0;
    this.jjnewStateCnt = 37;
    int j = 1;
    this.jjstateSet[0] = paramInt1;
    int k = 2147483647;
    while (true)
    {
      if (++this.jjround == 2147483647)
        ReInitRounds();
      long l1;
      if (this.curChar < '@')
      {
        l1 = 1L << this.curChar;
        do
          switch (this.jjstateSet[(--j)])
          {
          case 0:
            if ((0xFFFFFDFF & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddStates(0, 6);
            }
            else if ((0x200 & l1) != 0L)
            {
              if (k > 6)
                k = 6;
            }
            else if (this.curChar == '"')
            {
              jjCheckNAdd(15);
            }
            else if ((this.curChar == '!') && (k > 9))
            {
              k = 9;
            }
            if (this.curChar == '&')
              this.jjstateSet[(this.jjnewStateCnt++)] = 4;
            break;
          case 4:
            if ((this.curChar == '&') && (k > 7))
              k = 7;
            break;
          case 5:
            if (this.curChar == '&')
              this.jjstateSet[(this.jjnewStateCnt++)] = 4;
            break;
          case 13:
            if ((this.curChar == '!') && (k > 9))
              k = 9;
            break;
          case 14:
            if (this.curChar == '"')
              jjCheckNAdd(15);
            break;
          case 15:
            if ((0xFFFFFFFF & l1) != 0L)
              jjCheckNAddTwoStates(15, 16);
            break;
          case 16:
            if ((this.curChar == '"') && (k > 16))
              k = 16;
            break;
          case 18:
            if ((0x0 & l1) != 0L)
            {
              if (k > 19)
                k = 19;
              this.jjstateSet[(this.jjnewStateCnt++)] = 18;
            }
            break;
          case 20:
            jjAddStates(7, 8);
            break;
          case 23:
            jjAddStates(9, 10);
            break;
          case 25:
            if ((0xFFFFFDFF & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddStates(0, 6);
            }
            break;
          case 26:
            if ((0xFFFFFDFF & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddTwoStates(26, 27);
            }
            break;
          case 28:
            if ((0x0 & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddTwoStates(26, 27);
            }
            break;
          case 29:
            if ((0xFFFFFDFF & l1) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 30:
            if ((this.curChar == '*') && (k > 20))
              k = 20;
            break;
          case 32:
            if ((0x0 & l1) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 33:
            if ((0xFFFFFDFF & l1) != 0L)
            {
              if (k > 21)
                k = 21;
              jjCheckNAddTwoStates(33, 34);
            }
            break;
          case 35:
            if ((0x0 & l1) != 0L)
            {
              if (k > 21)
                k = 21;
              jjCheckNAddTwoStates(33, 34);
            }
            break;
          case 1:
          case 2:
          case 3:
          case 6:
          case 7:
          case 8:
          case 9:
          case 10:
          case 11:
          case 12:
          case 17:
          case 19:
          case 21:
          case 22:
          case 24:
          case 27:
          case 31:
          case 34:
          }
        while (j != i);
      }
      else if (this.curChar < '')
      {
        l1 = 1L << (this.curChar & 0x3F);
        do
          switch (this.jjstateSet[(--j)])
          {
          case 0:
            if ((0x97FFFFFF & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddStates(0, 6);
            }
            else if (this.curChar == '{')
            {
              jjCheckNAdd(23);
            }
            else if (this.curChar == '[')
            {
              jjCheckNAdd(20);
            }
            else if (this.curChar == '~')
            {
              this.jjstateSet[(this.jjnewStateCnt++)] = 18;
            }
            if (this.curChar == '\\')
              jjCheckNAddStates(14, 16);
            else if (this.curChar == 'N')
              this.jjstateSet[(this.jjnewStateCnt++)] = 11;
            else if (this.curChar == '|')
              this.jjstateSet[(this.jjnewStateCnt++)] = 8;
            else if (this.curChar == 'O')
              this.jjstateSet[(this.jjnewStateCnt++)] = 6;
            else if (this.curChar == 'A')
              this.jjstateSet[(this.jjnewStateCnt++)] = 2;
            break;
          case 1:
            if ((this.curChar == 'D') && (k > 7))
              k = 7;
            break;
          case 2:
            if (this.curChar == 'N')
              this.jjstateSet[(this.jjnewStateCnt++)] = 1;
            break;
          case 3:
            if (this.curChar == 'A')
              this.jjstateSet[(this.jjnewStateCnt++)] = 2;
            break;
          case 6:
            if ((this.curChar == 'R') && (k > 8))
              k = 8;
            break;
          case 7:
            if (this.curChar == 'O')
              this.jjstateSet[(this.jjnewStateCnt++)] = 6;
            break;
          case 8:
            if ((this.curChar == '|') && (k > 8))
              k = 8;
            break;
          case 9:
            if (this.curChar == '|')
              this.jjstateSet[(this.jjnewStateCnt++)] = 8;
            break;
          case 10:
            if ((this.curChar == 'T') && (k > 9))
              k = 9;
            break;
          case 11:
            if (this.curChar == 'O')
              this.jjstateSet[(this.jjnewStateCnt++)] = 10;
            break;
          case 12:
            if (this.curChar == 'N')
              this.jjstateSet[(this.jjnewStateCnt++)] = 11;
            break;
          case 15:
            jjAddStates(17, 18);
            break;
          case 17:
            if (this.curChar == '~')
              this.jjstateSet[(this.jjnewStateCnt++)] = 18;
            break;
          case 19:
            if (this.curChar == '[')
              jjCheckNAdd(20);
            break;
          case 20:
            if ((0xDFFFFFFF & l1) != 0L)
              jjCheckNAddTwoStates(20, 21);
            break;
          case 21:
            if ((this.curChar == ']') && (k > 22))
              k = 22;
            break;
          case 22:
            if (this.curChar == '{')
              jjCheckNAdd(23);
            break;
          case 23:
            if ((0xFFFFFFFF & l1) != 0L)
              jjCheckNAddTwoStates(23, 24);
            break;
          case 24:
            if ((this.curChar == '}') && (k > 23))
              k = 23;
            break;
          case 25:
            if ((0x97FFFFFF & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddStates(0, 6);
            }
            break;
          case 26:
            if ((0x97FFFFFF & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddTwoStates(26, 27);
            }
            break;
          case 27:
            if (this.curChar == '\\')
              jjCheckNAddTwoStates(28, 28);
            break;
          case 28:
            if ((0x78000000 & l1) != 0L)
            {
              if (k > 17)
                k = 17;
              jjCheckNAddTwoStates(26, 27);
            }
            break;
          case 29:
            if ((0x97FFFFFF & l1) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 31:
            if (this.curChar == '\\')
              jjCheckNAddTwoStates(32, 32);
            break;
          case 32:
            if ((0x78000000 & l1) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 33:
            if ((0x97FFFFFF & l1) != 0L)
            {
              if (k > 21)
                k = 21;
              jjCheckNAddTwoStates(33, 34);
            }
            break;
          case 34:
            if (this.curChar == '\\')
              jjCheckNAddTwoStates(35, 35);
            break;
          case 35:
            if ((0x78000000 & l1) != 0L)
            {
              if (k > 21)
                k = 21;
              jjCheckNAddTwoStates(33, 34);
            }
            break;
          case 36:
            if (this.curChar == '\\')
              jjCheckNAddStates(14, 16);
            break;
          case 4:
          case 5:
          case 13:
          case 14:
          case 16:
          case 18:
          case 30:
          }
        while (j != i);
      }
      else
      {
        int m = this.curChar >> '\b';
        int n = m >> 6;
        long l2 = 1L << (m & 0x3F);
        int i1 = (this.curChar & 0xFF) >> '\006';
        long l3 = 1L << (this.curChar & 0x3F);
        do
          switch (this.jjstateSet[(--j)])
          {
          case 0:
            if (jjCanMove_0(m, n, i1, l2, l3))
            {
              if (k > 17)
                k = 17;
              jjCheckNAddStates(0, 6);
            }
            break;
          case 15:
            if (jjCanMove_0(m, n, i1, l2, l3))
              jjAddStates(17, 18);
            break;
          case 20:
            if (jjCanMove_0(m, n, i1, l2, l3))
              jjAddStates(7, 8);
            break;
          case 23:
            if (jjCanMove_0(m, n, i1, l2, l3))
              jjAddStates(9, 10);
            break;
          case 26:
            if (jjCanMove_0(m, n, i1, l2, l3))
            {
              if (k > 17)
                k = 17;
              jjCheckNAddTwoStates(26, 27);
            }
            break;
          case 29:
            if (jjCanMove_0(m, n, i1, l2, l3))
              jjCheckNAddStates(11, 13);
            break;
          case 33:
            if (jjCanMove_0(m, n, i1, l2, l3))
            {
              if (k > 21)
                k = 21;
              jjCheckNAddTwoStates(33, 34);
            }
            break;
          }
        while (j != i);
      }
      if (k != 2147483647)
      {
        this.jjmatchedKind = k;
        this.jjmatchedPos = paramInt2;
        k = 2147483647;
      }
      paramInt2++;
      if ((j = this.jjnewStateCnt) == (i = 37 - (this.jjnewStateCnt = i)))
        return paramInt2;
      try
      {
        this.curChar = this.input_stream.readChar();
      }
      catch (IOException localIOException)
      {
      }
    }
    return paramInt2;
  }

  private final int jjMoveStringLiteralDfa0_0()
  {
    return jjMoveNfa_0(0, 0);
  }

  private final int jjMoveNfa_0(int paramInt1, int paramInt2)
  {
    int i = 0;
    this.jjnewStateCnt = 3;
    int j = 1;
    this.jjstateSet[0] = paramInt1;
    int k = 2147483647;
    while (true)
    {
      if (++this.jjround == 2147483647)
        ReInitRounds();
      long l1;
      if (this.curChar < '@')
      {
        l1 = 1L << this.curChar;
        do
          switch (this.jjstateSet[(--j)])
          {
          case 0:
            if ((0x0 & l1) != 0L)
            {
              if (k > 24)
                k = 24;
              jjAddStates(19, 20);
            }
            break;
          case 1:
            if (this.curChar == '.')
              jjCheckNAdd(2);
            break;
          case 2:
            if ((0x0 & l1) != 0L)
            {
              if (k > 24)
                k = 24;
              jjCheckNAdd(2);
            }
            break;
          }
        while (j != i);
      }
      else if (this.curChar < '')
      {
        l1 = 1L << (this.curChar & 0x3F);
        do
          switch (this.jjstateSet[(--j)])
          {
          }
        while (j != i);
      }
      else
      {
        int m = this.curChar >> '\b';
        int n = m >> 6;
        long l2 = 1L << (m & 0x3F);
        int i1 = (this.curChar & 0xFF) >> '\006';
        long l3 = 1L << (this.curChar & 0x3F);
        do
          switch (this.jjstateSet[(--j)])
          {
          }
        while (j != i);
      }
      if (k != 2147483647)
      {
        this.jjmatchedKind = k;
        this.jjmatchedPos = paramInt2;
        k = 2147483647;
      }
      paramInt2++;
      if ((j = this.jjnewStateCnt) == (i = 3 - (this.jjnewStateCnt = i)))
        return paramInt2;
      try
      {
        this.curChar = this.input_stream.readChar();
      }
      catch (IOException localIOException)
      {
      }
    }
    return paramInt2;
  }

  private static final boolean jjCanMove_0(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2)
  {
    switch (paramInt1)
    {
    case 0:
      return (jjbitVec2[paramInt3] & paramLong2) != 0L;
    }
    return (jjbitVec0[paramInt2] & paramLong1) != 0L;
  }

  public QueryParserTokenManager(CharStream paramCharStream)
  {
    this.input_stream = paramCharStream;
  }

  public QueryParserTokenManager(CharStream paramCharStream, int paramInt)
  {
    this(paramCharStream);
    SwitchTo(paramInt);
  }

  public void ReInit(CharStream paramCharStream)
  {
    this.jjmatchedPos = (this.jjnewStateCnt = 0);
    this.curLexState = this.defaultLexState;
    this.input_stream = paramCharStream;
    ReInitRounds();
  }

  private final void ReInitRounds()
  {
    this.jjround = -2147483647;
    int i = 37;
    while (i-- > 0)
      this.jjrounds[i] = -2147483648;
  }

  public void ReInit(CharStream paramCharStream, int paramInt)
  {
    ReInit(paramCharStream);
    SwitchTo(paramInt);
  }

  public void SwitchTo(int paramInt)
  {
    if ((paramInt >= 2) || (paramInt < 0))
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + paramInt + ". State unchanged.", 2);
    this.curLexState = paramInt;
  }

  private final Token jjFillToken()
  {
    Token localToken = Token.newToken(this.jjmatchedKind);
    localToken.kind = this.jjmatchedKind;
    String str = jjstrLiteralImages[this.jjmatchedKind];
    localToken.image = (str == null ? this.input_stream.GetImage() : str);
    localToken.beginLine = this.input_stream.getBeginLine();
    localToken.beginColumn = this.input_stream.getBeginColumn();
    localToken.endLine = this.input_stream.getEndLine();
    localToken.endColumn = this.input_stream.getEndColumn();
    return localToken;
  }

  public final Token getNextToken()
  {
    Object localObject = null;
    int i = 0;
    while (true)
    {
      Token localToken;
      try
      {
        this.curChar = this.input_stream.BeginToken();
      }
      catch (IOException localIOException1)
      {
        this.jjmatchedKind = 0;
        localToken = jjFillToken();
        return localToken;
      }
      switch (this.curLexState)
      {
      case 0:
        this.jjmatchedKind = 2147483647;
        this.jjmatchedPos = 0;
        i = jjMoveStringLiteralDfa0_0();
        break;
      case 1:
        this.jjmatchedKind = 2147483647;
        this.jjmatchedPos = 0;
        i = jjMoveStringLiteralDfa0_1();
      }
      if (this.jjmatchedKind == 2147483647)
        break;
      if (this.jjmatchedPos + 1 < i)
        this.input_stream.backup(i - this.jjmatchedPos - 1);
      if ((jjtoToken[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) != 0L)
      {
        localToken = jjFillToken();
        if (jjnewLexState[this.jjmatchedKind] != -1)
          this.curLexState = jjnewLexState[this.jjmatchedKind];
        return localToken;
      }
      if (jjnewLexState[this.jjmatchedKind] != -1)
        this.curLexState = jjnewLexState[this.jjmatchedKind];
    }
    int j = this.input_stream.getEndLine();
    int k = this.input_stream.getEndColumn();
    String str = null;
    boolean bool = false;
    try
    {
      this.input_stream.readChar();
      this.input_stream.backup(1);
    }
    catch (IOException localIOException2)
    {
      bool = true;
      str = i <= 1 ? "" : this.input_stream.GetImage();
      if ((this.curChar == '\n') || (this.curChar == '\r'))
      {
        j++;
        k = 0;
      }
      else
      {
        k++;
      }
    }
    if (!bool)
    {
      this.input_stream.backup(1);
      str = i <= 1 ? "" : this.input_stream.GetImage();
    }
    throw new TokenMgrError(bool, this.curLexState, j, k, str, this.curChar, 0);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.queryParser.QueryParserTokenManager
 * JD-Core Version:    0.6.2
 */