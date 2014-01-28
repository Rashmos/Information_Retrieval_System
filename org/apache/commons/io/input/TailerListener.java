package org.apache.commons.io.input;

public abstract interface TailerListener
{
  public abstract void init(Tailer paramTailer);

  public abstract void fileNotFound();

  public abstract void fileRotated();

  public abstract void handle(String paramString);

  public abstract void handle(Exception paramException);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.input.TailerListener
 * JD-Core Version:    0.6.2
 */