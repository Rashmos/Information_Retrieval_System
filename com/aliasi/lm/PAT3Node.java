/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class PAT3Node extends AbstractPATNode
/*     */ {
/*     */   char mC1;
/*     */   char mC2;
/*     */   char mC3;
/*     */ 
/*     */   PAT3Node(char c1, char c2, char c3)
/*     */   {
/* 645 */     this.mC1 = c1;
/* 646 */     this.mC2 = c2;
/* 647 */     this.mC3 = c3;
/*     */   }
/*     */ 
/*     */   char[] chars() {
/* 651 */     return new char[] { this.mC1, this.mC2, this.mC3 };
/*     */   }
/*     */   int length() {
/* 654 */     return 3;
/*     */   }
/*     */ 
/*     */   boolean stringMatch(char[] cs, int start, int end)
/*     */   {
/* 659 */     switch (end - start) { case 3:
/* 660 */       if (cs[(start + 2)] != this.mC3) return false; case 2:
/* 661 */       if (cs[(start + 1)] != this.mC2) return false; case 1:
/* 662 */       if (cs[start] != this.mC1) return false; break; }
/* 663 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT3Node
 * JD-Core Version:    0.6.2
 */