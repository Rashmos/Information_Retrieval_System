package org.apache.lucene.util;

public abstract class MemoryModel
{
  public abstract int getArraySize();

  public abstract int getClassSize();

  public abstract int getPrimitiveSize(Class paramClass);

  public abstract int getReferenceSize();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.MemoryModel
 * JD-Core Version:    0.6.2
 */