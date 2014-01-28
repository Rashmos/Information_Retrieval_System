/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ class DtrMapMap extends TreeMap<Integer, IntNode>
/*     */   implements DtrMap
/*     */ {
/*     */   static final long serialVersionUID = -840053413688713070L;
/*     */ 
/*     */   DtrMapMap(int out1, int out2, int out3, IntNode dtr1, IntNode dtr2, IntNode dtr3)
/*     */   {
/* 506 */     add(out1, dtr1);
/* 507 */     add(out2, dtr2);
/* 508 */     add(out3, dtr3);
/*     */   }
/*     */   public DtrMap prune(int minCount) {
/* 511 */     Iterator entryIt = entrySet().iterator();
/* 512 */     while (entryIt.hasNext()) {
/* 513 */       Map.Entry entry = (Map.Entry)entryIt.next();
/* 514 */       IntNode node = (IntNode)entry.getValue();
/* 515 */       if (node.count() < minCount)
/* 516 */         entryIt.remove();
/* 517 */       node.prune(minCount);
/*     */     }
/* 519 */     return reduce();
/*     */   }
/*     */   public DtrMap rescale(double countMultiplier) {
/* 522 */     Iterator entryIt = entrySet().iterator();
/* 523 */     while (entryIt.hasNext()) {
/* 524 */       Map.Entry entry = (Map.Entry)entryIt.next();
/* 525 */       IntNode node = (IntNode)entry.getValue();
/* 526 */       node.rescale(countMultiplier);
/* 527 */       if (node.count() == 0)
/* 528 */         entryIt.remove();
/*     */     }
/* 530 */     return reduce();
/*     */   }
/*     */ 
/*     */   public DtrMap reduce() {
/* 534 */     if (size() == 0)
/* 535 */       return DtrMap0.EMPTY_DTR_MAP;
/* 536 */     if (size() == 1) {
/* 537 */       Iterator entryIt = entrySet().iterator();
/* 538 */       Map.Entry entry = (Map.Entry)entryIt.next();
/* 539 */       int token = ((Integer)entry.getKey()).intValue();
/* 540 */       IntNode node = (IntNode)entry.getValue();
/* 541 */       return new DtrMap1(token, node);
/*     */     }
/* 543 */     if (size() == 2) {
/* 544 */       Iterator entryIt = entrySet().iterator();
/* 545 */       Map.Entry entry1 = (Map.Entry)entryIt.next();
/* 546 */       int token1 = ((Integer)entry1.getKey()).intValue();
/* 547 */       IntNode node1 = (IntNode)entry1.getValue();
/* 548 */       Map.Entry entry2 = (Map.Entry)entryIt.next();
/* 549 */       int token2 = ((Integer)entry2.getKey()).intValue();
/* 550 */       IntNode node2 = (IntNode)entry2.getValue();
/* 551 */       return new DtrMap2(token1, token2, node1, node2);
/*     */     }
/* 553 */     return this;
/*     */   }
/*     */ 
/*     */   public int numExtensions() {
/* 557 */     return size();
/*     */   }
/*     */   private void add(int out, IntNode dtr) {
/* 560 */     put(Integer.valueOf(out), dtr);
/*     */   }
/*     */   public void toString(StringBuilder sb, int depth, SymbolTable st) {
/* 563 */     Iterator it = entrySet().iterator();
/* 564 */     for (int i = 0; it.hasNext(); i++) {
/* 565 */       if (i > 0)
/* 566 */         AbstractNode.indent(sb, depth);
/* 567 */       Map.Entry entry = (Map.Entry)it.next();
/* 568 */       Integer key = (Integer)entry.getKey();
/* 569 */       int tok = key.intValue();
/* 570 */       IntNode node = (IntNode)entry.getValue();
/* 571 */       if (st != null)
/* 572 */         sb.append(IntNode.idToSymbol(tok, st));
/*     */       else
/* 574 */         sb.append(tok);
/* 575 */       sb.append(": ");
/* 576 */       node.toString(sb, depth + 1, st);
/*     */     }
/*     */   }
/*     */ 
/* 580 */   public void addDtrs(List<IntNode> queue) { queue.addAll(values()); }
/*     */ 
/*     */   public int dtrsTrieSize() {
/* 583 */     int size = 0;
/* 584 */     for (IntNode node : values())
/* 585 */       size += node.trieSize();
/* 586 */     return size;
/*     */   }
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end) {
/* 589 */     if (start == end) return this;
/* 590 */     Integer tok = Integer.valueOf(tokIndices[start]);
/* 591 */     IntNode dtr = getNode(tok);
/* 592 */     if (dtr == null)
/* 593 */       put(tok, new IntNode(tokIndices, start + 1, end));
/*     */     else
/* 595 */       dtr.increment(tokIndices, start + 1, end);
/* 596 */     return this;
/*     */   }
/*     */ 
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end, int count) {
/* 600 */     if (start == end) return this;
/* 601 */     Integer tok = Integer.valueOf(tokIndices[start]);
/* 602 */     IntNode dtr = getNode(tok);
/* 603 */     if (dtr == null)
/* 604 */       put(tok, new IntNode(tokIndices, start + 1, end, count));
/*     */     else
/* 606 */       dtr.increment(tokIndices, start + 1, end, count);
/* 607 */     return this;
/*     */   }
/*     */ 
/*     */   public DtrMap incrementSequence(int[] tokIndices, int start, int end, int count) {
/* 611 */     if (start == end) return this;
/* 612 */     Integer tok = Integer.valueOf(tokIndices[start]);
/* 613 */     IntNode dtr = getNode(tok);
/* 614 */     if (dtr == null)
/* 615 */       put(tok, new IntNode(tokIndices, start + 1, end, count, false));
/*     */     else
/* 617 */       dtr.incrementSequence(tokIndices, start + 1, end, count);
/* 618 */     return this;
/*     */   }
/*     */   public IntNode getDtr(int tok) {
/* 621 */     return getNode(Integer.valueOf(tok));
/*     */   }
/*     */   IntNode getNode(Integer tok) {
/* 624 */     return (IntNode)get(tok);
/*     */   }
/*     */   public int[] integersFollowing() {
/* 627 */     int[] result = new int[keySet().size()];
/* 628 */     Iterator it = keySet().iterator();
/* 629 */     for (int i = 0; it.hasNext(); i++)
/* 630 */       result[i] = ((Integer)it.next()).intValue();
/* 631 */     return result;
/*     */   }
/*     */   public long extensionCount() {
/* 634 */     long extensionCount = 0L;
/* 635 */     for (IntNode node : values())
/* 636 */       extensionCount += node.count();
/* 637 */     return extensionCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.DtrMapMap
 * JD-Core Version:    0.6.2
 */