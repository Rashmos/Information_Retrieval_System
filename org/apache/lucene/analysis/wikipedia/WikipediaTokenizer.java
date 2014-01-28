/*     */ package org.apache.lucene.analysis.wikipedia;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ import org.apache.lucene.util.AttributeSource.State;
/*     */ 
/*     */ public final class WikipediaTokenizer extends Tokenizer
/*     */ {
/*     */   public static final String INTERNAL_LINK = "il";
/*     */   public static final String EXTERNAL_LINK = "el";
/*     */   public static final String EXTERNAL_LINK_URL = "elu";
/*     */   public static final String CITATION = "ci";
/*     */   public static final String CATEGORY = "c";
/*     */   public static final String BOLD = "b";
/*     */   public static final String ITALICS = "i";
/*     */   public static final String BOLD_ITALICS = "bi";
/*     */   public static final String HEADING = "h";
/*     */   public static final String SUB_HEADING = "sh";
/*     */   public static final int ALPHANUM_ID = 0;
/*     */   public static final int APOSTROPHE_ID = 1;
/*     */   public static final int ACRONYM_ID = 2;
/*     */   public static final int COMPANY_ID = 3;
/*     */   public static final int EMAIL_ID = 4;
/*     */   public static final int HOST_ID = 5;
/*     */   public static final int NUM_ID = 6;
/*     */   public static final int CJ_ID = 7;
/*     */   public static final int INTERNAL_LINK_ID = 8;
/*     */   public static final int EXTERNAL_LINK_ID = 9;
/*     */   public static final int CITATION_ID = 10;
/*     */   public static final int CATEGORY_ID = 11;
/*     */   public static final int BOLD_ID = 12;
/*     */   public static final int ITALICS_ID = 13;
/*     */   public static final int BOLD_ITALICS_ID = 14;
/*     */   public static final int HEADING_ID = 15;
/*     */   public static final int SUB_HEADING_ID = 16;
/*     */   public static final int EXTERNAL_LINK_URL_ID = 17;
/*  73 */   public static final String[] TOKEN_TYPES = { "<ALPHANUM>", "<APOSTROPHE>", "<ACRONYM>", "<COMPANY>", "<EMAIL>", "<HOST>", "<NUM>", "<CJ>", "il", "el", "ci", "c", "b", "i", "bi", "h", "sh", "elu" };
/*     */   public static final int TOKENS_ONLY = 0;
/*     */   public static final int UNTOKENIZED_ONLY = 1;
/*     */   public static final int BOTH = 2;
/*     */   public static final int UNTOKENIZED_TOKEN_FLAG = 1;
/*     */   private final WikipediaTokenizerImpl scanner;
/* 115 */   private int tokenOutput = 0;
/* 116 */   private Set<String> untokenizedTypes = Collections.emptySet();
/* 117 */   private Iterator<AttributeSource.State> tokens = null;
/*     */ 
/* 119 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/* 120 */   private final TypeAttribute typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
/* 121 */   private final PositionIncrementAttribute posIncrAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/* 122 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 123 */   private final FlagsAttribute flagsAtt = (FlagsAttribute)addAttribute(FlagsAttribute.class);
/*     */ 
/*     */   public WikipediaTokenizer(Reader input)
/*     */   {
/* 132 */     this(input, 0, Collections.emptySet());
/*     */   }
/*     */ 
/*     */   public WikipediaTokenizer(Reader input, int tokenOutput, Set<String> untokenizedTypes)
/*     */   {
/* 144 */     super(input);
/* 145 */     this.scanner = new WikipediaTokenizerImpl(input);
/* 146 */     init(tokenOutput, untokenizedTypes);
/*     */   }
/*     */ 
/*     */   public WikipediaTokenizer(AttributeSource.AttributeFactory factory, Reader input, int tokenOutput, Set<String> untokenizedTypes)
/*     */   {
/* 158 */     super(factory, input);
/* 159 */     this.scanner = new WikipediaTokenizerImpl(input);
/* 160 */     init(tokenOutput, untokenizedTypes);
/*     */   }
/*     */ 
/*     */   public WikipediaTokenizer(AttributeSource source, Reader input, int tokenOutput, Set<String> untokenizedTypes)
/*     */   {
/* 172 */     super(source, input);
/* 173 */     this.scanner = new WikipediaTokenizerImpl(input);
/* 174 */     init(tokenOutput, untokenizedTypes);
/*     */   }
/*     */ 
/*     */   private void init(int tokenOutput, Set<String> untokenizedTypes) {
/* 178 */     this.tokenOutput = tokenOutput;
/* 179 */     this.untokenizedTypes = untokenizedTypes;
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken()
/*     */     throws IOException
/*     */   {
/* 189 */     if ((this.tokens != null) && (this.tokens.hasNext())) {
/* 190 */       AttributeSource.State state = (AttributeSource.State)this.tokens.next();
/* 191 */       restoreState(state);
/* 192 */       return true;
/*     */     }
/* 194 */     clearAttributes();
/* 195 */     int tokenType = this.scanner.getNextToken();
/*     */ 
/* 197 */     if (tokenType == -1) {
/* 198 */       return false;
/*     */     }
/* 200 */     String type = WikipediaTokenizerImpl.TOKEN_TYPES[tokenType];
/* 201 */     if ((this.tokenOutput == 0) || (!this.untokenizedTypes.contains(type)))
/* 202 */       setupToken();
/* 203 */     else if ((this.tokenOutput == 1) && (this.untokenizedTypes.contains(type) == true)) {
/* 204 */       collapseTokens(tokenType);
/*     */     }
/* 207 */     else if (this.tokenOutput == 2)
/*     */     {
/* 210 */       collapseAndSaveTokens(tokenType, type);
/*     */     }
/* 212 */     this.posIncrAtt.setPositionIncrement(this.scanner.getPositionIncrement());
/* 213 */     this.typeAtt.setType(type);
/* 214 */     return true;
/*     */   }
/*     */ 
/*     */   private void collapseAndSaveTokens(int tokenType, String type) throws IOException
/*     */   {
/* 219 */     StringBuilder buffer = new StringBuilder(32);
/* 220 */     int numAdded = this.scanner.setText(buffer);
/*     */ 
/* 222 */     int theStart = this.scanner.yychar();
/* 223 */     int lastPos = theStart + numAdded;
/*     */ 
/* 225 */     int numSeen = 0;
/* 226 */     List tmp = new ArrayList();
/* 227 */     setupSavedToken(0, type);
/* 228 */     tmp.add(captureState());
/*     */     int tmpTokType;
/* 230 */     while (((tmpTokType = this.scanner.getNextToken()) != -1) && (tmpTokType == tokenType) && (this.scanner.getNumWikiTokensSeen() > numSeen)) {
/* 231 */       int currPos = this.scanner.yychar();
/*     */ 
/* 233 */       for (int i = 0; i < currPos - lastPos; i++) {
/* 234 */         buffer.append(' ');
/*     */       }
/* 236 */       numAdded = this.scanner.setText(buffer);
/* 237 */       setupSavedToken(this.scanner.getPositionIncrement(), type);
/* 238 */       tmp.add(captureState());
/* 239 */       numSeen++;
/* 240 */       lastPos = currPos + numAdded;
/*     */     }
/*     */ 
/* 244 */     String s = buffer.toString().trim();
/* 245 */     this.termAtt.setEmpty().append(s);
/* 246 */     this.offsetAtt.setOffset(correctOffset(theStart), correctOffset(theStart + s.length()));
/* 247 */     this.flagsAtt.setFlags(1);
/*     */ 
/* 249 */     if (tmpTokType != -1) {
/* 250 */       this.scanner.yypushback(this.scanner.yylength());
/*     */     }
/* 252 */     this.tokens = tmp.iterator();
/*     */   }
/*     */ 
/*     */   private void setupSavedToken(int positionInc, String type) {
/* 256 */     setupToken();
/* 257 */     this.posIncrAtt.setPositionIncrement(positionInc);
/* 258 */     this.typeAtt.setType(type);
/*     */   }
/*     */ 
/*     */   private void collapseTokens(int tokenType) throws IOException
/*     */   {
/* 263 */     StringBuilder buffer = new StringBuilder(32);
/* 264 */     int numAdded = this.scanner.setText(buffer);
/*     */ 
/* 266 */     int theStart = this.scanner.yychar();
/* 267 */     int lastPos = theStart + numAdded;
/*     */ 
/* 269 */     int numSeen = 0;
/*     */     int tmpTokType;
/* 271 */     while (((tmpTokType = this.scanner.getNextToken()) != -1) && (tmpTokType == tokenType) && (this.scanner.getNumWikiTokensSeen() > numSeen)) {
/* 272 */       int currPos = this.scanner.yychar();
/*     */ 
/* 274 */       for (int i = 0; i < currPos - lastPos; i++) {
/* 275 */         buffer.append(' ');
/*     */       }
/* 277 */       numAdded = this.scanner.setText(buffer);
/* 278 */       numSeen++;
/* 279 */       lastPos = currPos + numAdded;
/*     */     }
/*     */ 
/* 283 */     String s = buffer.toString().trim();
/* 284 */     this.termAtt.setEmpty().append(s);
/* 285 */     this.offsetAtt.setOffset(correctOffset(theStart), correctOffset(theStart + s.length()));
/* 286 */     this.flagsAtt.setFlags(1);
/*     */ 
/* 288 */     if (tmpTokType != -1)
/* 289 */       this.scanner.yypushback(this.scanner.yylength());
/*     */     else
/* 291 */       this.tokens = null;
/*     */   }
/*     */ 
/*     */   private void setupToken()
/*     */   {
/* 296 */     this.scanner.getText(this.termAtt);
/* 297 */     int start = this.scanner.yychar();
/* 298 */     this.offsetAtt.setOffset(correctOffset(start), correctOffset(start + this.termAtt.length()));
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 308 */     super.reset();
/* 309 */     this.scanner.yyreset(this.input);
/*     */   }
/*     */ 
/*     */   public void reset(Reader reader) throws IOException
/*     */   {
/* 314 */     super.reset(reader);
/* 315 */     reset();
/*     */   }
/*     */ 
/*     */   public void end()
/*     */     throws IOException
/*     */   {
/* 321 */     int finalOffset = correctOffset(this.scanner.yychar() + this.scanner.yylength());
/* 322 */     this.offsetAtt.setOffset(finalOffset, finalOffset);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.wikipedia.WikipediaTokenizer
 * JD-Core Version:    0.6.2
 */