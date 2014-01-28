package org.apache.lucene.store;

import java.io.IOException;

public abstract class Lock
{
  public abstract boolean obtain()
    throws IOException;

  public abstract void release();

  public static abstract class With
  {
    private Lock lock;
    private int sleepInterval = 1000;
    private int maxSleeps = 10;

    public With(Lock paramLock)
    {
      this.lock = paramLock;
    }

    protected abstract Object doBody()
      throws IOException;

    public Object run()
      throws IOException
    {
      boolean bool = false;
      try
      {
        bool = this.lock.obtain();
        int i = 0;
        while (!bool)
        {
          i++;
          if (i == this.maxSleeps)
            throw new IOException("Timed out waiting for: " + this.lock);
          try
          {
            Thread.sleep(this.sleepInterval);
          }
          catch (InterruptedException localInterruptedException)
          {
            throw new IOException(localInterruptedException.toString());
          }
          bool = this.lock.obtain();
        }
        Object localObject1 = doBody();
        return localObject1;
      }
      finally
      {
        if (bool)
          this.lock.release();
      }
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.Lock
 * JD-Core Version:    0.6.2
 */