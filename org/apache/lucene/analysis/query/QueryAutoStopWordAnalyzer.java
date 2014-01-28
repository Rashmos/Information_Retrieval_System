/*     */ package org.apache.lucene.analysis.query;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.IndexReader.FieldOption;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.index.TermEnum;
/*     */ import org.apache.lucene.util.StringHelper;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class QueryAutoStopWordAnalyzer extends Analyzer
/*     */ {
/*     */   private final Analyzer delegate;
/*  49 */   private final Map<String, Set<String>> stopWordsPerField = new HashMap();
/*     */   public static final float defaultMaxDocFreqPercent = 0.4F;
/*     */   private final Version matchVersion;
/*     */ 
/*     */   @Deprecated
/*     */   public QueryAutoStopWordAnalyzer(Version matchVersion, Analyzer delegate)
/*     */   {
/*  63 */     this.delegate = delegate;
/*  64 */     this.matchVersion = matchVersion;
/*     */   }
/*     */ 
/*     */   public QueryAutoStopWordAnalyzer(Version matchVersion, Analyzer delegate, IndexReader indexReader)
/*     */     throws IOException
/*     */   {
/*  81 */     this(matchVersion, delegate, indexReader, 0.4F);
/*     */   }
/*     */ 
/*     */   public QueryAutoStopWordAnalyzer(Version matchVersion, Analyzer delegate, IndexReader indexReader, int maxDocFreq)
/*     */     throws IOException
/*     */   {
/* 100 */     this(matchVersion, delegate, indexReader, indexReader.getFieldNames(IndexReader.FieldOption.INDEXED), maxDocFreq);
/*     */   }
/*     */ 
/*     */   public QueryAutoStopWordAnalyzer(Version matchVersion, Analyzer delegate, IndexReader indexReader, float maxPercentDocs)
/*     */     throws IOException
/*     */   {
/* 120 */     this(matchVersion, delegate, indexReader, indexReader.getFieldNames(IndexReader.FieldOption.INDEXED), maxPercentDocs);
/*     */   }
/*     */ 
/*     */   public QueryAutoStopWordAnalyzer(Version matchVersion, Analyzer delegate, IndexReader indexReader, Collection<String> fields, float maxPercentDocs)
/*     */     throws IOException
/*     */   {
/* 142 */     this(matchVersion, delegate, indexReader, fields, (int)(indexReader.numDocs() * maxPercentDocs));
/*     */   }
/*     */ 
/*     */   public QueryAutoStopWordAnalyzer(Version matchVersion, Analyzer delegate, IndexReader indexReader, Collection<String> fields, int maxDocFreq)
/*     */     throws IOException
/*     */   {
/* 163 */     this.matchVersion = matchVersion;
/* 164 */     this.delegate = delegate;
/*     */ 
/* 166 */     for (String field : fields) {
/* 167 */       Set stopWords = new HashSet();
/* 168 */       String internedFieldName = StringHelper.intern(field);
/* 169 */       TermEnum te = indexReader.terms(new Term(field));
/* 170 */       Term term = te.term();
/* 171 */       while ((term != null) && 
/* 172 */         (term.field() == internedFieldName))
/*     */       {
/* 175 */         if (te.docFreq() > maxDocFreq) {
/* 176 */           stopWords.add(term.text());
/*     */         }
/* 178 */         if (!te.next()) {
/*     */           break;
/*     */         }
/* 181 */         term = te.term();
/*     */       }
/* 183 */       this.stopWordsPerField.put(field, stopWords);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int addStopWords(IndexReader reader)
/*     */     throws IOException
/*     */   {
/* 199 */     return addStopWords(reader, 0.4F);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int addStopWords(IndexReader reader, int maxDocFreq)
/*     */     throws IOException
/*     */   {
/* 216 */     int numStopWords = 0;
/* 217 */     Collection fieldNames = reader.getFieldNames(IndexReader.FieldOption.INDEXED);
/* 218 */     for (Iterator iter = fieldNames.iterator(); iter.hasNext(); ) {
/* 219 */       String fieldName = (String)iter.next();
/* 220 */       numStopWords += addStopWords(reader, fieldName, maxDocFreq);
/*     */     }
/* 222 */     return numStopWords;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int addStopWords(IndexReader reader, float maxPercentDocs)
/*     */     throws IOException
/*     */   {
/* 239 */     int numStopWords = 0;
/* 240 */     Collection fieldNames = reader.getFieldNames(IndexReader.FieldOption.INDEXED);
/* 241 */     for (Iterator iter = fieldNames.iterator(); iter.hasNext(); ) {
/* 242 */       String fieldName = (String)iter.next();
/* 243 */       numStopWords += addStopWords(reader, fieldName, maxPercentDocs);
/*     */     }
/* 245 */     return numStopWords;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int addStopWords(IndexReader reader, String fieldName, float maxPercentDocs)
/*     */     throws IOException
/*     */   {
/* 263 */     return addStopWords(reader, fieldName, (int)(reader.numDocs() * maxPercentDocs));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int addStopWords(IndexReader reader, String fieldName, int maxDocFreq)
/*     */     throws IOException
/*     */   {
/* 281 */     HashSet stopWords = new HashSet();
/* 282 */     String internedFieldName = StringHelper.intern(fieldName);
/* 283 */     TermEnum te = reader.terms(new Term(fieldName));
/* 284 */     Term term = te.term();
/* 285 */     while ((term != null) && 
/* 286 */       (term.field() == internedFieldName))
/*     */     {
/* 289 */       if (te.docFreq() > maxDocFreq) {
/* 290 */         stopWords.add(term.text());
/*     */       }
/* 292 */       if (!te.next()) {
/*     */         break;
/*     */       }
/* 295 */       term = te.term();
/*     */     }
/* 297 */     this.stopWordsPerField.put(fieldName, stopWords);
/*     */ 
/* 303 */     Map streamMap = (Map)getPreviousTokenStream();
/* 304 */     if (streamMap != null) {
/* 305 */       streamMap.remove(fieldName);
/*     */     }
/* 307 */     return stopWords.size();
/*     */   }
/*     */ 
/*     */   public TokenStream tokenStream(String fieldName, Reader reader)
/*     */   {
/*     */     TokenStream result;
/*     */     try {
/* 314 */       result = this.delegate.reusableTokenStream(fieldName, reader);
/*     */     } catch (IOException e) {
/* 316 */       result = this.delegate.tokenStream(fieldName, reader);
/*     */     }
/* 318 */     Set stopWords = (Set)this.stopWordsPerField.get(fieldName);
/* 319 */     if (stopWords != null) {
/* 320 */       result = new StopFilter(this.matchVersion, result, stopWords);
/*     */     }
/* 322 */     return result;
/*     */   }
/*     */ 
/*     */   public TokenStream reusableTokenStream(String fieldName, Reader reader)
/*     */     throws IOException
/*     */   {
/* 341 */     Map streamMap = (Map)getPreviousTokenStream();
/* 342 */     if (streamMap == null) {
/* 343 */       streamMap = new HashMap();
/* 344 */       setPreviousTokenStream(streamMap);
/*     */     }
/*     */ 
/* 347 */     SavedStreams streams = (SavedStreams)streamMap.get(fieldName);
/* 348 */     if (streams == null)
/*     */     {
/* 350 */       streams = new SavedStreams(null);
/* 351 */       streamMap.put(fieldName, streams);
/* 352 */       streams.wrapped = this.delegate.reusableTokenStream(fieldName, reader);
/*     */ 
/* 355 */       Set stopWords = (Set)this.stopWordsPerField.get(fieldName);
/* 356 */       if (stopWords != null)
/* 357 */         streams.withStopFilter = new StopFilter(this.matchVersion, streams.wrapped, stopWords);
/*     */       else {
/* 359 */         streams.withStopFilter = streams.wrapped;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 367 */       TokenStream result = this.delegate.reusableTokenStream(fieldName, reader);
/* 368 */       if (result != streams.wrapped)
/*     */       {
/* 375 */         streams.wrapped = result;
/* 376 */         Set stopWords = (Set)this.stopWordsPerField.get(fieldName);
/* 377 */         if (stopWords != null)
/* 378 */           streams.withStopFilter = new StopFilter(this.matchVersion, streams.wrapped, stopWords);
/*     */         else {
/* 380 */           streams.withStopFilter = streams.wrapped;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 385 */     return streams.withStopFilter;
/*     */   }
/*     */ 
/*     */   public String[] getStopWords(String fieldName)
/*     */   {
/* 396 */     Set stopWords = (Set)this.stopWordsPerField.get(fieldName);
/* 397 */     return stopWords != null ? (String[])stopWords.toArray(new String[stopWords.size()]) : new String[0];
/*     */   }
/*     */ 
/*     */   public Term[] getStopWords()
/*     */   {
/* 406 */     List allStopWords = new ArrayList();
/* 407 */     for (Iterator i$ = this.stopWordsPerField.keySet().iterator(); i$.hasNext(); ) { fieldName = (String)i$.next();
/* 408 */       Set stopWords = (Set)this.stopWordsPerField.get(fieldName);
/* 409 */       for (String text : stopWords)
/* 410 */         allStopWords.add(new Term(fieldName, text));
/*     */     }
/*     */     String fieldName;
/* 413 */     return (Term[])allStopWords.toArray(new Term[allStopWords.size()]);
/*     */   }
/*     */ 
/*     */   private class SavedStreams
/*     */   {
/*     */     TokenStream wrapped;
/*     */     TokenStream withStopFilter;
/*     */ 
/*     */     private SavedStreams()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.query.QueryAutoStopWordAnalyzer
 * JD-Core Version:    0.6.2
 */