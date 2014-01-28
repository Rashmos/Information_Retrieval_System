package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;

final class FieldsReader
{
  private FieldInfos fieldInfos;
  private InputStream fieldsStream;
  private InputStream indexStream;
  private int size;

  FieldsReader(Directory paramDirectory, String paramString, FieldInfos paramFieldInfos)
    throws IOException
  {
    this.fieldInfos = paramFieldInfos;
    this.fieldsStream = paramDirectory.openFile(paramString + ".fdt");
    this.indexStream = paramDirectory.openFile(paramString + ".fdx");
    this.size = ((int)this.indexStream.length() / 8);
  }

  final void close()
    throws IOException
  {
    this.fieldsStream.close();
    this.indexStream.close();
  }

  final int size()
  {
    return this.size;
  }

  final Document doc(int paramInt)
    throws IOException
  {
    this.indexStream.seek(paramInt * 8L);
    long l = this.indexStream.readLong();
    this.fieldsStream.seek(l);
    Document localDocument = new Document();
    int i = this.fieldsStream.readVInt();
    for (int j = 0; j < i; j++)
    {
      int k = this.fieldsStream.readVInt();
      FieldInfo localFieldInfo = this.fieldInfos.fieldInfo(k);
      int m = this.fieldsStream.readByte();
      localDocument.add(new Field(localFieldInfo.name, this.fieldsStream.readString(), true, localFieldInfo.isIndexed, (m & 0x1) != 0));
    }
    return localDocument;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FieldsReader
 * JD-Core Version:    0.6.2
 */