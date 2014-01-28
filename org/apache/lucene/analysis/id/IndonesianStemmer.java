/*     */ package org.apache.lucene.analysis.id;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class IndonesianStemmer
/*     */ {
/*     */   private int numSyllables;
/*     */   private int flags;
/*     */   private static final int REMOVED_KE = 1;
/*     */   private static final int REMOVED_PENG = 2;
/*     */   private static final int REMOVED_DI = 4;
/*     */   private static final int REMOVED_MENG = 8;
/*     */   private static final int REMOVED_TER = 16;
/*     */   private static final int REMOVED_BER = 32;
/*     */   private static final int REMOVED_PE = 64;
/*     */ 
/*     */   public int stem(char[] text, int length, boolean stemDerivational)
/*     */   {
/*  48 */     this.flags = 0;
/*  49 */     this.numSyllables = 0;
/*  50 */     for (int i = 0; i < length; i++) {
/*  51 */       if (isVowel(text[i]))
/*  52 */         this.numSyllables += 1;
/*     */     }
/*  54 */     if (this.numSyllables > 2) length = removeParticle(text, length);
/*  55 */     if (this.numSyllables > 2) length = removePossessivePronoun(text, length);
/*     */ 
/*  57 */     if (stemDerivational)
/*  58 */       length = stemDerivational(text, length);
/*  59 */     return length;
/*     */   }
/*     */ 
/*     */   private int stemDerivational(char[] text, int length) {
/*  63 */     int oldLength = length;
/*  64 */     if (this.numSyllables > 2) length = removeFirstOrderPrefix(text, length);
/*  65 */     if (oldLength != length) {
/*  66 */       oldLength = length;
/*  67 */       if (this.numSyllables > 2) length = removeSuffix(text, length);
/*  68 */       if ((oldLength != length) && 
/*  69 */         (this.numSyllables > 2)) length = removeSecondOrderPrefix(text, length); 
/*     */     }
/*  71 */     else { if (this.numSyllables > 2) length = removeSecondOrderPrefix(text, length);
/*  72 */       if (this.numSyllables > 2) length = removeSuffix(text, length);
/*     */     }
/*  74 */     return length;
/*     */   }
/*     */ 
/*     */   private boolean isVowel(char ch) {
/*  78 */     switch (ch) {
/*     */     case 'a':
/*     */     case 'e':
/*     */     case 'i':
/*     */     case 'o':
/*     */     case 'u':
/*  84 */       return true;
/*     */     }
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   private int removeParticle(char[] text, int length)
/*     */   {
/*  91 */     if ((StemmerUtil.endsWith(text, length, "kah")) || (StemmerUtil.endsWith(text, length, "lah")) || (StemmerUtil.endsWith(text, length, "pun")))
/*     */     {
/*  94 */       this.numSyllables -= 1;
/*  95 */       return length - 3;
/*     */     }
/*     */ 
/*  98 */     return length;
/*     */   }
/*     */ 
/*     */   private int removePossessivePronoun(char[] text, int length) {
/* 102 */     if ((StemmerUtil.endsWith(text, length, "ku")) || (StemmerUtil.endsWith(text, length, "mu"))) {
/* 103 */       this.numSyllables -= 1;
/* 104 */       return length - 2;
/*     */     }
/*     */ 
/* 107 */     if (StemmerUtil.endsWith(text, length, "nya")) {
/* 108 */       this.numSyllables -= 1;
/* 109 */       return length - 3;
/*     */     }
/*     */ 
/* 112 */     return length;
/*     */   }
/*     */ 
/*     */   private int removeFirstOrderPrefix(char[] text, int length) {
/* 116 */     if (StemmerUtil.startsWith(text, length, "meng")) {
/* 117 */       this.flags |= 8;
/* 118 */       this.numSyllables -= 1;
/* 119 */       return StemmerUtil.deleteN(text, 0, length, 4);
/*     */     }
/*     */ 
/* 122 */     if ((StemmerUtil.startsWith(text, length, "meny")) && (length > 4) && (isVowel(text[4]))) {
/* 123 */       this.flags |= 8;
/* 124 */       text[3] = 's';
/* 125 */       this.numSyllables -= 1;
/* 126 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 129 */     if (StemmerUtil.startsWith(text, length, "men")) {
/* 130 */       this.flags |= 8;
/* 131 */       this.numSyllables -= 1;
/* 132 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 135 */     if (StemmerUtil.startsWith(text, length, "mem")) {
/* 136 */       this.flags |= 8;
/* 137 */       this.numSyllables -= 1;
/* 138 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 141 */     if (StemmerUtil.startsWith(text, length, "me")) {
/* 142 */       this.flags |= 8;
/* 143 */       this.numSyllables -= 1;
/* 144 */       return StemmerUtil.deleteN(text, 0, length, 2);
/*     */     }
/*     */ 
/* 147 */     if (StemmerUtil.startsWith(text, length, "peng")) {
/* 148 */       this.flags |= 2;
/* 149 */       this.numSyllables -= 1;
/* 150 */       return StemmerUtil.deleteN(text, 0, length, 4);
/*     */     }
/*     */ 
/* 153 */     if ((StemmerUtil.startsWith(text, length, "peny")) && (length > 4) && (isVowel(text[4]))) {
/* 154 */       this.flags |= 2;
/* 155 */       text[3] = 's';
/* 156 */       this.numSyllables -= 1;
/* 157 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 160 */     if (StemmerUtil.startsWith(text, length, "peny")) {
/* 161 */       this.flags |= 2;
/* 162 */       this.numSyllables -= 1;
/* 163 */       return StemmerUtil.deleteN(text, 0, length, 4);
/*     */     }
/*     */ 
/* 166 */     if ((StemmerUtil.startsWith(text, length, "pen")) && (length > 3) && (isVowel(text[3]))) {
/* 167 */       this.flags |= 2;
/* 168 */       text[2] = 't';
/* 169 */       this.numSyllables -= 1;
/* 170 */       return StemmerUtil.deleteN(text, 0, length, 2);
/*     */     }
/*     */ 
/* 173 */     if (StemmerUtil.startsWith(text, length, "pen")) {
/* 174 */       this.flags |= 2;
/* 175 */       this.numSyllables -= 1;
/* 176 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 179 */     if (StemmerUtil.startsWith(text, length, "pem")) {
/* 180 */       this.flags |= 2;
/* 181 */       this.numSyllables -= 1;
/* 182 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 185 */     if (StemmerUtil.startsWith(text, length, "di")) {
/* 186 */       this.flags |= 4;
/* 187 */       this.numSyllables -= 1;
/* 188 */       return StemmerUtil.deleteN(text, 0, length, 2);
/*     */     }
/*     */ 
/* 191 */     if (StemmerUtil.startsWith(text, length, "ter")) {
/* 192 */       this.flags |= 16;
/* 193 */       this.numSyllables -= 1;
/* 194 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 197 */     if (StemmerUtil.startsWith(text, length, "ke")) {
/* 198 */       this.flags |= 1;
/* 199 */       this.numSyllables -= 1;
/* 200 */       return StemmerUtil.deleteN(text, 0, length, 2);
/*     */     }
/*     */ 
/* 203 */     return length;
/*     */   }
/*     */ 
/*     */   private int removeSecondOrderPrefix(char[] text, int length) {
/* 207 */     if (StemmerUtil.startsWith(text, length, "ber")) {
/* 208 */       this.flags |= 32;
/* 209 */       this.numSyllables -= 1;
/* 210 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 213 */     if ((length == 7) && (StemmerUtil.startsWith(text, length, "belajar"))) {
/* 214 */       this.flags |= 32;
/* 215 */       this.numSyllables -= 1;
/* 216 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 219 */     if ((StemmerUtil.startsWith(text, length, "be")) && (length > 4) && (!isVowel(text[2])) && (text[3] == 'e') && (text[4] == 'r'))
/*     */     {
/* 221 */       this.flags |= 32;
/* 222 */       this.numSyllables -= 1;
/* 223 */       return StemmerUtil.deleteN(text, 0, length, 2);
/*     */     }
/*     */ 
/* 226 */     if (StemmerUtil.startsWith(text, length, "per")) {
/* 227 */       this.numSyllables -= 1;
/* 228 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 231 */     if ((length == 7) && (StemmerUtil.startsWith(text, length, "pelajar"))) {
/* 232 */       this.numSyllables -= 1;
/* 233 */       return StemmerUtil.deleteN(text, 0, length, 3);
/*     */     }
/*     */ 
/* 236 */     if (StemmerUtil.startsWith(text, length, "pe")) {
/* 237 */       this.flags |= 64;
/* 238 */       this.numSyllables -= 1;
/* 239 */       return StemmerUtil.deleteN(text, 0, length, 2);
/*     */     }
/*     */ 
/* 242 */     return length;
/*     */   }
/*     */ 
/*     */   private int removeSuffix(char[] text, int length) {
/* 246 */     if ((StemmerUtil.endsWith(text, length, "kan")) && ((this.flags & 0x1) == 0) && ((this.flags & 0x2) == 0) && ((this.flags & 0x40) == 0))
/*     */     {
/* 250 */       this.numSyllables -= 1;
/* 251 */       return length - 3;
/*     */     }
/*     */ 
/* 254 */     if ((StemmerUtil.endsWith(text, length, "an")) && ((this.flags & 0x4) == 0) && ((this.flags & 0x8) == 0) && ((this.flags & 0x10) == 0))
/*     */     {
/* 258 */       this.numSyllables -= 1;
/* 259 */       return length - 2;
/*     */     }
/*     */ 
/* 262 */     if ((StemmerUtil.endsWith(text, length, "i")) && (!StemmerUtil.endsWith(text, length, "si")) && ((this.flags & 0x20) == 0) && ((this.flags & 0x1) == 0) && ((this.flags & 0x2) == 0))
/*     */     {
/* 267 */       this.numSyllables -= 1;
/* 268 */       return length - 1;
/*     */     }
/* 270 */     return length;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.id.IndonesianStemmer
 * JD-Core Version:    0.6.2
 */