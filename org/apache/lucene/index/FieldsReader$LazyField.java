/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.document.AbstractField;
/*     */ import org.apache.lucene.document.Field.Index;
/*     */ import org.apache.lucene.document.Field.Store;
/*     */ import org.apache.lucene.document.Field.TermVector;
/*     */ import org.apache.lucene.document.Fieldable;
/*     */ import org.apache.lucene.store.IndexInput;
/*     */ import org.apache.lucene.util.CloseableThreadLocal;
/*     */ 
/*     */ class FieldsReader$LazyField extends AbstractField
/*     */   implements Fieldable
/*     */ {
/*     */   private int toRead;
/*     */   private long pointer;
/*     */ 
/*     */   /** @deprecated */
/*     */   private boolean isCompressed;
/*     */ 
/*     */   public FieldsReader$LazyField(FieldsReader paramFieldsReader, String name, Field.Store store, int toRead, long pointer, boolean isBinary, boolean isCompressed)
/*     */   {
/* 418 */     super(name, store, Field.Index.NO, Field.TermVector.NO);
/* 419 */     this.toRead = toRead;
/* 420 */     this.pointer = pointer;
/* 421 */     this.isBinary = isBinary;
/* 422 */     if (isBinary)
/* 423 */       this.binaryLength = toRead;
/* 424 */     this.lazy = true;
/* 425 */     this.isCompressed = isCompressed;
/*     */   }
/*     */ 
/*     */   public FieldsReader$LazyField(FieldsReader paramFieldsReader, String name, Field.Store store, Field.Index index, Field.TermVector termVector, int toRead, long pointer, boolean isBinary, boolean isCompressed) {
/* 429 */     super(name, store, index, termVector);
/* 430 */     this.toRead = toRead;
/* 431 */     this.pointer = pointer;
/* 432 */     this.isBinary = isBinary;
/* 433 */     if (isBinary)
/* 434 */       this.binaryLength = toRead;
/* 435 */     this.lazy = true;
/* 436 */     this.isCompressed = isCompressed;
/*     */   }
/*     */ 
/*     */   private IndexInput getFieldStream() {
/* 440 */     IndexInput localFieldsStream = (IndexInput)FieldsReader.access$000(this.this$0).get();
/* 441 */     if (localFieldsStream == null) {
/* 442 */       localFieldsStream = (IndexInput)FieldsReader.access$100(this.this$0).clone();
/* 443 */       FieldsReader.access$000(this.this$0).set(localFieldsStream);
/*     */     }
/* 445 */     return localFieldsStream;
/*     */   }
/*     */ 
/*     */   public Reader readerValue()
/*     */   {
/* 452 */     this.this$0.ensureOpen();
/* 453 */     return null;
/*     */   }
/*     */ 
/*     */   public TokenStream tokenStreamValue()
/*     */   {
/* 460 */     this.this$0.ensureOpen();
/* 461 */     return null;
/*     */   }
/*     */ 
/*     */   public String stringValue()
/*     */   {
/* 468 */     this.this$0.ensureOpen();
/* 469 */     if (this.isBinary) {
/* 470 */       return null;
/*     */     }
/* 472 */     if (this.fieldsData == null) {
/* 473 */       IndexInput localFieldsStream = getFieldStream();
/*     */       try {
/* 475 */         localFieldsStream.seek(this.pointer);
/* 476 */         if (this.isCompressed) {
/* 477 */           byte[] b = new byte[this.toRead];
/* 478 */           localFieldsStream.readBytes(b, 0, b.length);
/* 479 */           this.fieldsData = new String(FieldsReader.access$200(this.this$0, b), "UTF-8");
/*     */         }
/* 481 */         else if (FieldsReader.access$300(this.this$0) >= 1) {
/* 482 */           byte[] bytes = new byte[this.toRead];
/* 483 */           localFieldsStream.readBytes(bytes, 0, this.toRead);
/* 484 */           this.fieldsData = new String(bytes, "UTF-8");
/*     */         }
/*     */         else {
/* 487 */           char[] chars = new char[this.toRead];
/* 488 */           localFieldsStream.readChars(chars, 0, this.toRead);
/* 489 */           this.fieldsData = new String(chars);
/*     */         }
/*     */       }
/*     */       catch (IOException e) {
/* 493 */         throw new FieldReaderException(e);
/*     */       }
/*     */     }
/* 496 */     return (String)this.fieldsData;
/*     */   }
/*     */ 
/*     */   public long getPointer()
/*     */   {
/* 501 */     this.this$0.ensureOpen();
/* 502 */     return this.pointer;
/*     */   }
/*     */ 
/*     */   public void setPointer(long pointer) {
/* 506 */     this.this$0.ensureOpen();
/* 507 */     this.pointer = pointer;
/*     */   }
/*     */ 
/*     */   public int getToRead() {
/* 511 */     this.this$0.ensureOpen();
/* 512 */     return this.toRead;
/*     */   }
/*     */ 
/*     */   public void setToRead(int toRead) {
/* 516 */     this.this$0.ensureOpen();
/* 517 */     this.toRead = toRead;
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue(byte[] result)
/*     */   {
/* 522 */     this.this$0.ensureOpen();
/*     */ 
/* 524 */     if (this.isBinary) {
/* 525 */       if (this.fieldsData == null)
/*     */       {
/*     */         byte[] b;
/*     */         byte[] b;
/* 528 */         if ((result == null) || (result.length < this.toRead))
/* 529 */           b = new byte[this.toRead];
/*     */         else {
/* 531 */           b = result;
/*     */         }
/* 533 */         IndexInput localFieldsStream = getFieldStream();
/*     */         try
/*     */         {
/* 538 */           localFieldsStream.seek(this.pointer);
/* 539 */           localFieldsStream.readBytes(b, 0, this.toRead);
/* 540 */           if (this.isCompressed == true)
/* 541 */             this.fieldsData = FieldsReader.access$200(this.this$0, b);
/*     */           else
/* 543 */             this.fieldsData = b;
/*     */         }
/*     */         catch (IOException e) {
/* 546 */           throw new FieldReaderException(e);
/*     */         }
/*     */ 
/* 549 */         this.binaryOffset = 0;
/* 550 */         this.binaryLength = this.toRead;
/*     */       }
/*     */ 
/* 553 */       return (byte[])this.fieldsData;
/*     */     }
/* 555 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FieldsReader.LazyField
 * JD-Core Version:    0.6.2
 */