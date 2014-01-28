/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public abstract class TextContentFilter extends ElementStackFilter
/*     */ {
/*  88 */   private final Set<String> mFilteredElements = new HashSet();
/*     */ 
/*     */   public TextContentFilter(DefaultHandler handler)
/*     */   {
/*  97 */     super(handler);
/*     */   }
/*     */ 
/*     */   public TextContentFilter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void filterElement(String qName)
/*     */   {
/* 116 */     this.mFilteredElements.add(qName);
/*     */   }
/*     */ 
/*     */   public void characters(char[] cs, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 134 */     if (this.mFilteredElements.contains(currentElement()))
/* 135 */       filteredCharacters(cs, start, length);
/*     */     else
/* 137 */       super.characters(cs, start, length);
/*     */   }
/*     */ 
/*     */   public abstract void filteredCharacters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws SAXException;
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.TextContentFilter
 * JD-Core Version:    0.6.2
 */