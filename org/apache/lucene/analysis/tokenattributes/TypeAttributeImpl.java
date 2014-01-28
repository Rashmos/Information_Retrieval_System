/*    */ package org.apache.lucene.analysis.tokenattributes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.lucene.util.AttributeImpl;
/*    */ 
/*    */ public class TypeAttributeImpl extends AttributeImpl
/*    */   implements TypeAttribute, Cloneable, Serializable
/*    */ {
/*    */   private String type;
/*    */   public static final String DEFAULT_TYPE = "word";
/*    */ 
/*    */   public TypeAttributeImpl()
/*    */   {
/* 32 */     this("word");
/*    */   }
/*    */ 
/*    */   public TypeAttributeImpl(String type) {
/* 36 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public String type()
/*    */   {
/* 41 */     return this.type;
/*    */   }
/*    */ 
/*    */   public void setType(String type)
/*    */   {
/* 47 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 52 */     this.type = "word";
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 57 */     if (other == this) {
/* 58 */       return true;
/*    */     }
/*    */ 
/* 61 */     if ((other instanceof TypeAttributeImpl)) {
/* 62 */       return this.type.equals(((TypeAttributeImpl)other).type);
/*    */     }
/*    */ 
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 70 */     return this.type.hashCode();
/*    */   }
/*    */ 
/*    */   public void copyTo(AttributeImpl target)
/*    */   {
/* 75 */     TypeAttribute t = (Serializable)target;
/* 76 */     t.setType(this.type);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tokenattributes.TypeAttributeImpl
 * JD-Core Version:    0.6.2
 */