/*    */ package set;
/*    */ 
/*    */ import org.tartarus.snowball.ext.PorterStemmer;
/*    */ 
/*    */ public class Stemming
/*    */ {
/*    */   public String stem(String word)
/*    */   {
/*  8 */     PorterStemmer stem = new PorterStemmer();
/*  9 */     stem.setCurrent(word);
/* 10 */     stem.stem();
/* 11 */     return stem.getCurrent();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     set.Stemming
 * JD-Core Version:    0.6.2
 */