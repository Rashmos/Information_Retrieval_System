/*     */ package com.aliasi.io;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public abstract class Reporter
/*     */ {
/*     */   private LogLevel mLevel;
/*     */ 
/*     */   public Reporter()
/*     */   {
/*  83 */     this(LogLevel.NONE);
/*     */   }
/*     */ 
/*     */   public Reporter(LogLevel level)
/*     */   {
/*  93 */     this.mLevel = level;
/*     */   }
/*     */ 
/*     */   public abstract void report(LogLevel paramLogLevel, String paramString);
/*     */ 
/*     */   public void trace(String msg)
/*     */   {
/* 121 */     report(LogLevel.TRACE, msg);
/*     */   }
/*     */ 
/*     */   public void debug(String msg)
/*     */   {
/* 133 */     report(LogLevel.DEBUG, msg);
/*     */   }
/*     */ 
/*     */   public void info(String msg)
/*     */   {
/* 145 */     report(LogLevel.INFO, msg);
/*     */   }
/*     */ 
/*     */   public void warn(String msg)
/*     */   {
/* 157 */     report(LogLevel.WARN, msg);
/*     */   }
/*     */ 
/*     */   public void error(String msg)
/*     */   {
/* 169 */     report(LogLevel.ERROR, msg);
/*     */   }
/*     */ 
/*     */   public void fatal(String msg)
/*     */   {
/* 181 */     report(LogLevel.FATAL, msg);
/*     */   }
/*     */ 
/*     */   public final synchronized LogLevel getLevel()
/*     */   {
/* 193 */     return this.mLevel;
/*     */   }
/*     */ 
/*     */   public boolean isEnabled(LogLevel level)
/*     */   {
/* 208 */     return LogLevel.COMPARATOR.compare(level, getLevel()) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean isTraceEnabled()
/*     */   {
/* 221 */     return isEnabled(LogLevel.TRACE);
/*     */   }
/*     */ 
/*     */   public boolean isDebugEnabled()
/*     */   {
/* 234 */     return isEnabled(LogLevel.DEBUG);
/*     */   }
/*     */ 
/*     */   public boolean isInfoEnabled()
/*     */   {
/* 247 */     return isEnabled(LogLevel.INFO);
/*     */   }
/*     */ 
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 260 */     return isEnabled(LogLevel.WARN);
/*     */   }
/*     */ 
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 274 */     return isEnabled(LogLevel.ERROR);
/*     */   }
/*     */ 
/*     */   public boolean isFatalEnabled()
/*     */   {
/* 288 */     return isEnabled(LogLevel.FATAL);
/*     */   }
/*     */ 
/*     */   public final synchronized Reporter setLevel(LogLevel level)
/*     */   {
/* 309 */     this.mLevel = level;
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */   public abstract void close();
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.io.Reporter
 * JD-Core Version:    0.6.2
 */