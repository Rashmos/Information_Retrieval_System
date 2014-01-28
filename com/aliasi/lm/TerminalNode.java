/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.util.Strings;
/*     */ 
/*     */ abstract class TerminalNode extends AbstractDtrNode
/*     */ {
/*     */   char[] chars()
/*     */   {
/* 306 */     return Strings.EMPTY_CHAR_ARRAY;
/*     */   }
/*     */ 
/*     */   Node[] dtrs() {
/* 310 */     return NodeFactory.EMPTY_NODES;
/*     */   }
/*     */ 
/*     */   public long contextCount(char[] cs, int start, int end) {
/* 314 */     return 0L;
/*     */   }
/*     */   public Node getDtr(char c) {
/* 317 */     return null;
/*     */   }
/* 319 */   public int numDtrs() { return 0; }
/*     */ 
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TerminalNode
 * JD-Core Version:    0.6.2
 */