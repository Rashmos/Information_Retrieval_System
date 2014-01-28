package org.apache.lucene.analysis.de;

public class GermanStemmer
{
  private StringBuffer sb = new StringBuffer();
  private boolean uppercase = false;
  private int substCount = 0;

  protected String stem(String paramString)
  {
    if (!isStemmable(paramString))
      return paramString;
    if (Character.isUpperCase(paramString.charAt(0)))
      this.uppercase = true;
    else
      this.uppercase = false;
    paramString = paramString.toLowerCase();
    this.sb.delete(0, this.sb.length());
    this.sb.insert(0, paramString);
    this.sb = substitute(this.sb);
    if ((this.uppercase) && (this.sb.length() > 3))
    {
      if (this.sb.substring(this.sb.length() - 3, this.sb.length()).equals("ern"))
        this.sb.delete(this.sb.length() - 3, this.sb.length());
      else if (this.sb.substring(this.sb.length() - 2, this.sb.length()).equals("en"))
        this.sb.delete(this.sb.length() - 2, this.sb.length());
      else if (this.sb.substring(this.sb.length() - 2, this.sb.length()).equals("er"))
        this.sb.delete(this.sb.length() - 2, this.sb.length());
      else if (this.sb.substring(this.sb.length() - 2, this.sb.length()).equals("es"))
        this.sb.delete(this.sb.length() - 2, this.sb.length());
      else if (this.sb.charAt(this.sb.length() - 1) == 'e')
        this.sb.deleteCharAt(this.sb.length() - 1);
      else if (this.sb.charAt(this.sb.length() - 1) == 'n')
        this.sb.deleteCharAt(this.sb.length() - 1);
      else if (this.sb.charAt(this.sb.length() - 1) == 's')
        this.sb.deleteCharAt(this.sb.length() - 1);
      if ((this.sb.length() > 5) && (this.sb.substring(this.sb.length() - 3, this.sb.length()).equals("erin*")))
        this.sb.deleteCharAt(this.sb.length() - 1);
      if (this.sb.charAt(this.sb.length() - 1) == 'z')
        this.sb.setCharAt(this.sb.length() - 1, 'x');
    }
    else
    {
      int i = 1;
      while ((this.sb.length() > 3) && (i != 0))
        if ((this.sb.length() + this.substCount > 5) && (this.sb.substring(this.sb.length() - 2, this.sb.length()).equals("nd")))
          this.sb.delete(this.sb.length() - 2, this.sb.length());
        else if ((this.sb.length() + this.substCount > 4) && (this.sb.substring(this.sb.length() - 2, this.sb.length()).equals("er")))
          this.sb.delete(this.sb.length() - 2, this.sb.length());
        else if ((this.sb.length() + this.substCount > 4) && (this.sb.substring(this.sb.length() - 2, this.sb.length()).equals("em")))
          this.sb.delete(this.sb.length() - 2, this.sb.length());
        else if (this.sb.charAt(this.sb.length() - 1) == 't')
          this.sb.deleteCharAt(this.sb.length() - 1);
        else if (this.sb.charAt(this.sb.length() - 1) == 'n')
          this.sb.deleteCharAt(this.sb.length() - 1);
        else if (this.sb.charAt(this.sb.length() - 1) == 's')
          this.sb.deleteCharAt(this.sb.length() - 1);
        else if (this.sb.charAt(this.sb.length() - 1) == 'e')
          this.sb.deleteCharAt(this.sb.length() - 1);
        else
          i = 0;
    }
    this.sb = resubstitute(this.sb);
    if (!this.uppercase)
      this.sb = removeParticleDenotion(this.sb);
    return this.sb.toString();
  }

  private StringBuffer removeParticleDenotion(StringBuffer paramStringBuffer)
  {
    for (int i = 0; i < paramStringBuffer.length(); i++)
      if ((i < paramStringBuffer.length() - 4) && (paramStringBuffer.charAt(i) == 'g') && (paramStringBuffer.charAt(i + 1) == 'e'))
        paramStringBuffer.delete(0, i + 2);
    return this.sb;
  }

  private StringBuffer substitute(StringBuffer paramStringBuffer)
  {
    this.substCount = 0;
    for (int i = 0; i < paramStringBuffer.length(); i++)
    {
      if ((i > 0) && (paramStringBuffer.charAt(i) == paramStringBuffer.charAt(i - 1)))
        paramStringBuffer.setCharAt(i, '*');
      else if (paramStringBuffer.charAt(i) == 'ä')
        paramStringBuffer.setCharAt(i, 'a');
      else if (paramStringBuffer.charAt(i) == 'ö')
        paramStringBuffer.setCharAt(i, 'o');
      else if (paramStringBuffer.charAt(i) == 'ü')
        paramStringBuffer.setCharAt(i, 'u');
      if (i < paramStringBuffer.length() - 1)
        if (paramStringBuffer.charAt(i) == 'ß')
        {
          paramStringBuffer.setCharAt(i, 's');
          paramStringBuffer.insert(i + 1, 's');
          this.substCount += 1;
        }
        else if ((i < paramStringBuffer.length() - 2) && (paramStringBuffer.charAt(i) == 's') && (paramStringBuffer.charAt(i + 1) == 'c') && (paramStringBuffer.charAt(i + 2) == 'h'))
        {
          paramStringBuffer.setCharAt(i, '$');
          paramStringBuffer.delete(i + 1, i + 3);
          this.substCount = 2;
        }
        else if ((paramStringBuffer.charAt(i) == 'c') && (paramStringBuffer.charAt(i + 1) == 'h'))
        {
          paramStringBuffer.setCharAt(i, '§');
          paramStringBuffer.deleteCharAt(i + 1);
          this.substCount += 1;
        }
        else if ((paramStringBuffer.charAt(i) == 'e') && (paramStringBuffer.charAt(i + 1) == 'i'))
        {
          paramStringBuffer.setCharAt(i, '%');
          paramStringBuffer.deleteCharAt(i + 1);
          this.substCount += 1;
        }
        else if ((paramStringBuffer.charAt(i) == 'i') && (paramStringBuffer.charAt(i + 1) == 'e'))
        {
          paramStringBuffer.setCharAt(i, '&');
          paramStringBuffer.deleteCharAt(i + 1);
          this.substCount += 1;
        }
        else if ((paramStringBuffer.charAt(i) == 'i') && (paramStringBuffer.charAt(i + 1) == 'g'))
        {
          paramStringBuffer.setCharAt(i, '#');
          paramStringBuffer.deleteCharAt(i + 1);
          this.substCount += 1;
        }
        else if ((paramStringBuffer.charAt(i) == 's') && (paramStringBuffer.charAt(i + 1) == 't'))
        {
          paramStringBuffer.setCharAt(i, '!');
          paramStringBuffer.deleteCharAt(i + 1);
          this.substCount += 1;
        }
    }
    return paramStringBuffer;
  }

  private boolean isStemmable(String paramString)
  {
    int i = 0;
    int j = -1;
    for (int k = 0; k < paramString.length(); k++)
    {
      if (!Character.isLetter(paramString.charAt(k)))
        return false;
      if (Character.isUpperCase(paramString.charAt(k)))
      {
        if (i != 0)
          return false;
        j = k;
        i = 1;
      }
    }
    return j <= 0;
  }

  private StringBuffer resubstitute(StringBuffer paramStringBuffer)
  {
    for (int i = 0; i < paramStringBuffer.length(); i++)
      if (paramStringBuffer.charAt(i) == '*')
      {
        char c = paramStringBuffer.charAt(i - 1);
        paramStringBuffer.setCharAt(i, c);
      }
      else if (paramStringBuffer.charAt(i) == '$')
      {
        paramStringBuffer.setCharAt(i, 's');
        paramStringBuffer.insert(i + 1, new char[] { 'c', 'h' }, 0, 2);
      }
      else if (paramStringBuffer.charAt(i) == '§')
      {
        paramStringBuffer.setCharAt(i, 'c');
        paramStringBuffer.insert(i + 1, 'h');
      }
      else if (paramStringBuffer.charAt(i) == '%')
      {
        paramStringBuffer.setCharAt(i, 'e');
        paramStringBuffer.insert(i + 1, 'i');
      }
      else if (paramStringBuffer.charAt(i) == '&')
      {
        paramStringBuffer.setCharAt(i, 'i');
        paramStringBuffer.insert(i + 1, 'e');
      }
      else if (paramStringBuffer.charAt(i) == '#')
      {
        paramStringBuffer.setCharAt(i, 'i');
        paramStringBuffer.insert(i + 1, 'g');
      }
      else if (paramStringBuffer.charAt(i) == '!')
      {
        paramStringBuffer.setCharAt(i, 's');
        paramStringBuffer.insert(i + 1, 't');
      }
    return paramStringBuffer;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.de.GermanStemmer
 * JD-Core Version:    0.6.2
 */