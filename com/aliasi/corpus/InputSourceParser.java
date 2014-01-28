/*    */ package com.aliasi.corpus;
/*    */ 
/*    */ import com.aliasi.util.Strings;
/*    */ import java.io.CharArrayReader;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ public abstract class InputSourceParser<H extends Handler> extends Parser<H>
/*    */ {
/*    */   protected InputSourceParser()
/*    */   {
/* 43 */     this(null);
/*    */   }
/*    */ 
/*    */   protected InputSourceParser(H handler)
/*    */   {
/* 52 */     super(handler);
/*    */   }
/*    */ 
/*    */   public void parseString(char[] cs, int start, int end)
/*    */     throws IOException
/*    */   {
/* 73 */     Strings.checkArgsStartEnd(cs, start, end);
/* 74 */     CharArrayReader reader = new CharArrayReader(cs, start, end - start);
/* 75 */     InputSource in = new InputSource(reader);
/* 76 */     parse(in);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.InputSourceParser
 * JD-Core Version:    0.6.2
 */