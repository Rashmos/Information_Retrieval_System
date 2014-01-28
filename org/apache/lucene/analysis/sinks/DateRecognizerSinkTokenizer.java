/*    */ package org.apache.lucene.analysis.sinks;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import org.apache.lucene.analysis.SinkTokenizer;
/*    */ import org.apache.lucene.analysis.Token;
/*    */ 
/*    */ public class DateRecognizerSinkTokenizer extends SinkTokenizer
/*    */ {
/*    */   public static final String DATE_TYPE = "date";
/*    */   protected DateFormat dateFormat;
/*    */ 
/*    */   public DateRecognizerSinkTokenizer()
/*    */   {
/* 46 */     this(null, SimpleDateFormat.getDateInstance());
/*    */   }
/*    */ 
/*    */   public DateRecognizerSinkTokenizer(DateFormat dateFormat) {
/* 50 */     this(null, dateFormat);
/*    */   }
/*    */ 
/*    */   public DateRecognizerSinkTokenizer(List input)
/*    */   {
/* 58 */     this(input, SimpleDateFormat.getDateInstance());
/*    */   }
/*    */ 
/*    */   public DateRecognizerSinkTokenizer(List input, DateFormat dateFormat)
/*    */   {
/* 67 */     super(input);
/* 68 */     this.dateFormat = dateFormat;
/*    */   }
/*    */ 
/*    */   public void add(Token t)
/*    */   {
/* 74 */     if (t != null)
/*    */       try {
/* 76 */         Date date = this.dateFormat.parse(t.term());
/* 77 */         if (date != null) {
/* 78 */           t.setType("date");
/* 79 */           super.add(t);
/*    */         }
/*    */       }
/*    */       catch (ParseException e)
/*    */       {
/*    */       }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.sinks.DateRecognizerSinkTokenizer
 * JD-Core Version:    0.6.2
 */