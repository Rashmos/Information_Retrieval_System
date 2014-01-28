/*     */ package org.apache.lucene.analysis.synonym;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.store.ByteArrayDataOutput;
/*     */ import org.apache.lucene.util.BytesRef;
/*     */ import org.apache.lucene.util.BytesRefHash;
/*     */ import org.apache.lucene.util.CharsRef;
/*     */ import org.apache.lucene.util.UnicodeUtil;
/*     */ import org.apache.lucene.util.fst.Builder;
/*     */ import org.apache.lucene.util.fst.ByteSequenceOutputs;
/*     */ import org.apache.lucene.util.fst.FST;
/*     */ import org.apache.lucene.util.fst.FST.INPUT_TYPE;
/*     */ 
/*     */ public class SynonymMap
/*     */ {
/*     */   public static final char WORD_SEPARATOR = '\000';
/*     */   public final FST<BytesRef> fst;
/*     */   public final BytesRefHash words;
/*     */   public final int maxHorizontalContext;
/*     */ 
/*     */   public SynonymMap(FST<BytesRef> fst, BytesRefHash words, int maxHorizontalContext)
/*     */   {
/*  55 */     this.fst = fst;
/*  56 */     this.words = words;
/*  57 */     this.maxHorizontalContext = maxHorizontalContext;
/*     */   }
/*     */ 
/*     */   public static class Builder
/*     */   {
/*  67 */     private final HashMap<CharsRef, MapEntry> workingSet = new HashMap();
/*  68 */     private final BytesRefHash words = new BytesRefHash();
/*  69 */     private final BytesRef utf8Scratch = new BytesRef(8);
/*     */     private int maxHorizontalContext;
/*     */     private final boolean dedup;
/*     */ 
/*     */     public Builder(boolean dedup)
/*     */     {
/*  76 */       this.dedup = dedup;
/*     */     }
/*     */ 
/*     */     public static CharsRef join(String[] words, CharsRef reuse)
/*     */     {
/*  89 */       int upto = 0;
/*  90 */       char[] buffer = reuse.chars;
/*  91 */       for (String word : words) {
/*  92 */         if (upto > 0) {
/*  93 */           if (upto >= buffer.length) {
/*  94 */             reuse.grow(upto);
/*  95 */             buffer = reuse.chars;
/*     */           }
/*  97 */           buffer[(upto++)] = '\000';
/*     */         }
/*     */ 
/* 100 */         int wordLen = word.length();
/* 101 */         int needed = upto + wordLen;
/* 102 */         if (needed > buffer.length) {
/* 103 */           reuse.grow(needed);
/* 104 */           buffer = reuse.chars;
/*     */         }
/*     */ 
/* 107 */         word.getChars(0, wordLen, buffer, upto);
/* 108 */         upto += wordLen;
/*     */       }
/*     */ 
/* 111 */       return reuse;
/*     */     }
/*     */ 
/*     */     public static CharsRef analyze(Analyzer analyzer, String text, CharsRef reuse)
/*     */       throws IOException
/*     */     {
/* 118 */       TokenStream ts = analyzer.reusableTokenStream("", new StringReader(text));
/* 119 */       CharTermAttribute termAtt = (CharTermAttribute)ts.addAttribute(CharTermAttribute.class);
/* 120 */       PositionIncrementAttribute posIncAtt = (PositionIncrementAttribute)ts.addAttribute(PositionIncrementAttribute.class);
/* 121 */       ts.reset();
/* 122 */       reuse.length = 0;
/* 123 */       while (ts.incrementToken()) {
/* 124 */         int length = termAtt.length();
/* 125 */         if (length == 0) {
/* 126 */           throw new IllegalArgumentException("term: " + text + " analyzed to a zero-length token");
/*     */         }
/* 128 */         if (posIncAtt.getPositionIncrement() != 1) {
/* 129 */           throw new IllegalArgumentException("term: " + text + " analyzed to a token with posinc != 1");
/*     */         }
/* 131 */         reuse.grow(reuse.length + length + 1);
/* 132 */         int end = reuse.offset + reuse.length;
/* 133 */         if (reuse.length > 0) {
/* 134 */           reuse.chars[(end++)] = '\000';
/* 135 */           reuse.length += 1;
/*     */         }
/* 137 */         System.arraycopy(termAtt.buffer(), 0, reuse.chars, end, length);
/* 138 */         reuse.length += length;
/*     */       }
/* 140 */       ts.end();
/* 141 */       ts.close();
/* 142 */       if (reuse.length == 0) {
/* 143 */         throw new IllegalArgumentException("term: " + text + " was completely eliminated by analyzer");
/*     */       }
/* 145 */       return reuse;
/*     */     }
/*     */ 
/*     */     private boolean hasHoles(CharsRef chars)
/*     */     {
/* 150 */       int end = chars.offset + chars.length;
/* 151 */       for (int idx = chars.offset + 1; idx < end; idx++) {
/* 152 */         if ((chars.chars[idx] == 0) && (chars.chars[(idx - 1)] == 0)) {
/* 153 */           return true;
/*     */         }
/*     */       }
/* 156 */       if (chars.chars[chars.offset] == 0) {
/* 157 */         return true;
/*     */       }
/* 159 */       if (chars.chars[(chars.offset + chars.length - 1)] == 0) {
/* 160 */         return true;
/*     */       }
/*     */ 
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */     private void add(CharsRef input, int numInputWords, CharsRef output, int numOutputWords, boolean includeOrig)
/*     */     {
/* 173 */       if (numInputWords <= 0) {
/* 174 */         throw new IllegalArgumentException("numInputWords must be > 0 (got " + numInputWords + ")");
/*     */       }
/* 176 */       if (input.length <= 0) {
/* 177 */         throw new IllegalArgumentException("input.length must be > 0 (got " + input.length + ")");
/*     */       }
/* 179 */       if (numOutputWords <= 0) {
/* 180 */         throw new IllegalArgumentException("numOutputWords must be > 0 (got " + numOutputWords + ")");
/*     */       }
/* 182 */       if (output.length <= 0) {
/* 183 */         throw new IllegalArgumentException("output.length must be > 0 (got " + output.length + ")");
/*     */       }
/*     */ 
/* 186 */       assert (!hasHoles(input)) : ("input has holes: " + input);
/* 187 */       assert (!hasHoles(output)) : ("output has holes: " + output);
/*     */ 
/* 190 */       int hashCode = UnicodeUtil.UTF16toUTF8WithHash(output.chars, output.offset, output.length, this.utf8Scratch);
/*     */ 
/* 192 */       int ord = this.words.add(this.utf8Scratch, hashCode);
/* 193 */       if (ord < 0)
/*     */       {
/* 195 */         ord = -ord - 1;
/*     */       }
/*     */ 
/* 201 */       MapEntry e = (MapEntry)this.workingSet.get(input);
/* 202 */       if (e == null) {
/* 203 */         e = new MapEntry(null);
/* 204 */         this.workingSet.put(new CharsRef(input), e);
/*     */       }
/*     */ 
/* 207 */       e.ords.add(Integer.valueOf(ord));
/* 208 */       e.includeOrig |= includeOrig;
/* 209 */       this.maxHorizontalContext = Math.max(this.maxHorizontalContext, numInputWords);
/* 210 */       this.maxHorizontalContext = Math.max(this.maxHorizontalContext, numOutputWords);
/*     */     }
/*     */ 
/*     */     private int countWords(CharsRef chars) {
/* 214 */       int wordCount = 1;
/* 215 */       int upto = chars.offset;
/* 216 */       int limit = chars.offset + chars.length;
/* 217 */       while (upto < limit) {
/* 218 */         if (chars.chars[(upto++)] == 0) {
/* 219 */           wordCount++;
/*     */         }
/*     */       }
/* 222 */       return wordCount;
/*     */     }
/*     */ 
/*     */     public void add(CharsRef input, CharsRef output, boolean includeOrig)
/*     */     {
/* 237 */       add(input, countWords(input), output, countWords(output), includeOrig);
/*     */     }
/*     */ 
/*     */     public SynonymMap build()
/*     */       throws IOException
/*     */     {
/* 244 */       ByteSequenceOutputs outputs = ByteSequenceOutputs.getSingleton();
/*     */ 
/* 246 */       Builder builder = new Builder(FST.INPUT_TYPE.BYTE4, outputs);
/*     */ 
/* 249 */       BytesRef scratch = new BytesRef(64);
/* 250 */       ByteArrayDataOutput scratchOutput = new ByteArrayDataOutput();
/*     */       Set dedupSet;
/*     */       Set dedupSet;
/* 254 */       if (this.dedup)
/* 255 */         dedupSet = new HashSet();
/*     */       else {
/* 257 */         dedupSet = null;
/*     */       }
/*     */ 
/* 260 */       byte[] spare = new byte[5];
/*     */ 
/* 262 */       Set keys = this.workingSet.keySet();
/* 263 */       CharsRef[] sortedKeys = (CharsRef[])keys.toArray(new CharsRef[keys.size()]);
/* 264 */       Arrays.sort(sortedKeys, CharsRef.getUTF16SortedAsUTF8Comparator());
/*     */ 
/* 267 */       for (int keyIdx = 0; keyIdx < sortedKeys.length; keyIdx++) {
/* 268 */         CharsRef input = sortedKeys[keyIdx];
/* 269 */         MapEntry output = (MapEntry)this.workingSet.get(input);
/*     */ 
/* 271 */         int numEntries = output.ords.size();
/*     */ 
/* 273 */         int estimatedSize = 5 + numEntries * 5;
/*     */ 
/* 275 */         scratch.grow(estimatedSize);
/* 276 */         scratchOutput.reset(scratch.bytes, scratch.offset, scratch.bytes.length);
/* 277 */         assert (scratch.offset == 0);
/*     */ 
/* 280 */         int count = 0;
/* 281 */         for (int i = 0; i < numEntries; i++) {
/* 282 */           if (dedupSet != null)
/*     */           {
/* 284 */             Integer ent = (Integer)output.ords.get(i);
/* 285 */             if (!dedupSet.contains(ent))
/*     */             {
/* 288 */               dedupSet.add(ent);
/*     */             }
/*     */           } else { scratchOutput.writeVInt(((Integer)output.ords.get(i)).intValue());
/* 291 */             count++;
/*     */           }
/*     */         }
/* 294 */         int pos = scratchOutput.getPosition();
/* 295 */         scratchOutput.writeVInt(count << 1 | (output.includeOrig ? 0 : 1));
/* 296 */         int pos2 = scratchOutput.getPosition();
/* 297 */         int vIntLen = pos2 - pos;
/*     */ 
/* 300 */         System.arraycopy(scratch.bytes, pos, spare, 0, vIntLen);
/* 301 */         System.arraycopy(scratch.bytes, 0, scratch.bytes, vIntLen, pos);
/* 302 */         System.arraycopy(spare, 0, scratch.bytes, 0, vIntLen);
/*     */ 
/* 304 */         if (dedupSet != null) {
/* 305 */           dedupSet.clear();
/*     */         }
/*     */ 
/* 308 */         scratch.length = (scratchOutput.getPosition() - scratch.offset);
/*     */ 
/* 310 */         builder.add(input, new BytesRef(scratch));
/*     */       }
/*     */ 
/* 313 */       FST fst = builder.finish();
/* 314 */       return new SynonymMap(fst, this.words, this.maxHorizontalContext);
/*     */     }
/*     */ 
/*     */     private static class MapEntry
/*     */     {
/*     */       boolean includeOrig;
/*  82 */       ArrayList<Integer> ords = new ArrayList();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.synonym.SynonymMap
 * JD-Core Version:    0.6.2
 */