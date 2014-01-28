/*     */ package org.apache.lucene.analysis.compound.hyphenation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class PatternParser extends DefaultHandler
/*     */   implements PatternConsumer
/*     */ {
/*     */   XMLReader parser;
/*     */   int currElement;
/*     */   PatternConsumer consumer;
/*     */   StringBuilder token;
/*     */   ArrayList<Object> exception;
/*     */   char hyphenChar;
/*     */   String errMsg;
/*     */   static final int ELEM_CLASSES = 1;
/*     */   static final int ELEM_EXCEPTIONS = 2;
/*     */   static final int ELEM_PATTERNS = 3;
/*     */   static final int ELEM_HYPHEN = 4;
/*     */ 
/*     */   public PatternParser()
/*     */     throws HyphenationException
/*     */   {
/*  68 */     this.token = new StringBuilder();
/*  69 */     this.parser = createParser();
/*  70 */     this.parser.setContentHandler(this);
/*  71 */     this.parser.setErrorHandler(this);
/*  72 */     this.parser.setEntityResolver(this);
/*  73 */     this.hyphenChar = '-';
/*     */   }
/*     */ 
/*     */   public PatternParser(PatternConsumer consumer) throws HyphenationException
/*     */   {
/*  78 */     this();
/*  79 */     this.consumer = consumer;
/*     */   }
/*     */ 
/*     */   public void setConsumer(PatternConsumer consumer) {
/*  83 */     this.consumer = consumer;
/*     */   }
/*     */ 
/*     */   public void parse(String filename)
/*     */     throws HyphenationException
/*     */   {
/*  93 */     parse(new InputSource(filename));
/*     */   }
/*     */ 
/*     */   public void parse(File file)
/*     */     throws HyphenationException
/*     */   {
/*     */     try
/*     */     {
/* 104 */       InputSource src = new InputSource(file.toURL().toExternalForm());
/* 105 */       parse(src);
/*     */     } catch (MalformedURLException e) {
/* 107 */       throw new HyphenationException("Error converting the File '" + file + "' to a URL: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parse(InputSource source)
/*     */     throws HyphenationException
/*     */   {
/*     */     try
/*     */     {
/* 120 */       this.parser.parse(source);
/*     */     } catch (FileNotFoundException fnfe) {
/* 122 */       throw new HyphenationException("File not found: " + fnfe.getMessage());
/*     */     } catch (IOException ioe) {
/* 124 */       throw new HyphenationException(ioe.getMessage());
/*     */     } catch (SAXException e) {
/* 126 */       throw new HyphenationException(this.errMsg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static XMLReader createParser()
/*     */   {
/*     */     try
/*     */     {
/* 137 */       SAXParserFactory factory = SAXParserFactory.newInstance();
/* 138 */       factory.setNamespaceAware(true);
/* 139 */       return factory.newSAXParser().getXMLReader();
/*     */     } catch (Exception e) {
/* 141 */       throw new RuntimeException("Couldn't create XMLReader: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String readToken(StringBuffer chars)
/*     */   {
/* 147 */     boolean space = false;
/*     */ 
/* 149 */     for (int i = 0; (i < chars.length()) && 
/* 150 */       (Character.isWhitespace(chars.charAt(i))); i++)
/*     */     {
/* 151 */       space = true;
/*     */     }
/*     */ 
/* 156 */     if (space)
/*     */     {
/* 158 */       for (int countr = i; countr < chars.length(); countr++) {
/* 159 */         chars.setCharAt(countr - i, chars.charAt(countr));
/*     */       }
/* 161 */       chars.setLength(chars.length() - i);
/* 162 */       if (this.token.length() > 0) {
/* 163 */         String word = this.token.toString();
/* 164 */         this.token.setLength(0);
/* 165 */         return word;
/*     */       }
/*     */     }
/* 168 */     space = false;
/* 169 */     for (i = 0; i < chars.length(); i++) {
/* 170 */       if (Character.isWhitespace(chars.charAt(i))) {
/* 171 */         space = true;
/* 172 */         break;
/*     */       }
/*     */     }
/* 175 */     this.token.append(chars.toString().substring(0, i));
/*     */ 
/* 177 */     for (int countr = i; countr < chars.length(); countr++) {
/* 178 */       chars.setCharAt(countr - i, chars.charAt(countr));
/*     */     }
/* 180 */     chars.setLength(chars.length() - i);
/* 181 */     if (space) {
/* 182 */       String word = this.token.toString();
/* 183 */       this.token.setLength(0);
/* 184 */       return word;
/*     */     }
/* 186 */     this.token.append(chars);
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */   protected static String getPattern(String word) {
/* 191 */     StringBuilder pat = new StringBuilder();
/* 192 */     int len = word.length();
/* 193 */     for (int i = 0; i < len; i++) {
/* 194 */       if (!Character.isDigit(word.charAt(i))) {
/* 195 */         pat.append(word.charAt(i));
/*     */       }
/*     */     }
/* 198 */     return pat.toString();
/*     */   }
/*     */ 
/*     */   protected ArrayList<Object> normalizeException(ArrayList<?> ex) {
/* 202 */     ArrayList res = new ArrayList();
/* 203 */     for (int i = 0; i < ex.size(); i++) {
/* 204 */       Object item = ex.get(i);
/* 205 */       if ((item instanceof String)) {
/* 206 */         String str = (String)item;
/* 207 */         StringBuilder buf = new StringBuilder();
/* 208 */         for (int j = 0; j < str.length(); j++) {
/* 209 */           char c = str.charAt(j);
/* 210 */           if (c != this.hyphenChar) {
/* 211 */             buf.append(c);
/*     */           } else {
/* 213 */             res.add(buf.toString());
/* 214 */             buf.setLength(0);
/* 215 */             char[] h = new char[1];
/* 216 */             h[0] = this.hyphenChar;
/*     */ 
/* 219 */             res.add(new Hyphen(new String(h), null, null));
/*     */           }
/*     */         }
/* 222 */         if (buf.length() > 0)
/* 223 */           res.add(buf.toString());
/*     */       }
/*     */       else {
/* 226 */         res.add(item);
/*     */       }
/*     */     }
/* 229 */     return res;
/*     */   }
/*     */ 
/*     */   protected String getExceptionWord(ArrayList<?> ex) {
/* 233 */     StringBuilder res = new StringBuilder();
/* 234 */     for (int i = 0; i < ex.size(); i++) {
/* 235 */       Object item = ex.get(i);
/* 236 */       if ((item instanceof String)) {
/* 237 */         res.append((String)item);
/*     */       }
/* 239 */       else if (((Hyphen)item).noBreak != null) {
/* 240 */         res.append(((Hyphen)item).noBreak);
/*     */       }
/*     */     }
/*     */ 
/* 244 */     return res.toString();
/*     */   }
/*     */ 
/*     */   protected static String getInterletterValues(String pat) {
/* 248 */     StringBuilder il = new StringBuilder();
/* 249 */     String word = pat + "a";
/* 250 */     int len = word.length();
/* 251 */     for (int i = 0; i < len; i++) {
/* 252 */       char c = word.charAt(i);
/* 253 */       if (Character.isDigit(c)) {
/* 254 */         il.append(c);
/* 255 */         i++;
/*     */       } else {
/* 257 */         il.append('0');
/*     */       }
/*     */     }
/* 260 */     return il.toString();
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */   {
/* 269 */     if (((systemId != null) && (systemId.matches("(?i).*\\bhyphenation.dtd\\b.*"))) || ("hyphenation-info".equals(publicId)))
/*     */     {
/* 274 */       return new InputSource(getClass().getResource("hyphenation.dtd").toExternalForm());
/*     */     }
/* 276 */     return null;
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String local, String raw, Attributes attrs)
/*     */   {
/* 290 */     if (local.equals("hyphen-char")) {
/* 291 */       String h = attrs.getValue("value");
/* 292 */       if ((h != null) && (h.length() == 1))
/* 293 */         this.hyphenChar = h.charAt(0);
/*     */     }
/* 295 */     else if (local.equals("classes")) {
/* 296 */       this.currElement = 1;
/* 297 */     } else if (local.equals("patterns")) {
/* 298 */       this.currElement = 3;
/* 299 */     } else if (local.equals("exceptions")) {
/* 300 */       this.currElement = 2;
/* 301 */       this.exception = new ArrayList();
/* 302 */     } else if (local.equals("hyphen")) {
/* 303 */       if (this.token.length() > 0) {
/* 304 */         this.exception.add(this.token.toString());
/*     */       }
/* 306 */       this.exception.add(new Hyphen(attrs.getValue("pre"), attrs.getValue("no"), attrs.getValue("post")));
/*     */ 
/* 308 */       this.currElement = 4;
/*     */     }
/* 310 */     this.token.setLength(0);
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String local, String raw)
/*     */   {
/* 321 */     if (this.token.length() > 0) {
/* 322 */       String word = this.token.toString();
/* 323 */       switch (this.currElement) {
/*     */       case 1:
/* 325 */         this.consumer.addClass(word);
/* 326 */         break;
/*     */       case 2:
/* 328 */         this.exception.add(word);
/* 329 */         this.exception = normalizeException(this.exception);
/* 330 */         this.consumer.addException(getExceptionWord(this.exception), (ArrayList)this.exception.clone());
/*     */ 
/* 332 */         break;
/*     */       case 3:
/* 334 */         this.consumer.addPattern(getPattern(word), getInterletterValues(word));
/* 335 */         break;
/*     */       case 4:
/*     */       }
/*     */ 
/* 340 */       if (this.currElement != 4) {
/* 341 */         this.token.setLength(0);
/*     */       }
/*     */     }
/* 344 */     if (this.currElement == 4)
/* 345 */       this.currElement = 2;
/*     */     else
/* 347 */       this.currElement = 0;
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */   {
/* 358 */     StringBuffer chars = new StringBuffer(length);
/* 359 */     chars.append(ch, start, length);
/* 360 */     String word = readToken(chars);
/* 361 */     while (word != null)
/*     */     {
/* 363 */       switch (this.currElement) {
/*     */       case 1:
/* 365 */         this.consumer.addClass(word);
/* 366 */         break;
/*     */       case 2:
/* 368 */         this.exception.add(word);
/* 369 */         this.exception = normalizeException(this.exception);
/* 370 */         this.consumer.addException(getExceptionWord(this.exception), (ArrayList)this.exception.clone());
/*     */ 
/* 372 */         this.exception.clear();
/* 373 */         break;
/*     */       case 3:
/* 375 */         this.consumer.addPattern(getPattern(word), getInterletterValues(word));
/*     */       }
/*     */ 
/* 378 */       word = readToken(chars);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getLocationString(SAXParseException ex)
/*     */   {
/* 387 */     StringBuilder str = new StringBuilder();
/*     */ 
/* 389 */     String systemId = ex.getSystemId();
/* 390 */     if (systemId != null) {
/* 391 */       int index = systemId.lastIndexOf('/');
/* 392 */       if (index != -1) {
/* 393 */         systemId = systemId.substring(index + 1);
/*     */       }
/* 395 */       str.append(systemId);
/*     */     }
/* 397 */     str.append(':');
/* 398 */     str.append(ex.getLineNumber());
/* 399 */     str.append(':');
/* 400 */     str.append(ex.getColumnNumber());
/*     */ 
/* 402 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public void addClass(String c)
/*     */   {
/* 408 */     System.out.println("class: " + c);
/*     */   }
/*     */ 
/*     */   public void addException(String w, ArrayList<Object> e) {
/* 412 */     System.out.println("exception: " + w + " : " + e.toString());
/*     */   }
/*     */ 
/*     */   public void addPattern(String p, String v) {
/* 416 */     System.out.println("pattern: " + p + " : " + v);
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) throws Exception {
/* 420 */     if (args.length > 0) {
/* 421 */       PatternParser pp = new PatternParser();
/* 422 */       pp.setConsumer(pp);
/* 423 */       pp.parse(args[0]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.PatternParser
 * JD-Core Version:    0.6.2
 */