package com.aliasi.matrix;

public abstract interface Vector
{
  public abstract int numDimensions();

  public abstract int[] nonZeroDimensions();

  public abstract void increment(double paramDouble, Vector paramVector);

  public abstract double value(int paramInt);

  public abstract void setValue(int paramInt, double paramDouble);

  public abstract double dotProduct(Vector paramVector);

  public abstract double cosine(Vector paramVector);

  public abstract double length();

  public abstract Vector add(Vector paramVector);

  public abstract int hashCode();

  public abstract boolean equals(Object paramObject);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.Vector
 * JD-Core Version:    0.6.2
 */