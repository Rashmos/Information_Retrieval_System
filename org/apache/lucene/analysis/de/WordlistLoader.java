package org.apache.lucene.analysis.de;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Hashtable;

public class WordlistLoader
{
  public static Hashtable getWordtable(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null))
      return new Hashtable();
    return getWordtable(new File(paramString1, paramString2));
  }

  public static Hashtable getWordtable(String paramString)
  {
    if (paramString == null)
      return new Hashtable();
    return getWordtable(new File(paramString));
  }

  public static Hashtable getWordtable(File paramFile)
  {
    if (paramFile == null)
      return new Hashtable();
    Hashtable localHashtable = null;
    try
    {
      LineNumberReader localLineNumberReader = new LineNumberReader(new FileReader(paramFile));
      String str = null;
      Object localObject = new String[100];
      int i = 0;
      while ((str = localLineNumberReader.readLine()) != null)
      {
        i++;
        if (i == localObject.length)
        {
          String[] arrayOfString = new String[localObject.length + 50];
          System.arraycopy(localObject, 0, arrayOfString, 0, i);
          localObject = arrayOfString;
        }
        localObject[(i - 1)] = str;
      }
      localHashtable = makeWordTable((String[])localObject, i);
    }
    catch (IOException localIOException)
    {
      localHashtable = new Hashtable();
    }
    return localHashtable;
  }

  private static Hashtable makeWordTable(String[] paramArrayOfString, int paramInt)
  {
    Hashtable localHashtable = new Hashtable(paramInt);
    for (int i = 0; i < paramInt; i++)
      localHashtable.put(paramArrayOfString[i], paramArrayOfString[i]);
    return localHashtable;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.de.WordlistLoader
 * JD-Core Version:    0.6.2
 */