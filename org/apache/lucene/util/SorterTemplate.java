/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ public abstract class SorterTemplate
/*     */ {
/*     */   private static final int MERGESORT_THRESHOLD = 12;
/*     */   private static final int QUICKSORT_THRESHOLD = 7;
/*     */ 
/*     */   protected abstract void swap(int paramInt1, int paramInt2);
/*     */ 
/*     */   protected abstract int compare(int paramInt1, int paramInt2);
/*     */ 
/*     */   public void quickSort(int lo, int hi)
/*     */   {
/*  31 */     quickSortHelper(lo, hi);
/*  32 */     insertionSort(lo, hi);
/*     */   }
/*     */ 
/*     */   private void quickSortHelper(int lo, int hi) {
/*     */     while (true) {
/*  37 */       int diff = hi - lo;
/*  38 */       if (diff <= 7) {
/*     */         break;
/*     */       }
/*  41 */       int i = (hi + lo) / 2;
/*  42 */       if (compare(lo, i) > 0) {
/*  43 */         swap(lo, i);
/*     */       }
/*  45 */       if (compare(lo, hi) > 0) {
/*  46 */         swap(lo, hi);
/*     */       }
/*  48 */       if (compare(i, hi) > 0) {
/*  49 */         swap(i, hi);
/*     */       }
/*  51 */       int j = hi - 1;
/*  52 */       swap(i, j);
/*  53 */       i = lo;
/*  54 */       int v = j;
/*     */       while (true)
/*  56 */         if (compare(++i, v) >= 0)
/*     */         {
/*  59 */           while (compare(--j, v) > 0);
/*  62 */           if (j < i) {
/*     */             break;
/*     */           }
/*  65 */           swap(i, j);
/*     */         }
/*  67 */       swap(i, hi - 1);
/*  68 */       if (j - lo <= hi - i + 1) {
/*  69 */         quickSortHelper(lo, j);
/*  70 */         lo = i + 1;
/*     */       } else {
/*  72 */         quickSortHelper(i + 1, hi);
/*  73 */         hi = j;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void insertionSort(int lo, int hi) {
/*  79 */     for (int i = lo + 1; i <= hi; i++)
/*  80 */       for (int j = i; (j > lo) && 
/*  81 */         (compare(j - 1, j) > 0); j--)
/*     */       {
/*  82 */         swap(j - 1, j);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void mergeSort(int lo, int hi)
/*     */   {
/*  91 */     int diff = hi - lo;
/*  92 */     if (diff <= 12) {
/*  93 */       insertionSort(lo, hi);
/*  94 */       return;
/*     */     }
/*  96 */     int mid = lo + diff / 2;
/*  97 */     mergeSort(lo, mid);
/*  98 */     mergeSort(mid, hi);
/*  99 */     merge(lo, mid, hi, mid - lo, hi - mid);
/*     */   }
/*     */ 
/*     */   private void merge(int lo, int pivot, int hi, int len1, int len2) {
/* 103 */     if ((len1 == 0) || (len2 == 0)) {
/* 104 */       return;
/*     */     }
/* 106 */     if (len1 + len2 == 2) {
/* 107 */       if (compare(pivot, lo) < 0)
/* 108 */         swap(pivot, lo);
/*     */       return;
/*     */     }
/*     */     int len22;
/*     */     int len22;
/*     */     int second_cut;
/*     */     int first_cut;
/*     */     int len11;
/* 114 */     if (len1 > len2) {
/* 115 */       int len11 = len1 / 2;
/* 116 */       int first_cut = lo + len11;
/* 117 */       int second_cut = lower(pivot, hi, first_cut);
/* 118 */       len22 = second_cut - pivot;
/*     */     } else {
/* 120 */       len22 = len2 / 2;
/* 121 */       second_cut = pivot + len22;
/* 122 */       first_cut = upper(lo, pivot, second_cut);
/* 123 */       len11 = first_cut - lo;
/*     */     }
/* 125 */     rotate(first_cut, pivot, second_cut);
/* 126 */     int new_mid = first_cut + len22;
/* 127 */     merge(lo, first_cut, new_mid, len11, len22);
/* 128 */     merge(new_mid, second_cut, hi, len1 - len11, len2 - len22);
/*     */   }
/*     */ 
/*     */   private void rotate(int lo, int mid, int hi) {
/* 132 */     int lot = lo;
/* 133 */     int hit = mid - 1;
/* 134 */     while (lot < hit) {
/* 135 */       swap(lot++, hit--);
/*     */     }
/* 137 */     lot = mid; hit = hi - 1;
/* 138 */     while (lot < hit) {
/* 139 */       swap(lot++, hit--);
/*     */     }
/* 141 */     lot = lo; hit = hi - 1;
/* 142 */     while (lot < hit)
/* 143 */       swap(lot++, hit--);
/*     */   }
/*     */ 
/*     */   private int lower(int lo, int hi, int val)
/*     */   {
/* 148 */     int len = hi - lo;
/* 149 */     while (len > 0) {
/* 150 */       int half = len / 2;
/* 151 */       int mid = lo + half;
/* 152 */       if (compare(mid, val) < 0) {
/* 153 */         lo = mid + 1;
/* 154 */         len = len - half - 1;
/*     */       } else {
/* 156 */         len = half;
/*     */       }
/*     */     }
/* 159 */     return lo;
/*     */   }
/*     */ 
/*     */   private int upper(int lo, int hi, int val) {
/* 163 */     int len = hi - lo;
/* 164 */     while (len > 0) {
/* 165 */       int half = len / 2;
/* 166 */       int mid = lo + half;
/* 167 */       if (compare(val, mid) < 0) {
/* 168 */         len = half;
/*     */       } else {
/* 170 */         lo = mid + 1;
/* 171 */         len = len - half - 1;
/*     */       }
/*     */     }
/* 174 */     return lo;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.SorterTemplate
 * JD-Core Version:    0.6.2
 */