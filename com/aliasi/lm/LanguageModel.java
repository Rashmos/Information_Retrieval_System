package com.aliasi.lm;

import com.aliasi.util.Compilable;

public abstract interface LanguageModel
{
  public abstract double log2Estimate(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract double log2Estimate(CharSequence paramCharSequence);

  public static abstract interface Tokenized extends LanguageModel
  {
    public abstract double tokenLog2Probability(String[] paramArrayOfString, int paramInt1, int paramInt2);

    public abstract double tokenProbability(String[] paramArrayOfString, int paramInt1, int paramInt2);
  }

  public static abstract interface Dynamic extends Compilable, LanguageModel
  {
    public abstract void train(CharSequence paramCharSequence);

    public abstract void train(CharSequence paramCharSequence, int paramInt);

    public abstract void train(char[] paramArrayOfChar, int paramInt1, int paramInt2);

    public abstract void train(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3);
  }

  public static abstract interface Process extends LanguageModel
  {
  }

  public static abstract interface Sequence extends LanguageModel
  {
  }

  public static abstract interface Conditional extends LanguageModel
  {
    public abstract double log2ConditionalEstimate(char[] paramArrayOfChar, int paramInt1, int paramInt2);

    public abstract double log2ConditionalEstimate(CharSequence paramCharSequence);

    public abstract char[] observedCharacters();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.LanguageModel
 * JD-Core Version:    0.6.2
 */