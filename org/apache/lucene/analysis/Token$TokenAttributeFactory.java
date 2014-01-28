/*     */ package org.apache.lucene.analysis;
/*     */ 
/*     */ import org.apache.lucene.util.Attribute;
/*     */ import org.apache.lucene.util.AttributeImpl;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ 
/*     */ public final class Token$TokenAttributeFactory extends AttributeSource.AttributeFactory
/*     */ {
/*     */   private final AttributeSource.AttributeFactory delegate;
/*     */ 
/*     */   public Token$TokenAttributeFactory(AttributeSource.AttributeFactory delegate)
/*     */   {
/* 786 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   public AttributeImpl createAttributeInstance(Class<? extends Attribute> attClass)
/*     */   {
/* 791 */     return attClass.isAssignableFrom(Token.class) ? new Token() : this.delegate.createAttributeInstance(attClass);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 797 */     if (this == other) return true;
/* 798 */     if ((other instanceof TokenAttributeFactory)) {
/* 799 */       TokenAttributeFactory af = (TokenAttributeFactory)other;
/* 800 */       return this.delegate.equals(af.delegate);
/*     */     }
/* 802 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 807 */     return this.delegate.hashCode() ^ 0xA45AA31;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.Token.TokenAttributeFactory
 * JD-Core Version:    0.6.2
 */