/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TaggedIOException extends IOExceptionWithCause
/*     */ {
/*     */   private static final long serialVersionUID = -6994123481142850163L;
/*     */   private final Serializable tag;
/*     */ 
/*     */   public static boolean isTaggedWith(Throwable throwable, Object tag)
/*     */   {
/*  65 */     return (tag != null) && ((throwable instanceof TaggedIOException)) && (tag.equals(((TaggedIOException)throwable).tag));
/*     */   }
/*     */ 
/*     */   public static void throwCauseIfTaggedWith(Throwable throwable, Object tag)
/*     */     throws IOException
/*     */   {
/*  94 */     if (isTaggedWith(throwable, tag))
/*  95 */       throw ((TaggedIOException)throwable).getCause();
/*     */   }
/*     */ 
/*     */   public TaggedIOException(IOException original, Serializable tag)
/*     */   {
/* 111 */     super(original.getMessage(), original);
/* 112 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */   public Serializable getTag()
/*     */   {
/* 121 */     return this.tag;
/*     */   }
/*     */ 
/*     */   public IOException getCause()
/*     */   {
/* 132 */     return (IOException)super.getCause();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.TaggedIOException
 * JD-Core Version:    0.6.2
 */