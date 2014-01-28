package com.aliasi.matrix;

import com.aliasi.util.Proximity;

public abstract interface KernelFunction extends Proximity<Vector>
{
  public abstract double proximity(Vector paramVector1, Vector paramVector2);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.KernelFunction
 * JD-Core Version:    0.6.2
 */