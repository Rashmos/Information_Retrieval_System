package org.apache.lucene.index;

import java.io.IOException;

abstract class FormatPostingsFieldsConsumer
{
  abstract FormatPostingsTermsConsumer addField(FieldInfo paramFieldInfo)
    throws IOException;

  abstract void finish()
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FormatPostingsFieldsConsumer
 * JD-Core Version:    0.6.2
 */