package org.apache.lucene.analysis;

import java.io.IOException;
import java.io.Reader;

public abstract class CharTokenizer extends Tokenizer
{
  private int offset = 0;
  private int bufferIndex = 0;
  private int dataLen = 0;
  private static final int MAX_WORD_LEN = 255;
  private static final int IO_BUFFER_SIZE = 1024;
  private final char[] buffer = new char['ÿ'];
  private final char[] ioBuffer = new char[1024];

  public CharTokenizer(Reader paramReader)
  {
    this.input = paramReader;
  }

  protected abstract boolean isTokenChar(char paramChar);

  protected char normalize(char paramChar)
  {
    return paramChar;
  }

  public final Token next()
    throws IOException
  {
    int i = 0;
    int j = this.offset;
    while (true)
    {
      this.offset += 1;
      if (this.bufferIndex >= this.dataLen)
      {
        this.dataLen = this.input.read(this.ioBuffer);
        this.bufferIndex = 0;
      }
      if (this.dataLen == -1)
      {
        if (i <= 0)
          return null;
      }
      else
      {
        char c = this.ioBuffer[(this.bufferIndex++)];
        if (isTokenChar(c))
        {
          if (i == 0)
            j = this.offset - 1;
          this.buffer[(i++)] = normalize(c);
          if (i == 255)
            break;
        }
        else if (i > 0)
        {
          break;
        }
      }
    }
    return new Token(new String(this.buffer, 0, i), j, j + i);
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.CharTokenizer
 * JD-Core Version:    0.6.2
 */