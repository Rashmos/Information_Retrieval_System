/*      */ package org.apache.lucene.analysis.shingle;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import org.apache.lucene.analysis.Token;
/*      */ import org.apache.lucene.analysis.TokenStream;
/*      */ import org.apache.lucene.analysis.miscellaneous.EmptyTokenStream;
/*      */ import org.apache.lucene.analysis.payloads.PayloadHelper;
/*      */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*      */ import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
/*      */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*      */ import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
/*      */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*      */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*      */ import org.apache.lucene.index.Payload;
/*      */ 
/*      */ @Deprecated
/*      */ public final class ShingleMatrixFilter extends TokenStream
/*      */ {
/*  123 */   public static Character defaultSpacerCharacter = Character.valueOf('_');
/*  124 */   public static TokenSettingsCodec defaultSettingsCodec = new OneDimensionalNonWeightedTokenSettingsCodec();
/*  125 */   public static boolean ignoringSinglePrefixOrSuffixShingleByDefault = false;
/*      */   private TokenSettingsCodec settingsCodec;
/*      */   private int minimumShingleSize;
/*      */   private int maximumShingleSize;
/*  193 */   private boolean ignoringSinglePrefixOrSuffixShingle = false;
/*      */ 
/*  195 */   private Character spacerCharacter = defaultSpacerCharacter;
/*      */   private TokenStream input;
/*      */   private CharTermAttribute termAtt;
/*      */   private PositionIncrementAttribute posIncrAtt;
/*      */   private PayloadAttribute payloadAtt;
/*      */   private OffsetAttribute offsetAtt;
/*      */   private TypeAttribute typeAtt;
/*      */   private FlagsAttribute flagsAtt;
/*      */   private CharTermAttribute in_termAtt;
/*      */   private PositionIncrementAttribute in_posIncrAtt;
/*      */   private PayloadAttribute in_payloadAtt;
/*      */   private OffsetAttribute in_offsetAtt;
/*      */   private TypeAttribute in_typeAtt;
/*      */   private FlagsAttribute in_flagsAtt;
/*      */   private Iterator<ShingleMatrixFilter.Matrix.Column.Row[]> permutations;
/*      */   private List<Token> currentPermuationTokens;
/*      */   private List<ShingleMatrixFilter.Matrix.Column.Row> currentPermutationRows;
/*      */   private int currentPermutationTokensStartOffset;
/*      */   private int currentShingleLength;
/*  349 */   private Set<List<Token>> shinglesSeen = new HashSet();
/*      */   private Matrix matrix;
/*  361 */   private Token reusableToken = new Token();
/*      */ 
/*  414 */   private static final Token request_next_token = new Token();
/*      */   private Token readColumnBuf;
/*      */ 
/*      */   public ShingleMatrixFilter(Matrix matrix, int minimumShingleSize, int maximumShingleSize, Character spacerCharacter, boolean ignoringSinglePrefixOrSuffixShingle, TokenSettingsCodec settingsCodec)
/*      */   {
/*  228 */     this.matrix = matrix;
/*  229 */     this.minimumShingleSize = minimumShingleSize;
/*  230 */     this.maximumShingleSize = maximumShingleSize;
/*  231 */     this.spacerCharacter = spacerCharacter;
/*  232 */     this.ignoringSinglePrefixOrSuffixShingle = ignoringSinglePrefixOrSuffixShingle;
/*  233 */     this.settingsCodec = settingsCodec;
/*      */ 
/*  235 */     this.termAtt = ((CharTermAttribute)addAttribute(CharTermAttribute.class));
/*  236 */     this.posIncrAtt = ((PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class));
/*  237 */     this.payloadAtt = ((PayloadAttribute)addAttribute(PayloadAttribute.class));
/*  238 */     this.offsetAtt = ((OffsetAttribute)addAttribute(OffsetAttribute.class));
/*  239 */     this.typeAtt = ((TypeAttribute)addAttribute(TypeAttribute.class));
/*  240 */     this.flagsAtt = ((FlagsAttribute)addAttribute(FlagsAttribute.class));
/*      */ 
/*  243 */     this.input = new EmptyTokenStream();
/*      */ 
/*  245 */     this.in_termAtt = ((CharTermAttribute)this.input.addAttribute(CharTermAttribute.class));
/*  246 */     this.in_posIncrAtt = ((PositionIncrementAttribute)this.input.addAttribute(PositionIncrementAttribute.class));
/*  247 */     this.in_payloadAtt = ((PayloadAttribute)this.input.addAttribute(PayloadAttribute.class));
/*  248 */     this.in_offsetAtt = ((OffsetAttribute)this.input.addAttribute(OffsetAttribute.class));
/*  249 */     this.in_typeAtt = ((TypeAttribute)this.input.addAttribute(TypeAttribute.class));
/*  250 */     this.in_flagsAtt = ((FlagsAttribute)this.input.addAttribute(FlagsAttribute.class));
/*      */   }
/*      */ 
/*      */   public ShingleMatrixFilter(TokenStream input, int minimumShingleSize, int maximumShingleSize)
/*      */   {
/*  265 */     this(input, minimumShingleSize, maximumShingleSize, defaultSpacerCharacter);
/*      */   }
/*      */ 
/*      */   public ShingleMatrixFilter(TokenStream input, int minimumShingleSize, int maximumShingleSize, Character spacerCharacter)
/*      */   {
/*  281 */     this(input, minimumShingleSize, maximumShingleSize, spacerCharacter, ignoringSinglePrefixOrSuffixShingleByDefault);
/*      */   }
/*      */ 
/*      */   public ShingleMatrixFilter(TokenStream input, int minimumShingleSize, int maximumShingleSize, Character spacerCharacter, boolean ignoringSinglePrefixOrSuffixShingle)
/*      */   {
/*  296 */     this(input, minimumShingleSize, maximumShingleSize, spacerCharacter, ignoringSinglePrefixOrSuffixShingle, defaultSettingsCodec);
/*      */   }
/*      */ 
/*      */   public ShingleMatrixFilter(TokenStream input, int minimumShingleSize, int maximumShingleSize, Character spacerCharacter, boolean ignoringSinglePrefixOrSuffixShingle, TokenSettingsCodec settingsCodec)
/*      */   {
/*  311 */     this.input = input;
/*  312 */     this.minimumShingleSize = minimumShingleSize;
/*  313 */     this.maximumShingleSize = maximumShingleSize;
/*  314 */     this.spacerCharacter = spacerCharacter;
/*  315 */     this.ignoringSinglePrefixOrSuffixShingle = ignoringSinglePrefixOrSuffixShingle;
/*  316 */     this.settingsCodec = settingsCodec;
/*  317 */     this.termAtt = ((CharTermAttribute)addAttribute(CharTermAttribute.class));
/*  318 */     this.posIncrAtt = ((PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class));
/*  319 */     this.payloadAtt = ((PayloadAttribute)addAttribute(PayloadAttribute.class));
/*  320 */     this.offsetAtt = ((OffsetAttribute)addAttribute(OffsetAttribute.class));
/*  321 */     this.typeAtt = ((TypeAttribute)addAttribute(TypeAttribute.class));
/*  322 */     this.flagsAtt = ((FlagsAttribute)addAttribute(FlagsAttribute.class));
/*      */ 
/*  324 */     this.in_termAtt = ((CharTermAttribute)input.addAttribute(CharTermAttribute.class));
/*  325 */     this.in_posIncrAtt = ((PositionIncrementAttribute)input.addAttribute(PositionIncrementAttribute.class));
/*  326 */     this.in_payloadAtt = ((PayloadAttribute)input.addAttribute(PayloadAttribute.class));
/*  327 */     this.in_offsetAtt = ((OffsetAttribute)input.addAttribute(OffsetAttribute.class));
/*  328 */     this.in_typeAtt = ((TypeAttribute)input.addAttribute(TypeAttribute.class));
/*  329 */     this.in_flagsAtt = ((FlagsAttribute)input.addAttribute(FlagsAttribute.class));
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */     throws IOException
/*      */   {
/*  354 */     this.permutations = null;
/*  355 */     this.shinglesSeen.clear();
/*  356 */     this.input.reset();
/*      */   }
/*      */ 
/*      */   public final boolean incrementToken()
/*      */     throws IOException
/*      */   {
/*  365 */     if (this.matrix == null)
/*      */     {
/*  366 */       this.matrix = new Matrix();
/*      */ 
/*  368 */       while ((this.matrix.columns.size() < this.maximumShingleSize) && (readColumn()));
/*      */     }
/*      */ 
/*      */     Token token;
/*      */     do
/*      */     {
/*  378 */       token = produceNextToken(this.reusableToken);
/*  379 */     }while (token == request_next_token);
/*  380 */     if (token == null) return false;
/*      */ 
/*  382 */     clearAttributes();
/*  383 */     this.termAtt.copyBuffer(token.buffer(), 0, token.length());
/*  384 */     this.posIncrAtt.setPositionIncrement(token.getPositionIncrement());
/*  385 */     this.flagsAtt.setFlags(token.getFlags());
/*  386 */     this.offsetAtt.setOffset(token.startOffset(), token.endOffset());
/*  387 */     this.typeAtt.setType(token.type());
/*  388 */     this.payloadAtt.setPayload(token.getPayload());
/*  389 */     return true;
/*      */   }
/*      */ 
/*      */   private Token getNextInputToken(Token token) throws IOException {
/*  393 */     if (!this.input.incrementToken()) return null;
/*  394 */     token.copyBuffer(this.in_termAtt.buffer(), 0, this.in_termAtt.length());
/*  395 */     token.setPositionIncrement(this.in_posIncrAtt.getPositionIncrement());
/*  396 */     token.setFlags(this.in_flagsAtt.getFlags());
/*  397 */     token.setOffset(this.in_offsetAtt.startOffset(), this.in_offsetAtt.endOffset());
/*  398 */     token.setType(this.in_typeAtt.type());
/*  399 */     token.setPayload(this.in_payloadAtt.getPayload());
/*  400 */     return token;
/*      */   }
/*      */ 
/*      */   private Token getNextToken(Token token) throws IOException {
/*  404 */     if (!incrementToken()) return null;
/*  405 */     token.copyBuffer(this.termAtt.buffer(), 0, this.termAtt.length());
/*  406 */     token.setPositionIncrement(this.posIncrAtt.getPositionIncrement());
/*  407 */     token.setFlags(this.flagsAtt.getFlags());
/*  408 */     token.setOffset(this.offsetAtt.startOffset(), this.offsetAtt.endOffset());
/*  409 */     token.setType(this.typeAtt.type());
/*  410 */     token.setPayload(this.payloadAtt.getPayload());
/*  411 */     return token;
/*      */   }
/*      */ 
/*      */   private Token produceNextToken(Token reusableToken)
/*      */     throws IOException
/*      */   {
/*  427 */     if (this.currentPermuationTokens != null) {
/*  428 */       this.currentShingleLength += 1;
/*      */ 
/*  430 */       if ((this.currentShingleLength + this.currentPermutationTokensStartOffset <= this.currentPermuationTokens.size()) && (this.currentShingleLength <= this.maximumShingleSize))
/*      */       {
/*  435 */         if ((this.ignoringSinglePrefixOrSuffixShingle) && (this.currentShingleLength == 1) && ((((ShingleMatrixFilter.Matrix.Column.Row)this.currentPermutationRows.get(this.currentPermutationTokensStartOffset)).getColumn().isFirst()) || (((ShingleMatrixFilter.Matrix.Column.Row)this.currentPermutationRows.get(this.currentPermutationTokensStartOffset)).getColumn().isLast())))
/*      */         {
/*  438 */           return getNextToken(reusableToken);
/*      */         }
/*      */ 
/*  441 */         int termLength = 0;
/*      */ 
/*  443 */         List shingle = new ArrayList(this.currentShingleLength);
/*      */ 
/*  445 */         for (int i = 0; i < this.currentShingleLength; i++) {
/*  446 */           Token shingleToken = (Token)this.currentPermuationTokens.get(i + this.currentPermutationTokensStartOffset);
/*  447 */           termLength += shingleToken.length();
/*  448 */           shingle.add(shingleToken);
/*      */         }
/*  450 */         if (this.spacerCharacter != null) {
/*  451 */           termLength += this.currentShingleLength - 1;
/*      */         }
/*      */ 
/*  455 */         if (!this.shinglesSeen.add(shingle)) {
/*  456 */           return request_next_token;
/*      */         }
/*      */ 
/*  460 */         StringBuilder sb = new StringBuilder(termLength + 10);
/*  461 */         for (Token shingleToken : shingle) {
/*  462 */           if ((this.spacerCharacter != null) && (sb.length() > 0)) {
/*  463 */             sb.append(this.spacerCharacter);
/*      */           }
/*  465 */           sb.append(shingleToken.buffer(), 0, shingleToken.length());
/*      */         }
/*  467 */         reusableToken.setEmpty().append(sb);
/*  468 */         updateToken(reusableToken, shingle, this.currentPermutationTokensStartOffset, this.currentPermutationRows, this.currentPermuationTokens);
/*      */ 
/*  470 */         return reusableToken;
/*      */       }
/*      */ 
/*  476 */       if (this.currentPermutationTokensStartOffset < this.currentPermuationTokens.size() - 1)
/*      */       {
/*  478 */         this.currentPermutationTokensStartOffset += 1;
/*  479 */         this.currentShingleLength = (this.minimumShingleSize - 1);
/*  480 */         return request_next_token;
/*      */       }
/*      */ 
/*  484 */       if (this.permutations == null)
/*      */       {
/*  486 */         return null;
/*      */       }
/*      */ 
/*  490 */       if (!this.permutations.hasNext())
/*      */       {
/*  494 */         if ((this.input != null) && (readColumn()));
/*  501 */         ShingleMatrixFilter.Matrix.Column deletedColumn = (ShingleMatrixFilter.Matrix.Column)this.matrix.columns.remove(0);
/*      */ 
/*  504 */         List deletedColumnTokens = new ArrayList();
/*  505 */         for (ShingleMatrixFilter.Matrix.Column.Row row : deletedColumn.getRows()) {
/*  506 */           for (Token token : row.getTokens()) {
/*  507 */             deletedColumnTokens.add(token);
/*      */           }
/*      */         }
/*      */ 
/*  511 */         for (Iterator shinglesSeenIterator = this.shinglesSeen.iterator(); shinglesSeenIterator.hasNext(); ) {
/*  512 */           shingle = (List)shinglesSeenIterator.next();
/*  513 */           for (Token deletedColumnToken : deletedColumnTokens)
/*  514 */             if (shingle.contains(deletedColumnToken)) {
/*  515 */               shinglesSeenIterator.remove();
/*  516 */               break;
/*      */             }
/*      */         }
/*      */         List shingle;
/*  522 */         if (this.matrix.columns.size() < this.minimumShingleSize)
/*      */         {
/*  524 */           return null;
/*      */         }
/*      */ 
/*  528 */         this.permutations = this.matrix.permutationIterator();
/*      */       }
/*      */ 
/*  531 */       nextTokensPermutation();
/*  532 */       return request_next_token;
/*      */     }
/*      */ 
/*  537 */     if (this.permutations == null) {
/*  538 */       this.permutations = this.matrix.permutationIterator();
/*      */     }
/*      */ 
/*  541 */     if (!this.permutations.hasNext()) {
/*  542 */       return null;
/*      */     }
/*      */ 
/*  545 */     nextTokensPermutation();
/*      */ 
/*  547 */     return request_next_token;
/*      */   }
/*      */ 
/*      */   private void nextTokensPermutation()
/*      */   {
/*  557 */     ShingleMatrixFilter.Matrix.Column.Row[] rowsPermutation = (ShingleMatrixFilter.Matrix.Column.Row[])this.permutations.next();
/*  558 */     List currentPermutationRows = new ArrayList();
/*  559 */     List currentPermuationTokens = new ArrayList();
/*      */     ShingleMatrixFilter.Matrix.Column.Row row;
/*  560 */     for (row : rowsPermutation) {
/*  561 */       for (Token token : row.getTokens()) {
/*  562 */         currentPermuationTokens.add(token);
/*  563 */         currentPermutationRows.add(row);
/*      */       }
/*      */     }
/*  566 */     this.currentPermuationTokens = currentPermuationTokens;
/*  567 */     this.currentPermutationRows = currentPermutationRows;
/*      */ 
/*  569 */     this.currentPermutationTokensStartOffset = 0;
/*  570 */     this.currentShingleLength = (this.minimumShingleSize - 1);
/*      */   }
/*      */ 
/*      */   public void updateToken(Token token, List<Token> shingle, int currentPermutationStartOffset, List<ShingleMatrixFilter.Matrix.Column.Row> currentPermutationRows, List<Token> currentPermuationTokens)
/*      */   {
/*  586 */     token.setType(ShingleMatrixFilter.class.getName());
/*  587 */     token.setFlags(0);
/*  588 */     token.setPositionIncrement(1);
/*  589 */     token.setStartOffset(((Token)shingle.get(0)).startOffset());
/*  590 */     token.setEndOffset(((Token)shingle.get(shingle.size() - 1)).endOffset());
/*  591 */     this.settingsCodec.setWeight(token, calculateShingleWeight(token, shingle, currentPermutationStartOffset, currentPermutationRows, currentPermuationTokens));
/*      */   }
/*      */ 
/*      */   public float calculateShingleWeight(Token shingleToken, List<Token> shingle, int currentPermutationStartOffset, List<ShingleMatrixFilter.Matrix.Column.Row> currentPermutationRows, List<Token> currentPermuationTokens)
/*      */   {
/*  611 */     double[] weights = new double[shingle.size()];
/*      */ 
/*  613 */     double total = 0.0D;
/*  614 */     double top = 0.0D;
/*      */ 
/*  617 */     for (int i = 0; i < weights.length; i++) {
/*  618 */       weights[i] = this.settingsCodec.getWeight((Token)shingle.get(i));
/*      */ 
/*  620 */       double tmp = weights[i];
/*  621 */       if (tmp > top) {
/*  622 */         top = tmp;
/*      */       }
/*  624 */       total += tmp;
/*      */     }
/*      */ 
/*  627 */     double factor = 1.0D / Math.sqrt(total);
/*      */ 
/*  629 */     double weight = 0.0D;
/*  630 */     for (double partWeight : weights) {
/*  631 */       weight += partWeight * factor;
/*      */     }
/*      */ 
/*  634 */     return (float)weight;
/*      */   }
/*      */ 
/*      */   private boolean readColumn()
/*      */     throws IOException
/*      */   {
/*      */     Token token;
/*  651 */     if (this.readColumnBuf != null) {
/*  652 */       Token token = this.readColumnBuf;
/*  653 */       this.readColumnBuf = null;
/*      */     } else {
/*  655 */       token = getNextInputToken(new Token());
/*      */     }
/*      */ 
/*  658 */     if (token == null)
/*  659 */       return false;
/*      */     Matrix tmp46_43 = this.matrix; tmp46_43.getClass(); ShingleMatrixFilter.Matrix.Column currentReaderColumn = new ShingleMatrixFilter.Matrix.Column(tmp46_43);
/*      */     ShingleMatrixFilter.Matrix.Column tmp60_59 = currentReaderColumn; tmp60_59.getClass(); ShingleMatrixFilter.Matrix.Column.Row currentReaderRow = new ShingleMatrixFilter.Matrix.Column.Row(tmp60_59);
/*      */ 
/*  665 */     currentReaderRow.getTokens().add(token);
/*      */     TokenPositioner tokenPositioner;
/*  668 */     while (((this.readColumnBuf = getNextInputToken(new Token())) != null) && ((tokenPositioner = this.settingsCodec.getTokenPositioner(this.readColumnBuf)) != TokenPositioner.newColumn))
/*      */     {
/*  670 */       if (tokenPositioner == TokenPositioner.sameRow) {
/*  671 */         currentReaderRow.getTokens().add(this.readColumnBuf);
/*      */       }
/*      */       else
/*      */       {
/*      */         ShingleMatrixFilter.Matrix.Column tmp149_148 = currentReaderColumn; tmp149_148.getClass(); currentReaderRow = new ShingleMatrixFilter.Matrix.Column.Row(tmp149_148);
/*  674 */         currentReaderRow.getTokens().add(this.readColumnBuf);
/*      */       }
/*  676 */       this.readColumnBuf = null;
/*      */     }
/*      */ 
/*  680 */     if (this.readColumnBuf == null) {
/*  681 */       this.readColumnBuf = getNextInputToken(new Token());
/*  682 */       if (this.readColumnBuf == null) {
/*  683 */         currentReaderColumn.setLast(true);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  688 */     return true;
/*      */   }
/*      */ 
/*      */   public int getMinimumShingleSize()
/*      */   {
/*  885 */     return this.minimumShingleSize;
/*      */   }
/*      */ 
/*      */   public void setMinimumShingleSize(int minimumShingleSize) {
/*  889 */     this.minimumShingleSize = minimumShingleSize;
/*      */   }
/*      */ 
/*      */   public int getMaximumShingleSize() {
/*  893 */     return this.maximumShingleSize;
/*      */   }
/*      */ 
/*      */   public void setMaximumShingleSize(int maximumShingleSize) {
/*  897 */     this.maximumShingleSize = maximumShingleSize;
/*      */   }
/*      */ 
/*      */   public Matrix getMatrix()
/*      */   {
/*  902 */     return this.matrix;
/*      */   }
/*      */ 
/*      */   public void setMatrix(Matrix matrix) {
/*  906 */     this.matrix = matrix;
/*      */   }
/*      */ 
/*      */   public Character getSpacerCharacter() {
/*  910 */     return this.spacerCharacter;
/*      */   }
/*      */ 
/*      */   public void setSpacerCharacter(Character spacerCharacter) {
/*  914 */     this.spacerCharacter = spacerCharacter;
/*      */   }
/*      */ 
/*      */   public boolean isIgnoringSinglePrefixOrSuffixShingle() {
/*  918 */     return this.ignoringSinglePrefixOrSuffixShingle;
/*      */   }
/*      */ 
/*      */   public void setIgnoringSinglePrefixOrSuffixShingle(boolean ignoringSinglePrefixOrSuffixShingle) {
/*  922 */     this.ignoringSinglePrefixOrSuffixShingle = ignoringSinglePrefixOrSuffixShingle;
/*      */   }
/*      */ 
/*      */   public static class SimpleThreeDimensionalTokenSettingsCodec extends ShingleMatrixFilter.TokenSettingsCodec
/*      */   {
/*      */     public ShingleMatrixFilter.TokenPositioner getTokenPositioner(Token token)
/*      */       throws IOException
/*      */     {
/* 1004 */       switch (token.getFlags()) {
/*      */       case 0:
/* 1006 */         return ShingleMatrixFilter.TokenPositioner.newColumn;
/*      */       case 1:
/* 1008 */         return ShingleMatrixFilter.TokenPositioner.newRow;
/*      */       case 2:
/* 1010 */         return ShingleMatrixFilter.TokenPositioner.sameRow;
/*      */       }
/* 1012 */       throw new IOException("Unknown matrix positioning of token " + token);
/*      */     }
/*      */ 
/*      */     public void setTokenPositioner(Token token, ShingleMatrixFilter.TokenPositioner tokenPositioner)
/*      */     {
/* 1023 */       token.setFlags(tokenPositioner.getIndex());
/*      */     }
/*      */ 
/*      */     public float getWeight(Token token)
/*      */     {
/* 1034 */       if ((token.getPayload() == null) || (token.getPayload().getData() == null)) {
/* 1035 */         return 1.0F;
/*      */       }
/* 1037 */       return PayloadHelper.decodeFloat(token.getPayload().getData());
/*      */     }
/*      */ 
/*      */     public void setWeight(Token token, float weight)
/*      */     {
/* 1048 */       if (weight == 1.0F)
/* 1049 */         token.setPayload(null);
/*      */       else
/* 1051 */         token.setPayload(new Payload(PayloadHelper.encodeFloat(weight)));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TwoDimensionalNonWeightedSynonymTokenSettingsCodec extends ShingleMatrixFilter.TokenSettingsCodec
/*      */   {
/*      */     public ShingleMatrixFilter.TokenPositioner getTokenPositioner(Token token)
/*      */       throws IOException
/*      */     {
/*  963 */       if (token.getPositionIncrement() == 0) {
/*  964 */         return ShingleMatrixFilter.TokenPositioner.newRow;
/*      */       }
/*  966 */       return ShingleMatrixFilter.TokenPositioner.newColumn;
/*      */     }
/*      */ 
/*      */     public void setTokenPositioner(Token token, ShingleMatrixFilter.TokenPositioner tokenPositioner)
/*      */     {
/*  972 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public float getWeight(Token token)
/*      */     {
/*  977 */       return 1.0F;
/*      */     }
/*      */ 
/*      */     public void setWeight(Token token, float weight)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class OneDimensionalNonWeightedTokenSettingsCodec extends ShingleMatrixFilter.TokenSettingsCodec
/*      */   {
/*      */     public ShingleMatrixFilter.TokenPositioner getTokenPositioner(Token token)
/*      */       throws IOException
/*      */     {
/*  935 */       return ShingleMatrixFilter.TokenPositioner.newColumn;
/*      */     }
/*      */ 
/*      */     public void setTokenPositioner(Token token, ShingleMatrixFilter.TokenPositioner tokenPositioner)
/*      */     {
/*      */     }
/*      */ 
/*      */     public float getWeight(Token token)
/*      */     {
/*  944 */       return 1.0F;
/*      */     }
/*      */ 
/*      */     public void setWeight(Token token, float weight)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Matrix
/*      */   {
/*      */     private boolean columnsHasBeenCreated;
/*      */     private List<Column> columns;
/*      */ 
/*      */     public Matrix()
/*      */     {
/*  709 */       this.columnsHasBeenCreated = false;
/*      */ 
/*  711 */       this.columns = new ArrayList();
/*      */     }
/*      */     public List<Column> getColumns() {
/*  714 */       return this.columns;
/*      */     }
/*      */ 
/*      */     public Iterator<ShingleMatrixFilter.Matrix.Column.Row[]> permutationIterator()
/*      */     {
/*  832 */       return new Iterator()
/*      */       {
/*  834 */         private int[] columnRowCounters = new int[ShingleMatrixFilter.Matrix.this.columns.size()];
/*      */ 
/*      */         public void remove() {
/*  837 */           throw new IllegalStateException("not implemented");
/*      */         }
/*      */ 
/*      */         public boolean hasNext() {
/*  841 */           int s = this.columnRowCounters.length;
/*  842 */           int n = ShingleMatrixFilter.Matrix.this.columns.size();
/*  843 */           return (s != 0) && (n >= s) && (this.columnRowCounters[(s - 1)] < ((ShingleMatrixFilter.Matrix.Column)ShingleMatrixFilter.Matrix.this.columns.get(s - 1)).getRows().size());
/*      */         }
/*      */ 
/*      */         public ShingleMatrixFilter.Matrix.Column.Row[] next() {
/*  847 */           if (!hasNext()) {
/*  848 */             throw new NoSuchElementException("no more elements");
/*      */           }
/*      */ 
/*  851 */           ShingleMatrixFilter.Matrix.Column.Row[] rows = new ShingleMatrixFilter.Matrix.Column.Row[this.columnRowCounters.length];
/*      */ 
/*  853 */           for (int i = 0; i < this.columnRowCounters.length; i++) {
/*  854 */             rows[i] = ((ShingleMatrixFilter.Matrix.Column.Row)((ShingleMatrixFilter.Matrix.Column)ShingleMatrixFilter.Matrix.access$000(ShingleMatrixFilter.Matrix.this).get(i)).rows.get(this.columnRowCounters[i]));
/*      */           }
/*  856 */           incrementColumnRowCounters();
/*      */ 
/*  858 */           return rows;
/*      */         }
/*      */ 
/*      */         private void incrementColumnRowCounters() {
/*  862 */           for (int i = 0; i < this.columnRowCounters.length; i++) {
/*  863 */             this.columnRowCounters[i] += 1;
/*  864 */             if ((this.columnRowCounters[i] != ((ShingleMatrixFilter.Matrix.Column)ShingleMatrixFilter.Matrix.access$000(ShingleMatrixFilter.Matrix.this).get(i)).rows.size()) || (i >= this.columnRowCounters.length - 1))
/*      */               break;
/*  866 */             this.columnRowCounters[i] = 0;
/*      */           }
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  877 */       return "Matrix{columns=" + this.columns + '}';
/*      */     }
/*      */ 
/*      */     public class Column
/*      */     {
/*      */       private boolean last;
/*      */       private boolean first;
/*  742 */       private List<Row> rows = new ArrayList();
/*      */ 
/*      */       public ShingleMatrixFilter.Matrix getMatrix()
/*      */       {
/*  723 */         return ShingleMatrixFilter.Matrix.this;
/*      */       }
/*      */ 
/*      */       public Column(Token token) {
/*  727 */         this();
/*  728 */         Row row = new Row();
/*  729 */         row.getTokens().add(token);
/*      */       }
/*      */ 
/*      */       public Column() {
/*  733 */         synchronized (ShingleMatrixFilter.Matrix.this) {
/*  734 */           if (!ShingleMatrixFilter.Matrix.this.columnsHasBeenCreated) {
/*  735 */             setFirst(true);
/*  736 */             ShingleMatrixFilter.Matrix.this.columnsHasBeenCreated = true;
/*      */           }
/*      */         }
/*  739 */         ShingleMatrixFilter.Matrix.this.columns.add(this);
/*      */       }
/*      */ 
/*      */       public List<Row> getRows()
/*      */       {
/*  745 */         return this.rows;
/*      */       }
/*      */ 
/*      */       public int getIndex()
/*      */       {
/*  750 */         return ShingleMatrixFilter.Matrix.this.columns.indexOf(this);
/*      */       }
/*      */ 
/*      */       public String toString()
/*      */       {
/*  755 */         return "Column{first=" + this.first + ", last=" + this.last + ", rows=" + this.rows + '}';
/*      */       }
/*      */ 
/*      */       public boolean isFirst()
/*      */       {
/*  763 */         return this.first;
/*      */       }
/*      */ 
/*      */       public void setFirst(boolean first) {
/*  767 */         this.first = first;
/*      */       }
/*      */ 
/*      */       public void setLast(boolean last) {
/*  771 */         this.last = last;
/*      */       }
/*      */ 
/*      */       public boolean isLast() {
/*  775 */         return this.last;
/*      */       }
/*      */ 
/*      */       public class Row
/*      */       {
/*  784 */         private List<Token> tokens = new LinkedList();
/*      */ 
/*      */         public ShingleMatrixFilter.Matrix.Column getColumn()
/*      */         {
/*  781 */           return ShingleMatrixFilter.Matrix.Column.this;
/*      */         }
/*      */ 
/*      */         public Row()
/*      */         {
/*  787 */           ShingleMatrixFilter.Matrix.Column.this.rows.add(this);
/*      */         }
/*      */ 
/*      */         public int getIndex() {
/*  791 */           return ShingleMatrixFilter.Matrix.Column.this.rows.indexOf(this);
/*      */         }
/*      */ 
/*      */         public List<Token> getTokens() {
/*  795 */           return this.tokens;
/*      */         }
/*      */ 
/*      */         public void setTokens(List<Token> tokens) {
/*  799 */           this.tokens = tokens;
/*      */         }
/*      */ 
/*      */         public String toString()
/*      */         {
/*  820 */           return "Row{index=" + getIndex() + ", tokens=" + (this.tokens == null ? null : this.tokens) + '}';
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TokenPositioner
/*      */   {
/*  171 */     public static final TokenPositioner newColumn = new TokenPositioner(0);
/*  172 */     public static final TokenPositioner newRow = new TokenPositioner(1);
/*  173 */     public static final TokenPositioner sameRow = new TokenPositioner(2);
/*      */     private final int index;
/*      */ 
/*      */     private TokenPositioner(int index)
/*      */     {
/*  178 */       this.index = index;
/*      */     }
/*      */ 
/*      */     public int getIndex() {
/*  182 */       return this.index;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class TokenSettingsCodec
/*      */   {
/*      */     public abstract ShingleMatrixFilter.TokenPositioner getTokenPositioner(Token paramToken)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract void setTokenPositioner(Token paramToken, ShingleMatrixFilter.TokenPositioner paramTokenPositioner);
/*      */ 
/*      */     public abstract float getWeight(Token paramToken);
/*      */ 
/*      */     public abstract void setWeight(Token paramToken, float paramFloat);
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.shingle.ShingleMatrixFilter
 * JD-Core Version:    0.6.2
 */