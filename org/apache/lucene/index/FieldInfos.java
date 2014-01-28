package org.apache.lucene.index;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.OutputStream;

final class FieldInfos
{
  private Vector byNumber = new Vector();
  private Hashtable byName = new Hashtable();

  FieldInfos()
  {
    add("", false);
  }

  FieldInfos(Directory paramDirectory, String paramString)
    throws IOException
  {
    InputStream localInputStream = paramDirectory.openFile(paramString);
    try
    {
      read(localInputStream);
    }
    finally
    {
      localInputStream.close();
    }
  }

  final void add(Document paramDocument)
  {
    Enumeration localEnumeration = paramDocument.fields();
    while (localEnumeration.hasMoreElements())
    {
      Field localField = (Field)localEnumeration.nextElement();
      add(localField.name(), localField.isIndexed());
    }
  }

  final void add(FieldInfos paramFieldInfos)
  {
    for (int i = 0; i < paramFieldInfos.size(); i++)
    {
      FieldInfo localFieldInfo = paramFieldInfos.fieldInfo(i);
      add(localFieldInfo.name, localFieldInfo.isIndexed);
    }
  }

  final void add(String paramString, boolean paramBoolean)
  {
    FieldInfo localFieldInfo = fieldInfo(paramString);
    if (localFieldInfo == null)
      addInternal(paramString, paramBoolean);
    else if (localFieldInfo.isIndexed != paramBoolean)
      localFieldInfo.isIndexed = true;
  }

  private final void addInternal(String paramString, boolean paramBoolean)
  {
    FieldInfo localFieldInfo = new FieldInfo(paramString, paramBoolean, this.byNumber.size());
    this.byNumber.addElement(localFieldInfo);
    this.byName.put(paramString, localFieldInfo);
  }

  final int fieldNumber(String paramString)
  {
    FieldInfo localFieldInfo = fieldInfo(paramString);
    if (localFieldInfo != null)
      return localFieldInfo.number;
    return -1;
  }

  final FieldInfo fieldInfo(String paramString)
  {
    return (FieldInfo)this.byName.get(paramString);
  }

  final String fieldName(int paramInt)
  {
    return fieldInfo(paramInt).name;
  }

  final FieldInfo fieldInfo(int paramInt)
  {
    return (FieldInfo)this.byNumber.elementAt(paramInt);
  }

  final int size()
  {
    return this.byNumber.size();
  }

  final void write(Directory paramDirectory, String paramString)
    throws IOException
  {
    OutputStream localOutputStream = paramDirectory.createFile(paramString);
    try
    {
      write(localOutputStream);
    }
    finally
    {
      localOutputStream.close();
    }
  }

  final void write(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.writeVInt(size());
    for (int i = 0; i < size(); i++)
    {
      FieldInfo localFieldInfo = fieldInfo(i);
      paramOutputStream.writeString(localFieldInfo.name);
      paramOutputStream.writeByte((byte)(localFieldInfo.isIndexed ? 1 : 0));
    }
  }

  private final void read(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.readVInt();
    for (int j = 0; j < i; j++)
      addInternal(paramInputStream.readString().intern(), paramInputStream.readByte() != 0);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FieldInfos
 * JD-Core Version:    0.6.2
 */