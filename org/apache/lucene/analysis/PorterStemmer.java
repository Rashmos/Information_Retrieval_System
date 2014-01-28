package org.apache.lucene.analysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

class PorterStemmer
{
  private char[] b = new char[50];
  private int i = 0;
  private int j;
  private int k;
  private int k0;
  private boolean dirty = false;
  private static final int INC = 50;
  private static final int EXTRA = 1;

  public void reset()
  {
    this.i = 0;
    this.dirty = false;
  }

  public void add(char paramChar)
  {
    if (this.b.length <= this.i + 1)
    {
      char[] arrayOfChar = new char[this.b.length + 50];
      for (int m = 0; m < this.b.length; m++)
        arrayOfChar[m] = this.b[m];
      this.b = arrayOfChar;
    }
    this.b[(this.i++)] = paramChar;
  }

  public String toString()
  {
    return new String(this.b, 0, this.i);
  }

  public int getResultLength()
  {
    return this.i;
  }

  public char[] getResultBuffer()
  {
    return this.b;
  }

  private final boolean cons(int paramInt)
  {
    switch (this.b[paramInt])
    {
    case 'a':
    case 'e':
    case 'i':
    case 'o':
    case 'u':
      return false;
    case 'y':
      return paramInt == this.k0;
    }
    return true;
  }

  private final int m()
  {
    int m = 0;
    int n = this.k0;
    while (true)
    {
      if (n > this.j)
        return m;
      if (!cons(n))
        break;
      n++;
    }
    n++;
    while (true)
    {
      if (n > this.j)
        return m;
      if (!cons(n))
      {
        n++;
      }
      else
      {
        n++;
        m++;
        while (true)
        {
          if (n > this.j)
            return m;
          if (!cons(n))
            break;
          n++;
        }
        n++;
      }
    }
  }

  private final boolean vowelinstem()
  {
    for (int m = this.k0; m <= this.j; m++)
      if (!cons(m))
        return true;
    return false;
  }

  private final boolean doublec(int paramInt)
  {
    if (paramInt < this.k0 + 1)
      return false;
    if (this.b[paramInt] != this.b[(paramInt - 1)])
      return false;
    return cons(paramInt);
  }

  private final boolean cvc(int paramInt)
  {
    if ((paramInt < this.k0 + 2) || (!cons(paramInt)) || (cons(paramInt - 1)) || (!cons(paramInt - 2)))
      return false;
    int m = this.b[paramInt];
    return (m != 119) && (m != 120) && (m != 121);
  }

  private final boolean ends(String paramString)
  {
    int m = paramString.length();
    int n = this.k - m + 1;
    if (n < this.k0)
      return false;
    for (int i1 = 0; i1 < m; i1++)
      if (this.b[(n + i1)] != paramString.charAt(i1))
        return false;
    this.j = (this.k - m);
    return true;
  }

  void setto(String paramString)
  {
    int m = paramString.length();
    int n = this.j + 1;
    for (int i1 = 0; i1 < m; i1++)
      this.b[(n + i1)] = paramString.charAt(i1);
    this.k = (this.j + m);
    this.dirty = true;
  }

  void r(String paramString)
  {
    if (m() > 0)
      setto(paramString);
  }

  private final void step1()
  {
    if (this.b[this.k] == 's')
      if (ends("sses"))
        this.k -= 2;
      else if (ends("ies"))
        setto("i");
      else if (this.b[(this.k - 1)] != 's')
        this.k -= 1;
    if (ends("eed"))
    {
      if (m() > 0)
        this.k -= 1;
    }
    else if (((ends("ed")) || (ends("ing"))) && (vowelinstem()))
    {
      this.k = this.j;
      if (ends("at"))
      {
        setto("ate");
      }
      else if (ends("bl"))
      {
        setto("ble");
      }
      else if (ends("iz"))
      {
        setto("ize");
      }
      else if (doublec(this.k))
      {
        int m = this.b[(this.k--)];
        if ((m == 108) || (m == 115) || (m == 122))
          this.k += 1;
      }
      else if ((m() == 1) && (cvc(this.k)))
      {
        setto("e");
      }
    }
  }

  private final void step2()
  {
    if ((ends("y")) && (vowelinstem()))
    {
      this.b[this.k] = 'i';
      this.dirty = true;
    }
  }

  private final void step3()
  {
    if (this.k == this.k0)
      return;
    switch (this.b[(this.k - 1)])
    {
    case 'a':
      if (ends("ational"))
        r("ate");
      else if (ends("tional"))
        r("tion");
      break;
    case 'c':
      if (ends("enci"))
        r("ence");
      else if (ends("anci"))
        r("ance");
      break;
    case 'e':
      if (ends("izer"))
        r("ize");
      break;
    case 'l':
      if (ends("bli"))
        r("ble");
      else if (ends("alli"))
        r("al");
      else if (ends("entli"))
        r("ent");
      else if (ends("eli"))
        r("e");
      else if (ends("ousli"))
        r("ous");
      break;
    case 'o':
      if (ends("ization"))
        r("ize");
      else if (ends("ation"))
        r("ate");
      else if (ends("ator"))
        r("ate");
      break;
    case 's':
      if (ends("alism"))
        r("al");
      else if (ends("iveness"))
        r("ive");
      else if (ends("fulness"))
        r("ful");
      else if (ends("ousness"))
        r("ous");
      break;
    case 't':
      if (ends("aliti"))
        r("al");
      else if (ends("iviti"))
        r("ive");
      else if (ends("biliti"))
        r("ble");
      break;
    case 'g':
      if (ends("logi"))
        r("log");
      break;
    case 'b':
    case 'd':
    case 'f':
    case 'h':
    case 'i':
    case 'j':
    case 'k':
    case 'm':
    case 'n':
    case 'p':
    case 'q':
    case 'r':
    }
  }

  private final void step4()
  {
    switch (this.b[this.k])
    {
    case 'e':
      if (ends("icate"))
        r("ic");
      else if (ends("ative"))
        r("");
      else if (ends("alize"))
        r("al");
      break;
    case 'i':
      if (ends("iciti"))
        r("ic");
      break;
    case 'l':
      if (ends("ical"))
        r("ic");
      else if (ends("ful"))
        r("");
      break;
    case 's':
      if (ends("ness"))
        r("");
      break;
    }
  }

  private final void step5()
  {
    if (this.k == this.k0)
      return;
    switch (this.b[(this.k - 1)])
    {
    case 'a':
      if (!ends("al"))
        return;
      break;
    case 'c':
      if ((!ends("ance")) && (!ends("ence")))
        return;
      break;
    case 'e':
      if (!ends("er"))
        return;
      break;
    case 'i':
      if (!ends("ic"))
        return;
      break;
    case 'l':
      if ((!ends("able")) && (!ends("ible")))
        return;
      break;
    case 'n':
      if ((!ends("ant")) && (!ends("ement")) && (!ends("ment")) && (!ends("ent")))
        return;
      break;
    case 'o':
      if (((!ends("ion")) || (this.j < 0) || ((this.b[this.j] != 's') && (this.b[this.j] != 't'))) && (!ends("ou")))
        return;
      break;
    case 's':
      if (!ends("ism"))
        return;
      break;
    case 't':
      if ((!ends("ate")) && (!ends("iti")))
        return;
      break;
    case 'u':
      if (!ends("ous"))
        return;
      break;
    case 'v':
      if (!ends("ive"))
        return;
      break;
    case 'z':
      if (!ends("ize"))
        return;
      break;
    case 'b':
    case 'd':
    case 'f':
    case 'g':
    case 'h':
    case 'j':
    case 'k':
    case 'm':
    case 'p':
    case 'q':
    case 'r':
    case 'w':
    case 'x':
    case 'y':
    default:
      return;
    }
    if (m() > 1)
      this.k = this.j;
  }

  private final void step6()
  {
    this.j = this.k;
    if (this.b[this.k] == 'e')
    {
      int m = m();
      if ((m > 1) || ((m == 1) && (!cvc(this.k - 1))))
        this.k -= 1;
    }
    if ((this.b[this.k] == 'l') && (doublec(this.k)) && (m() > 1))
      this.k -= 1;
  }

  public String stem(String paramString)
  {
    if (stem(paramString.toCharArray(), paramString.length()))
      return toString();
    return paramString;
  }

  public boolean stem(char[] paramArrayOfChar)
  {
    return stem(paramArrayOfChar, paramArrayOfChar.length);
  }

  public boolean stem(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    reset();
    if (this.b.length < paramInt2)
    {
      char[] arrayOfChar = new char[paramInt2 + 1];
      this.b = arrayOfChar;
    }
    for (int m = 0; m < paramInt2; m++)
      this.b[m] = paramArrayOfChar[(paramInt1 + m)];
    this.i = paramInt2;
    return stem(0);
  }

  public boolean stem(char[] paramArrayOfChar, int paramInt)
  {
    return stem(paramArrayOfChar, 0, paramInt);
  }

  public boolean stem()
  {
    return stem(0);
  }

  public boolean stem(int paramInt)
  {
    this.k = (this.i - 1);
    this.k0 = paramInt;
    if (this.k > this.k0 + 1)
    {
      step1();
      step2();
      step3();
      step4();
      step5();
      step6();
    }
    if (this.i != this.k + 1)
      this.dirty = true;
    this.i = (this.k + 1);
    return this.dirty;
  }

  public static void main(String[] paramArrayOfString)
  {
    PorterStemmer localPorterStemmer = new PorterStemmer();
    for (int m = 0; m < paramArrayOfString.length; m++)
      try
      {
        FileInputStream localFileInputStream = new FileInputStream(paramArrayOfString[m]);
        byte[] arrayOfByte = new byte[1024];
        int n = localFileInputStream.read(arrayOfByte);
        int i1 = 0;
        localPorterStemmer.reset();
        while (true)
        {
          int i2;
          if (i1 < n)
          {
            i2 = arrayOfByte[(i1++)];
          }
          else
          {
            n = localFileInputStream.read(arrayOfByte);
            i1 = 0;
            if (n < 0)
              i2 = -1;
            else
              i2 = arrayOfByte[(i1++)];
          }
          if (Character.isLetter((char)i2))
          {
            localPorterStemmer.add(Character.toLowerCase((char)i2));
          }
          else
          {
            localPorterStemmer.stem();
            System.out.print(localPorterStemmer.toString());
            localPorterStemmer.reset();
            if (i2 < 0)
              break;
            System.out.print((char)i2);
          }
        }
        localFileInputStream.close();
      }
      catch (IOException localIOException)
      {
        System.out.println("error reading " + paramArrayOfString[m]);
      }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.PorterStemmer
 * JD-Core Version:    0.6.2
 */