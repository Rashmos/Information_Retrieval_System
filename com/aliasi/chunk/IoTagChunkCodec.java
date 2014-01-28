/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.tag.StringTagging;
/*     */ import com.aliasi.tag.TagLattice;
/*     */ import com.aliasi.tag.Tagging;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class IoTagChunkCodec extends AbstractTagChunkCodec
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3871326314223465927L;
/*     */   final BioTagChunkCodec mBioCodec;
/*     */ 
/*     */   public IoTagChunkCodec()
/*     */   {
/* 176 */     this(null, false);
/*     */   }
/*     */ 
/*     */   public IoTagChunkCodec(TokenizerFactory tokenizerFactory, boolean enforceConsistency)
/*     */   {
/* 191 */     super(tokenizerFactory, enforceConsistency);
/* 192 */     this.mBioCodec = new BioTagChunkCodec(tokenizerFactory, enforceConsistency);
/*     */   }
/*     */ 
/*     */   boolean isEncodable(Chunking chunking, StringBuilder sb)
/*     */   {
/* 197 */     if (!this.mBioCodec.isEncodable(chunking, sb))
/* 198 */       return false;
/* 199 */     Tagging tagging = this.mBioCodec.toTagging(chunking);
/* 200 */     String lastTag = "O";
/* 201 */     for (String tag : tagging.tags()) {
/* 202 */       if (startSameType(lastTag, tag)) {
/* 203 */         if (sb != null) {
/* 204 */           sb.append("Two consectuive chunks of type " + tag.substring(2));
/*     */         }
/* 206 */         return false;
/*     */       }
/* 208 */       lastTag = tag;
/*     */     }
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */   boolean startSameType(String lastTag, String tag) {
/* 214 */     return (tag.startsWith("B_")) && (!"O".equals(lastTag)) && (lastTag.substring(2).equals(tag.substring(2)));
/*     */   }
/*     */ 
/*     */   public Set<String> tagSet(Set<String> chunkTypes)
/*     */   {
/* 220 */     Set tagSet = new HashSet();
/* 221 */     tagSet.addAll(chunkTypes);
/* 222 */     tagSet.add("O");
/* 223 */     return tagSet;
/*     */   }
/*     */ 
/*     */   public boolean legalTagSubSequence(String[] tags) {
/* 227 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean legalTags(String[] tags) {
/* 231 */     return true;
/*     */   }
/*     */ 
/*     */   public Chunking toChunking(StringTagging tagging) {
/* 235 */     enforceConsistency(tagging);
/* 236 */     ChunkingImpl chunking = new ChunkingImpl(tagging.characters());
/* 237 */     for (int n = 0; n < tagging.size(); n++) {
/* 238 */       String tag = tagging.tag(n);
/* 239 */       if (!"O".equals(tag)) {
/* 240 */         String type = tag;
/* 241 */         int start = tagging.tokenStart(n);
/* 242 */         while ((n + 1 < tagging.size()) && (tagging.tag(n + 1).equals(type)))
/* 243 */           n++;
/* 244 */         int end = tagging.tokenEnd(n);
/* 245 */         Chunk chunk = ChunkFactory.createChunk(start, end, type);
/* 246 */         chunking.add(chunk);
/*     */       }
/*     */     }
/* 248 */     return chunking;
/*     */   }
/*     */ 
/*     */   public StringTagging toStringTagging(Chunking chunking)
/*     */   {
/* 257 */     if (this.mTokenizerFactory == null) {
/* 258 */       String msg = "Tokenizer factory must be non-null to convert chunking to tagging.";
/* 259 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 261 */     enforceConsistency(chunking);
/* 262 */     List tokenList = new ArrayList();
/* 263 */     List tagList = new ArrayList();
/* 264 */     List tokenStartList = new ArrayList();
/* 265 */     List tokenEndList = new ArrayList();
/* 266 */     this.mBioCodec.toTagging(chunking, tokenList, tagList, tokenStartList, tokenEndList);
/*     */ 
/* 268 */     transformTags(tagList);
/* 269 */     StringTagging tagging = new StringTagging(tokenList, tagList, chunking.charSequence(), tokenStartList, tokenEndList);
/*     */ 
/* 274 */     return tagging;
/*     */   }
/*     */ 
/*     */   public Tagging<String> toTagging(Chunking chunking)
/*     */   {
/* 283 */     if (this.mTokenizerFactory == null) {
/* 284 */       String msg = "Tokenizer factory must be non-null to convert chunking to tagging.";
/* 285 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 287 */     enforceConsistency(chunking);
/* 288 */     List tokens = new ArrayList();
/* 289 */     List tags = new ArrayList();
/* 290 */     this.mBioCodec.toTagging(chunking, tokens, tags, null, null);
/* 291 */     transformTags(tags);
/* 292 */     return new Tagging(tokens, tags);
/*     */   }
/*     */ 
/*     */   public Iterator<Chunk> nBestChunks(TagLattice<String> lattice, int[] tokenStarts, int[] tokenEnds, int maxResults)
/*     */   {
/* 299 */     throw new UnsupportedOperationException("no n-best chunks yet for IO encodings");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 308 */     return "IoTagChunkCodec";
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 312 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static void transformTags(List<String> tagList)
/*     */   {
/* 317 */     for (int i = 0; i < tagList.size(); i++) {
/* 318 */       String tag = (String)tagList.get(i);
/* 319 */       if (!"O".equals(tag))
/*     */       {
/* 321 */         String transformedTag = tag.substring(2);
/* 322 */         tagList.set(i, transformedTag);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable { static final long serialVersionUID = -3559983129637286794L;
/*     */     private final IoTagChunkCodec mCodec;
/*     */ 
/* 331 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(IoTagChunkCodec codec) {
/* 334 */       this.mCodec = codec;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException
/*     */     {
/* 339 */       out.writeBoolean(this.mCodec.mEnforceConsistency);
/* 340 */       out.writeObject(this.mCodec.mTokenizerFactory != null ? this.mCodec.mTokenizerFactory : Boolean.FALSE);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 347 */       boolean enforceConsistency = in.readBoolean();
/* 348 */       Object tfObj = in.readObject();
/* 349 */       TokenizerFactory tf = (tfObj instanceof TokenizerFactory) ? (TokenizerFactory)tfObj : null;
/*     */ 
/* 353 */       return new IoTagChunkCodec(tf, enforceConsistency);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.IoTagChunkCodec
 * JD-Core Version:    0.6.2
 */