/*     */ package org.apache.lucene.analysis.el;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.util.CharacterUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class GreekLowerCaseFilter extends TokenFilter
/*     */ {
/*  38 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*     */   private final CharacterUtils charUtils;
/*     */ 
/*     */   @Deprecated
/*     */   public GreekLowerCaseFilter(TokenStream in)
/*     */   {
/*  44 */     this(Version.LUCENE_30, in);
/*     */   }
/*     */ 
/*     */   public GreekLowerCaseFilter(Version matchVersion, TokenStream in)
/*     */   {
/*  55 */     super(in);
/*  56 */     this.charUtils = CharacterUtils.getInstance(matchVersion);
/*     */   }
/*     */ 
/*     */   public boolean incrementToken() throws IOException
/*     */   {
/*  61 */     if (this.input.incrementToken()) {
/*  62 */       char[] chArray = this.termAtt.buffer();
/*  63 */       int chLen = this.termAtt.length();
/*  64 */       for (int i = 0; i < chLen; ) {
/*  65 */         i += Character.toChars(lowerCase(this.charUtils.codePointAt(chArray, i)), chArray, i);
/*     */       }
/*     */ 
/*  68 */       return true;
/*     */     }
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   private int lowerCase(int codepoint)
/*     */   {
/*  75 */     switch (codepoint)
/*     */     {
/*     */     case 962:
/*  83 */       return 963;
/*     */     case 902:
/*     */     case 940:
/*  91 */       return 945;
/*     */     case 904:
/*     */     case 941:
/*  95 */       return 949;
/*     */     case 905:
/*     */     case 942:
/*  99 */       return 951;
/*     */     case 906:
/*     */     case 912:
/*     */     case 938:
/*     */     case 943:
/*     */     case 970:
/* 106 */       return 953;
/*     */     case 910:
/*     */     case 939:
/*     */     case 944:
/*     */     case 971:
/*     */     case 973:
/* 113 */       return 965;
/*     */     case 908:
/*     */     case 972:
/* 117 */       return 959;
/*     */     case 911:
/*     */     case 974:
/* 121 */       return 969;
/*     */     case 930:
/* 128 */       return 962;
/*     */     case 903:
/*     */     case 907:
/*     */     case 909:
/*     */     case 913:
/*     */     case 914:
/*     */     case 915:
/*     */     case 916:
/*     */     case 917:
/*     */     case 918:
/*     */     case 919:
/*     */     case 920:
/*     */     case 921:
/*     */     case 922:
/*     */     case 923:
/*     */     case 924:
/*     */     case 925:
/*     */     case 926:
/*     */     case 927:
/*     */     case 928:
/*     */     case 929:
/*     */     case 931:
/*     */     case 932:
/*     */     case 933:
/*     */     case 934:
/*     */     case 935:
/*     */     case 936:
/*     */     case 937:
/*     */     case 945:
/*     */     case 946:
/*     */     case 947:
/*     */     case 948:
/*     */     case 949:
/*     */     case 950:
/*     */     case 951:
/*     */     case 952:
/*     */     case 953:
/*     */     case 954:
/*     */     case 955:
/*     */     case 956:
/*     */     case 957:
/*     */     case 958:
/*     */     case 959:
/*     */     case 960:
/*     */     case 961:
/*     */     case 963:
/*     */     case 964:
/*     */     case 965:
/*     */     case 966:
/*     */     case 967:
/*     */     case 968:
/* 131 */     case 969: } return Character.toLowerCase(codepoint);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.el.GreekLowerCaseFilter
 * JD-Core Version:    0.6.2
 */