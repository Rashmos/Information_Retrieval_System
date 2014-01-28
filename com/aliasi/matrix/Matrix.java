package com.aliasi.matrix;

public abstract interface Matrix
{
  public abstract int numRows();

  public abstract int numColumns();

  public abstract double value(int paramInt1, int paramInt2);

  public abstract void setValue(int paramInt1, int paramInt2, double paramDouble);

  public abstract Vector rowVector(int paramInt);

  public abstract Vector columnVector(int paramInt);

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.Matrix
 * JD-Core Version:    0.6.2
 */