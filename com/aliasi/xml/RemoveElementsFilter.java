/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class RemoveElementsFilter extends SAXFilterHandler
/*     */ {
/*  41 */   private final Set<String> mElementsToRemove = new HashSet();
/*     */ 
/*     */   public RemoveElementsFilter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public RemoveElementsFilter(DefaultHandler handler)
/*     */   {
/*  59 */     super(handler);
/*     */   }
/*     */ 
/*     */   public void removeElement(String qName)
/*     */   {
/*  69 */     this.mElementsToRemove.add(qName);
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/*  91 */     if (!this.mElementsToRemove.contains(qName))
/*  92 */       this.mHandler.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 114 */     if (!this.mElementsToRemove.contains(qName))
/* 115 */       this.mHandler.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.RemoveElementsFilter
 * JD-Core Version:    0.6.2
 */