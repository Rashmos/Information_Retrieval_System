/*     */ package org.apache.lucene.analysis.miscellaneous;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.Token;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*     */ import org.apache.lucene.index.Payload;
/*     */ 
/*     */ public class PrefixAwareTokenFilter extends TokenStream
/*     */ {
/*     */   private TokenStream prefix;
/*     */   private TokenStream suffix;
/*     */   private CharTermAttribute termAtt;
/*     */   private PositionIncrementAttribute posIncrAtt;
/*     */   private PayloadAttribute payloadAtt;
/*     */   private OffsetAttribute offsetAtt;
/*     */   private TypeAttribute typeAtt;
/*     */   private FlagsAttribute flagsAtt;
/*     */   private CharTermAttribute p_termAtt;
/*     */   private PositionIncrementAttribute p_posIncrAtt;
/*     */   private PayloadAttribute p_payloadAtt;
/*     */   private OffsetAttribute p_offsetAtt;
/*     */   private TypeAttribute p_typeAtt;
/*     */   private FlagsAttribute p_flagsAtt;
/*  82 */   private Token previousPrefixToken = new Token();
/*  83 */   private Token reusableToken = new Token();
/*     */   private boolean prefixExhausted;
/*     */ 
/*     */   public PrefixAwareTokenFilter(TokenStream prefix, TokenStream suffix)
/*     */   {
/*  62 */     super(suffix);
/*  63 */     this.suffix = suffix;
/*  64 */     this.prefix = prefix;
/*  65 */     this.prefixExhausted = false;
/*     */ 
/*  67 */     this.termAtt = ((CharTermAttribute)addAttribute(CharTermAttribute.class));
/*  68 */     this.posIncrAtt = ((PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class));
/*  69 */     this.payloadAtt = ((PayloadAttribute)addAttribute(PayloadAttribute.class));
/*  70 */     this.offsetAtt = ((OffsetAttribute)addAttribute(OffsetAttribute.class));
/*  71 */     this.typeAtt = ((TypeAttribute)addAttribute(TypeAttribute.class));
/*  72 */     this.flagsAtt = ((FlagsAttribute)addAttribute(FlagsAttribute.class));
/*     */ 
/*  74 */     this.p_termAtt = ((CharTermAttribute)prefix.addAttribute(CharTermAttribute.class));
/*  75 */     this.p_posIncrAtt = ((PositionIncrementAttribute)prefix.addAttribute(PositionIncrementAttribute.class));
/*  76 */     this.p_payloadAtt = ((PayloadAttribute)prefix.addAttribute(PayloadAttribute.class));
/*  77 */     this.p_offsetAtt = ((OffsetAttribute)prefix.addAttribute(OffsetAttribute.class));
/*  78 */     this.p_typeAtt = ((TypeAttribute)prefix.addAttribute(TypeAttribute.class));
/*  79 */     this.p_flagsAtt = ((FlagsAttribute)prefix.addAttribute(FlagsAttribute.class));
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*  89 */     if (!this.prefixExhausted) {
/*  90 */       Token nextToken = getNextPrefixInputToken(this.reusableToken);
/*  91 */       if (nextToken == null) {
/*  92 */         this.prefixExhausted = true;
/*     */       } else {
/*  94 */         this.previousPrefixToken.reinit(nextToken);
/*     */ 
/*  96 */         Payload p = this.previousPrefixToken.getPayload();
/*  97 */         if (p != null) {
/*  98 */           this.previousPrefixToken.setPayload((Payload)p.clone());
/*     */         }
/* 100 */         setCurrentToken(nextToken);
/* 101 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 105 */     Token nextToken = getNextSuffixInputToken(this.reusableToken);
/* 106 */     if (nextToken == null) {
/* 107 */       return false;
/*     */     }
/*     */ 
/* 110 */     nextToken = updateSuffixToken(nextToken, this.previousPrefixToken);
/* 111 */     setCurrentToken(nextToken);
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   private void setCurrentToken(Token token) {
/* 116 */     if (token == null) return;
/* 117 */     clearAttributes();
/* 118 */     this.termAtt.copyBuffer(token.buffer(), 0, token.length());
/* 119 */     this.posIncrAtt.setPositionIncrement(token.getPositionIncrement());
/* 120 */     this.flagsAtt.setFlags(token.getFlags());
/* 121 */     this.offsetAtt.setOffset(token.startOffset(), token.endOffset());
/* 122 */     this.typeAtt.setType(token.type());
/* 123 */     this.payloadAtt.setPayload(token.getPayload());
/*     */   }
/*     */ 
/*     */   private Token getNextPrefixInputToken(Token token) throws IOException {
/* 127 */     if (!this.prefix.incrementToken()) return null;
/* 128 */     token.copyBuffer(this.p_termAtt.buffer(), 0, this.p_termAtt.length());
/* 129 */     token.setPositionIncrement(this.p_posIncrAtt.getPositionIncrement());
/* 130 */     token.setFlags(this.p_flagsAtt.getFlags());
/* 131 */     token.setOffset(this.p_offsetAtt.startOffset(), this.p_offsetAtt.endOffset());
/* 132 */     token.setType(this.p_typeAtt.type());
/* 133 */     token.setPayload(this.p_payloadAtt.getPayload());
/* 134 */     return token;
/*     */   }
/*     */ 
/*     */   private Token getNextSuffixInputToken(Token token) throws IOException {
/* 138 */     if (!this.suffix.incrementToken()) return null;
/* 139 */     token.copyBuffer(this.termAtt.buffer(), 0, this.termAtt.length());
/* 140 */     token.setPositionIncrement(this.posIncrAtt.getPositionIncrement());
/* 141 */     token.setFlags(this.flagsAtt.getFlags());
/* 142 */     token.setOffset(this.offsetAtt.startOffset(), this.offsetAtt.endOffset());
/* 143 */     token.setType(this.typeAtt.type());
/* 144 */     token.setPayload(this.payloadAtt.getPayload());
/* 145 */     return token;
/*     */   }
/*     */ 
/*     */   public Token updateSuffixToken(Token suffixToken, Token lastPrefixToken)
/*     */   {
/* 156 */     suffixToken.setStartOffset(lastPrefixToken.endOffset() + suffixToken.startOffset());
/* 157 */     suffixToken.setEndOffset(lastPrefixToken.endOffset() + suffixToken.endOffset());
/* 158 */     return suffixToken;
/*     */   }
/*     */ 
/*     */   public void end() throws IOException
/*     */   {
/* 163 */     this.prefix.end();
/* 164 */     this.suffix.end();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/* 169 */     this.prefix.close();
/* 170 */     this.suffix.close();
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 175 */     super.reset();
/* 176 */     if (this.prefix != null) {
/* 177 */       this.prefixExhausted = false;
/* 178 */       this.prefix.reset();
/*     */     }
/* 180 */     if (this.suffix != null)
/* 181 */       this.suffix.reset();
/*     */   }
/*     */ 
/*     */   public TokenStream getPrefix()
/*     */   {
/* 188 */     return this.prefix;
/*     */   }
/*     */ 
/*     */   public void setPrefix(TokenStream prefix) {
/* 192 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */   public TokenStream getSuffix() {
/* 196 */     return this.suffix;
/*     */   }
/*     */ 
/*     */   public void setSuffix(TokenStream suffix) {
/* 200 */     this.suffix = suffix;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.miscellaneous.PrefixAwareTokenFilter
 * JD-Core Version:    0.6.2
 */