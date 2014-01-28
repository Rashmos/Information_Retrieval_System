package org.apache.lucene.analysis.standard;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

public final class FastCharStream
  implements CharStream
{
  char[] buffer = null;
  int bufferLength = 0;
  int bufferPosition = 0;
  int tokenStart = 0;
  int bufferStart = 0;
  Reader input;

  public FastCharStream(Reader paramReader)
  {
    this.input = paramReader;
  }

  public final char readChar()
    throws IOException
  {
    if (this.bufferPosition >= this.bufferLength)
      refill();
    return this.buffer[(this.bufferPosition++)];
  }

  private final void refill()
    throws IOException
  {
    int i = this.bufferLength - this.tokenStart;
    if (this.tokenStart == 0)
    {
      if (this.buffer == null)
      {
        this.buffer = new char[2048];
      }
      else if (this.bufferLength == this.buffer.length)
      {
        char[] arrayOfChar = new char[this.buffer.length * 2];
        System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.bufferLength);
        this.buffer = arrayOfChar;
      }
    }
    else
      System.arraycopy(this.buffer, this.tokenStart, this.buffer, 0, i);
    this.bufferLength = i;
    this.bufferPosition = i;
    this.bufferStart += this.tokenStart;
    this.tokenStart = 0;
    int j = this.input.read(this.buffer, i, this.buffer.length - i);
    if (j == -1)
      throw new IOException("read past eof");
    this.bufferLength += j;
  }

  public final char BeginToken()
    throws IOException
  {
    this.tokenStart = this.bufferPosition;
    return readChar();
  }

  public final void backup(int paramInt)
  {
    this.bufferPosition -= paramInt;
  }

  public final String GetImage()
  {
    return new String(this.buffer, this.tokenStart, this.bufferPosition - this.tokenStart);
  }

  public final char[] GetSuffix(int paramInt)
  {
    char[] arrayOfChar = new char[paramInt];
    System.arraycopy(this.buffer, this.bufferPosition - paramInt, arrayOfChar, 0, paramInt);
    return arrayOfChar;
  }

  public final void Done()
  {
    try
    {
      this.input.close();
    }
    catch (IOException localIOException)
    {
      System.err.println("Caught: " + localIOException + "; ignoring.");
    }
  }

  public final int getColumn()
  {
    return this.bufferStart + this.bufferPosition;
  }

  public final int getLine()
  {
    return 1;
  }

  public final int getEndColumn()
  {
    return this.bufferStart + this.bufferPosition;
  }

  public final int getEndLine()
  {
    return 1;
  }

  public final int getBeginColumn()
  {
    return this.bufferStart + this.tokenStart;
  }

  public final int getBeginLine()
  {
    return 1;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.standard.FastCharStream
 * JD-Core Version:    0.6.2
 */