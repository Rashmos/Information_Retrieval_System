/*    */ package com.aliasi.io;
/*    */ 
/*    */ import com.aliasi.util.Strings;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.IllegalFormatException;
/*    */ 
/*    */ class PrintWriterReporter extends Reporter
/*    */ {
/*    */   private long mStartTime;
/*    */   private final PrintWriter mWriter;
/*    */   private LogLevel mLevel;
/*    */ 
/*    */   public PrintWriterReporter(PrintWriter writer)
/*    */   {
/* 35 */     this.mWriter = writer;
/* 36 */     this.mStartTime = System.nanoTime();
/*    */   }
/*    */ 
/*    */   public synchronized void report(LogLevel level, String msg) {
/* 40 */     if (!isEnabled(level)) return;
/* 41 */     if (this.mWriter == null) return;
/* 42 */     printTimeStamp();
/* 43 */     this.mWriter.println(msg);
/* 44 */     this.mWriter.flush();
/*    */   }
/*    */ 
/*    */   public synchronized void reportf(LogLevel level, String format, Object[] args) {
/* 48 */     if (!isEnabled(level)) return;
/* 49 */     if (this.mWriter == null) return;
/* 50 */     printTimeStamp();
/*    */     try {
/* 52 */       this.mWriter.printf(format, args);
/*    */     } catch (IllegalFormatException e) {
/* 54 */       report(LogLevel.WARN, "Illegal format in printf");
/* 55 */       if (format != null)
/* 56 */         this.mWriter.print("format=" + format);
/* 57 */       for (int i = 0; i < args.length; i++)
/* 58 */         this.mWriter.print("; arg[" + i + "]=" + args[i]);
/* 59 */       this.mWriter.println();
/*    */     }
/* 61 */     this.mWriter.flush();
/*    */   }
/*    */ 
/*    */   public synchronized void close()
/*    */   {
/* 66 */     if (this.mWriter != null)
/* 67 */       this.mWriter.close();
/*    */   }
/*    */ 
/*    */   void printTimeStamp() {
/* 71 */     this.mWriter.printf("%9s ", new Object[] { elapsedTime() });
/*    */   }
/*    */ 
/*    */   long elapsedTimeNano() {
/* 75 */     return System.nanoTime() - this.mStartTime;
/*    */   }
/*    */ 
/*    */   String elapsedTime() {
/* 79 */     return Strings.nsToString(elapsedTimeNano());
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.io.PrintWriterReporter
 * JD-Core Version:    0.6.2
 */