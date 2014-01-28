/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.document.Document;
/*     */ import org.apache.lucene.document.Fieldable;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ 
/*     */ final class DocInverterPerField extends DocFieldConsumerPerField
/*     */ {
/*     */   private final DocInverterPerThread perThread;
/*     */   private final FieldInfo fieldInfo;
/*     */   final InvertedDocConsumerPerField consumer;
/*     */   final InvertedDocEndConsumerPerField endConsumer;
/*     */   final DocumentsWriter.DocState docState;
/*     */   final FieldInvertState fieldState;
/*     */ 
/*     */   public DocInverterPerField(DocInverterPerThread perThread, FieldInfo fieldInfo)
/*     */   {
/*  46 */     this.perThread = perThread;
/*  47 */     this.fieldInfo = fieldInfo;
/*  48 */     this.docState = perThread.docState;
/*  49 */     this.fieldState = perThread.fieldState;
/*  50 */     this.consumer = perThread.consumer.addField(this, fieldInfo);
/*  51 */     this.endConsumer = perThread.endConsumer.addField(this, fieldInfo);
/*     */   }
/*     */ 
/*     */   void abort()
/*     */   {
/*  56 */     this.consumer.abort();
/*  57 */     this.endConsumer.abort();
/*     */   }
/*     */ 
/*     */   public void processFields(Fieldable[] fields, int count)
/*     */     throws IOException
/*     */   {
/*  64 */     this.fieldState.reset(this.docState.doc.getBoost());
/*     */ 
/*  66 */     int maxFieldLength = this.docState.maxFieldLength;
/*     */ 
/*  68 */     boolean doInvert = this.consumer.start(fields, count);
/*     */ 
/*  70 */     for (int i = 0; i < count; i++)
/*     */     {
/*  72 */       Fieldable field = fields[i];
/*     */ 
/*  77 */       if ((field.isIndexed()) && (doInvert))
/*     */       {
/*  81 */         if (this.fieldState.length > 0)
/*  82 */           this.fieldState.position += this.docState.analyzer.getPositionIncrementGap(this.fieldInfo.name);
/*     */         boolean anyToken;
/*     */         boolean anyToken;
/*  84 */         if (!field.isTokenized()) {
/*  85 */           String stringValue = field.stringValue();
/*  86 */           int valueLength = stringValue.length();
/*  87 */           this.perThread.singleToken.reinit(stringValue, 0, valueLength);
/*  88 */           this.fieldState.attributeSource = this.perThread.singleToken;
/*  89 */           this.consumer.start(field);
/*     */ 
/*  91 */           boolean success = false;
/*     */           try {
/*  93 */             this.consumer.add();
/*  94 */             success = true;
/*     */           } finally {
/*  96 */             if (!success)
/*  97 */               this.docState.docWriter.setAborting();
/*     */           }
/*  99 */           this.fieldState.offset += valueLength;
/* 100 */           this.fieldState.length += 1;
/* 101 */           this.fieldState.position += 1;
/* 102 */           anyToken = valueLength > 0;
/*     */         }
/*     */         else {
/* 105 */           TokenStream streamValue = field.tokenStreamValue();
/*     */           TokenStream stream;
/*     */           TokenStream stream;
/* 107 */           if (streamValue != null) {
/* 108 */             stream = streamValue;
/*     */           }
/*     */           else
/*     */           {
/* 113 */             Reader readerValue = field.readerValue();
/*     */             Reader reader;
/*     */             Reader reader;
/* 115 */             if (readerValue != null) {
/* 116 */               reader = readerValue;
/*     */             } else {
/* 118 */               String stringValue = field.stringValue();
/* 119 */               if (stringValue == null)
/* 120 */                 throw new IllegalArgumentException("field must have either TokenStream, String or Reader value");
/* 121 */               this.perThread.stringReader.init(stringValue);
/* 122 */               reader = this.perThread.stringReader;
/*     */             }
/*     */ 
/* 126 */             stream = this.docState.analyzer.reusableTokenStream(this.fieldInfo.name, reader);
/*     */           }
/*     */ 
/* 130 */           stream.reset();
/*     */ 
/* 132 */           int startLength = this.fieldState.length;
/*     */           try
/*     */           {
/* 135 */             int offsetEnd = this.fieldState.offset - 1;
/*     */ 
/* 137 */             boolean hasMoreTokens = stream.incrementToken();
/*     */ 
/* 139 */             this.fieldState.attributeSource = stream;
/*     */ 
/* 141 */             OffsetAttribute offsetAttribute = (OffsetAttribute)this.fieldState.attributeSource.addAttribute(OffsetAttribute.class);
/* 142 */             PositionIncrementAttribute posIncrAttribute = (PositionIncrementAttribute)this.fieldState.attributeSource.addAttribute(PositionIncrementAttribute.class);
/*     */ 
/* 144 */             this.consumer.start(field);
/*     */ 
/* 155 */             while (hasMoreTokens)
/*     */             {
/* 157 */               int posIncr = posIncrAttribute.getPositionIncrement();
/* 158 */               this.fieldState.position += posIncr;
/* 159 */               if (this.fieldState.position > 0) {
/* 160 */                 this.fieldState.position -= 1;
/*     */               }
/*     */ 
/* 163 */               if (posIncr == 0) {
/* 164 */                 this.fieldState.numOverlap += 1;
/*     */               }
/* 166 */               boolean success = false;
/*     */               try
/*     */               {
/* 174 */                 this.consumer.add();
/* 175 */                 success = true;
/*     */               } finally {
/* 177 */                 if (!success)
/* 178 */                   this.docState.docWriter.setAborting();
/*     */               }
/* 180 */               this.fieldState.position += 1;
/* 181 */               offsetEnd = this.fieldState.offset + offsetAttribute.endOffset();
/* 182 */               if (++this.fieldState.length >= maxFieldLength) {
/* 183 */                 if (this.docState.infoStream == null) break;
/* 184 */                 this.docState.infoStream.println("maxFieldLength " + maxFieldLength + " reached for field " + this.fieldInfo.name + ", ignoring following tokens"); break;
/*     */               }
/*     */ 
/* 188 */               hasMoreTokens = stream.incrementToken();
/*     */             }
/*     */ 
/* 191 */             stream.end();
/*     */ 
/* 193 */             this.fieldState.offset += offsetAttribute.endOffset();
/* 194 */             anyToken = this.fieldState.length > startLength;
/*     */           } finally {
/* 196 */             stream.close();
/*     */           }
/*     */         }
/*     */ 
/* 200 */         if (anyToken)
/* 201 */           this.fieldState.offset += this.docState.analyzer.getOffsetGap(field);
/* 202 */         this.fieldState.boost *= field.getBoost();
/*     */       }
/*     */ 
/* 207 */       fields[i] = null;
/*     */     }
/*     */ 
/* 210 */     this.consumer.finish();
/* 211 */     this.endConsumer.finish();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocInverterPerField
 * JD-Core Version:    0.6.2
 */