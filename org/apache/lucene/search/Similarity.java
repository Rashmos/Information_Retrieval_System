package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.Term;

public final class Similarity
{
  static final float[] NORM_TABLE = makeNormTable();

  public static final byte norm(int paramInt)
  {
    return (byte)(int)Math.ceil(255.0D / Math.sqrt(paramInt));
  }

  private static final float[] makeNormTable()
  {
    float[] arrayOfFloat = new float[256];
    for (int i = 0; i < 256; i++)
      arrayOfFloat[i] = (i / 255.0F);
    return arrayOfFloat;
  }

  static final float norm(byte paramByte)
  {
    return NORM_TABLE[(paramByte & 0xFF)];
  }

  static final float tf(int paramInt)
  {
    return (float)Math.sqrt(paramInt);
  }

  static final float tf(float paramFloat)
  {
    return (float)Math.sqrt(paramFloat);
  }

  static final float idf(Term paramTerm, Searcher paramSearcher)
    throws IOException
  {
    return idf(paramSearcher.docFreq(paramTerm), paramSearcher.maxDoc());
  }

  static final float idf(int paramInt1, int paramInt2)
  {
    return (float)(Math.log(paramInt2 / (paramInt1 + 1)) + 1.0D);
  }

  static final float coord(int paramInt1, int paramInt2)
  {
    return paramInt1 / paramInt2;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Similarity
 * JD-Core Version:    0.6.2
 */