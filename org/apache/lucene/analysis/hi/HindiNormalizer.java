/*     */ package org.apache.lucene.analysis.hi;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class HindiNormalizer
/*     */ {
/*     */   public int normalize(char[] s, int len)
/*     */   {
/*  51 */     for (int i = 0; i < len; i++) {
/*  52 */       switch (s[i])
/*     */       {
/*     */       case 'न':
/*  55 */         if ((i + 1 < len) && (s[(i + 1)] == '्')) {
/*  56 */           s[i] = 'ं';
/*  57 */           len = StemmerUtil.delete(s, i + 1, len); } break;
/*     */       case 'ँ':
/*  62 */         s[i] = 'ं';
/*  63 */         break;
/*     */       case '़':
/*  66 */         len = StemmerUtil.delete(s, i, len);
/*  67 */         i--;
/*  68 */         break;
/*     */       case 'ऩ':
/*  70 */         s[i] = 'न';
/*  71 */         break;
/*     */       case 'ऱ':
/*  73 */         s[i] = 'र';
/*  74 */         break;
/*     */       case 'ऴ':
/*  76 */         s[i] = 'ळ';
/*  77 */         break;
/*     */       case 'क़':
/*  79 */         s[i] = 'क';
/*  80 */         break;
/*     */       case 'ख़':
/*  82 */         s[i] = 'ख';
/*  83 */         break;
/*     */       case 'ग़':
/*  85 */         s[i] = 'ग';
/*  86 */         break;
/*     */       case 'ज़':
/*  88 */         s[i] = 'ज';
/*  89 */         break;
/*     */       case 'ड़':
/*  91 */         s[i] = 'ड';
/*  92 */         break;
/*     */       case 'ढ़':
/*  94 */         s[i] = 'ढ';
/*  95 */         break;
/*     */       case 'फ़':
/*  97 */         s[i] = 'फ';
/*  98 */         break;
/*     */       case 'य़':
/* 100 */         s[i] = 'य';
/* 101 */         break;
/*     */       case '‌':
/*     */       case '‍':
/* 105 */         len = StemmerUtil.delete(s, i, len);
/* 106 */         i--;
/* 107 */         break;
/*     */       case '्':
/* 110 */         len = StemmerUtil.delete(s, i, len);
/* 111 */         i--;
/* 112 */         break;
/*     */       case 'ॅ':
/*     */       case 'ॆ':
/* 116 */         s[i] = 'े';
/* 117 */         break;
/*     */       case 'ॉ':
/*     */       case 'ॊ':
/* 120 */         s[i] = 'ो';
/* 121 */         break;
/*     */       case 'ऍ':
/*     */       case 'ऎ':
/* 124 */         s[i] = 'ए';
/* 125 */         break;
/*     */       case 'ऑ':
/*     */       case 'ऒ':
/* 128 */         s[i] = 'ओ';
/* 129 */         break;
/*     */       case 'ॲ':
/* 131 */         s[i] = 'अ';
/* 132 */         break;
/*     */       case 'आ':
/* 135 */         s[i] = 'अ';
/* 136 */         break;
/*     */       case 'ई':
/* 138 */         s[i] = 'इ';
/* 139 */         break;
/*     */       case 'ऊ':
/* 141 */         s[i] = 'उ';
/* 142 */         break;
/*     */       case 'ॠ':
/* 144 */         s[i] = 'ऋ';
/* 145 */         break;
/*     */       case 'ॡ':
/* 147 */         s[i] = 'ऌ';
/* 148 */         break;
/*     */       case 'ऐ':
/* 150 */         s[i] = 'ए';
/* 151 */         break;
/*     */       case 'औ':
/* 153 */         s[i] = 'ओ';
/* 154 */         break;
/*     */       case 'ी':
/* 157 */         s[i] = 'ि';
/* 158 */         break;
/*     */       case 'ू':
/* 160 */         s[i] = 'ु';
/* 161 */         break;
/*     */       case 'ॄ':
/* 163 */         s[i] = 'ृ';
/* 164 */         break;
/*     */       case 'ॣ':
/* 166 */         s[i] = 'ॢ';
/* 167 */         break;
/*     */       case 'ै':
/* 169 */         s[i] = 'े';
/* 170 */         break;
/*     */       case 'ौ':
/* 172 */         s[i] = 'ो';
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 179 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hi.HindiNormalizer
 * JD-Core Version:    0.6.2
 */