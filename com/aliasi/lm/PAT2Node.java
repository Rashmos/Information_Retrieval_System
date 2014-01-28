/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class PAT2Node extends AbstractPATNode
/*     */ {
/*     */   char mC1;
/*     */   char mC2;
/*     */ 
/*     */   PAT2Node(char c1, char c2)
/*     */   {
/* 619 */     this.mC1 = c1;
/* 620 */     this.mC2 = c2;
/*     */   }
/*     */ 
/*     */   char[] chars() {
/* 624 */     return new char[] { this.mC1, this.mC2 };
/*     */   }
/*     */   int length() {
/* 627 */     return 2;
/*     */   }
/*     */ 
/*     */   boolean stringMatch(char[] cs, int start, int end)
/*     */   {
/* 632 */     switch (end - start) { case 2:
/* 633 */       if (cs[(start + 1)] != this.mC2) return false; case 1:
/* 634 */       if (cs[start] != this.mC1) return false; break; }
/* 635 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT2Node
 * JD-Core Version:    0.6.2
 */