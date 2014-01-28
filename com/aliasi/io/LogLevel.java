/*     */ package com.aliasi.io;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public enum LogLevel
/*     */ {
/*  76 */   ALL(-2147483648), 
/*     */ 
/*  81 */   TRACE(0), 
/*     */ 
/*  86 */   DEBUG(1), 
/*     */ 
/*  91 */   INFO(2), 
/*     */ 
/*  96 */   WARN(3), 
/*     */ 
/* 101 */   ERROR(4), 
/*     */ 
/* 106 */   FATAL(5), 
/*     */ 
/* 111 */   NONE(2147483647);
/*     */ 
/*     */   int mSeverity;
/* 126 */   public static final Comparator<LogLevel> COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(LogLevel level1, LogLevel level2) {
/* 129 */       return level1.mSeverity < level2.mSeverity ? -1 : level1.mSeverity > level2.mSeverity ? 1 : 0;
/*     */     }
/* 126 */   };
/*     */ 
/*     */   private LogLevel(int severity)
/*     */   {
/* 115 */     this.mSeverity = severity;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.io.LogLevel
 * JD-Core Version:    0.6.2
 */