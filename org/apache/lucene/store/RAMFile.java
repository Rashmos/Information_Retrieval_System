package org.apache.lucene.store;

import java.util.Vector;

final class RAMFile
{
  Vector buffers = new Vector();
  long length;
  long lastModified = System.currentTimeMillis();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.RAMFile
 * JD-Core Version:    0.6.2
 */