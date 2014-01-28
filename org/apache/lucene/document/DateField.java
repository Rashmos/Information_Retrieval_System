package org.apache.lucene.document;

import java.util.Date;

public class DateField
{
  private static int DATE_LEN = Long.toString(31536000000000L, 36).length();

  public static String MIN_DATE_STRING()
  {
    return timeToString(0L);
  }

  public static String MAX_DATE_STRING()
  {
    char[] arrayOfChar = new char[DATE_LEN];
    int i = Character.forDigit(35, 36);
    for (int j = 0; j < DATE_LEN; j++)
      arrayOfChar[j] = i;
    return new String(arrayOfChar);
  }

  public static String dateToString(Date paramDate)
  {
    return timeToString(paramDate.getTime());
  }

  public static String timeToString(long paramLong)
  {
    if (paramLong < 0L)
      throw new RuntimeException("time too early");
    String str = Long.toString(paramLong, 36);
    if (str.length() > DATE_LEN)
      throw new RuntimeException("time too late");
    while (str.length() < DATE_LEN)
      str = "0" + str;
    return str;
  }

  public static long stringToTime(String paramString)
  {
    return Long.parseLong(paramString, 36);
  }

  public static Date stringToDate(String paramString)
  {
    return new Date(stringToTime(paramString));
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.DateField
 * JD-Core Version:    0.6.2
 */