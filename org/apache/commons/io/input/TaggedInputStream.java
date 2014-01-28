/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.UUID;
/*     */ import org.apache.commons.io.TaggedIOException;
/*     */ 
/*     */ public class TaggedInputStream extends ProxyInputStream
/*     */ {
/*  69 */   private final Serializable tag = UUID.randomUUID();
/*     */ 
/*     */   public TaggedInputStream(InputStream proxy)
/*     */   {
/*  77 */     super(proxy);
/*     */   }
/*     */ 
/*     */   public boolean isCauseOf(Throwable exception)
/*     */   {
/*  88 */     return TaggedIOException.isTaggedWith(exception, this.tag);
/*     */   }
/*     */ 
/*     */   public void throwIfCauseOf(Throwable throwable)
/*     */     throws IOException
/*     */   {
/* 102 */     TaggedIOException.throwCauseIfTaggedWith(throwable, this.tag);
/*     */   }
/*     */ 
/*     */   protected void handleIOException(IOException e)
/*     */     throws IOException
/*     */   {
/* 113 */     throw new TaggedIOException(e, this.tag);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.input.TaggedInputStream
 * JD-Core Version:    0.6.2
 */