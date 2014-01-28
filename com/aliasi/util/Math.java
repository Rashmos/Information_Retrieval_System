/*     */ package com.aliasi.util;
/*     */ 
/*     */ public class Math
/*     */ {
/*  52 */   public static final double GOLDEN_RATIO = (1.0D + java.lang.Math.sqrt(5.0D)) / 2.0D;
/*     */ 
/*  57 */   public static final double LN_2 = java.lang.Math.log(2.0D);
/*     */ 
/*  59 */   static final double INV_LN_2 = 1.0D / LN_2;
/*     */ 
/*  66 */   public static final double LOG2_E = log2(2.718281828459045D);
/*     */ 
/* 103 */   public static final long[] FIBONACCI_SEQUENCE = { 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L, 55L, 89L, 144L, 233L, 377L, 610L, 987L, 1597L, 2584L, 4181L, 6765L, 10946L, 17711L, 28657L, 46368L, 75025L, 121393L, 196418L, 317811L, 514229L, 832040L, 1346269L, 2178309L, 3524578L, 5702887L, 9227465L, 14930352L, 24157817L, 39088169L, 63245986L, 102334155L, 165580141L, 267914296L, 433494437L, 701408733L, 1134903170L, 1836311903L, 2971215073L, 4807526976L, 7778742049L, 12586269025L, 20365011074L, 32951280099L, 53316291173L, 86267571272L, 139583862445L, 225851433717L, 365435296162L, 591286729879L, 956722026041L, 1548008755920L, 2504730781961L, 4052739537881L, 6557470319842L, 10610209857723L, 17167680177565L, 27777890035288L, 44945570212853L, 72723460248141L, 117669030460994L, 190392490709135L, 308061521170129L, 498454011879264L, 806515533049393L, 1304969544928657L, 2111485077978050L, 3416454622906707L, 5527939700884757L, 8944394323791464L, 14472334024676221L, 23416728348467685L, 37889062373143906L, 61305790721611591L, 99194853094755497L, 160500643816367088L, 259695496911122585L, 420196140727489673L, 679891637638612258L, 1100087778366101931L, 1779979416004714189L, 2880067194370816120L, 4660046610375530309L, 7540113804746346429L };
/*     */ 
/* 440 */   static double[] LANCZOS_COEFFS = { 0.9999999999998099D, 676.5203681218851D, -1259.1392167224028D, 771.32342877765313D, -176.61502916214059D, 12.507343278686905D, -0.1385710952657201D, 9.984369578019572E-06D, 1.505632735149312E-07D };
/*     */ 
/* 452 */   static double SQRT_2_PI = java.lang.Math.sqrt(6.283185307179586D);
/*     */ 
/* 568 */   static double NEGATIVE_DIGAMMA_1 = 0.5772156649015329D;
/*     */ 
/* 570 */   private static final double[] DIGAMMA_COEFFS = { 0.3045919855871516D, 0.7203797743918283D, -0.1245495924386137D, 0.02776945733192783D, -0.006776237143982246D, 0.001723875514224771D, -0.0004481769906425293D, 0.0001179366000015557D, -3.125389428098013E-05D, 8.317399701217328E-06D, -2.219142764378005E-06D, 5.930226672932935E-07D, -1.586305119147066E-07D, 4.24592039831936E-08D, -1.136912961695111E-08D, 3.045022172959317E-09D, -8.156845508075315E-10D, 2.185232474997546E-10D, -5.854649144168952E-11D, 1.568634845087121E-11D, -4.202949627314324E-12D, 1.126143571926491E-12D, -3.017435363686028E-13D, 8.085095525638953E-14D, -2.166377980942123E-14D, 5.804763427133939E-15D, -1.555376718920473E-15D, 4.167610859804081E-16D, -1.116706506422132E-16D };
/*     */ 
/*     */   public static boolean isPrime(int num)
/*     */   {
/* 207 */     if (num < 2) return false;
/* 208 */     for (int i = 2; i <= num / 2; i++)
/* 209 */       if (num % i == 0) return false;
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */   public static int nextPrime(int num)
/*     */   {
/* 223 */     if (num < 2) return 2;
/* 224 */     for (int i = num + 1; ; i++)
/* 225 */       if (isPrime(i)) return i;
/*     */   }
/*     */ 
/*     */   public static double naturalLogToBase2Log(double x)
/*     */   {
/* 240 */     return x * INV_LN_2;
/*     */   }
/*     */ 
/*     */   public static double logBase2ToNaturalLog(double x)
/*     */   {
/* 249 */     return x / LOG2_E;
/*     */   }
/*     */ 
/*     */   public static double log2(double x)
/*     */   {
/* 259 */     return naturalLogToBase2Log(java.lang.Math.log(x));
/*     */   }
/*     */ 
/*     */   public static int byteAsUnsigned(byte b)
/*     */   {
/* 273 */     return b >= 0 ? b : 256 + b;
/*     */   }
/*     */ 
/*     */   public static double log2Factorial(long n)
/*     */   {
/* 301 */     if (n < 0L) {
/* 302 */       String msg = "Factorials only defined for non-negative arguments. Found argument=" + n;
/*     */ 
/* 304 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 306 */     double sum = 0.0D;
/* 307 */     for (long i = 1L; i <= n; i += 1L)
/* 308 */       sum += log2(i);
/* 309 */     return sum;
/*     */   }
/*     */ 
/*     */   public static double sum(double[] xs)
/*     */   {
/* 319 */     double sum = 0.0D;
/* 320 */     for (int i = 0; i < xs.length; i++)
/* 321 */       sum += xs[i];
/* 322 */     return sum;
/*     */   }
/*     */ 
/*     */   public static double minimum(double[] xs)
/*     */   {
/* 334 */     if (xs.length == 0) return (0.0D / 0.0D);
/* 335 */     double min = xs[0];
/* 336 */     for (int i = 1; i < xs.length; i++)
/* 337 */       if (xs[i] < min) min = xs[i];
/* 338 */     return min;
/*     */   }
/*     */ 
/*     */   public static double maximum(double[] xs)
/*     */   {
/* 350 */     if (xs.length == 0) return (0.0D / 0.0D);
/* 351 */     double max = xs[0];
/* 352 */     for (int i = 1; i < xs.length; i++)
/* 353 */       if (xs[i] > max) max = xs[i];
/* 354 */     return max;
/*     */   }
/*     */ 
/*     */   public static double log2BinomialCoefficient(long n, long m)
/*     */   {
/* 377 */     return log2(n) - log2(m) - log2(n - m);
/*     */   }
/*     */ 
/*     */   public static double log2Gamma(double z)
/*     */   {
/* 427 */     if (z < 0.5D) {
/* 428 */       return log2(3.141592653589793D) - log2(java.lang.Math.sin(3.141592653589793D * z)) - log2Gamma(1.0D - z);
/*     */     }
/*     */ 
/* 432 */     double result = 0.0D;
/* 433 */     while (z > 1.5D) {
/* 434 */       result += log2(z - 1.0D);
/* 435 */       z -= 1.0D;
/*     */     }
/* 437 */     return result + log2(lanczosGamma(z));
/*     */   }
/*     */ 
/*     */   static double lanczosGamma(double z)
/*     */   {
/* 456 */     double zMinus1 = z - 1.0D;
/* 457 */     double x = LANCZOS_COEFFS[0];
/* 458 */     for (int i = 1; i < LANCZOS_COEFFS.length - 2; i++)
/* 459 */       x += LANCZOS_COEFFS[i] / (zMinus1 + i);
/* 460 */     double t = zMinus1 + (LANCZOS_COEFFS.length - 2) + 0.5D;
/* 461 */     return SQRT_2_PI * java.lang.Math.pow(t, zMinus1 + 0.5D) * java.lang.Math.exp(-t) * x;
/*     */   }
/*     */ 
/*     */   public static double digamma(double x)
/*     */   {
/* 513 */     if ((x <= 0.0D) && (x == ()x)) {
/* 514 */       return (0.0D / 0.0D);
/*     */     }
/* 516 */     double accum = 0.0D;
/* 517 */     if (x < 0.0D) {
/* 518 */       accum += 3.141592653589793D / java.lang.Math.tan(3.141592653589793D * (1.0D - x));
/*     */ 
/* 520 */       x = 1.0D - x;
/*     */     }
/*     */ 
/* 523 */     if (x < 1.0D) {
/* 524 */       while (x < 1.0D) {
/* 525 */         accum -= 1.0D / x++;
/*     */       }
/*     */     }
/* 528 */     if (x == 1.0D) {
/* 529 */       return accum - NEGATIVE_DIGAMMA_1;
/*     */     }
/* 531 */     if (x == 2.0D) {
/* 532 */       return accum + 1.0D - NEGATIVE_DIGAMMA_1;
/*     */     }
/* 534 */     if (x == 3.0D) {
/* 535 */       return accum + 1.5D - NEGATIVE_DIGAMMA_1;
/*     */     }
/*     */ 
/* 538 */     if (x > 3.0D) {
/* 539 */       while (x > 3.0D)
/* 540 */         accum += 1.0D / --x;
/* 541 */       return accum + digamma(x);
/*     */     }
/*     */ 
/* 544 */     x -= 2.0D;
/* 545 */     double tNMinus1 = 1.0D;
/* 546 */     double tN = x;
/* 547 */     double digamma = DIGAMMA_COEFFS[0] + DIGAMMA_COEFFS[1] * tN;
/* 548 */     for (int n = 2; n < DIGAMMA_COEFFS.length; n++) {
/* 549 */       double tN1 = 2.0D * x * tN - tNMinus1;
/* 550 */       digamma += DIGAMMA_COEFFS[n] * tN1;
/* 551 */       tNMinus1 = tN;
/* 552 */       tN = tN1;
/*     */     }
/* 554 */     return accum + digamma;
/*     */   }
/*     */ 
/*     */   public static double relativeAbsoluteDifference(double x, double y)
/*     */   {
/* 614 */     return (Double.isInfinite(x)) || (Double.isInfinite(y)) ? (1.0D / 0.0D) : java.lang.Math.abs(x - y) / (java.lang.Math.abs(x) + java.lang.Math.abs(y));
/*     */   }
/*     */ 
/*     */   public static double logSumOfExponentials(double[] xs)
/*     */   {
/* 648 */     if (xs.length == 1) return xs[0];
/* 649 */     double max = maximum(xs);
/* 650 */     double sum = 0.0D;
/* 651 */     for (int i = 0; i < xs.length; i++)
/* 652 */       if (xs[i] != (-1.0D / 0.0D))
/* 653 */         sum += java.lang.Math.exp(xs[i] - max);
/* 654 */     return max + java.lang.Math.log(sum);
/*     */   }
/*     */ 
/*     */   public static double max(double[] xs)
/*     */   {
/* 666 */     if (xs.length == 0)
/* 667 */       return (0.0D / 0.0D);
/* 668 */     double max = xs[0];
/* 669 */     for (int i = 1; i < xs.length; i++)
/* 670 */       max = java.lang.Math.max(max, xs[i]);
/* 671 */     return max;
/*     */   }
/*     */ 
/*     */   public static int max(int[] xs)
/*     */   {
/* 683 */     int max = xs[0];
/* 684 */     for (int i = 1; i < xs.length; i++)
/* 685 */       if (xs[i] > max)
/* 686 */         max = xs[i];
/* 687 */     return max;
/*     */   }
/*     */ 
/*     */   public static int sum(int[] xs)
/*     */   {
/* 701 */     int sum = 0;
/* 702 */     for (int i = 0; i < xs.length; i++)
/* 703 */       sum += xs[i];
/* 704 */     return sum;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Math
 * JD-Core Version:    0.6.2
 */