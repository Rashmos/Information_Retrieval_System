/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class SAXFilterHandler extends SimpleElementHandler
/*     */ {
/*     */   protected DefaultHandler mHandler;
/*     */ 
/*     */   public SAXFilterHandler(DefaultHandler handler)
/*     */   {
/*  76 */     this.mHandler = handler;
/*     */   }
/*     */ 
/*     */   public SAXFilterHandler()
/*     */   {
/*  84 */     this(NO_OP_DEFAULT_HANDLER);
/*     */   }
/*     */ 
/*     */   public void setHandler(DefaultHandler handler)
/*     */   {
/*  93 */     this.mHandler = handler;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 106 */     this.mHandler.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 117 */     this.mHandler.endDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 139 */     this.mHandler.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 160 */     this.mHandler.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 177 */     this.mHandler.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 194 */     this.mHandler.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 211 */     this.mHandler.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 227 */     this.mHandler.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/* 240 */     this.mHandler.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 253 */     this.mHandler.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 266 */     this.mHandler.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 301 */       return this.mHandler.resolveEntity(publicId, systemId);
/*     */     } catch (Throwable t) {
/* 303 */       io2SAXException(t);
/* 304 */     }return null;
/*     */   }
/*     */ 
/*     */   static void io2SAXException(Throwable t)
/*     */     throws SAXException
/*     */   {
/* 322 */     if ((t instanceof SAXException))
/* 323 */       throw ((SAXException)t);
/* 324 */     if ((t instanceof IOException)) {
/* 325 */       throw new SAXException("Converting IO to SAX exception", (IOException)t);
/*     */     }
/* 327 */     if ((t instanceof RuntimeException))
/* 328 */       throw ((RuntimeException)t);
/* 329 */     if ((t instanceof Error))
/* 330 */       throw ((Error)t);
/* 331 */     throw new Error("Unexpected unchecked, non-error, non-runtime exception throwable", t);
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 348 */     this.mHandler.error(exception);
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 364 */     this.mHandler.fatalError(exception);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 377 */     this.mHandler.warning(exception);
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 399 */     this.mHandler.notationDecl(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*     */     throws SAXException
/*     */   {
/* 419 */     this.mHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.SAXFilterHandler
 * JD-Core Version:    0.6.2
 */