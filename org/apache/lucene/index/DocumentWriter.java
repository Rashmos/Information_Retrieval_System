package org.apache.lucene.index;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.OutputStream;

final class DocumentWriter
{
  private Analyzer analyzer;
  private Directory directory;
  private FieldInfos fieldInfos;
  private int maxFieldLength;
  private final Hashtable postingTable = new Hashtable();
  private int[] fieldLengths;
  private final Term termBuffer = new Term("", "");

  DocumentWriter(Directory paramDirectory, Analyzer paramAnalyzer, int paramInt)
  {
    this.directory = paramDirectory;
    this.analyzer = paramAnalyzer;
    this.maxFieldLength = paramInt;
  }

  final void addDocument(String paramString, Document paramDocument)
    throws IOException
  {
    this.fieldInfos = new FieldInfos();
    this.fieldInfos.add(paramDocument);
    this.fieldInfos.write(this.directory, paramString + ".fnm");
    FieldsWriter localFieldsWriter = new FieldsWriter(this.directory, paramString, this.fieldInfos);
    try
    {
      localFieldsWriter.addDocument(paramDocument);
    }
    finally
    {
      localFieldsWriter.close();
    }
    this.postingTable.clear();
    this.fieldLengths = new int[this.fieldInfos.size()];
    invertDocument(paramDocument);
    Posting[] arrayOfPosting = sortPostingTable();
    writePostings(arrayOfPosting, paramString);
    writeNorms(paramDocument, paramString);
  }

  private final void invertDocument(Document paramDocument)
    throws IOException
  {
    Enumeration localEnumeration = paramDocument.fields();
    while (localEnumeration.hasMoreElements())
    {
      Field localField = (Field)localEnumeration.nextElement();
      String str = localField.name();
      int i = this.fieldInfos.fieldNumber(str);
      int j = this.fieldLengths[i];
      if (localField.isIndexed())
      {
        if (!localField.isTokenized())
        {
          addPosition(str, localField.stringValue(), j++);
        }
        else
        {
          Object localObject1;
          if (localField.readerValue() != null)
            localObject1 = localField.readerValue();
          else if (localField.stringValue() != null)
            localObject1 = new StringReader(localField.stringValue());
          else
            throw new IllegalArgumentException("field must have either String or Reader value");
          TokenStream localTokenStream = this.analyzer.tokenStream(str, (Reader)localObject1);
          try
          {
            for (Token localToken = localTokenStream.next(); localToken != null; localToken = localTokenStream.next())
            {
              addPosition(str, localToken.termText(), j++);
              if (j > this.maxFieldLength)
                break;
            }
          }
          finally
          {
            localTokenStream.close();
          }
        }
        this.fieldLengths[i] = j;
      }
    }
  }

  private final void addPosition(String paramString1, String paramString2, int paramInt)
  {
    this.termBuffer.set(paramString1, paramString2);
    Posting localPosting = (Posting)this.postingTable.get(this.termBuffer);
    if (localPosting != null)
    {
      int i = localPosting.freq;
      if (localPosting.positions.length == i)
      {
        int[] arrayOfInt1 = new int[i * 2];
        int[] arrayOfInt2 = localPosting.positions;
        for (int j = 0; j < i; j++)
          arrayOfInt1[j] = arrayOfInt2[j];
        localPosting.positions = arrayOfInt1;
      }
      localPosting.positions[i] = paramInt;
      localPosting.freq = (i + 1);
    }
    else
    {
      Term localTerm = new Term(paramString1, paramString2, false);
      this.postingTable.put(localTerm, new Posting(localTerm, paramInt));
    }
  }

  private final Posting[] sortPostingTable()
  {
    Posting[] arrayOfPosting = new Posting[this.postingTable.size()];
    Enumeration localEnumeration = this.postingTable.elements();
    for (int i = 0; localEnumeration.hasMoreElements(); i++)
      arrayOfPosting[i] = ((Posting)localEnumeration.nextElement());
    quickSort(arrayOfPosting, 0, arrayOfPosting.length - 1);
    return arrayOfPosting;
  }

  private static final void quickSort(Posting[] paramArrayOfPosting, int paramInt1, int paramInt2)
  {
    if (paramInt1 >= paramInt2)
      return;
    int i = (paramInt1 + paramInt2) / 2;
    Posting localPosting1;
    if (paramArrayOfPosting[paramInt1].term.compareTo(paramArrayOfPosting[i].term) > 0)
    {
      localPosting1 = paramArrayOfPosting[paramInt1];
      paramArrayOfPosting[paramInt1] = paramArrayOfPosting[i];
      paramArrayOfPosting[i] = localPosting1;
    }
    if (paramArrayOfPosting[i].term.compareTo(paramArrayOfPosting[paramInt2].term) > 0)
    {
      localPosting1 = paramArrayOfPosting[i];
      paramArrayOfPosting[i] = paramArrayOfPosting[paramInt2];
      paramArrayOfPosting[paramInt2] = localPosting1;
      if (paramArrayOfPosting[paramInt1].term.compareTo(paramArrayOfPosting[i].term) > 0)
      {
        Posting localPosting2 = paramArrayOfPosting[paramInt1];
        paramArrayOfPosting[paramInt1] = paramArrayOfPosting[i];
        paramArrayOfPosting[i] = localPosting2;
      }
    }
    int j = paramInt1 + 1;
    int k = paramInt2 - 1;
    if (j >= k)
      return;
    Term localTerm = paramArrayOfPosting[i].term;
    while (true)
    {
      k--;
      while (paramArrayOfPosting[k].term.compareTo(localTerm) <= 0)
      {
        while ((j < k) && (paramArrayOfPosting[j].term.compareTo(localTerm) <= 0))
          j++;
        if (j >= k)
          break label225;
        Posting localPosting3 = paramArrayOfPosting[j];
        paramArrayOfPosting[j] = paramArrayOfPosting[k];
        paramArrayOfPosting[k] = localPosting3;
        k--;
      }
    }
    label225: quickSort(paramArrayOfPosting, paramInt1, j);
    quickSort(paramArrayOfPosting, j + 1, paramInt2);
  }

  private final void writePostings(Posting[] paramArrayOfPosting, String paramString)
    throws IOException
  {
    OutputStream localOutputStream1 = null;
    OutputStream localOutputStream2 = null;
    TermInfosWriter localTermInfosWriter = null;
    try
    {
      localOutputStream1 = this.directory.createFile(paramString + ".frq");
      localOutputStream2 = this.directory.createFile(paramString + ".prx");
      localTermInfosWriter = new TermInfosWriter(this.directory, paramString, this.fieldInfos);
      TermInfo localTermInfo = new TermInfo();
      for (int i = 0; i < paramArrayOfPosting.length; i++)
      {
        Posting localPosting = paramArrayOfPosting[i];
        localTermInfo.set(1, localOutputStream1.getFilePointer(), localOutputStream2.getFilePointer());
        localTermInfosWriter.add(localPosting.term, localTermInfo);
        int j = localPosting.freq;
        if (j == 1)
        {
          localOutputStream1.writeVInt(1);
        }
        else
        {
          localOutputStream1.writeVInt(0);
          localOutputStream1.writeVInt(j);
        }
        int k = 0;
        int[] arrayOfInt = localPosting.positions;
        for (int m = 0; m < j; m++)
        {
          int n = arrayOfInt[m];
          localOutputStream2.writeVInt(n - k);
          k = n;
        }
      }
    }
    finally
    {
      if (localOutputStream1 != null)
        localOutputStream1.close();
      if (localOutputStream2 != null)
        localOutputStream2.close();
      if (localTermInfosWriter != null)
        localTermInfosWriter.close();
    }
  }

  private final void writeNorms(Document paramDocument, String paramString)
    throws IOException
  {
    Enumeration localEnumeration = paramDocument.fields();
    while (localEnumeration.hasMoreElements())
    {
      Field localField = (Field)localEnumeration.nextElement();
      if (localField.isIndexed())
      {
        int i = this.fieldInfos.fieldNumber(localField.name());
        OutputStream localOutputStream = this.directory.createFile(paramString + ".f" + i);
        try
        {
          localOutputStream.writeByte(Similarity.norm(this.fieldLengths[i]));
        }
        finally
        {
          localOutputStream.close();
        }
      }
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocumentWriter
 * JD-Core Version:    0.6.2
 */