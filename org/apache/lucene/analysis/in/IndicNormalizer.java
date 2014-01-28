/*     */ package org.apache.lucene.analysis.in;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class IndicNormalizer
/*     */ {
/*  45 */   private static final IdentityHashMap<Character.UnicodeBlock, ScriptData> scripts = new IdentityHashMap(9);
/*     */   private static final int[][] decompositions;
/*     */ 
/*     */   private static int flag(Character.UnicodeBlock ub)
/*     */   {
/*  49 */     return ((ScriptData)scripts.get(ub)).flag;
/*     */   }
/*     */ 
/*     */   public int normalize(char[] text, int len)
/*     */   {
/* 245 */     for (int i = 0; i < len; i++) {
/* 246 */       Character.UnicodeBlock block = Character.UnicodeBlock.of(text[i]);
/* 247 */       ScriptData sd = (ScriptData)scripts.get(block);
/* 248 */       if (sd != null) {
/* 249 */         int ch = text[i] - sd.base;
/* 250 */         if (sd.decompMask.get(ch))
/* 251 */           len = compose(ch, block, sd, text, i, len);
/*     */       }
/*     */     }
/* 254 */     return len;
/*     */   }
/*     */ 
/*     */   private int compose(int ch0, Character.UnicodeBlock block0, ScriptData sd, char[] text, int pos, int len)
/*     */   {
/* 262 */     if (pos + 1 >= len) {
/* 263 */       return len;
/*     */     }
/* 265 */     int ch1 = text[(pos + 1)] - sd.base;
/* 266 */     Character.UnicodeBlock block1 = Character.UnicodeBlock.of(text[(pos + 1)]);
/* 267 */     if (block1 != block0) {
/* 268 */       return len;
/*     */     }
/* 270 */     int ch2 = -1;
/*     */ 
/* 272 */     if (pos + 2 < len) {
/* 273 */       ch2 = text[(pos + 2)] - sd.base;
/* 274 */       Character.UnicodeBlock block2 = Character.UnicodeBlock.of(text[(pos + 2)]);
/* 275 */       if (text[(pos + 2)] == '‍')
/* 276 */         ch2 = 255;
/* 277 */       else if (block2 != block1) {
/* 278 */         ch2 = -1;
/*     */       }
/*     */     }
/* 281 */     for (int i = 0; i < decompositions.length; i++) {
/* 282 */       if ((decompositions[i][0] == ch0) && ((decompositions[i][4] & sd.flag) != 0) && 
/* 283 */         (decompositions[i][1] == ch1) && (
/* 283 */         (decompositions[i][2] < 0) || (decompositions[i][2] == ch2))) {
/* 284 */         text[pos] = ((char)(sd.base + decompositions[i][3]));
/* 285 */         len = StemmerUtil.delete(text, pos + 1, len);
/* 286 */         if (decompositions[i][2] >= 0)
/* 287 */           len = StemmerUtil.delete(text, pos + 1, len);
/* 288 */         return len;
/*     */       }
/*     */     }
/*     */ 
/* 292 */     return len;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  53 */     scripts.put(Character.UnicodeBlock.DEVANAGARI, new ScriptData(1, 2304));
/*  54 */     scripts.put(Character.UnicodeBlock.BENGALI, new ScriptData(2, 2432));
/*  55 */     scripts.put(Character.UnicodeBlock.GURMUKHI, new ScriptData(4, 2560));
/*  56 */     scripts.put(Character.UnicodeBlock.GUJARATI, new ScriptData(8, 2688));
/*  57 */     scripts.put(Character.UnicodeBlock.ORIYA, new ScriptData(16, 2816));
/*  58 */     scripts.put(Character.UnicodeBlock.TAMIL, new ScriptData(32, 2944));
/*  59 */     scripts.put(Character.UnicodeBlock.TELUGU, new ScriptData(64, 3072));
/*  60 */     scripts.put(Character.UnicodeBlock.KANNADA, new ScriptData(128, 3200));
/*  61 */     scripts.put(Character.UnicodeBlock.MALAYALAM, new ScriptData(256, 3328));
/*     */ 
/*  77 */     decompositions = new int[][] { { 5, 62, 69, 17, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 5, 62, 70, 18, flag(Character.UnicodeBlock.DEVANAGARI) }, { 5, 62, 71, 19, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 5, 62, 72, 20, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 5, 62, -1, 6, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.BENGALI) | flag(Character.UnicodeBlock.GURMUKHI) | flag(Character.UnicodeBlock.GUJARATI) | flag(Character.UnicodeBlock.ORIYA) }, { 5, 69, -1, 114, flag(Character.UnicodeBlock.DEVANAGARI) }, { 5, 69, -1, 13, flag(Character.UnicodeBlock.GUJARATI) }, { 5, 70, -1, 4, flag(Character.UnicodeBlock.DEVANAGARI) }, { 5, 71, -1, 15, flag(Character.UnicodeBlock.GUJARATI) }, { 5, 72, -1, 16, flag(Character.UnicodeBlock.GURMUKHI) | flag(Character.UnicodeBlock.GUJARATI) }, { 5, 73, -1, 17, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 5, 74, -1, 18, flag(Character.UnicodeBlock.DEVANAGARI) }, { 5, 75, -1, 19, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 5, 76, -1, 20, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GURMUKHI) | flag(Character.UnicodeBlock.GUJARATI) }, { 6, 69, -1, 17, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 6, 70, -1, 18, flag(Character.UnicodeBlock.DEVANAGARI) }, { 6, 71, -1, 19, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 6, 72, -1, 20, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 7, 87, -1, 8, flag(Character.UnicodeBlock.MALAYALAM) }, { 9, 65, -1, 10, flag(Character.UnicodeBlock.DEVANAGARI) }, { 9, 87, -1, 10, flag(Character.UnicodeBlock.TAMIL) | flag(Character.UnicodeBlock.MALAYALAM) }, { 14, 70, -1, 16, flag(Character.UnicodeBlock.MALAYALAM) }, { 15, 69, -1, 13, flag(Character.UnicodeBlock.DEVANAGARI) }, { 15, 70, -1, 14, flag(Character.UnicodeBlock.DEVANAGARI) }, { 15, 71, -1, 16, flag(Character.UnicodeBlock.DEVANAGARI) }, { 15, 87, -1, 16, flag(Character.UnicodeBlock.ORIYA) }, { 18, 62, -1, 19, flag(Character.UnicodeBlock.MALAYALAM) }, { 18, 76, -1, 20, flag(Character.UnicodeBlock.TELUGU) | flag(Character.UnicodeBlock.KANNADA) }, { 18, 85, -1, 19, flag(Character.UnicodeBlock.TELUGU) }, { 18, 87, -1, 20, flag(Character.UnicodeBlock.TAMIL) | flag(Character.UnicodeBlock.MALAYALAM) }, { 19, 87, -1, 20, flag(Character.UnicodeBlock.ORIYA) }, { 21, 60, -1, 88, flag(Character.UnicodeBlock.DEVANAGARI) }, { 22, 60, -1, 89, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GURMUKHI) }, { 23, 60, -1, 90, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GURMUKHI) }, { 28, 60, -1, 91, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GURMUKHI) }, { 33, 60, -1, 92, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.BENGALI) | flag(Character.UnicodeBlock.ORIYA) }, { 34, 60, -1, 93, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.BENGALI) | flag(Character.UnicodeBlock.ORIYA) }, { 35, 77, 255, 122, flag(Character.UnicodeBlock.MALAYALAM) }, { 36, 77, 255, 78, flag(Character.UnicodeBlock.BENGALI) }, { 40, 60, -1, 41, flag(Character.UnicodeBlock.DEVANAGARI) }, { 40, 77, 255, 123, flag(Character.UnicodeBlock.MALAYALAM) }, { 43, 60, -1, 94, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GURMUKHI) }, { 47, 60, -1, 95, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.BENGALI) }, { 44, 65, 65, 11, flag(Character.UnicodeBlock.TELUGU) }, { 48, 60, -1, 49, flag(Character.UnicodeBlock.DEVANAGARI) }, { 48, 77, 255, 124, flag(Character.UnicodeBlock.MALAYALAM) }, { 50, 77, 255, 125, flag(Character.UnicodeBlock.MALAYALAM) }, { 51, 60, -1, 52, flag(Character.UnicodeBlock.DEVANAGARI) }, { 51, 77, 255, 126, flag(Character.UnicodeBlock.MALAYALAM) }, { 53, 65, -1, 46, flag(Character.UnicodeBlock.TELUGU) }, { 62, 69, -1, 73, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 62, 70, -1, 74, flag(Character.UnicodeBlock.DEVANAGARI) }, { 62, 71, -1, 75, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 62, 72, -1, 76, flag(Character.UnicodeBlock.DEVANAGARI) | flag(Character.UnicodeBlock.GUJARATI) }, { 63, 85, -1, 64, flag(Character.UnicodeBlock.KANNADA) }, { 65, 65, -1, 66, flag(Character.UnicodeBlock.GURMUKHI) }, { 70, 62, -1, 74, flag(Character.UnicodeBlock.TAMIL) | flag(Character.UnicodeBlock.MALAYALAM) }, { 70, 66, 85, 75, flag(Character.UnicodeBlock.KANNADA) }, { 70, 66, -1, 74, flag(Character.UnicodeBlock.KANNADA) }, { 70, 70, -1, 72, flag(Character.UnicodeBlock.MALAYALAM) }, { 70, 85, -1, 71, flag(Character.UnicodeBlock.TELUGU) | flag(Character.UnicodeBlock.KANNADA) }, { 70, 86, -1, 72, flag(Character.UnicodeBlock.TELUGU) | flag(Character.UnicodeBlock.KANNADA) }, { 70, 87, -1, 76, flag(Character.UnicodeBlock.TAMIL) | flag(Character.UnicodeBlock.MALAYALAM) }, { 71, 62, -1, 75, flag(Character.UnicodeBlock.BENGALI) | flag(Character.UnicodeBlock.ORIYA) | flag(Character.UnicodeBlock.TAMIL) | flag(Character.UnicodeBlock.MALAYALAM) }, { 71, 87, -1, 76, flag(Character.UnicodeBlock.BENGALI) | flag(Character.UnicodeBlock.ORIYA) }, { 74, 85, -1, 75, flag(Character.UnicodeBlock.KANNADA) }, { 114, 63, -1, 7, flag(Character.UnicodeBlock.GURMUKHI) }, { 114, 64, -1, 8, flag(Character.UnicodeBlock.GURMUKHI) }, { 114, 71, -1, 15, flag(Character.UnicodeBlock.GURMUKHI) }, { 115, 65, -1, 9, flag(Character.UnicodeBlock.GURMUKHI) }, { 115, 66, -1, 10, flag(Character.UnicodeBlock.GURMUKHI) }, { 115, 75, -1, 19, flag(Character.UnicodeBlock.GURMUKHI) } };
/*     */ 
/* 225 */     for (ScriptData sd : scripts.values()) {
/* 226 */       sd.decompMask = new BitSet(127);
/* 227 */       for (int i = 0; i < decompositions.length; i++) {
/* 228 */         int ch = decompositions[i][0];
/* 229 */         int flags = decompositions[i][4];
/* 230 */         if ((flags & sd.flag) != 0)
/* 231 */           sd.decompMask.set(ch);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ScriptData
/*     */   {
/*     */     final int flag;
/*     */     final int base;
/*     */     BitSet decompMask;
/*     */ 
/*     */     ScriptData(int flag, int base)
/*     */     {
/*  40 */       this.flag = flag;
/*  41 */       this.base = base;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.in.IndicNormalizer
 * JD-Core Version:    0.6.2
 */