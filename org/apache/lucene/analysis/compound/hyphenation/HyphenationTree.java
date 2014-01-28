/*     */ package org.apache.lucene.analysis.compound.hyphenation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class HyphenationTree extends TernaryTree
/*     */   implements PatternConsumer, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7842107987915665573L;
/*     */   protected ByteVector vspace;
/*     */   protected HashMap<String, ArrayList<Object>> stoplist;
/*     */   protected TernaryTree classmap;
/*     */   private transient TernaryTree ivalues;
/*     */ 
/*     */   public HyphenationTree()
/*     */   {
/*  60 */     this.stoplist = new HashMap(23);
/*  61 */     this.classmap = new TernaryTree();
/*  62 */     this.vspace = new ByteVector();
/*  63 */     this.vspace.alloc(1);
/*     */   }
/*     */ 
/*     */   protected int packValues(String values)
/*     */   {
/*  76 */     int n = values.length();
/*  77 */     int m = (n & 0x1) == 1 ? (n >> 1) + 2 : (n >> 1) + 1;
/*  78 */     int offset = this.vspace.alloc(m);
/*  79 */     byte[] va = this.vspace.getArray();
/*  80 */     for (int i = 0; i < n; i++) {
/*  81 */       int j = i >> 1;
/*  82 */       byte v = (byte)(values.charAt(i) - '0' + 1 & 0xF);
/*  83 */       if ((i & 0x1) == 1)
/*  84 */         va[(j + offset)] = ((byte)(va[(j + offset)] | v));
/*     */       else {
/*  86 */         va[(j + offset)] = ((byte)(v << 4));
/*     */       }
/*     */     }
/*  89 */     va[(m - 1 + offset)] = 0;
/*  90 */     return offset;
/*     */   }
/*     */ 
/*     */   protected String unpackValues(int k) {
/*  94 */     StringBuilder buf = new StringBuilder();
/*  95 */     byte v = this.vspace.get(k++);
/*  96 */     while (v != 0) {
/*  97 */       char c = (char)((v >>> 4) - 1 + 48);
/*  98 */       buf.append(c);
/*  99 */       c = (char)(v & 0xF);
/* 100 */       if (c == 0) {
/*     */         break;
/*     */       }
/* 103 */       c = (char)(c - '\001' + 48);
/* 104 */       buf.append(c);
/* 105 */       v = this.vspace.get(k++);
/*     */     }
/* 107 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public void loadPatterns(File f)
/*     */     throws HyphenationException
/*     */   {
/*     */     try
/*     */     {
/* 118 */       InputSource src = new InputSource(f.toURL().toExternalForm());
/* 119 */       loadPatterns(src);
/*     */     } catch (MalformedURLException e) {
/* 121 */       throw new HyphenationException("Error converting the File '" + f + "' to a URL: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void loadPatterns(InputSource source)
/*     */     throws HyphenationException
/*     */   {
/* 133 */     PatternParser pp = new PatternParser(this);
/* 134 */     this.ivalues = new TernaryTree();
/*     */ 
/* 136 */     pp.parse(source);
/*     */ 
/* 140 */     trimToSize();
/* 141 */     this.vspace.trimToSize();
/* 142 */     this.classmap.trimToSize();
/*     */ 
/* 145 */     this.ivalues = null;
/*     */   }
/*     */ 
/*     */   public String findPattern(String pat) {
/* 149 */     int k = super.find(pat);
/* 150 */     if (k >= 0) {
/* 151 */       return unpackValues(k);
/*     */     }
/* 153 */     return "";
/*     */   }
/*     */ 
/*     */   protected int hstrcmp(char[] s, int si, char[] t, int ti)
/*     */   {
/* 160 */     for (; s[si] == t[ti]; ti++) {
/* 161 */       if (s[si] == 0)
/* 162 */         return 0;
/* 160 */       si++;
/*     */     }
/*     */ 
/* 165 */     if (t[ti] == 0) {
/* 166 */       return 0;
/*     */     }
/* 168 */     return s[si] - t[ti];
/*     */   }
/*     */ 
/*     */   protected byte[] getValues(int k) {
/* 172 */     StringBuilder buf = new StringBuilder();
/* 173 */     byte v = this.vspace.get(k++);
/* 174 */     while (v != 0) {
/* 175 */       char c = (char)((v >>> 4) - 1);
/* 176 */       buf.append(c);
/* 177 */       c = (char)(v & 0xF);
/* 178 */       if (c == 0) {
/*     */         break;
/*     */       }
/* 181 */       c = (char)(c - '\001');
/* 182 */       buf.append(c);
/* 183 */       v = this.vspace.get(k++);
/*     */     }
/* 185 */     byte[] res = new byte[buf.length()];
/* 186 */     for (int i = 0; i < res.length; i++) {
/* 187 */       res[i] = ((byte)buf.charAt(i));
/*     */     }
/* 189 */     return res;
/*     */   }
/*     */ 
/*     */   protected void searchPatterns(char[] word, int index, byte[] il)
/*     */   {
/* 220 */     int i = index;
/*     */ 
/* 222 */     char sp = word[i];
/* 223 */     char p = this.root;
/*     */ 
/* 225 */     while ((p > 0) && (p < this.sc.length)) {
/* 226 */       if (this.sc[p] == 65535) {
/* 227 */         if (hstrcmp(word, i, this.kv.getArray(), this.lo[p]) == 0) {
/* 228 */           byte[] values = getValues(this.eq[p]);
/* 229 */           int j = index;
/* 230 */           for (int k = 0; k < values.length; k++) {
/* 231 */             if ((j < il.length) && (values[k] > il[j])) {
/* 232 */               il[j] = values[k];
/*     */             }
/* 234 */             j++;
/*     */           }
/*     */         }
/* 237 */         return;
/*     */       }
/* 239 */       int d = sp - this.sc[p];
/* 240 */       if (d == 0) {
/* 241 */         if (sp == 0) {
/*     */           break;
/*     */         }
/* 244 */         sp = word[(++i)];
/* 245 */         p = this.eq[p];
/* 246 */         char q = p;
/*     */ 
/* 250 */         while ((q > 0) && (q < this.sc.length) && 
/* 251 */           (this.sc[q] != 65535))
/*     */         {
/* 254 */           if (this.sc[q] == 0) {
/* 255 */             byte[] values = getValues(this.eq[q]);
/* 256 */             int j = index;
/* 257 */             for (int k = 0; k < values.length; k++) {
/* 258 */               if ((j < il.length) && (values[k] > il[j])) {
/* 259 */                 il[j] = values[k];
/*     */               }
/* 261 */               j++;
/*     */             }
/* 263 */             break;
/*     */           }
/* 265 */           q = this.lo[q];
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 274 */       p = d < 0 ? this.lo[p] : this.hi[p];
/*     */     }
/*     */   }
/*     */ 
/*     */   public Hyphenation hyphenate(String word, int remainCharCount, int pushCharCount)
/*     */   {
/* 292 */     char[] w = word.toCharArray();
/* 293 */     return hyphenate(w, 0, w.length, remainCharCount, pushCharCount);
/*     */   }
/*     */ 
/*     */   public Hyphenation hyphenate(char[] w, int offset, int len, int remainCharCount, int pushCharCount)
/*     */   {
/* 326 */     char[] word = new char[len + 3];
/*     */ 
/* 329 */     char[] c = new char[2];
/* 330 */     int iIgnoreAtBeginning = 0;
/* 331 */     int iLength = len;
/* 332 */     boolean bEndOfLetters = false;
/* 333 */     for (int i = 1; i <= len; i++) {
/* 334 */       c[0] = w[(offset + i - 1)];
/* 335 */       int nc = this.classmap.find(c, 0);
/* 336 */       if (nc < 0) {
/* 337 */         if (i == 1 + iIgnoreAtBeginning)
/*     */         {
/* 339 */           iIgnoreAtBeginning++;
/*     */         }
/*     */         else {
/* 342 */           bEndOfLetters = true;
/*     */         }
/* 344 */         iLength--;
/*     */       }
/* 346 */       else if (!bEndOfLetters) {
/* 347 */         word[(i - iIgnoreAtBeginning)] = ((char)nc);
/*     */       } else {
/* 349 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 353 */     len = iLength;
/* 354 */     if (len < remainCharCount + pushCharCount)
/*     */     {
/* 356 */       return null;
/*     */     }
/* 358 */     int[] result = new int[len + 1];
/* 359 */     int k = 0;
/*     */ 
/* 362 */     String sw = new String(word, 1, len);
/* 363 */     if (this.stoplist.containsKey(sw))
/*     */     {
/* 366 */       ArrayList hw = (ArrayList)this.stoplist.get(sw);
/* 367 */       int j = 0;
/* 368 */       for (i = 0; i < hw.size(); i++) {
/* 369 */         Object o = hw.get(i);
/*     */ 
/* 372 */         if ((o instanceof String)) {
/* 373 */           j += ((String)o).length();
/* 374 */           if ((j >= remainCharCount) && (j < len - pushCharCount))
/* 375 */             result[(k++)] = (j + iIgnoreAtBeginning);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 381 */       word[0] = '.';
/* 382 */       word[(len + 1)] = '.';
/* 383 */       word[(len + 2)] = '\000';
/* 384 */       byte[] il = new byte[len + 3];
/* 385 */       for (i = 0; i < len + 1; i++) {
/* 386 */         searchPatterns(word, i, il);
/*     */       }
/*     */ 
/* 393 */       for (i = 0; i < len; i++) {
/* 394 */         if (((il[(i + 1)] & 0x1) == 1) && (i >= remainCharCount) && (i <= len - pushCharCount))
/*     */         {
/* 396 */           result[(k++)] = (i + iIgnoreAtBeginning);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 401 */     if (k > 0)
/*     */     {
/* 403 */       int[] res = new int[k + 2];
/* 404 */       System.arraycopy(result, 0, res, 1, k);
/*     */ 
/* 407 */       res[0] = 0;
/* 408 */       res[(k + 1)] = len;
/* 409 */       return new Hyphenation(res);
/*     */     }
/* 411 */     return null;
/*     */   }
/*     */ 
/*     */   public void addClass(String chargroup)
/*     */   {
/* 426 */     if (chargroup.length() > 0) {
/* 427 */       char equivChar = chargroup.charAt(0);
/* 428 */       char[] key = new char[2];
/* 429 */       key[1] = '\000';
/* 430 */       for (int i = 0; i < chargroup.length(); i++) {
/* 431 */         key[0] = chargroup.charAt(i);
/* 432 */         this.classmap.insert(key, 0, equivChar);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addException(String word, ArrayList<Object> hyphenatedword)
/*     */   {
/* 447 */     this.stoplist.put(word, hyphenatedword);
/*     */   }
/*     */ 
/*     */   public void addPattern(String pattern, String ivalue)
/*     */   {
/* 461 */     int k = this.ivalues.find(ivalue);
/* 462 */     if (k <= 0) {
/* 463 */       k = packValues(ivalue);
/* 464 */       this.ivalues.insert(ivalue, (char)k);
/*     */     }
/* 466 */     insert(pattern, (char)k);
/*     */   }
/*     */ 
/*     */   public void printStats()
/*     */   {
/* 471 */     System.out.println("Value space size = " + Integer.toString(this.vspace.length()));
/*     */ 
/* 473 */     super.printStats();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.HyphenationTree
 * JD-Core Version:    0.6.2
 */