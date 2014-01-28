/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class SimpleElementHandler extends DefaultHandler
/*     */ {
/* 199 */   public static final Attributes EMPTY_ATTS = new AttributesImpl();
/*     */ 
/* 479 */   public static final DefaultHandler NO_OP_DEFAULT_HANDLER = new DefaultHandler();
/*     */   protected static final String CDATA_ATTS_TYPE = "CDATA";
/*     */ 
/*     */   public void characters(String s)
/*     */     throws SAXException
/*     */   {
/*  45 */     characters(this, s);
/*     */   }
/*     */ 
/*     */   public void characters(char[] cs)
/*     */     throws SAXException
/*     */   {
/*  57 */     characters(this, cs);
/*     */   }
/*     */ 
/*     */   public void startSimpleElement(String name)
/*     */     throws SAXException
/*     */   {
/*  72 */     startSimpleElement(this, name);
/*     */   }
/*     */ 
/*     */   public void startSimpleElement(String name, String att, String value)
/*     */     throws SAXException
/*     */   {
/*  91 */     startSimpleElement(this, name, att, value);
/*     */   }
/*     */ 
/*     */   public void startSimpleElement(String name, String att1, String val1, String att2, String val2)
/*     */     throws SAXException
/*     */   {
/* 111 */     Attributes atts = createAttributes(att1, val1, att2, val2);
/*     */ 
/* 113 */     startSimpleElement(name, atts);
/*     */   }
/*     */ 
/*     */   public void startSimpleElement(String localName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 131 */     startSimpleElement(this, localName, atts);
/*     */   }
/*     */ 
/*     */   public void endSimpleElement(String localName)
/*     */     throws SAXException
/*     */   {
/* 143 */     endSimpleElement(this, localName);
/*     */   }
/*     */ 
/*     */   public void startEndSimpleElement(String localName)
/*     */     throws SAXException
/*     */   {
/* 157 */     startSimpleElement(localName);
/* 158 */     endSimpleElement(localName);
/*     */   }
/*     */ 
/*     */   public void startEndSimpleElement(String localName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 173 */     startSimpleElement(localName, atts);
/* 174 */     endSimpleElement(localName);
/*     */   }
/*     */ 
/*     */   public static void addSimpleAttribute(AttributesImpl atts, String localName, String value)
/*     */   {
/* 190 */     atts.addAttribute(null, localName, localName, "CDATA", value);
/*     */   }
/*     */ 
/*     */   public static final Attributes createAttributes(String attribute, String value)
/*     */   {
/* 211 */     AttributesImpl atts = new AttributesImpl();
/* 212 */     addSimpleAttribute(atts, attribute, value);
/* 213 */     return atts;
/*     */   }
/*     */ 
/*     */   public static final Attributes createAttributes(String attribute1, String value1, String attribute2, String value2)
/*     */   {
/* 230 */     AttributesImpl atts = new AttributesImpl();
/* 231 */     addSimpleAttribute(atts, attribute1, value1);
/* 232 */     addSimpleAttribute(atts, attribute2, value2);
/* 233 */     return atts;
/*     */   }
/*     */ 
/*     */   public static final Attributes createAttributes(String attribute1, String value1, String attribute2, String value2, String attribute3, String value3)
/*     */   {
/* 254 */     AttributesImpl atts = new AttributesImpl();
/* 255 */     addSimpleAttribute(atts, attribute1, value1);
/* 256 */     addSimpleAttribute(atts, attribute2, value2);
/* 257 */     addSimpleAttribute(atts, attribute3, value3);
/* 258 */     return atts;
/*     */   }
/*     */ 
/*     */   public static final Attributes createAttributes(String attribute1, String value1, String attribute2, String value2, String attribute3, String value3, String attribute4, String value4)
/*     */   {
/* 283 */     AttributesImpl atts = new AttributesImpl();
/* 284 */     addSimpleAttribute(atts, attribute1, value1);
/* 285 */     addSimpleAttribute(atts, attribute2, value2);
/* 286 */     addSimpleAttribute(atts, attribute3, value3);
/* 287 */     addSimpleAttribute(atts, attribute4, value4);
/* 288 */     return atts;
/*     */   }
/*     */ 
/*     */   public static final Attributes createAttributes(String attribute1, String value1, String attribute2, String value2, String attribute3, String value3, String attribute4, String value4, String attribute5, String value5)
/*     */   {
/* 318 */     AttributesImpl atts = new AttributesImpl();
/* 319 */     addSimpleAttribute(atts, attribute1, value1);
/* 320 */     addSimpleAttribute(atts, attribute2, value2);
/* 321 */     addSimpleAttribute(atts, attribute3, value3);
/* 322 */     addSimpleAttribute(atts, attribute4, value4);
/* 323 */     addSimpleAttribute(atts, attribute5, value5);
/* 324 */     return atts;
/*     */   }
/*     */ 
/*     */   public static final Attributes createAttributes(String attribute1, String value1, String attribute2, String value2, String attribute3, String value3, String attribute4, String value4, String attribute5, String value5, String attribute6, String value6)
/*     */   {
/* 358 */     AttributesImpl atts = new AttributesImpl();
/* 359 */     addSimpleAttribute(atts, attribute1, value1);
/* 360 */     addSimpleAttribute(atts, attribute2, value2);
/* 361 */     addSimpleAttribute(atts, attribute3, value3);
/* 362 */     addSimpleAttribute(atts, attribute4, value4);
/* 363 */     addSimpleAttribute(atts, attribute5, value5);
/* 364 */     addSimpleAttribute(atts, attribute6, value6);
/* 365 */     return atts;
/*     */   }
/*     */ 
/*     */   public static void characters(DefaultHandler handler, String s)
/*     */     throws SAXException
/*     */   {
/* 383 */     handler.characters(s.toCharArray(), 0, s.length());
/*     */   }
/*     */ 
/*     */   public static void characters(DefaultHandler handler, char[] cs)
/*     */     throws SAXException
/*     */   {
/* 398 */     handler.characters(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public static void startSimpleElement(DefaultHandler handler, String name)
/*     */     throws SAXException
/*     */   {
/* 414 */     startSimpleElement(handler, name, EMPTY_ATTS);
/*     */   }
/*     */ 
/*     */   public static void startSimpleElement(DefaultHandler handler, String name, String att, String value)
/*     */     throws SAXException
/*     */   {
/* 435 */     startSimpleElement(handler, name, createAttributes(att, value));
/*     */   }
/*     */ 
/*     */   public static void startSimpleElement(DefaultHandler handler, String localName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 454 */     handler.startElement(null, localName, localName, atts);
/*     */   }
/*     */ 
/*     */   public static void endSimpleElement(DefaultHandler handler, String localName)
/*     */     throws SAXException
/*     */   {
/* 471 */     handler.endElement(null, localName, localName);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.SimpleElementHandler
 * JD-Core Version:    0.6.2
 */