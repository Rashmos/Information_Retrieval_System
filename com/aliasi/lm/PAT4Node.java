/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class PAT4Node extends AbstractPATNode
/*     */ {
/*     */   char mC1;
/*     */   char mC2;
/*     */   char mC3;
/*     */   char mC4;
/*     */ 
/*     */   PAT4Node(char c1, char c2, char c3, char c4)
/*     */   {
/* 674 */     this.mC1 = c1;
/* 675 */     this.mC2 = c2;
/* 676 */     this.mC3 = c3;
/* 677 */     this.mC4 = c4;
/*     */   }
/*     */ 
/*     */   char[] chars() {
/* 681 */     return new char[] { this.mC1, this.mC2, this.mC3, this.mC4 };
/*     */   }
/*     */   int length() {
/* 684 */     return 4;
/*     */   }
/*     */ 
/*     */   boolean stringMatch(char[] cs, int start, int end)
/*     */   {
/* 689 */     switch (end - start) { case 4:
/* 690 */       if (cs[(start + 3)] != this.mC4) return false; case 3:
/* 691 */       if (cs[(start + 2)] != this.mC3) return false; case 2:
/* 692 */       if (cs[(start + 1)] != this.mC2) return false; case 1:
/* 693 */       if (cs[start] != this.mC1) return false; break; }
/* 694 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT4Node
 * JD-Core Version:    0.6.2
 */