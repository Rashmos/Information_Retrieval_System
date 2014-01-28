/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class PAT1Node extends AbstractPATNode
/*     */ {
/*     */   char mC;
/*     */ 
/*     */   PAT1Node(char c)
/*     */   {
/* 596 */     this.mC = c;
/*     */   }
/*     */ 
/*     */   char[] chars() {
/* 600 */     return new char[] { this.mC };
/*     */   }
/*     */   int length() {
/* 603 */     return 1;
/*     */   }
/*     */ 
/*     */   boolean stringMatch(char[] cs, int start, int end)
/*     */   {
/* 608 */     switch (end - start) { case 1:
/* 609 */       if (cs[start] != this.mC) return false; break; }
/* 610 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT1Node
 * JD-Core Version:    0.6.2
 */