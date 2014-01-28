package org.apache.lucene.index;

import java.io.IOException;

class SegmentsTermPositions extends SegmentsTermDocs
  implements TermPositions
{
  SegmentsTermPositions(SegmentReader[] paramArrayOfSegmentReader, int[] paramArrayOfInt)
  {
    super(paramArrayOfSegmentReader, paramArrayOfInt);
  }

  protected final SegmentTermDocs termDocs(SegmentReader paramSegmentReader)
    throws IOException
  {
    return (SegmentTermDocs)paramSegmentReader.termPositions();
  }

  public final int nextPosition()
    throws IOException
  {
    return ((SegmentTermPositions)this.current).nextPosition();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentsTermPositions
 * JD-Core Version:    0.6.2
 */