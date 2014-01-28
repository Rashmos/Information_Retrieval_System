package org.apache.lucene.util;

public abstract class PriorityQueue
{
  private Object[] heap;
  private int size;

  protected abstract boolean lessThan(Object paramObject1, Object paramObject2);

  protected final void initialize(int paramInt)
  {
    this.size = 0;
    int i = paramInt * 2 + 1;
    this.heap = new Object[i];
  }

  public final void put(Object paramObject)
  {
    this.size += 1;
    this.heap[this.size] = paramObject;
    upHeap();
  }

  public final Object top()
  {
    if (this.size > 0)
      return this.heap[1];
    return null;
  }

  public final Object pop()
  {
    if (this.size > 0)
    {
      Object localObject = this.heap[1];
      this.heap[1] = this.heap[this.size];
      this.heap[this.size] = null;
      this.size -= 1;
      downHeap();
      return localObject;
    }
    return null;
  }

  public final void adjustTop()
  {
    downHeap();
  }

  public final int size()
  {
    return this.size;
  }

  public final void clear()
  {
    for (int i = 0; i < this.size; i++)
      this.heap[i] = null;
    this.size = 0;
  }

  private final void upHeap()
  {
    int i = this.size;
    Object localObject = this.heap[i];
    int j = i >>> 1;
    while ((j > 0) && (lessThan(localObject, this.heap[j])))
    {
      this.heap[i] = this.heap[j];
      i = j;
      j >>>= 1;
    }
    this.heap[i] = localObject;
  }

  private final void downHeap()
  {
    int i = 1;
    Object localObject = this.heap[i];
    int j = i << 1;
    int k = j + 1;
    if ((k <= this.size) && (lessThan(this.heap[k], this.heap[j])));
    label108: for (j = k; (j <= this.size) && (lessThan(this.heap[j], localObject)); j = k)
    {
      this.heap[i] = this.heap[j];
      i = j;
      j = i << 1;
      k = j + 1;
      if ((k > this.size) || (!lessThan(this.heap[k], this.heap[j])))
        break label108;
    }
    this.heap[i] = localObject;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.PriorityQueue
 * JD-Core Version:    0.6.2
 */