/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class DelegatingHandler extends SimpleElementHandler
/*     */ {
/*  56 */   final Map<String, DefaultHandler> mDelegateMap = new HashMap();
/*     */   final String[] mQNameStack;
/*     */   final DefaultHandler[] mDelegateStack;
/*  59 */   int mStackTop = -1;
/*     */ 
/*     */   public DelegatingHandler(int maxDelegationDepth)
/*     */   {
/*  70 */     this.mDelegateStack = new DefaultHandler[maxDelegationDepth];
/*  71 */     this.mQNameStack = new String[maxDelegationDepth];
/*     */   }
/*     */ 
/*     */   public DelegatingHandler()
/*     */   {
/*  78 */     this(512);
/*     */   }
/*     */ 
/*     */   public void setDelegate(String qName, DefaultHandler handler)
/*     */   {
/*  99 */     if (((handler instanceof DelegateHandler)) && (this != ((DelegateHandler)handler).mDelegatingHandler))
/*     */     {
/* 101 */       String msg = "Delegate handlers must wrap this delegating handler.";
/* 102 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 104 */     this.mDelegateMap.put(qName, handler);
/*     */   }
/*     */ 
/*     */   public void finishDelegate(String qName, DefaultHandler handler)
/*     */   {
/*     */   }
/*     */ 
/*     */   public DefaultHandler getDelegate(String qName)
/*     */   {
/* 136 */     return (DefaultHandler)this.mDelegateMap.get(qName);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 148 */     this.mStackTop = -1;
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 170 */     if (this.mStackTop >= 0) {
/* 171 */       this.mDelegateStack[this.mStackTop].startElement(namespaceURI, localName, qName, atts);
/*     */ 
/* 173 */       return;
/*     */     }
/* 175 */     DefaultHandler handler = getDelegate(qName);
/* 176 */     if (handler == null) {
/* 177 */       return;
/*     */     }
/* 179 */     handler.startDocument();
/* 180 */     handler.startElement(namespaceURI, localName, qName, atts);
/* 181 */     this.mStackTop = 0;
/* 182 */     this.mDelegateStack[0] = handler;
/* 183 */     this.mQNameStack[0] = qName;
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 204 */     if (this.mStackTop < 0) return;
/* 205 */     DefaultHandler handler = this.mDelegateStack[this.mStackTop];
/* 206 */     handler.endElement(namespaceURI, localName, qName);
/* 207 */     if (!qName.equals(this.mQNameStack[this.mStackTop])) return;
/* 208 */     handler.endDocument();
/* 209 */     this.mStackTop -= 1;
/* 210 */     if (this.mStackTop < 0) {
/* 211 */       finishDelegate(qName, handler);
/*     */     } else {
/* 213 */       DelegateHandler delHandler = (DelegateHandler)this.mDelegateStack[this.mStackTop];
/*     */ 
/* 215 */       delHandler.finishDelegate(qName, handler);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 232 */     if (this.mStackTop >= 0)
/* 233 */       this.mDelegateStack[this.mStackTop].characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 267 */     if (this.mStackTop >= 0)
/* 268 */       this.mDelegateStack[this.mStackTop].processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 284 */     if (this.mStackTop >= 0)
/* 285 */       this.mDelegateStack[this.mStackTop].startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/* 298 */     if (this.mStackTop >= 0)
/* 299 */       this.mDelegateStack[this.mStackTop].endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 312 */     if (this.mStackTop >= 0)
/* 313 */       this.mDelegateStack[this.mStackTop].skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 326 */     if (this.mStackTop >= 0)
/* 327 */       this.mDelegateStack[this.mStackTop].setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 346 */       return this.mStackTop < 0 ? super.resolveEntity(publicId, systemId) : this.mDelegateStack[this.mStackTop].resolveEntity(publicId, systemId);
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 350 */       SAXFilterHandler.io2SAXException(t);
/* 351 */     }return null;
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 368 */     if (this.mStackTop >= 0)
/* 369 */       this.mDelegateStack[this.mStackTop].error(exception);
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 385 */     if (this.mStackTop >= 0)
/* 386 */       this.mDelegateStack[this.mStackTop].fatalError(exception);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 399 */     if (this.mStackTop >= 0)
/* 400 */       this.mDelegateStack[this.mStackTop].warning(exception);
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 422 */     if (this.mStackTop >= 0)
/* 423 */       this.mDelegateStack[this.mStackTop].notationDecl(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*     */     throws SAXException
/*     */   {
/* 443 */     if (this.mStackTop >= 0)
/* 444 */       this.mDelegateStack[this.mStackTop].unparsedEntityDecl(name, publicId, systemId, notationName);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.DelegatingHandler
 * JD-Core Version:    0.6.2
 */