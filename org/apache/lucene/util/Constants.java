package org.apache.lucene.util;

public final class Constants
{
  public static final String JAVA_VERSION = System.getProperty("java.version");
  public static final boolean JAVA_1_1 = JAVA_VERSION.startsWith("1.1.");
  public static final boolean JAVA_1_2 = JAVA_VERSION.startsWith("1.2.");
  public static final boolean JAVA_1_3 = JAVA_VERSION.startsWith("1.3.");
  public static final String OS_NAME = System.getProperty("os.name");
  public static final boolean LINUX = OS_NAME.startsWith("Linux");
  public static final boolean WINDOWS = OS_NAME.startsWith("Windows");
  public static final boolean SUN_OS = OS_NAME.startsWith("SunOS");
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.Constants
 * JD-Core Version:    0.6.2
 */