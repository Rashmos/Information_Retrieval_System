/*     */ package org.apache.lucene.analysis.compound.hyphenation;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class TernaryTree
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   protected char[] lo;
/*     */   protected char[] hi;
/*     */   protected char[] eq;
/*     */   protected char[] sc;
/*     */   protected CharVector kv;
/*     */   protected char root;
/*     */   protected char freenode;
/*     */   protected int length;
/*     */   protected static final int BLOCK_SIZE = 2048;
/*     */ 
/*     */   TernaryTree()
/*     */   {
/* 122 */     init();
/*     */   }
/*     */ 
/*     */   protected void init() {
/* 126 */     this.root = '\000';
/* 127 */     this.freenode = '\001';
/* 128 */     this.length = 0;
/* 129 */     this.lo = new char[2048];
/* 130 */     this.hi = new char[2048];
/* 131 */     this.eq = new char[2048];
/* 132 */     this.sc = new char[2048];
/* 133 */     this.kv = new CharVector();
/*     */   }
/*     */ 
/*     */   public void insert(String key, char val)
/*     */   {
/* 144 */     int len = key.length() + 1;
/* 145 */     if (this.freenode + len > this.eq.length) {
/* 146 */       redimNodeArrays(this.eq.length + 2048);
/*     */     }
/* 148 */     char[] strkey = new char[len--];
/* 149 */     key.getChars(0, len, strkey, 0);
/* 150 */     strkey[len] = '\000';
/* 151 */     this.root = insert(this.root, strkey, 0, val);
/*     */   }
/*     */ 
/*     */   public void insert(char[] key, int start, char val) {
/* 155 */     int len = strlen(key) + 1;
/* 156 */     if (this.freenode + len > this.eq.length) {
/* 157 */       redimNodeArrays(this.eq.length + 2048);
/*     */     }
/* 159 */     this.root = insert(this.root, key, start, val);
/*     */   }
/*     */ 
/*     */   private char insert(char p, char[] key, int start, char val)
/*     */   {
/* 166 */     int len = strlen(key, start);
/* 167 */     if (p == 0)
/*     */     {
/* 171 */       p = this.freenode++;
/* 172 */       this.eq[p] = val;
/* 173 */       this.length += 1;
/* 174 */       this.hi[p] = '\000';
/* 175 */       if (len > 0) {
/* 176 */         this.sc[p] = 65535;
/* 177 */         this.lo[p] = ((char)this.kv.alloc(len + 1));
/* 178 */         strcpy(this.kv.getArray(), this.lo[p], key, start);
/*     */       } else {
/* 180 */         this.sc[p] = '\000';
/* 181 */         this.lo[p] = '\000';
/*     */       }
/* 183 */       return p;
/*     */     }
/*     */ 
/* 186 */     if (this.sc[p] == 65535)
/*     */     {
/* 190 */       char pp = this.freenode++;
/* 191 */       this.lo[pp] = this.lo[p];
/* 192 */       this.eq[pp] = this.eq[p];
/* 193 */       this.lo[p] = '\000';
/* 194 */       if (len > 0) {
/* 195 */         this.sc[p] = this.kv.get(this.lo[pp]);
/* 196 */         this.eq[p] = pp;
/*     */         char tmp214_212 = pp;
/*     */         char[] tmp214_209 = this.lo; tmp214_209[tmp214_212] = ((char)(tmp214_209[tmp214_212] + '\001'));
/* 198 */         if (this.kv.get(this.lo[pp]) == 0)
/*     */         {
/* 200 */           this.lo[pp] = '\000';
/* 201 */           this.sc[pp] = '\000';
/* 202 */           this.hi[pp] = '\000';
/*     */         }
/*     */         else {
/* 205 */           this.sc[pp] = 65535;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 210 */         this.sc[pp] = 65535;
/* 211 */         this.hi[p] = pp;
/* 212 */         this.sc[p] = '\000';
/* 213 */         this.eq[p] = val;
/* 214 */         this.length += 1;
/* 215 */         return p;
/*     */       }
/*     */     }
/* 218 */     char s = key[start];
/* 219 */     if (s < this.sc[p])
/* 220 */       this.lo[p] = insert(this.lo[p], key, start, val);
/* 221 */     else if (s == this.sc[p]) {
/* 222 */       if (s != 0) {
/* 223 */         this.eq[p] = insert(this.eq[p], key, start + 1, val);
/*     */       }
/*     */       else
/* 226 */         this.eq[p] = val;
/*     */     }
/*     */     else {
/* 229 */       this.hi[p] = insert(this.hi[p], key, start, val);
/*     */     }
/* 231 */     return p;
/*     */   }
/*     */ 
/*     */   public static int strcmp(char[] a, int startA, char[] b, int startB)
/*     */   {
/* 238 */     for (; a[startA] == b[startB]; startB++) {
/* 239 */       if (a[startA] == 0)
/* 240 */         return 0;
/* 238 */       startA++;
/*     */     }
/*     */ 
/* 243 */     return a[startA] - b[startB];
/*     */   }
/*     */ 
/*     */   public static int strcmp(String str, char[] a, int start)
/*     */   {
/* 250 */     int len = str.length();
/* 251 */     for (int i = 0; i < len; i++) {
/* 252 */       int d = str.charAt(i) - a[(start + i)];
/* 253 */       if (d != 0) {
/* 254 */         return d;
/*     */       }
/* 256 */       if (a[(start + i)] == 0) {
/* 257 */         return d;
/*     */       }
/*     */     }
/* 260 */     if (a[(start + i)] != 0) {
/* 261 */       return -a[(start + i)];
/*     */     }
/* 263 */     return 0;
/*     */   }
/*     */ 
/*     */   public static void strcpy(char[] dst, int di, char[] src, int si)
/*     */   {
/* 268 */     while (src[si] != 0) {
/* 269 */       dst[(di++)] = src[(si++)];
/*     */     }
/* 271 */     dst[di] = '\000';
/*     */   }
/*     */ 
/*     */   public static int strlen(char[] a, int start) {
/* 275 */     int len = 0;
/* 276 */     for (int i = start; (i < a.length) && (a[i] != 0); i++) {
/* 277 */       len++;
/*     */     }
/* 279 */     return len;
/*     */   }
/*     */ 
/*     */   public static int strlen(char[] a) {
/* 283 */     return strlen(a, 0);
/*     */   }
/*     */ 
/*     */   public int find(String key) {
/* 287 */     int len = key.length();
/* 288 */     char[] strkey = new char[len + 1];
/* 289 */     key.getChars(0, len, strkey, 0);
/* 290 */     strkey[len] = '\000';
/*     */ 
/* 292 */     return find(strkey, 0);
/*     */   }
/*     */ 
/*     */   public int find(char[] key, int start)
/*     */   {
/* 297 */     char p = this.root;
/* 298 */     int i = start;
/*     */ 
/* 301 */     while (p != 0) {
/* 302 */       if (this.sc[p] == 65535) {
/* 303 */         if (strcmp(key, i, this.kv.getArray(), this.lo[p]) == 0) {
/* 304 */           return this.eq[p];
/*     */         }
/* 306 */         return -1;
/*     */       }
/*     */ 
/* 309 */       char c = key[i];
/* 310 */       int d = c - this.sc[p];
/* 311 */       if (d == 0) {
/* 312 */         if (c == 0) {
/* 313 */           return this.eq[p];
/*     */         }
/* 315 */         i++;
/* 316 */         p = this.eq[p];
/* 317 */       } else if (d < 0) {
/* 318 */         p = this.lo[p];
/*     */       } else {
/* 320 */         p = this.hi[p];
/*     */       }
/*     */     }
/* 323 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean knows(String key) {
/* 327 */     return find(key) >= 0;
/*     */   }
/*     */ 
/*     */   private void redimNodeArrays(int newsize)
/*     */   {
/* 332 */     int len = newsize < this.lo.length ? newsize : this.lo.length;
/* 333 */     char[] na = new char[newsize];
/* 334 */     System.arraycopy(this.lo, 0, na, 0, len);
/* 335 */     this.lo = na;
/* 336 */     na = new char[newsize];
/* 337 */     System.arraycopy(this.hi, 0, na, 0, len);
/* 338 */     this.hi = na;
/* 339 */     na = new char[newsize];
/* 340 */     System.arraycopy(this.eq, 0, na, 0, len);
/* 341 */     this.eq = na;
/* 342 */     na = new char[newsize];
/* 343 */     System.arraycopy(this.sc, 0, na, 0, len);
/* 344 */     this.sc = na;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 348 */     return this.length;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 353 */     TernaryTree t = new TernaryTree();
/* 354 */     t.lo = ((char[])this.lo.clone());
/* 355 */     t.hi = ((char[])this.hi.clone());
/* 356 */     t.eq = ((char[])this.eq.clone());
/* 357 */     t.sc = ((char[])this.sc.clone());
/* 358 */     t.kv = ((CharVector)this.kv.clone());
/* 359 */     t.root = this.root;
/* 360 */     t.freenode = this.freenode;
/* 361 */     t.length = this.length;
/*     */ 
/* 363 */     return t;
/*     */   }
/*     */ 
/*     */   protected void insertBalanced(String[] k, char[] v, int offset, int n)
/*     */   {
/* 373 */     if (n < 1) {
/* 374 */       return;
/*     */     }
/* 376 */     int m = n >> 1;
/*     */ 
/* 378 */     insert(k[(m + offset)], v[(m + offset)]);
/* 379 */     insertBalanced(k, v, offset, m);
/*     */ 
/* 381 */     insertBalanced(k, v, offset + m + 1, n - m - 1);
/*     */   }
/*     */ 
/*     */   public void balance()
/*     */   {
/* 391 */     int i = 0; int n = this.length;
/* 392 */     String[] k = new String[n];
/* 393 */     char[] v = new char[n];
/* 394 */     Iterator iter = new Iterator();
/* 395 */     while (iter.hasMoreElements()) {
/* 396 */       v[i] = iter.getValue();
/* 397 */       k[(i++)] = iter.nextElement();
/*     */     }
/* 399 */     init();
/* 400 */     insertBalanced(k, v, 0, n);
/*     */   }
/*     */ 
/*     */   public void trimToSize()
/*     */   {
/* 420 */     balance();
/*     */ 
/* 423 */     redimNodeArrays(this.freenode);
/*     */ 
/* 426 */     CharVector kx = new CharVector();
/* 427 */     kx.alloc(1);
/* 428 */     TernaryTree map = new TernaryTree();
/* 429 */     compact(kx, map, this.root);
/* 430 */     this.kv = kx;
/* 431 */     this.kv.trimToSize();
/*     */   }
/*     */ 
/*     */   private void compact(CharVector kx, TernaryTree map, char p)
/*     */   {
/* 436 */     if (p == 0) {
/* 437 */       return;
/*     */     }
/* 439 */     if (this.sc[p] == 65535) {
/* 440 */       int k = map.find(this.kv.getArray(), this.lo[p]);
/* 441 */       if (k < 0) {
/* 442 */         k = kx.alloc(strlen(this.kv.getArray(), this.lo[p]) + 1);
/* 443 */         strcpy(kx.getArray(), k, this.kv.getArray(), this.lo[p]);
/* 444 */         map.insert(kx.getArray(), k, (char)k);
/*     */       }
/* 446 */       this.lo[p] = ((char)k);
/*     */     } else {
/* 448 */       compact(kx, map, this.lo[p]);
/* 449 */       if (this.sc[p] != 0) {
/* 450 */         compact(kx, map, this.eq[p]);
/*     */       }
/* 452 */       compact(kx, map, this.hi[p]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<String> keys() {
/* 457 */     return new Iterator();
/*     */   }
/*     */ 
/*     */   public void printStats()
/*     */   {
/* 638 */     System.out.println("Number of keys = " + Integer.toString(this.length));
/* 639 */     System.out.println("Node count = " + Integer.toString(this.freenode));
/*     */ 
/* 641 */     System.out.println("Key Array length = " + Integer.toString(this.kv.length()));
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/* 653 */     TernaryTree tt = new TernaryTree();
/* 654 */     tt.insert("Carlos", 'C');
/* 655 */     tt.insert("Car", 'r');
/* 656 */     tt.insert("palos", 'l');
/* 657 */     tt.insert("pa", 'p');
/* 658 */     tt.trimToSize();
/* 659 */     System.out.println((char)tt.find("Car"));
/* 660 */     System.out.println((char)tt.find("Carlos"));
/* 661 */     System.out.println((char)tt.find("alto"));
/* 662 */     tt.printStats();
/*     */   }
/*     */ 
/*     */   public class Iterator
/*     */     implements Enumeration<String>
/*     */   {
/*     */     int cur;
/*     */     String curkey;
/*     */     Stack<Item> ns;
/*     */     StringBuilder ks;
/*     */ 
/*     */     public Iterator()
/*     */     {
/* 505 */       this.cur = -1;
/* 506 */       this.ns = new Stack();
/* 507 */       this.ks = new StringBuilder();
/* 508 */       rewind();
/*     */     }
/*     */ 
/*     */     public void rewind() {
/* 512 */       this.ns.removeAllElements();
/* 513 */       this.ks.setLength(0);
/* 514 */       this.cur = TernaryTree.this.root;
/* 515 */       run();
/*     */     }
/*     */ 
/*     */     public String nextElement() {
/* 519 */       String res = new String(this.curkey);
/* 520 */       this.cur = up();
/* 521 */       run();
/* 522 */       return res;
/*     */     }
/*     */ 
/*     */     public char getValue() {
/* 526 */       if (this.cur >= 0) {
/* 527 */         return TernaryTree.this.eq[this.cur];
/*     */       }
/* 529 */       return '\000';
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 533 */       return this.cur != -1;
/*     */     }
/*     */ 
/*     */     private int up()
/*     */     {
/* 540 */       Item i = new Item();
/* 541 */       int res = 0;
/*     */ 
/* 543 */       if (this.ns.empty()) {
/* 544 */         return -1;
/*     */       }
/*     */ 
/* 547 */       if ((this.cur != 0) && (TernaryTree.this.sc[this.cur] == 0)) {
/* 548 */         return TernaryTree.this.lo[this.cur];
/*     */       }
/*     */ 
/* 551 */       boolean climb = true;
/*     */ 
/* 553 */       while (climb) {
/* 554 */         i = (Item)this.ns.pop();
/*     */         Item tmp76_75 = i; tmp76_75.child = ((char)(tmp76_75.child + '\001'));
/* 556 */         switch (i.child) {
/*     */         case '\001':
/* 558 */           if (TernaryTree.this.sc[i.parent] != 0) {
/* 559 */             res = TernaryTree.this.eq[i.parent];
/* 560 */             this.ns.push((Item)i.clone());
/* 561 */             this.ks.append(TernaryTree.this.sc[i.parent]);
/*     */           }
/*     */           else
/*     */           {
/*     */             Item tmp183_182 = i; tmp183_182.child = ((char)(tmp183_182.child + '\001'));
/* 564 */             this.ns.push((Item)i.clone());
/* 565 */             res = TernaryTree.this.hi[i.parent];
/*     */           }
/* 567 */           climb = false;
/* 568 */           break;
/*     */         case '\002':
/* 571 */           res = TernaryTree.this.hi[i.parent];
/* 572 */           this.ns.push((Item)i.clone());
/* 573 */           if (this.ks.length() > 0) {
/* 574 */             this.ks.setLength(this.ks.length() - 1);
/*     */           }
/* 576 */           climb = false;
/* 577 */           break;
/*     */         default:
/* 580 */           if (this.ns.empty()) {
/* 581 */             return -1;
/*     */           }
/* 583 */           climb = true;
/*     */         }
/*     */       }
/*     */ 
/* 587 */       return res;
/*     */     }
/*     */ 
/*     */     private int run()
/*     */     {
/* 594 */       if (this.cur == -1) {
/* 595 */         return -1;
/*     */       }
/*     */ 
/* 598 */       boolean leaf = false;
/*     */       do
/*     */       {
/* 601 */         while (this.cur != 0) {
/* 602 */           if (TernaryTree.this.sc[this.cur] == 65535) {
/* 603 */             leaf = true;
/* 604 */             break;
/*     */           }
/* 606 */           this.ns.push(new Item((char)this.cur, '\000'));
/* 607 */           if (TernaryTree.this.sc[this.cur] == 0) {
/* 608 */             leaf = true;
/* 609 */             break;
/*     */           }
/* 611 */           this.cur = TernaryTree.this.lo[this.cur];
/*     */         }
/* 613 */         if (leaf)
/*     */         {
/*     */           break;
/*     */         }
/* 617 */         this.cur = up();
/* 618 */       }while (this.cur != -1);
/* 619 */       return -1;
/*     */ 
/* 624 */       StringBuilder buf = new StringBuilder(this.ks.toString());
/* 625 */       if (TernaryTree.this.sc[this.cur] == 65535) {
/* 626 */         int p = TernaryTree.this.lo[this.cur];
/* 627 */         while (TernaryTree.this.kv.get(p) != 0) {
/* 628 */           buf.append(TernaryTree.this.kv.get(p++));
/*     */         }
/*     */       }
/* 631 */       this.curkey = buf.toString();
/* 632 */       return 0;
/*     */     }
/*     */ 
/*     */     private class Item
/*     */       implements Cloneable
/*     */     {
/*     */       char parent;
/*     */       char child;
/*     */ 
/*     */       public Item()
/*     */       {
/* 478 */         this.parent = '\000';
/* 479 */         this.child = '\000';
/*     */       }
/*     */ 
/*     */       public Item(char p, char c) {
/* 483 */         this.parent = p;
/* 484 */         this.child = c;
/*     */       }
/*     */ 
/*     */       public Object clone()
/*     */       {
/* 489 */         return new Item(TernaryTree.Iterator.this, this.parent, this.child);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.TernaryTree
 * JD-Core Version:    0.6.2
 */