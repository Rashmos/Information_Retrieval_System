package org.apache.lucene.index;

import java.io.IOException;
import java.util.List;

public abstract interface IndexDeletionPolicy
{
  public abstract void onInit(List<? extends IndexCommit> paramList)
    throws IOException;

  public abstract void onCommit(List<? extends IndexCommit> paramList)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexDeletionPolicy
 * JD-Core Version:    0.6.2
 */