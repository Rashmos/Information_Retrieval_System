package org.apache.lucene.messages;

import java.io.Serializable;
import java.util.Locale;

public abstract interface Message extends Serializable
{
  public abstract String getKey();

  public abstract Object[] getArguments();

  public abstract String getLocalizedMessage();

  public abstract String getLocalizedMessage(Locale paramLocale);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.messages.Message
 * JD-Core Version:    0.6.2
 */