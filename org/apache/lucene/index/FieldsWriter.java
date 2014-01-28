package org.apache.lucene.index;

import java.io.IOException;
import java.util.Enumeration;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.OutputStream;

final class FieldsWriter
{
  private FieldInfos fieldInfos;
  private OutputStream fieldsStream;
  private OutputStream indexStream;

  FieldsWriter(Directory paramDirectory, String paramString, FieldInfos paramFieldInfos)
    throws IOException
  {
    this.fieldInfos = paramFieldInfos;
    this.fieldsStream = paramDirectory.createFile(paramString + ".fdt");
    this.indexStream = paramDirectory.createFile(paramString + ".fdx");
  }

  final void close()
    throws IOException
  {
    this.fieldsStream.close();
    this.indexStream.close();
  }

  final void addDocument(Document paramDocument)
    throws IOException
  {
    this.indexStream.writeLong(this.fieldsStream.getFilePointer());
    int i = 0;
    Enumeration localEnumeration = paramDocument.fields();
    Field localField;
    while (localEnumeration.hasMoreElements())
    {
      localField = (Field)localEnumeration.nextElement();
      if (localField.isStored())
        i++;
    }
    this.fieldsStream.writeVInt(i);
    localEnumeration = paramDocument.fields();
    while (localEnumeration.hasMoreElements())
    {
      localField = (Field)localEnumeration.nextElement();
      if (localField.isStored())
      {
        this.fieldsStream.writeVInt(this.fieldInfos.fieldNumber(localField.name()));
        byte b = 0;
        if (localField.isTokenized())
          b = (byte)(b | 0x1);
        this.fieldsStream.writeByte(b);
        this.fieldsStream.writeString(localField.stringValue());
      }
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FieldsWriter
 * JD-Core Version:    0.6.2
 */