package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.store.InputStream;

final class SegmentTermPositions extends SegmentTermDocs
  implements TermPositions
{
  private InputStream proxStream = (InputStream)this.parent.proxStream.clone();
  private int proxCount;
  private int position;

  SegmentTermPositions(SegmentReader paramSegmentReader)
    throws IOException
  {
    super(paramSegmentReader);
  }

  final void seek(TermInfo paramTermInfo)
    throws IOException
  {
    super.seek(paramTermInfo);
    if (paramTermInfo != null)
      this.proxStream.seek(paramTermInfo.proxPointer);
    else
      this.proxCount = 0;
  }

  public final void close()
    throws IOException
  {
    super.close();
    this.proxStream.close();
  }

  public final int nextPosition()
    throws IOException
  {
    this.proxCount -= 1;
    return this.position += this.proxStream.readVInt();
  }

  protected final void skippingDoc()
    throws IOException
  {
    for (int i = this.freq; i > 0; i--)
      this.proxStream.readVInt();
  }

  public final boolean next()
    throws IOException
  {
    for (int i = this.proxCount; i > 0; i--)
      this.proxStream.readVInt();
    if (super.next())
    {
      this.proxCount = this.freq;
      this.position = 0;
      return true;
    }
    return false;
  }

  public final int read(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws IOException
  {
    throw new RuntimeException();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentTermPositions
 * JD-Core Version:    0.6.2
 */