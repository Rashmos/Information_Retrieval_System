/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import com.aliasi.util.Strings;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class GroupCharactersFilter extends SAXFilterHandler
/*     */ {
/*     */   private final boolean mRemoveWhitespace;
/*     */   private StringBuilder mCharAccumulator;
/*     */ 
/*     */   public GroupCharactersFilter(DefaultHandler handler)
/*     */   {
/*  59 */     this(handler, false);
/*     */   }
/*     */ 
/*     */   public GroupCharactersFilter(DefaultHandler handler, boolean removeWhitespace)
/*     */   {
/*  69 */     super(handler);
/*  70 */     this.mRemoveWhitespace = removeWhitespace;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/*  82 */     this.mCharAccumulator = new StringBuilder();
/*  83 */     super.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*  96 */     checkCharacters();
/*  97 */     super.endDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 120 */     checkCharacters();
/* 121 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 141 */     checkCharacters();
/* 142 */     super.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ 
/*     */   public final void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 156 */     this.mCharAccumulator.append(ch, start, length);
/*     */   }
/*     */ 
/*     */   private void checkCharacters()
/*     */     throws SAXException
/*     */   {
/* 169 */     if (this.mCharAccumulator.length() == 0) return;
/* 170 */     if ((this.mRemoveWhitespace) && (Strings.allWhitespace(this.mCharAccumulator))) {
/* 171 */       this.mCharAccumulator = new StringBuilder();
/* 172 */       return;
/*     */     }
/* 174 */     super.characters(this.mCharAccumulator.toString().toCharArray(), 0, this.mCharAccumulator.length());
/*     */ 
/* 176 */     this.mCharAccumulator = new StringBuilder();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.GroupCharactersFilter
 * JD-Core Version:    0.6.2
 */