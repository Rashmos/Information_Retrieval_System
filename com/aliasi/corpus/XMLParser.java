/*    */ package com.aliasi.corpus;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ import org.xml.sax.helpers.XMLReaderFactory;
/*    */ 
/*    */ public abstract class XMLParser<H extends Handler> extends InputSourceParser<H>
/*    */ {
/*    */   static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
/*    */ 
/*    */   public XMLParser()
/*    */   {
/*    */   }
/*    */ 
/*    */   public XMLParser(H handler)
/*    */   {
/* 59 */     super(handler);
/*    */   }
/*    */ 
/*    */   protected abstract DefaultHandler getXMLHandler();
/*    */ 
/*    */   public void parse(InputSource inSource)
/*    */     throws IOException
/*    */   {
/*    */     try
/*    */     {
/* 95 */       XMLReader xmlReader = XMLReaderFactory.createXMLReader();
/* 96 */       xmlReader.setFeature("http://xml.org/sax/features/validation", false);
/* 97 */       DefaultHandler xmlHandler = getXMLHandler();
/* 98 */       xmlReader.setContentHandler(xmlHandler);
/* 99 */       xmlReader.setDTDHandler(xmlHandler);
/* 100 */       xmlReader.setEntityResolver(xmlHandler);
/* 101 */       xmlReader.parse(inSource);
/*    */     } catch (SAXException e) {
/* 103 */       throw new IOException("SAXException=" + e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.XMLParser
 * JD-Core Version:    0.6.2
 */