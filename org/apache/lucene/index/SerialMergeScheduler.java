package org.apache.lucene.index;

import java.io.IOException;

public class SerialMergeScheduler extends MergeScheduler
{
  public synchronized void merge(IndexWriter writer)
    throws CorruptIndexException, IOException
  {
    while (true)
    {
      MergePolicy.OneMerge merge = writer.getNextMerge();
      if (merge == null)
        break;
      writer.merge(merge);
    }
  }

  public void close()
  {
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SerialMergeScheduler
 * JD-Core Version:    0.6.2
 */