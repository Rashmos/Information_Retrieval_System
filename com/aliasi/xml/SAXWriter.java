/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class SAXWriter extends SimpleElementHandler
/*     */ {
/*     */   private CharsetEncoder mCharsetEncoder;
/*     */   private PrintWriter mPrinter;
/*     */   private BufferedWriter mBufWriter;
/*     */   private OutputStreamWriter mWriter;
/*     */   private String mCharsetName;
/* 202 */   private String mDtdString = null;
/*     */   private boolean mStartedElement;
/*     */   private String mStartedNamespaceURI;
/*     */   private String mStartedLocalName;
/*     */   private String mStartedQName;
/*     */   private boolean mStartedHasAtts;
/* 215 */   private final Map<String, String> mPrefixMap = new HashMap();
/*     */   private boolean mXhtmlMode;
/* 687 */   private static String START_COMMENT = "<!--";
/* 688 */   private static String END_COMMENT = "-->";
/*     */ 
/*     */   public SAXWriter(OutputStream out, String charsetName)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 237 */     this(out, charsetName, false);
/*     */   }
/*     */ 
/*     */   public SAXWriter(OutputStream out, String charsetName, boolean xhtmlMode)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 271 */     this(xhtmlMode);
/* 272 */     setOutputStream(out, charsetName);
/*     */   }
/*     */ 
/*     */   public SAXWriter()
/*     */   {
/* 287 */     this(false);
/*     */   }
/*     */ 
/*     */   public SAXWriter(boolean xhtmlMode)
/*     */   {
/* 304 */     this.mXhtmlMode = xhtmlMode;
/*     */   }
/*     */ 
/*     */   public void setDTDString(String dtdString)
/*     */   {
/* 317 */     this.mDtdString = dtdString;
/*     */   }
/*     */ 
/*     */   public final void setOutputStream(OutputStream out, String charsetName)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 338 */     Charset charset = Charset.forName(charsetName);
/* 339 */     this.mCharsetEncoder = charset.newEncoder();
/* 340 */     this.mWriter = new OutputStreamWriter(out, this.mCharsetEncoder);
/* 341 */     this.mBufWriter = new BufferedWriter(this.mWriter);
/* 342 */     this.mPrinter = new PrintWriter(this.mBufWriter);
/* 343 */     this.mCharsetName = charsetName;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/* 354 */     printXMLDeclaration();
/* 355 */     this.mStartedElement = false;
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */   {
/* 364 */     if (this.mStartedElement) {
/* 365 */       endElement(this.mStartedNamespaceURI, this.mStartedLocalName, this.mStartedQName);
/*     */     }
/*     */ 
/* 369 */     this.mPrinter.flush();
/*     */     try {
/* 371 */       this.mBufWriter.flush();
/*     */     }
/*     */     catch (IOException e) {
/*     */     }
/*     */     try {
/* 376 */       this.mWriter.flush();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */   {
/* 393 */     this.mPrefixMap.put(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */   {
/* 412 */     if (this.mStartedElement) {
/* 413 */       this.mPrinter.print(">");
/* 414 */       this.mStartedElement = false;
/*     */     }
/* 416 */     this.mPrinter.print('<');
/* 417 */     this.mPrinter.print(qName);
/* 418 */     printAttributes(atts);
/* 419 */     this.mStartedElement = true;
/* 420 */     this.mStartedNamespaceURI = namespaceURI;
/* 421 */     this.mStartedLocalName = localName;
/* 422 */     this.mStartedQName = qName;
/* 423 */     this.mStartedHasAtts = (atts.getLength() > 0);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */   {
/* 443 */     if ((this.mStartedElement) && (!this.mXhtmlMode)) {
/* 444 */       this.mStartedElement = false;
/* 445 */       this.mPrinter.print("/>");
/* 446 */     } else if ((this.mStartedElement) && (!this.mStartedHasAtts)) {
/* 447 */       this.mStartedElement = false;
/* 448 */       this.mPrinter.print(" />");
/*     */     } else {
/* 450 */       if (this.mStartedElement) {
/* 451 */         this.mPrinter.print(">");
/* 452 */         this.mStartedElement = false;
/*     */       }
/* 454 */       this.mPrinter.print('<');
/* 455 */       this.mPrinter.print('/');
/* 456 */       this.mPrinter.print(qName);
/* 457 */       this.mPrinter.print('>');
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */   {
/* 470 */     if (this.mStartedElement) {
/* 471 */       this.mPrinter.print('>');
/* 472 */       this.mStartedElement = false;
/*     */     }
/* 474 */     printCharacters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */   {
/* 500 */     if (this.mStartedElement) {
/* 501 */       this.mPrinter.print('>');
/* 502 */       this.mStartedElement = false;
/*     */     }
/* 504 */     this.mPrinter.print("<?");
/* 505 */     this.mPrinter.print(target);
/* 506 */     if ((data != null) && (data.length() > 0)) {
/* 507 */       this.mPrinter.print(' ');
/* 508 */       this.mPrinter.print(data);
/*     */     }
/* 510 */     this.mPrinter.print("?>");
/*     */   }
/*     */ 
/*     */   public void comment(char[] cs, int start, int length)
/*     */   {
/* 529 */     comment(new String(cs, start, length));
/*     */   }
/*     */ 
/*     */   public void comment(String comment)
/*     */   {
/* 555 */     this.mPrinter.print(START_COMMENT);
/* 556 */     String noDoubleHyphenComment = comment.replaceAll("--", "- -");
/* 557 */     if (noDoubleHyphenComment.startsWith("-"))
/* 558 */       this.mPrinter.print(" ");
/* 559 */     this.mPrinter.print(noDoubleHyphenComment);
/* 560 */     if (noDoubleHyphenComment.endsWith("-"))
/* 561 */       this.mPrinter.print(" ");
/* 562 */     this.mPrinter.print(END_COMMENT);
/*     */   }
/*     */ 
/*     */   public String charsetName()
/*     */   {
/* 573 */     return this.mCharsetName;
/*     */   }
/*     */ 
/*     */   private void printAttributes(Attributes atts)
/*     */   {
/* 578 */     if (this.mPrefixMap.size() > 0) {
/* 579 */       for (Map.Entry entry : this.mPrefixMap.entrySet()) {
/* 580 */         String key = (String)entry.getKey();
/* 581 */         String value = (String)entry.getValue();
/* 582 */         printAttribute("xmlns:" + key, value);
/*     */       }
/*     */ 
/* 587 */       this.mPrefixMap.clear();
/*     */     }
/* 589 */     Set orderedAtts = new TreeSet();
/* 590 */     for (int i = 0; i < atts.getLength(); i++)
/* 591 */       orderedAtts.add(atts.getQName(i));
/* 592 */     for (String attQName : orderedAtts)
/* 593 */       printAttribute(attQName, atts.getValue(attQName));
/*     */   }
/*     */ 
/*     */   private void printAttribute(String att, String val)
/*     */   {
/* 605 */     this.mPrinter.print(' ');
/* 606 */     this.mPrinter.print(att);
/* 607 */     this.mPrinter.print('=');
/* 608 */     this.mPrinter.print('"');
/* 609 */     printCharacters(val);
/* 610 */     this.mPrinter.print('"');
/*     */   }
/*     */ 
/*     */   private void printCharacters(String s)
/*     */   {
/* 619 */     printCharacters(s.toCharArray(), 0, s.length());
/*     */   }
/*     */ 
/*     */   private void printCharacters(char[] ch, int start, int length)
/*     */   {
/* 630 */     for (int i = start; i < start + length; i++)
/* 631 */       printCharacter(ch[i]);
/*     */   }
/*     */ 
/*     */   private void printCharacter(char c)
/*     */   {
/* 642 */     if (!this.mCharsetEncoder.canEncode(c)) {
/* 643 */       printEntity("#x" + Integer.toHexString(c));
/* 644 */       return;
/*     */     }
/* 646 */     switch (c) { case '<':
/* 647 */       printEntity("lt"); break;
/*     */     case '>':
/* 648 */       printEntity("gt"); break;
/*     */     case '&':
/* 649 */       printEntity("amp"); break;
/*     */     case '"':
/* 650 */       printEntity("quot"); break;
/*     */     default:
/* 651 */       this.mPrinter.print(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printEntity(String entity)
/*     */   {
/* 661 */     this.mPrinter.print('&');
/* 662 */     this.mPrinter.print(entity);
/* 663 */     this.mPrinter.print(';');
/*     */   }
/*     */ 
/*     */   private void printXMLDeclaration()
/*     */   {
/* 678 */     this.mPrinter.print("<?xml");
/* 679 */     printAttribute("version", "1.0");
/* 680 */     printAttribute("encoding", this.mCharsetName);
/* 681 */     this.mPrinter.print("?>");
/* 682 */     if (this.mDtdString != null)
/* 683 */       this.mPrinter.print(this.mDtdString);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.SAXWriter
 * JD-Core Version:    0.6.2
 */