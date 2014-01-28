/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.Attributes2Impl;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class ElementStackFilter extends SAXFilterHandler
/*     */ {
/*  49 */   private final Stack<String> mElementStack = new Stack();
/*     */ 
/*  54 */   private final Stack<Attributes> mAttributesStack = new Stack();
/*     */ 
/*     */   public ElementStackFilter(DefaultHandler handler)
/*     */   {
/*  64 */     super(handler);
/*     */   }
/*     */ 
/*     */   public ElementStackFilter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public List<String> getElementStack()
/*     */   {
/*  92 */     return Collections.unmodifiableList(this.mElementStack);
/*     */   }
/*     */ 
/*     */   public List<Attributes> getAttributesStack()
/*     */   {
/* 114 */     return Collections.unmodifiableList(this.mAttributesStack);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 127 */     this.mElementStack.clear();
/* 128 */     super.startDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 151 */     this.mElementStack.push(qName);
/* 152 */     this.mAttributesStack.push(copy(atts));
/* 153 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 175 */     this.mElementStack.pop();
/* 176 */     this.mAttributesStack.pop();
/* 177 */     super.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ 
/*     */   public boolean noElement()
/*     */   {
/* 188 */     return this.mElementStack.isEmpty();
/*     */   }
/*     */ 
/*     */   public String currentElement()
/*     */   {
/* 200 */     return (String)this.mElementStack.peek();
/*     */   }
/*     */ 
/*     */   public Attributes currentAttributes()
/*     */   {
/* 211 */     return (Attributes)this.mAttributesStack.peek();
/*     */   }
/*     */ 
/*     */   static Attributes copy(Attributes atts) {
/* 215 */     return new Attributes2Impl(atts);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.ElementStackFilter
 * JD-Core Version:    0.6.2
 */