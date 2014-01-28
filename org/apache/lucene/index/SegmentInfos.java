package org.apache.lucene.index;

import java.io.IOException;
import java.util.Vector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.OutputStream;

final class SegmentInfos extends Vector
{
  public int counter = 0;

  public final SegmentInfo info(int paramInt)
  {
    return (SegmentInfo)elementAt(paramInt);
  }

  public final void read(Directory paramDirectory)
    throws IOException
  {
    InputStream localInputStream = paramDirectory.openFile("segments");
    try
    {
      this.counter = localInputStream.readInt();
      for (int i = localInputStream.readInt(); i > 0; i--)
      {
        SegmentInfo localSegmentInfo = new SegmentInfo(localInputStream.readString(), localInputStream.readInt(), paramDirectory);
        addElement(localSegmentInfo);
      }
    }
    finally
    {
      localInputStream.close();
    }
  }

  public final void write(Directory paramDirectory)
    throws IOException
  {
    OutputStream localOutputStream = paramDirectory.createFile("segments.new");
    try
    {
      localOutputStream.writeInt(this.counter);
      localOutputStream.writeInt(size());
      for (int i = 0; i < size(); i++)
      {
        SegmentInfo localSegmentInfo = info(i);
        localOutputStream.writeString(localSegmentInfo.name);
        localOutputStream.writeInt(localSegmentInfo.docCount);
      }
    }
    finally
    {
      localOutputStream.close();
    }
    paramDirectory.renameFile("segments.new", "segments");
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentInfos
 * JD-Core Version:    0.6.2
 */