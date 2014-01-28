/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class DelegateHandler extends SimpleElementHandler
/*     */ {
/*  40 */   private final Map<String, DefaultHandler> mDelegateMap = new HashMap();
/*     */   final DelegatingHandler mDelegatingHandler;
/*     */ 
/*     */   public DelegateHandler(DelegatingHandler handler)
/*     */   {
/*  48 */     this.mDelegatingHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setDelegate(String qName, DefaultHandler handler)
/*     */   {
/*  65 */     if (((handler instanceof DelegateHandler)) && (this.mDelegatingHandler != ((DelegateHandler)handler).mDelegatingHandler))
/*     */     {
/*  68 */       String msg = "Delegate handlers must wrap the same delegating handler.";
/*  69 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  71 */     this.mDelegateMap.put(qName, handler);
/*     */   }
/*     */ 
/*     */   DefaultHandler getDelegate(String qName) {
/*  75 */     return (DefaultHandler)this.mDelegateMap.get(qName);
/*     */   }
/*     */ 
/*     */   public void finishDelegate(String qName, DefaultHandler handler)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 115 */     DefaultHandler handler = getDelegate(qName);
/* 116 */     if (handler == null) return;
/* 117 */     handler.startDocument();
/* 118 */     handler.startElement(namespaceURI, localName, qName, atts);
/* 119 */     int top = ++this.mDelegatingHandler.mStackTop;
/* 120 */     this.mDelegatingHandler.mQNameStack[top] = qName;
/* 121 */     this.mDelegatingHandler.mDelegateStack[top] = handler;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.DelegateHandler
 * JD-Core Version:    0.6.2
 */