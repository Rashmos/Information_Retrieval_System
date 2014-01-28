/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class ChunkTagHandlerAdapter2
/*     */ {
/*     */   private ObjectHandler<Chunking> mChunkHandler;
/* 117 */   public static String OUT_TAG = "O";
/*     */ 
/* 123 */   public static String BEGIN_TAG_PREFIX = "B-";
/*     */ 
/* 129 */   public static String IN_TAG_PREFIX = "I-";
/*     */ 
/*     */   public ChunkTagHandlerAdapter2()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ChunkTagHandlerAdapter2(ObjectHandler<Chunking> handler)
/*     */   {
/*  57 */     this.mChunkHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setChunkHandler(ObjectHandler<Chunking> handler)
/*     */   {
/*  70 */     this.mChunkHandler = handler;
/*     */   }
/*     */ 
/*     */   public void handle(String[] tokens, String[] whitespaces, String[] tags)
/*     */   {
/*  96 */     if (tokens.length != tags.length) {
/*  97 */       String msg = "Tags and tokens must be same length. Found tokens.length=" + tokens.length + " tags.length=" + tags.length;
/*     */ 
/* 100 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 102 */     if ((whitespaces != null) && (whitespaces.length != 1 + tokens.length))
/*     */     {
/* 104 */       String msg = "Whitespaces must be one longer than tokens. Found tokens.length=" + tokens.length + " whitespaces.length=" + whitespaces.length;
/*     */ 
/* 107 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 109 */     Chunking chunking = toChunkingBIO(tokens, whitespaces, tags);
/* 110 */     this.mChunkHandler.handle(chunking);
/*     */   }
/*     */ 
/*     */   public static String toBaseTag(String tag)
/*     */   {
/* 142 */     if ((isBeginTag(tag)) || (isInTag(tag))) return tag.substring(2);
/* 143 */     String msg = "Tag is neither begin not continuation tag. Tag=" + tag;
/*     */ 
/* 145 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   public static boolean isBeginTag(String tag)
/*     */   {
/* 159 */     return tag.startsWith(BEGIN_TAG_PREFIX);
/*     */   }
/*     */ 
/*     */   public static boolean isOutTag(String tag)
/*     */   {
/* 173 */     return tag.equals(OUT_TAG);
/*     */   }
/*     */ 
/*     */   public static boolean isInTag(String tag)
/*     */   {
/* 187 */     return tag.startsWith(IN_TAG_PREFIX);
/*     */   }
/*     */ 
/*     */   public static String toInTag(String type)
/*     */   {
/* 199 */     return IN_TAG_PREFIX + type;
/*     */   }
/*     */ 
/*     */   public static String toBeginTag(String type)
/*     */   {
/* 211 */     return BEGIN_TAG_PREFIX + type;
/*     */   }
/*     */ 
/*     */   public static Chunking toChunkingBIO(String[] tokens, String[] whitespaces, String[] tags)
/*     */   {
/* 227 */     StringBuilder sb = new StringBuilder();
/* 228 */     if (whitespaces == null) {
/* 229 */       whitespaces = new String[tokens.length + 1];
/* 230 */       Arrays.fill(whitespaces, " ");
/* 231 */       whitespaces[0] = "";
/* 232 */       whitespaces[(whitespaces.length - 1)] = "";
/*     */     }
/* 234 */     for (int i = 0; i < tokens.length; i++) {
/* 235 */       sb.append(whitespaces[i]);
/* 236 */       sb.append(tokens[i]);
/*     */     }
/* 238 */     sb.append(whitespaces[(whitespaces.length - 1)]);
/* 239 */     ChunkingImpl chunking = new ChunkingImpl(sb);
/*     */ 
/* 241 */     int pos = 0;
/* 242 */     for (int i = 0; i < tokens.length; ) {
/* 243 */       pos += whitespaces[i].length();
/* 244 */       if (!isBeginTag(tags[i])) {
/* 245 */         pos += tokens[i].length();
/* 246 */         i++;
/*     */       }
/*     */       else {
/* 249 */         int start = pos;
/* 250 */         String type = toBaseTag(tags[i]);
/*     */         while (true) {
/* 252 */           pos += tokens[i].length();
/* 253 */           i++;
/* 254 */           if ((i >= tokens.length) || (!isInTag(tags[i])))
/*     */           {
/* 256 */             chunking.add(ChunkFactory.createChunk(start, pos, type));
/* 257 */             break;
/*     */           }
/* 259 */           pos += whitespaces[i].length();
/*     */         }
/*     */       }
/*     */     }
/* 262 */     return chunking;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.ChunkTagHandlerAdapter2
 * JD-Core Version:    0.6.2
 */