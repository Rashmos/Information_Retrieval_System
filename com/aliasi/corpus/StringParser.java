/*    */ package com.aliasi.corpus;
/*    */ 
/*    */ import com.aliasi.util.Streams;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ public abstract class StringParser<H extends Handler> extends Parser<H>
/*    */ {
/*    */   protected StringParser()
/*    */   {
/* 42 */     this(null);
/*    */   }
/*    */ 
/*    */   protected StringParser(H handler)
/*    */   {
/* 51 */     super(handler);
/*    */   }
/*    */ 
/*    */   public void parse(InputSource in)
/*    */     throws IOException
/*    */   {
/* 67 */     char[] cs = Streams.toCharArray(in);
/* 68 */     parseString(cs, 0, cs.length);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.StringParser
 * JD-Core Version:    0.6.2
 */