package org.apache.lucene.document;

import java.io.Reader;
import java.io.Serializable;
import org.apache.lucene.analysis.TokenStream;

public abstract interface Fieldable extends Serializable
{
  public abstract void setBoost(float paramFloat);

  public abstract float getBoost();

  public abstract String name();

  public abstract String stringValue();

  public abstract Reader readerValue();

  public abstract TokenStream tokenStreamValue();

  public abstract boolean isStored();

  public abstract boolean isIndexed();

  public abstract boolean isTokenized();

  public abstract boolean isTermVectorStored();

  public abstract boolean isStoreOffsetWithTermVector();

  public abstract boolean isStorePositionWithTermVector();

  public abstract boolean isBinary();

  public abstract boolean getOmitNorms();

  public abstract void setOmitNorms(boolean paramBoolean);

  public abstract boolean isLazy();

  public abstract int getBinaryOffset();

  public abstract int getBinaryLength();

  public abstract byte[] getBinaryValue();

  public abstract byte[] getBinaryValue(byte[] paramArrayOfByte);

  public abstract boolean getOmitTermFreqAndPositions();

  public abstract void setOmitTermFreqAndPositions(boolean paramBoolean);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.Fieldable
 * JD-Core Version:    0.6.2
 */