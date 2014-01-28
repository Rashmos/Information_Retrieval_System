/*     */ package org.apache.lucene.analysis.synonym;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.Reader;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.util.CharsRef;
/*     */ 
/*     */ public class SolrSynonymParser extends SynonymMap.Builder
/*     */ {
/*     */   private final boolean expand;
/*     */   private final Analyzer analyzer;
/*     */ 
/*     */   public SolrSynonymParser(boolean dedup, boolean expand, Analyzer analyzer)
/*     */   {
/*  62 */     super(dedup);
/*  63 */     this.expand = expand;
/*  64 */     this.analyzer = analyzer;
/*     */   }
/*     */ 
/*     */   public void add(Reader in) throws IOException, ParseException {
/*  68 */     LineNumberReader br = new LineNumberReader(in);
/*     */     try {
/*  70 */       addInternal(br);
/*     */     } catch (IllegalArgumentException e) {
/*  72 */       ParseException ex = new ParseException("Invalid synonym rule at line " + br.getLineNumber(), 0);
/*  73 */       ex.initCause(e);
/*  74 */       throw ex;
/*     */     } finally {
/*  76 */       br.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addInternal(BufferedReader in) throws IOException {
/*  81 */     String line = null;
/*  82 */     while ((line = in.readLine()) != null)
/*  83 */       if ((line.length() != 0) && (line.charAt(0) != '#'))
/*     */       {
/*  91 */         String[] sides = split(line, "=>");
/*     */         CharsRef[] inputs;
/*     */         CharsRef[] outputs;
/*  92 */         if (sides.length > 1) {
/*  93 */           if (sides.length != 2) {
/*  94 */             throw new IllegalArgumentException("more than one explicit mapping specified on the same line");
/*     */           }
/*  96 */           String[] inputStrings = split(sides[0], ",");
/*  97 */           CharsRef[] inputs = new CharsRef[inputStrings.length];
/*  98 */           for (int i = 0; i < inputs.length; i++) {
/*  99 */             inputs[i] = analyze(this.analyzer, unescape(inputStrings[i]).trim(), new CharsRef());
/*     */           }
/*     */ 
/* 102 */           String[] outputStrings = split(sides[1], ",");
/* 103 */           CharsRef[] outputs = new CharsRef[outputStrings.length];
/* 104 */           for (int i = 0; i < outputs.length; i++)
/* 105 */             outputs[i] = analyze(this.analyzer, unescape(outputStrings[i]).trim(), new CharsRef());
/*     */         }
/*     */         else {
/* 108 */           String[] inputStrings = split(line, ",");
/* 109 */           inputs = new CharsRef[inputStrings.length];
/* 110 */           for (int i = 0; i < inputs.length; i++)
/* 111 */             inputs[i] = analyze(this.analyzer, unescape(inputStrings[i]).trim(), new CharsRef());
/*     */           CharsRef[] outputs;
/* 113 */           if (this.expand) {
/* 114 */             outputs = inputs;
/*     */           } else {
/* 116 */             outputs = new CharsRef[1];
/* 117 */             outputs[0] = inputs[0];
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 125 */         for (int i = 0; i < inputs.length; i++)
/* 126 */           for (int j = 0; j < outputs.length; j++)
/* 127 */             add(inputs[i], outputs[j], false);
/*     */       }
/*     */   }
/*     */ 
/*     */   private static String[] split(String s, String separator)
/*     */   {
/* 134 */     ArrayList list = new ArrayList(2);
/* 135 */     StringBuilder sb = new StringBuilder();
/* 136 */     int pos = 0; int end = s.length();
/* 137 */     while (pos < end) {
/* 138 */       if (s.startsWith(separator, pos)) {
/* 139 */         if (sb.length() > 0) {
/* 140 */           list.add(sb.toString());
/* 141 */           sb = new StringBuilder();
/*     */         }
/* 143 */         pos += separator.length();
/*     */       }
/*     */       else
/*     */       {
/* 147 */         char ch = s.charAt(pos++);
/* 148 */         if (ch == '\\') {
/* 149 */           sb.append(ch);
/* 150 */           if (pos >= end) break;
/* 151 */           ch = s.charAt(pos++);
/*     */         }
/*     */ 
/* 154 */         sb.append(ch);
/*     */       }
/*     */     }
/* 157 */     if (sb.length() > 0) {
/* 158 */       list.add(sb.toString());
/*     */     }
/*     */ 
/* 161 */     return (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */ 
/*     */   private String unescape(String s) {
/* 165 */     if (s.indexOf("\\") >= 0) {
/* 166 */       StringBuilder sb = new StringBuilder();
/* 167 */       for (int i = 0; i < s.length(); i++) {
/* 168 */         char ch = s.charAt(i);
/* 169 */         if ((ch == '\\') && (i < s.length() - 1))
/* 170 */           sb.append(s.charAt(++i));
/*     */         else {
/* 172 */           sb.append(ch);
/*     */         }
/*     */       }
/* 175 */       return sb.toString();
/*     */     }
/* 177 */     return s;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.synonym.SolrSynonymParser
 * JD-Core Version:    0.6.2
 */