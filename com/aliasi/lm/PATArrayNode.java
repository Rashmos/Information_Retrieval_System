/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class PATArrayNode extends AbstractPATNode
/*     */ {
/*     */   char[] mCs;
/*     */ 
/*     */   PATArrayNode(char[] cs)
/*     */   {
/* 702 */     this.mCs = cs;
/*     */   }
/*     */ 
/*     */   char[] chars() {
/* 706 */     return this.mCs;
/*     */   }
/*     */   int length() {
/* 709 */     return this.mCs.length;
/*     */   }
/*     */   boolean stringMatch(char[] cs, int start, int end) {
/* 712 */     for (int i = 0; i < end - start; i++)
/* 713 */       if (this.mCs[i] != cs[(start + i)]) return false;
/* 714 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PATArrayNode
 * JD-Core Version:    0.6.2
 */