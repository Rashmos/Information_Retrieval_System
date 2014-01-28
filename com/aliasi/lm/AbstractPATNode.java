/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ abstract class AbstractPATNode extends AbstractNode
/*     */ {
/*     */   abstract char[] chars();
/*     */ 
/*     */   abstract int length();
/*     */ 
/*     */   public Node prune(long minCount)
/*     */   {
/* 454 */     return count() < minCount ? null : this;
/*     */   }
/*     */   public long count(char[] cs, int start, int end) {
/* 457 */     return match(cs, start, end) ? count() : 0L;
/*     */   }
/*     */ 
/*     */   public long contextCount(char[] cs, int start, int end)
/*     */   {
/* 462 */     return properSubMatch(cs, start, end) ? count() : 0L;
/*     */   }
/*     */   boolean match(char[] cs, int start, int end) {
/* 465 */     if (end - start > length()) return false;
/* 466 */     return stringMatch(cs, start, end);
/*     */   }
/*     */   boolean properSubMatch(char[] cs, int start, int end) {
/* 469 */     if (end - start >= length()) return false;
/* 470 */     return stringMatch(cs, start, end);
/*     */   }
/*     */   abstract boolean stringMatch(char[] paramArrayOfChar, int paramInt1, int paramInt2);
/*     */ 
/*     */   public void addDtrNGramCounts(long[][] uniqueTotalCounts, int depth) {
/* 475 */     int patDepth = chars().length;
/* 476 */     long count = count();
/* 477 */     for (int i = 0; i < patDepth; i++) {
/* 478 */       uniqueTotalCounts[(depth + i)][0] += 1L;
/* 479 */       uniqueTotalCounts[(depth + i)][1] += count;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void topNGramsDtrs(NBestCounter counter, char[] csAccum, int level, int dtrLevel)
/*     */   {
/* 485 */     char[] patCs = chars();
/* 486 */     if (dtrLevel > patCs.length) return;
/* 487 */     for (int i = 0; i < dtrLevel; i++)
/* 488 */       csAccum[(level + i)] = patCs[i];
/* 489 */     counter.put(csAccum, level + dtrLevel, count());
/*     */   }
/*     */ 
/*     */   public void addDtrCounts(List<Long> accum, int nGramOrder) {
/* 493 */     char[] patCs = chars();
/* 494 */     if (nGramOrder < patCs.length)
/* 495 */       accum.add(Long.valueOf(count())); 
/*     */   }
/*     */ 
/* 498 */   public int numOutcomes(char[] cs, int start, int end) { return properSubMatch(cs, start, end) ? 1 : 0; }
/*     */ 
/*     */   public Node increment(char[] cs, int start, int end) {
/* 501 */     return increment(cs, start, end, 1);
/*     */   }
/*     */   public Node increment(char[] cs, int start, int end, int incr) {
/* 504 */     char[] patCs = chars();
/* 505 */     long count = count();
/* 506 */     if ((patCs.length == end - start) && (match(cs, start, end))) {
/* 507 */       return NodeFactory.createNode(patCs, 0, patCs.length, count + incr);
/*     */     }
/* 509 */     Node tailNode = NodeFactory.createNode(patCs, 1, patCs.length, count);
/*     */ 
/* 513 */     Node newNode = NodeFactory.createNode(patCs[0], tailNode, count);
/* 514 */     return newNode.increment(cs, start, end, incr);
/*     */   }
/*     */   public Node decrement(char[] cs, int start, int end) {
/* 517 */     if (end == start) return decrement();
/* 518 */     char[] patCs = chars();
/* 519 */     long count = count();
/* 520 */     Node tailNode = NodeFactory.createNode(patCs, 1, patCs.length, count);
/*     */ 
/* 524 */     Node newNode = NodeFactory.createNode(patCs[0], tailNode, count);
/* 525 */     return newNode.decrement(cs, start, end);
/*     */   }
/*     */   public Node decrement(char[] cs, int start, int end, int decr) {
/* 528 */     if (end == start) return decrement(decr);
/* 529 */     char[] patCs = chars();
/* 530 */     long count = count();
/* 531 */     Node tailNode = NodeFactory.createNode(patCs, 1, patCs.length, count);
/*     */ 
/* 535 */     Node newNode = NodeFactory.createNode(patCs[0], tailNode, count);
/* 536 */     return newNode.decrement(cs, start, end, decr);
/*     */   }
/*     */   public Node decrement() {
/* 539 */     long count = count();
/* 540 */     if (count == 0L) return this;
/* 541 */     char[] patCs = chars();
/* 542 */     Node tailNode = NodeFactory.createNode(patCs, 1, patCs.length, count);
/*     */ 
/* 544 */     return NodeFactory.createNode(patCs[0], tailNode, count - 1L);
/*     */   }
/*     */   public Node decrement(int decr) {
/* 547 */     long count = count();
/* 548 */     long decrL = Math.min(count, decr);
/* 549 */     char[] patCs = chars();
/* 550 */     Node tailNode = NodeFactory.createNode(patCs, 1, patCs.length, count - decrL);
/*     */ 
/* 552 */     return NodeFactory.createNode(patCs[0], tailNode, count - decrL);
/*     */   }
/*     */ 
/*     */   public long size()
/*     */   {
/* 557 */     return chars().length + 1;
/*     */   }
/*     */   public char[] outcomes(char[] cs, int start, int end) {
/* 560 */     char[] patCs = chars();
/* 561 */     for (int i = 0; i < patCs.length; i++) {
/* 562 */       if (start + i == end)
/* 563 */         return new char[] { patCs[i] };
/* 564 */       if (patCs[i] != cs[(start + i)])
/* 565 */         return Strings.EMPTY_CHAR_ARRAY;
/*     */     }
/* 567 */     return Strings.EMPTY_CHAR_ARRAY;
/*     */   }
/*     */ 
/*     */   public long dtrUniqueNGramCount(int dtrLevel) {
/* 571 */     return dtrLevel < chars().length ? 1L : 0L;
/*     */   }
/*     */ 
/*     */   public long dtrTotalNGramCount(int dtrLevel) {
/* 575 */     return dtrLevel < chars().length ? count() : 0L;
/*     */   }
/*     */   public void addDaughters(LinkedList<Node> queue) {
/* 578 */     char[] patCs = chars();
/* 579 */     Node tailNode = NodeFactory.createNode(patCs, 1, patCs.length, count());
/* 580 */     queue.add(tailNode);
/*     */   }
/*     */   public void toString(StringBuilder sb, int depth) {
/* 583 */     sb.append(new String(chars()));
/* 584 */     sb.append(' ');
/* 585 */     sb.append(count());
/*     */   }
/*     */ 
/*     */   public void countNodeTypes(ObjectToCounterMap<String> counter) {
/* 589 */     counter.increment(getClass().toString());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.AbstractPATNode
 * JD-Core Version:    0.6.2
 */