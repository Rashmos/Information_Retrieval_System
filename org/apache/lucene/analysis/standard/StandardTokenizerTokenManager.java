package org.apache.lucene.analysis.standard;

import java.io.IOException;
import java.io.PrintStream;

public class StandardTokenizerTokenManager
  implements StandardTokenizerConstants
{
  public PrintStream debugStream = System.out;
  static final long[] jjbitVec0 = { 5632L, 0L, 0L, 0L };
  static final long[] jjbitVec1 = { 0L, 281200098803712L, 0L, 281200098803712L };
  static final long[] jjbitVec2 = { 0L, 4393751543808L, 0L, 287948901175001088L };
  static final long[] jjbitVec3 = { 0L, 281200098803712L, 0L, 280925220896768L };
  static final long[] jjbitVec4 = { 0L, 281200098803712L, 0L, 0L };
  static final long[] jjbitVec5 = { 0L, 67043328L, 0L, 67043328L };
  static final long[] jjbitVec6 = { 0L, 1023L, 0L, 0L };
  static final long[] jjbitVec7 = { 2301339413881290750L, -16384L, 4294967295L, 432345564227567616L };
  static final long[] jjbitVec9 = { 0L, 0L, 0L, -36028797027352577L };
  static final long[] jjbitVec10 = { 0L, -1L, -1L, -1L };
  static final long[] jjbitVec11 = { -1L, -1L, 65535L, 0L };
  static final long[] jjbitVec12 = { -1L, -1L, 0L, 0L };
  static final long[] jjbitVec13 = { 70368744177663L, 0L, 0L, 0L };
  static final int[] jjnextStates = { 21, 22, 23, 27, 28, 30, 31, 35, 36, 37, 38, 44, 45, 46, 47, 57, 58, 1, 2, 4, 5, 11, 12, 60, 61, 63, 64, 67, 68, 1, 2, 11, 12, 17, 18, 41, 42, 65, 66, 6, 7, 8, 9, 15, 16, 32, 33, 39, 40, 48, 49, 52, 53, 54, 55 };
  public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, null, null, null, null, null, null };
  public static final String[] lexStateNames = { "DEFAULT" };
  static final long[] jjtoToken = { 255L };
  static final long[] jjtoSkip = { 8192L };
  private CharStream input_stream;
  private final int[] jjrounds = new int[70];
  private final int[] jjstateSet = new int[''];
  protected char curChar;
  int curLexState = 0;
  int defaultLexState = 0;
  int jjnewStateCnt;
  int jjround;
  int jjmatchedPos;
  int jjmatchedKind;

  public void setDebugStream(PrintStream paramPrintStream)
  {
    this.debugStream = paramPrintStream;
  }

  private final int jjMoveStringLiteralDfa0_0()
  {
    return jjMoveNfa_0(0, 0);
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

  private final int jjMoveNfa_0(int paramInt1, int paramInt2)
  {
    int i = 0;
    this.jjnewStateCnt = 70;
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
              if (k > 1)
                k = 1;
              jjCheckNAddStates(0, 16);
            }
            if ((0x0 & l1) != 0L)
              jjCheckNAddStates(17, 22);
            break;
          case 1:
          case 36:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(1, 2);
            break;
          case 2:
            if ((0x0 & l1) != 0L)
              jjCheckNAdd(3);
            break;
          case 3:
            if ((0x0 & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAdd(3);
            }
            break;
          case 4:
          case 45:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(4, 5);
            break;
          case 5:
            if ((0x0 & l1) != 0L)
              jjCheckNAdd(6);
            break;
          case 6:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(6, 7);
            break;
          case 7:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(8, 9);
            break;
          case 8:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(8, 9);
            break;
          case 9:
          case 10:
            if ((0x0 & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(5, 10);
            }
            break;
          case 11:
          case 58:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(11, 12);
            break;
          case 12:
            if ((0x0 & l1) != 0L)
              jjCheckNAdd(13);
            break;
          case 13:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(13, 14);
            break;
          case 14:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(15, 16);
            break;
          case 15:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(15, 16);
            break;
          case 16:
          case 17:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(17, 18);
            break;
          case 18:
            if ((0x0 & l1) != 0L)
              jjCheckNAdd(19);
            break;
          case 19:
            if ((0x0 & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(14, 19);
            }
            break;
          case 20:
            if ((0x0 & l1) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAddStates(0, 16);
            }
            break;
          case 21:
            if ((0x0 & l1) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAdd(21);
            }
            break;
          case 22:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(22, 23);
            break;
          case 24:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(24, 25);
            break;
          case 25:
            if (this.curChar == '.')
              jjCheckNAdd(26);
            break;
          case 26:
            if ((0x0 & l1) != 0L)
            {
              if (k > 5)
                k = 5;
              jjCheckNAddTwoStates(25, 26);
            }
            break;
          case 27:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(27, 28);
            break;
          case 28:
            if (this.curChar == '.')
              jjCheckNAdd(29);
            break;
          case 29:
            if ((0x0 & l1) != 0L)
            {
              if (k > 6)
                k = 6;
              jjCheckNAddTwoStates(28, 29);
            }
            break;
          case 30:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(30, 31);
            break;
          case 31:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(32, 33);
            break;
          case 32:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(32, 33);
            break;
          case 33:
          case 34:
            if ((0x0 & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAdd(34);
            }
            break;
          case 35:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(35, 36);
            break;
          case 37:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(37, 38);
            break;
          case 38:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(39, 40);
            break;
          case 39:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(39, 40);
            break;
          case 40:
          case 41:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(41, 42);
            break;
          case 42:
            if ((0x0 & l1) != 0L)
              jjCheckNAdd(43);
            break;
          case 43:
            if ((0x0 & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(38, 43);
            }
            break;
          case 44:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(44, 45);
            break;
          case 46:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(46, 47);
            break;
          case 47:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(48, 49);
            break;
          case 48:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(48, 49);
            break;
          case 49:
          case 50:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(50, 51);
            break;
          case 51:
            if ((0x0 & l1) != 0L)
              jjCheckNAdd(52);
            break;
          case 52:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(52, 53);
            break;
          case 53:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(54, 55);
            break;
          case 54:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(54, 55);
            break;
          case 55:
          case 56:
            if ((0x0 & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(51, 56);
            }
            break;
          case 57:
            if ((0x0 & l1) != 0L)
              jjCheckNAddTwoStates(57, 58);
            break;
          case 61:
            if (this.curChar == '\'')
              this.jjstateSet[(this.jjnewStateCnt++)] = 62;
            break;
          case 64:
            if (this.curChar == '.')
              jjCheckNAdd(65);
            break;
          case 66:
            if (this.curChar == '.')
            {
              if (k > 3)
                k = 3;
              jjCheckNAdd(65);
            }
            break;
          case 68:
            if (this.curChar == '&')
              this.jjstateSet[(this.jjnewStateCnt++)] = 69;
            break;
          case 23:
          case 59:
          case 60:
          case 62:
          case 63:
          case 65:
          case 67:
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
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddStates(23, 28);
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAddStates(0, 16);
            }
            break;
          case 1:
            if ((0x7FFFFFE & l1) != 0L)
              jjAddStates(29, 30);
            break;
          case 2:
            if (this.curChar == '_')
              jjCheckNAdd(3);
            break;
          case 3:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAdd(3);
            }
            break;
          case 4:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(4, 5);
            break;
          case 5:
            if (this.curChar == '_')
              jjCheckNAdd(6);
            break;
          case 6:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(6, 7);
            break;
          case 7:
            if (this.curChar == '_')
              jjCheckNAddTwoStates(8, 9);
            break;
          case 8:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(8, 9);
            break;
          case 10:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(5, 10);
            }
            break;
          case 11:
            if ((0x7FFFFFE & l1) != 0L)
              jjAddStates(31, 32);
            break;
          case 12:
            if (this.curChar == '_')
              jjCheckNAdd(13);
            break;
          case 13:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(13, 14);
            break;
          case 14:
            if (this.curChar == '_')
              jjCheckNAddTwoStates(15, 16);
            break;
          case 15:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(15, 16);
            break;
          case 17:
            if ((0x7FFFFFE & l1) != 0L)
              jjAddStates(33, 34);
            break;
          case 18:
            if (this.curChar == '_')
              jjCheckNAdd(19);
            break;
          case 19:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(14, 19);
            }
            break;
          case 20:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAddStates(0, 16);
            }
            break;
          case 21:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAdd(21);
            }
            break;
          case 22:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(22, 23);
            break;
          case 23:
            if (this.curChar == '@')
              jjCheckNAdd(24);
            break;
          case 24:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(24, 25);
            break;
          case 26:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 5)
                k = 5;
              jjCheckNAddTwoStates(25, 26);
            }
            break;
          case 27:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(27, 28);
            break;
          case 29:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 6)
                k = 6;
              jjCheckNAddTwoStates(28, 29);
            }
            break;
          case 30:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(30, 31);
            break;
          case 31:
            if (this.curChar == '_')
              jjCheckNAddTwoStates(32, 33);
            break;
          case 32:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(32, 33);
            break;
          case 34:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              this.jjstateSet[(this.jjnewStateCnt++)] = 34;
            }
            break;
          case 35:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(35, 36);
            break;
          case 37:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(37, 38);
            break;
          case 38:
            if (this.curChar == '_')
              jjCheckNAddTwoStates(39, 40);
            break;
          case 39:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(39, 40);
            break;
          case 41:
            if ((0x7FFFFFE & l1) != 0L)
              jjAddStates(35, 36);
            break;
          case 42:
            if (this.curChar == '_')
              jjCheckNAdd(43);
            break;
          case 43:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(38, 43);
            }
            break;
          case 44:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(44, 45);
            break;
          case 46:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(46, 47);
            break;
          case 47:
            if (this.curChar == '_')
              jjCheckNAddTwoStates(48, 49);
            break;
          case 48:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(48, 49);
            break;
          case 50:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(50, 51);
            break;
          case 51:
            if (this.curChar == '_')
              jjCheckNAdd(52);
            break;
          case 52:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(52, 53);
            break;
          case 53:
            if (this.curChar == '_')
              jjCheckNAddTwoStates(54, 55);
            break;
          case 54:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(54, 55);
            break;
          case 56:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(51, 56);
            }
            break;
          case 57:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(57, 58);
            break;
          case 59:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddStates(23, 28);
            break;
          case 60:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(60, 61);
            break;
          case 62:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 2)
                k = 2;
              jjCheckNAddTwoStates(61, 62);
            }
            break;
          case 63:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(63, 64);
            break;
          case 65:
            if ((0x7FFFFFE & l1) != 0L)
              jjAddStates(37, 38);
            break;
          case 67:
            if ((0x7FFFFFE & l1) != 0L)
              jjCheckNAddTwoStates(67, 68);
            break;
          case 68:
            if (this.curChar == '@')
              jjCheckNAdd(69);
            break;
          case 69:
            if ((0x7FFFFFE & l1) != 0L)
            {
              if (k > 4)
                k = 4;
              jjCheckNAdd(69);
            }
            break;
          case 9:
          case 16:
          case 25:
          case 28:
          case 33:
          case 36:
          case 40:
          case 45:
          case 49:
          case 55:
          case 58:
          case 61:
          case 64:
          case 66:
          }
        while (j != i);
      }
      else
      {
        int m = (this.curChar & 0xFF) >> '\006';
        long l2 = 1L << (this.curChar & 0x3F);
        do
          switch (this.jjstateSet[(--j)])
          {
          case 0:
            if ((jjbitVec2[m] & l2) != 0L)
              jjCheckNAddStates(17, 22);
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAddStates(0, 16);
            }
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddStates(23, 28);
            break;
          case 1:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(1, 2);
            break;
          case 3:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              this.jjstateSet[(this.jjnewStateCnt++)] = 3;
            }
            break;
          case 4:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(4, 5);
            break;
          case 6:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(39, 40);
            break;
          case 8:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(41, 42);
            break;
          case 9:
            if ((jjbitVec2[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(5, 10);
            }
            break;
          case 10:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(5, 10);
            }
            break;
          case 11:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(11, 12);
            break;
          case 13:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(13, 14);
            break;
          case 15:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(43, 44);
            break;
          case 16:
            if ((jjbitVec2[m] & l2) != 0L)
              jjCheckNAddTwoStates(17, 18);
            break;
          case 17:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(17, 18);
            break;
          case 19:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(14, 19);
            }
            break;
          case 20:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAddStates(0, 16);
            }
            break;
          case 21:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 1)
                k = 1;
              jjCheckNAdd(21);
            }
            break;
          case 22:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(22, 23);
            break;
          case 24:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(24, 25);
            break;
          case 26:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 5)
                k = 5;
              jjCheckNAddTwoStates(25, 26);
            }
            break;
          case 27:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(27, 28);
            break;
          case 29:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 6)
                k = 6;
              jjCheckNAddTwoStates(28, 29);
            }
            break;
          case 30:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(30, 31);
            break;
          case 32:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(45, 46);
            break;
          case 33:
            if ((jjbitVec2[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAdd(34);
            }
            break;
          case 34:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAdd(34);
            }
            break;
          case 35:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(35, 36);
            break;
          case 36:
            if ((jjbitVec2[m] & l2) != 0L)
              jjCheckNAddTwoStates(1, 2);
            break;
          case 37:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(37, 38);
            break;
          case 39:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(47, 48);
            break;
          case 40:
            if ((jjbitVec2[m] & l2) != 0L)
              jjCheckNAddTwoStates(41, 42);
            break;
          case 41:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(41, 42);
            break;
          case 43:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(38, 43);
            }
            break;
          case 44:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(44, 45);
            break;
          case 45:
            if ((jjbitVec2[m] & l2) != 0L)
              jjCheckNAddTwoStates(4, 5);
            break;
          case 46:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(46, 47);
            break;
          case 48:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(49, 50);
            break;
          case 49:
            if ((jjbitVec2[m] & l2) != 0L)
              jjCheckNAddTwoStates(50, 51);
            break;
          case 50:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(50, 51);
            break;
          case 52:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(51, 52);
            break;
          case 54:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(53, 54);
            break;
          case 55:
            if ((jjbitVec2[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(51, 56);
            }
            break;
          case 56:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 7)
                k = 7;
              jjCheckNAddTwoStates(51, 56);
            }
            break;
          case 57:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(57, 58);
            break;
          case 58:
            if ((jjbitVec2[m] & l2) != 0L)
              jjCheckNAddTwoStates(11, 12);
            break;
          case 59:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddStates(23, 28);
            break;
          case 60:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(60, 61);
            break;
          case 62:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 2)
                k = 2;
              jjCheckNAddTwoStates(61, 62);
            }
            break;
          case 63:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(63, 64);
            break;
          case 65:
            if ((jjbitVec9[m] & l2) != 0L)
              jjAddStates(37, 38);
            break;
          case 67:
            if ((jjbitVec9[m] & l2) != 0L)
              jjCheckNAddTwoStates(67, 68);
            break;
          case 69:
            if ((jjbitVec9[m] & l2) != 0L)
            {
              if (k > 4)
                k = 4;
              this.jjstateSet[(this.jjnewStateCnt++)] = 69;
            }
            break;
          case 2:
          case 5:
          case 7:
          case 12:
          case 14:
          case 18:
          case 23:
          case 25:
          case 28:
          case 31:
          case 38:
          case 42:
          case 47:
          case 51:
          case 53:
          case 61:
          case 64:
          case 66:
          case 68:
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
      if ((j = this.jjnewStateCnt) == (i = 70 - (this.jjnewStateCnt = i)))
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

  public StandardTokenizerTokenManager(CharStream paramCharStream)
  {
    this.input_stream = paramCharStream;
  }

  public StandardTokenizerTokenManager(CharStream paramCharStream, int paramInt)
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
    int i = 70;
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
    if ((paramInt >= 1) || (paramInt < 0))
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
    do
    {
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
      this.jjmatchedKind = 2147483647;
      this.jjmatchedPos = 0;
      i = jjMoveStringLiteralDfa0_0();
      if ((this.jjmatchedPos == 0) && (this.jjmatchedKind > 13))
        this.jjmatchedKind = 13;
      if (this.jjmatchedKind == 2147483647)
        break;
      if (this.jjmatchedPos + 1 < i)
        this.input_stream.backup(i - this.jjmatchedPos - 1);
    }
    while ((jjtoToken[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) == 0L);
    Token localToken = jjFillToken();
    return localToken;
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
 * Qualified Name:     org.apache.lucene.analysis.standard.StandardTokenizerTokenManager
 * JD-Core Version:    0.6.2
 */