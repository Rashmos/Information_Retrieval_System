/*     */ package org.apache.lucene.analysis.synonym;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.Reader;
/*     */ import java.text.ParseException;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.util.CharsRef;
/*     */ 
/*     */ public class WordnetSynonymParser extends SynonymMap.Builder
/*     */ {
/*     */   private final boolean expand;
/*     */   private final Analyzer analyzer;
/*     */ 
/*     */   public WordnetSynonymParser(boolean dedup, boolean expand, Analyzer analyzer)
/*     */   {
/*  40 */     super(dedup);
/*  41 */     this.expand = expand;
/*  42 */     this.analyzer = analyzer;
/*     */   }
/*     */ 
/*     */   public void add(Reader in) throws IOException, ParseException {
/*  46 */     LineNumberReader br = new LineNumberReader(in);
/*     */     try {
/*  48 */       String line = null;
/*  49 */       String lastSynSetID = "";
/*  50 */       CharsRef[] synset = new CharsRef[8];
/*  51 */       int synsetSize = 0;
/*     */ 
/*  53 */       while ((line = br.readLine()) != null) {
/*  54 */         String synSetID = line.substring(2, 11);
/*     */ 
/*  56 */         if (!synSetID.equals(lastSynSetID)) {
/*  57 */           addInternal(synset, synsetSize);
/*  58 */           synsetSize = 0;
/*     */         }
/*     */ 
/*  61 */         if (synset.length <= synsetSize + 1) {
/*  62 */           CharsRef[] larger = new CharsRef[synset.length * 2];
/*  63 */           System.arraycopy(synset, 0, larger, 0, synsetSize);
/*  64 */           synset = larger;
/*     */         }
/*     */ 
/*  67 */         synset[synsetSize] = parseSynonym(line, synset[synsetSize]);
/*  68 */         synsetSize++;
/*  69 */         lastSynSetID = synSetID;
/*     */       }
/*     */ 
/*  73 */       addInternal(synset, synsetSize);
/*     */     } catch (IllegalArgumentException e) {
/*  75 */       ParseException ex = new ParseException("Invalid synonym rule at line " + br.getLineNumber(), 0);
/*  76 */       ex.initCause(e);
/*  77 */       throw ex;
/*     */     } finally {
/*  79 */       br.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private CharsRef parseSynonym(String line, CharsRef reuse) throws IOException {
/*  84 */     if (reuse == null) {
/*  85 */       reuse = new CharsRef(8);
/*     */     }
/*     */ 
/*  88 */     int start = line.indexOf('\'') + 1;
/*  89 */     int end = line.lastIndexOf('\'');
/*     */ 
/*  91 */     String text = line.substring(start, end).replace("''", "'");
/*  92 */     return analyze(this.analyzer, text, reuse);
/*     */   }
/*     */ 
/*     */   private void addInternal(CharsRef[] synset, int size) throws IOException {
/*  96 */     if (size <= 1) {
/*  97 */       return;
/*     */     }
/*     */ 
/* 100 */     if (this.expand) {
/* 101 */       for (int i = 0; i < size; i++) {
/* 102 */         for (int j = 0; j < size; j++)
/* 103 */           add(synset[i], synset[j], false);
/*     */       }
/*     */     }
/*     */     else
/* 107 */       for (int i = 0; i < size; i++)
/* 108 */         add(synset[i], synset[0], false);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.synonym.WordnetSynonymParser
 * JD-Core Version:    0.6.2
 */