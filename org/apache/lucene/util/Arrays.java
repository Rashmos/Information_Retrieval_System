package org.apache.lucene.util;

public class Arrays
{
  public static void sort(String[] paramArrayOfString)
  {
    String[] arrayOfString = (String[])paramArrayOfString.clone();
    mergeSort(arrayOfString, paramArrayOfString, 0, paramArrayOfString.length);
  }

  private static void mergeSort(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt1, int paramInt2)
  {
    int i = paramInt2 - paramInt1;
    if (i < 7)
    {
      for (j = paramInt1; j < paramInt2; j++)
        for (k = j; (k > paramInt1) && (paramArrayOfString2[(k - 1)].compareTo(paramArrayOfString2[k]) > 0); k--)
          swap(paramArrayOfString2, k, k - 1);
      return;
    }
    int j = (paramInt1 + paramInt2) / 2;
    mergeSort(paramArrayOfString2, paramArrayOfString1, paramInt1, j);
    mergeSort(paramArrayOfString2, paramArrayOfString1, j, paramInt2);
    if (paramArrayOfString1[(j - 1)].compareTo(paramArrayOfString1[j]) <= 0)
    {
      System.arraycopy(paramArrayOfString1, paramInt1, paramArrayOfString2, paramInt1, i);
      return;
    }
    int k = paramInt1;
    int m = paramInt1;
    int n = j;
    while (k < paramInt2)
    {
      if ((n >= paramInt2) || ((m < j) && (paramArrayOfString1[m].compareTo(paramArrayOfString1[n]) <= 0)))
        paramArrayOfString2[k] = paramArrayOfString1[(m++)];
      else
        paramArrayOfString2[k] = paramArrayOfString1[(n++)];
      k++;
    }
  }

  private static void swap(String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    String str = paramArrayOfString[paramInt1];
    paramArrayOfString[paramInt1] = paramArrayOfString[paramInt2];
    paramArrayOfString[paramInt2] = str;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.Arrays
 * JD-Core Version:    0.6.2
 */