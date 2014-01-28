/*    */ package org.apache.lucene.analysis.sinks;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.ParseException;
/*    */ import java.util.Date;
/*    */ import org.apache.lucene.analysis.TeeSinkTokenFilter.SinkFilter;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.util.AttributeSource;
/*    */ 
/*    */ public class DateRecognizerSinkFilter extends TeeSinkTokenFilter.SinkFilter
/*    */ {
/*    */   public static final String DATE_TYPE = "date";
/*    */   protected DateFormat dateFormat;
/*    */   protected CharTermAttribute termAtt;
/*    */ 
/*    */   public DateRecognizerSinkFilter()
/*    */   {
/* 44 */     this(DateFormat.getDateInstance());
/*    */   }
/*    */ 
/*    */   public DateRecognizerSinkFilter(DateFormat dateFormat) {
/* 48 */     this.dateFormat = dateFormat;
/*    */   }
/*    */ 
/*    */   public boolean accept(AttributeSource source)
/*    */   {
/* 53 */     if (this.termAtt == null)
/* 54 */       this.termAtt = ((CharTermAttribute)source.addAttribute(CharTermAttribute.class));
/*    */     try
/*    */     {
/* 57 */       Date date = this.dateFormat.parse(this.termAtt.toString());
/* 58 */       if (date != null) {
/* 59 */         return true;
/*    */       }
/*    */     }
/*    */     catch (ParseException e)
/*    */     {
/*    */     }
/* 65 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.sinks.DateRecognizerSinkFilter
 * JD-Core Version:    0.6.2
 */