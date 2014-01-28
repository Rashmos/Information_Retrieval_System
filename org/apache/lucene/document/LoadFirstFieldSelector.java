/*    */ package org.apache.lucene.document;
/*    */ 
/*    */ public class LoadFirstFieldSelector
/*    */   implements FieldSelector
/*    */ {
/*    */   public FieldSelectorResult accept(String fieldName)
/*    */   {
/* 27 */     return FieldSelectorResult.LOAD_AND_BREAK;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.LoadFirstFieldSelector
 * JD-Core Version:    0.6.2
 */