package org.apache.lucene.search;

import java.io.IOException;
import java.util.BitSet;
import java.util.Date;
import org.apache.lucene.document.DateField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;

public final class DateFilter extends Filter
{
  String field;
  String start = DateField.MIN_DATE_STRING();
  String end = DateField.MAX_DATE_STRING();

  private DateFilter(String paramString)
  {
    this.field = paramString;
  }

  public DateFilter(String paramString, Date paramDate1, Date paramDate2)
  {
    this.field = paramString;
    this.start = DateField.dateToString(paramDate1);
    this.end = DateField.dateToString(paramDate2);
  }

  public DateFilter(String paramString, long paramLong1, long paramLong2)
  {
    this.field = paramString;
    this.start = DateField.timeToString(paramLong1);
    this.end = DateField.timeToString(paramLong2);
  }

  public static DateFilter Before(String paramString, Date paramDate)
  {
    DateFilter localDateFilter = new DateFilter(paramString);
    localDateFilter.end = DateField.dateToString(paramDate);
    return localDateFilter;
  }

  public static DateFilter Before(String paramString, long paramLong)
  {
    DateFilter localDateFilter = new DateFilter(paramString);
    localDateFilter.end = DateField.timeToString(paramLong);
    return localDateFilter;
  }

  public static DateFilter After(String paramString, Date paramDate)
  {
    DateFilter localDateFilter = new DateFilter(paramString);
    localDateFilter.start = DateField.dateToString(paramDate);
    return localDateFilter;
  }

  public static DateFilter After(String paramString, long paramLong)
  {
    DateFilter localDateFilter = new DateFilter(paramString);
    localDateFilter.start = DateField.timeToString(paramLong);
    return localDateFilter;
  }

  public final BitSet bits(IndexReader paramIndexReader)
    throws IOException
  {
    BitSet localBitSet = new BitSet(paramIndexReader.maxDoc());
    TermEnum localTermEnum = paramIndexReader.terms(new Term(this.field, this.start));
    TermDocs localTermDocs = paramIndexReader.termDocs();
    if (localTermEnum.term() == null)
      return localBitSet;
    try
    {
      Term localTerm = new Term(this.field, this.end);
      while (localTermEnum.term().compareTo(localTerm) <= 0)
      {
        localTermDocs.seek(localTermEnum.term());
        try
        {
          while (localTermDocs.next())
            localBitSet.set(localTermDocs.doc());
        }
        finally
        {
          localTermDocs.close();
        }
        if (!localTermEnum.next())
          break;
      }
    }
    finally
    {
      localTermEnum.close();
    }
    return localBitSet;
  }

  public final String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.field);
    localStringBuffer.append(":");
    localStringBuffer.append(DateField.stringToDate(this.start).toString());
    localStringBuffer.append("-");
    localStringBuffer.append(DateField.stringToDate(this.end).toString());
    return localStringBuffer.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.DateFilter
 * JD-Core Version:    0.6.2
 */