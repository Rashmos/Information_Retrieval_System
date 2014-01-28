package com.aliasi.util;

import java.io.IOException;
import java.io.ObjectOutput;

public abstract interface Compilable
{
  public abstract void compileTo(ObjectOutput paramObjectOutput)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Compilable
 * JD-Core Version:    0.6.2
 */