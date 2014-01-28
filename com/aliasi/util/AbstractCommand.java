/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public abstract class AbstractCommand
/*     */   implements Runnable
/*     */ {
/*     */   private final long mStartTime;
/*     */   private final Properties mProperties;
/*  57 */   private int mBareArgCount = 0;
/*     */ 
/*  62 */   private Properties mDefaultProperties = null;
/*     */   public static final String HAS_PROPERTY_VALUE = "*HAS_PROPERTY_VALUE*";
/*     */   public static final String BARE_ARG_PREFIX = "BARE_ARG_";
/*     */ 
/*     */   public AbstractCommand(String[] args)
/*     */   {
/*  73 */     this(args, new Properties());
/*     */   }
/*     */ 
/*     */   public AbstractCommand(String[] args, Properties defaultProperties)
/*     */   {
/*  87 */     this.mDefaultProperties = defaultProperties;
/*  88 */     this.mProperties = new Properties(defaultProperties);
/*  89 */     parse(args);
/*  90 */     this.mStartTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public void addDefaultProperty(String property, String defaultValue)
/*     */   {
/* 100 */     this.mDefaultProperties.setProperty(property, defaultValue);
/*     */   }
/*     */ 
/*     */   public long elapsedTimeMillis()
/*     */   {
/* 112 */     return System.currentTimeMillis() - this.mStartTime;
/*     */   }
/*     */ 
/*     */   public long startTimeMillis()
/*     */   {
/* 121 */     return this.mStartTime;
/*     */   }
/*     */ 
/*     */   public abstract void run();
/*     */ 
/*     */   public int numBareArguments()
/*     */   {
/* 137 */     return this.mBareArgCount;
/*     */   }
/*     */ 
/*     */   public String[] bareArguments()
/*     */   {
/* 149 */     String[] arguments = new String[numBareArguments()];
/* 150 */     for (int i = 0; i < arguments.length; i++)
/* 151 */       arguments[i] = getBareArgument(i);
/* 152 */     return arguments;
/*     */   }
/*     */ 
/*     */   public boolean hasFlag(String arg)
/*     */   {
/* 165 */     String value = this.mProperties.getProperty(arg);
/* 166 */     return (value != null) && (value.equals("*HAS_PROPERTY_VALUE*"));
/*     */   }
/*     */ 
/*     */   public boolean hasProperty(String arg)
/*     */   {
/* 176 */     String value = this.mProperties.getProperty(arg);
/* 177 */     return (value != null) && (!value.equals("*HAS_PROPERTY_VALUE*"));
/*     */   }
/*     */ 
/*     */   public String getBareArgument(int n)
/*     */   {
/* 189 */     return this.mProperties.getProperty(bareArgumentProperty(n));
/*     */   }
/*     */ 
/*     */   public String getArgument(String key)
/*     */   {
/* 203 */     return this.mProperties.getProperty(key);
/*     */   }
/*     */ 
/*     */   public Properties getArguments()
/*     */   {
/* 219 */     return this.mProperties;
/*     */   }
/*     */ 
/*     */   public String getExistingArgument(String key)
/*     */     throws IllegalArgumentException
/*     */   {
/* 234 */     String result = this.mProperties.getProperty(key);
/* 235 */     if (result == null) {
/* 236 */       illegalPropertyArgument("Require value.", key);
/*     */     }
/* 238 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean hasArgument(String key)
/*     */   {
/* 249 */     return this.mProperties.containsKey(key);
/*     */   }
/*     */ 
/*     */   public int getArgumentInt(String key)
/*     */   {
/* 264 */     String valString = getExistingArgument(key);
/*     */     try {
/* 266 */       return Integer.valueOf(valString).intValue();
/*     */     } catch (NumberFormatException e) {
/* 268 */       illegalPropertyArgument("Required integer.", key);
/*     */     }
/* 270 */     return -1;
/*     */   }
/*     */ 
/*     */   public long getArgumentLong(String key)
/*     */   {
/* 286 */     String valString = getExistingArgument(key);
/*     */     try {
/* 288 */       return Long.valueOf(valString).longValue();
/*     */     } catch (NumberFormatException e) {
/* 290 */       illegalPropertyArgument("Required integer.", key);
/*     */     }
/* 292 */     return -1L;
/*     */   }
/*     */ 
/*     */   public double getArgumentDouble(String key)
/*     */   {
/* 308 */     String valString = getArgument(key);
/* 309 */     if (valString == null) {
/* 310 */       throw new IllegalArgumentException("No value found for argument=" + key);
/*     */     }
/*     */     try
/*     */     {
/* 314 */       return Double.valueOf(valString).doubleValue(); } catch (NumberFormatException e) {
/*     */     }
/* 316 */     throw new IllegalArgumentException("Required double value for arg=" + key + " Found=" + valString);
/*     */   }
/*     */ 
/*     */   public File getArgumentFile(String key)
/*     */   {
/* 333 */     String fileName = getExistingArgument(key);
/* 334 */     return new File(fileName);
/*     */   }
/*     */ 
/*     */   public File getArgumentExistingNormalFile(String key)
/*     */   {
/* 348 */     File file = getArgumentFile(key);
/* 349 */     if (!file.isFile())
/* 350 */       illegalPropertyArgument("Require existing normal file.", key);
/* 351 */     return file;
/*     */   }
/*     */ 
/*     */   public File getArgumentDirectory(String key)
/*     */   {
/* 365 */     File dir = getArgumentFile(key);
/*     */     try {
/* 367 */       if (!dir.isDirectory())
/* 368 */         illegalPropertyArgument("Require existing directory.", key);
/*     */     }
/*     */     catch (SecurityException e) {
/* 371 */       illegalPropertyArgument("Security exception accessing directory.", key);
/*     */     }
/*     */ 
/* 374 */     return dir;
/*     */   }
/*     */ 
/*     */   public File getOrCreateArgumentDirectory(String key)
/*     */   {
/* 389 */     File dir = getArgumentFile(key);
/*     */     try {
/* 391 */       if (dir.isFile()) {
/* 392 */         illegalPropertyArgument("Must be existing or creatable directory.", key);
/*     */       }
/* 394 */       if ((!dir.isDirectory()) && (!dir.mkdirs()))
/* 395 */         illegalPropertyArgument("Could not create directory.", key);
/*     */     }
/*     */     catch (SecurityException e) {
/* 398 */       illegalPropertyArgument("Security exception inspecting or creating directory.", key);
/*     */     }
/*     */ 
/* 401 */     return dir;
/*     */   }
/*     */ 
/*     */   protected File getArgumentCreatableFile(String fileParam)
/*     */   {
/* 413 */     File file = getArgumentFile(fileParam);
/* 414 */     if (file.isDirectory())
/* 415 */       illegalPropertyArgument("File must be normal.  Found directory=", fileParam);
/* 416 */     File parentDir = file.getParentFile();
/* 417 */     if (parentDir == null)
/* 418 */       parentDir = new File(".");
/* 419 */     if (parentDir.isFile()) {
/* 420 */       illegalPropertyArgument("Parent cannot be ordinary file.", fileParam);
/*     */     }
/* 422 */     if (!parentDir.isDirectory()) {
/* 423 */       System.out.println("Creating model parent directory=" + parentDir);
/* 424 */       parentDir.mkdirs();
/*     */     }
/* 426 */     return file;
/*     */   }
/*     */ 
/*     */   private final void parse(String[] args)
/*     */   {
/* 437 */     for (int i = 0; i < args.length; i++)
/* 438 */       parseSingleArg(args[i]);
/*     */   }
/*     */ 
/*     */   private final void parseSingleArg(String arg)
/*     */   {
/* 449 */     if (arg.length() < 1)
/* 450 */       return;
/* 451 */     if (arg.charAt(0) == '-')
/* 452 */       parseSingleBody(arg.substring(1));
/*     */     else
/* 454 */       this.mProperties.setProperty(bareArgumentProperty(this.mBareArgCount++), arg);
/*     */   }
/*     */ 
/*     */   private void parseSingleBody(String arg)
/*     */   {
/* 468 */     if (arg.length() < 1) return;
/* 469 */     int pos = arg.indexOf('=');
/* 470 */     if (pos < 0) {
/* 471 */       this.mProperties.setProperty(arg, "*HAS_PROPERTY_VALUE*");
/* 472 */       return;
/*     */     }
/* 474 */     String property = arg.substring(0, pos);
/* 475 */     String value = arg.substring(pos + 1);
/* 476 */     if (property.length() <= 0) {
/* 477 */       illegalArgument("Property must have non-zero-length.", '-' + arg + '=' + value);
/*     */     }
/*     */ 
/* 484 */     this.mProperties.setProperty(property, value);
/*     */   }
/*     */ 
/*     */   protected void illegalPropertyArgument(String msg, String key)
/*     */   {
/* 498 */     throw new IllegalArgumentException(msg + " Found -" + key + "=" + getArgument(key));
/*     */   }
/*     */ 
/*     */   protected void illegalArgument(String msg, String arg)
/*     */   {
/* 514 */     illegalArgument(msg + "Found:" + arg);
/*     */   }
/*     */ 
/*     */   protected void illegalArgument(String msg, Exception e)
/*     */   {
/* 527 */     illegalArgument(msg + " Contained exception =" + e);
/*     */   }
/*     */ 
/*     */   protected void illegalArgument(String msg)
/*     */   {
/* 537 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   public void checkParameterImplication(String ifParam, String thenParam)
/*     */   {
/* 551 */     String ifVal = getArgument(ifParam);
/* 552 */     String thenVal = getArgument(thenParam);
/* 553 */     if ((ifVal != null) && (thenVal == null))
/* 554 */       illegalArgument("If param=" + ifParam + " is defined, then param=" + thenParam + " should be defined.");
/*     */   }
/*     */ 
/*     */   private static String bareArgumentProperty(int n)
/*     */   {
/* 567 */     return "BARE_ARG_" + n;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.AbstractCommand
 * JD-Core Version:    0.6.2
 */