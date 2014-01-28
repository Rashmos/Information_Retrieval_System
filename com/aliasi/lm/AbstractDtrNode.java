/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ abstract class AbstractDtrNode extends AbstractNode
/*     */ {
/*     */   abstract char[] chars();
/*     */ 
/*     */   abstract Node[] dtrs();
/*     */ 
/*     */   abstract int numDtrs();
/*     */ 
/*     */   Node getDtr(char c)
/*     */   {
/* 122 */     char[] cs = chars();
/* 123 */     int i = Arrays.binarySearch(cs, c);
/* 124 */     if (i < 0) return null;
/* 125 */     return dtrs()[i];
/*     */   }
/*     */ 
/*     */   public int numOutcomes(char[] cs, int start, int end) {
/* 129 */     if (start == end) return numDtrs();
/* 130 */     Node dtr = getDtr(cs[start]);
/* 131 */     if (dtr == null) return 0;
/* 132 */     return dtr.numOutcomes(cs, start + 1, end);
/*     */   }
/*     */   public long count(char[] cs, int start, int end) {
/* 135 */     if (start == end) {
/* 136 */       return count();
/*     */     }
/* 138 */     Node dtr = getDtr(cs[start]);
/* 139 */     if (dtr == null) return 0L;
/* 140 */     return dtr.count(cs, start + 1, end);
/*     */   }
/*     */   public long contextCount() {
/* 143 */     Node[] dtrs = dtrs();
/* 144 */     long dtrCount = 0L;
/* 145 */     for (int i = 0; i < dtrs.length; i++)
/* 146 */       dtrCount += dtrs[i].count();
/* 147 */     return dtrCount;
/*     */   }
/*     */   public long contextCount(char[] cs, int start, int end) {
/* 150 */     if (start == end) {
/* 151 */       return contextCount();
/*     */     }
/* 153 */     Node dtr = getDtr(cs[start]);
/* 154 */     if (dtr == null) return 0L;
/* 155 */     return dtr.contextCount(cs, start + 1, end);
/*     */   }
/*     */   public Node decrement() {
/* 158 */     return decrement(1);
/*     */   }
/*     */   public Node decrement(int decr) {
/* 161 */     return NodeFactory.createNode(chars(), dtrs(), count() - decr);
/*     */   }
/*     */   public Node decrement(char[] cs, int start, int end) {
/* 164 */     if (start == end)
/* 165 */       return decrement();
/* 166 */     char[] dtrCs = chars();
/* 167 */     int k = Arrays.binarySearch(dtrCs, cs[start]);
/* 168 */     if (k >= 0) {
/* 169 */       Node[] dtrs = dtrs();
/* 170 */       dtrs[k] = dtrs[k].decrement(cs, start + 1, end);
/* 171 */       return NodeFactory.createNodePrune(dtrCs, dtrs, count());
/*     */     }
/* 173 */     String msg = "Could not find string to decrement=" + new String(cs, start, end - start);
/*     */ 
/* 175 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */   public Node decrement(char[] cs, int start, int end, int decr) {
/* 178 */     if (start == end)
/* 179 */       return decrement(decr);
/* 180 */     char[] dtrCs = chars();
/* 181 */     int k = Arrays.binarySearch(dtrCs, cs[start]);
/* 182 */     if (k >= 0) {
/* 183 */       Node[] dtrs = dtrs();
/* 184 */       dtrs[k] = dtrs[k].decrement(cs, start + 1, end, decr);
/* 185 */       return NodeFactory.createNodePrune(dtrCs, dtrs, count());
/*     */     }
/* 187 */     String msg = "Could not find string to decrement=" + new String(cs, start, end - start);
/*     */ 
/* 189 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */   public Node increment(char[] cs, int start, int end) {
/* 192 */     return increment(cs, start, end, 1);
/*     */   }
/*     */   public Node increment(char[] cs, int start, int end, int incr) {
/* 195 */     if (start == end)
/* 196 */       return NodeFactory.createNode(chars(), dtrs(), count() + incr);
/* 197 */     char[] dtrCs = chars();
/* 198 */     int k = Arrays.binarySearch(dtrCs, cs[start]);
/* 199 */     Node[] dtrs = dtrs();
/* 200 */     if (k >= 0) {
/* 201 */       dtrs[k] = dtrs[k].increment(cs, start + 1, end, incr);
/* 202 */       return NodeFactory.createNode(dtrCs, dtrs, count() + incr);
/*     */     }
/* 204 */     char[] newCs = new char[dtrCs.length + 1];
/* 205 */     Node[] newDtrs = new Node[dtrs.length + 1];
/* 206 */     for (int i = 0; 
/* 207 */       (i < dtrCs.length) && (dtrCs[i] < cs[start]); i++) {
/* 208 */       newCs[i] = dtrCs[i];
/* 209 */       newDtrs[i] = dtrs[i];
/*     */     }
/* 211 */     newCs[i] = cs[start];
/* 212 */     newDtrs[i] = NodeFactory.createNode(cs, start + 1, end, incr);
/* 213 */     for (; i < dtrCs.length; i++) {
/* 214 */       newCs[(i + 1)] = dtrCs[i];
/* 215 */       newDtrs[(i + 1)] = dtrs[i];
/*     */     }
/* 217 */     return NodeFactory.createNode(newCs, newDtrs, count() + incr);
/*     */   }
/*     */ 
/*     */   public long size() {
/* 221 */     Node[] dtrs = dtrs();
/* 222 */     long size = 1L;
/* 223 */     for (int i = 0; i < dtrs.length; i++)
/* 224 */       size += dtrs[i].size();
/* 225 */     return size;
/*     */   }
/*     */ 
/*     */   public void topNGramsDtrs(NBestCounter counter, char[] csAccum, int level, int dtrLevel)
/*     */   {
/* 230 */     Node[] dtrs = dtrs();
/* 231 */     char[] cs = chars();
/* 232 */     for (int i = 0; i < dtrs.length; i++) {
/* 233 */       csAccum[level] = cs[i];
/* 234 */       dtrs[i].topNGrams(counter, csAccum, level + 1, dtrLevel - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addDtrNGramCounts(long[][] uniqueTotalCounts, int depth) {
/* 239 */     Node[] dtrs = dtrs();
/* 240 */     for (int i = 0; i < dtrs.length; i++)
/* 241 */       dtrs[i].addNGramCounts(uniqueTotalCounts, depth);
/*     */   }
/*     */ 
/*     */   public long dtrUniqueNGramCount(int dtrLevel) {
/* 245 */     Node[] dtrs = dtrs();
/* 246 */     long sum = 0L;
/* 247 */     for (int i = 0; i < dtrs.length; i++)
/* 248 */       sum += dtrs[i].uniqueNGramCount(dtrLevel);
/* 249 */     return sum;
/*     */   }
/*     */ 
/*     */   public long dtrTotalNGramCount(int dtrLevel) {
/* 253 */     Node[] dtrs = dtrs();
/* 254 */     long sum = 0L;
/* 255 */     for (int i = 0; i < dtrs.length; i++)
/* 256 */       sum += dtrs[i].totalNGramCount(dtrLevel);
/* 257 */     return sum;
/*     */   }
/*     */ 
/*     */   public void addDtrCounts(List<Long> accum, int nGramOrder) {
/* 261 */     Node[] dtrs = dtrs();
/* 262 */     for (int i = 0; i < dtrs.length; i++)
/* 263 */       dtrs[i].addCounts(accum, nGramOrder); 
/*     */   }
/*     */ 
/* 266 */   public void addDaughters(LinkedList<Node> queue) { Node[] dtrs = dtrs();
/* 267 */     for (int i = 0; i < dtrs.length; i++)
/* 268 */       queue.addLast(dtrs[i]); }
/*     */ 
/*     */   public char[] outcomes(char[] cs, int start, int end) {
/* 271 */     if (start == end)
/* 272 */       return chars();
/* 273 */     Node dtr = getDtr(cs[start]);
/* 274 */     if (dtr == null)
/* 275 */       return Strings.EMPTY_CHAR_ARRAY;
/* 276 */     return dtr.outcomes(cs, start + 1, end);
/*     */   }
/*     */ 
/*     */   public void countNodeTypes(ObjectToCounterMap<String> counter) {
/* 280 */     counter.increment(getClass().toString());
/* 281 */     Node[] dtrs = dtrs();
/* 282 */     for (int i = 0; i < dtrs.length; i++)
/* 283 */       dtrs[i].countNodeTypes(counter); 
/*     */   }
/*     */ 
/* 286 */   public void toString(StringBuilder sb, int depth) { char[] cs = chars();
/* 287 */     Node[] dtrs = dtrs();
/* 288 */     sb.append(' ');
/* 289 */     sb.append(count());
/* 290 */     for (int i = 0; i < dtrs.length; i++)
/* 291 */       toString(sb, cs[i], dtrs[i], depth); }
/*     */ 
/*     */   public Node prune(long minCount) {
/* 294 */     long count = count();
/* 295 */     if (count < minCount) return null;
/* 296 */     Node[] dtrs = dtrs();
/* 297 */     for (int i = 0; i < dtrs.length; i++)
/* 298 */       dtrs[i] = dtrs[i].prune(minCount);
/* 299 */     return NodeFactory.createNodePrune(chars(), dtrs, count);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.AbstractDtrNode
 * JD-Core Version:    0.6.2
 */