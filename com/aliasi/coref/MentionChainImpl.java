/*     */ package com.aliasi.coref;
/*     */ 
/*     */ import com.aliasi.coref.matchers.EntityTypeMatch;
/*     */ import com.aliasi.coref.matchers.ExactPhraseMatch;
/*     */ import com.aliasi.coref.matchers.GenderKiller;
/*     */ import com.aliasi.coref.matchers.HonorificConflictKiller;
/*     */ import com.aliasi.coref.matchers.SequenceSubstringMatch;
/*     */ import com.aliasi.coref.matchers.SynonymMatch;
/*     */ 
/*     */ public class MentionChainImpl extends AbstractMentionChain
/*     */ {
/*     */   private int mMatcherDescriptor;
/*     */   private static final int MALE_OR_FEMALE_MATCHERS = 0;
/*     */   private static final int THING_MATCHERS = 1;
/*     */   private static final int MALE_PRONOUN_MATCHERS = 2;
/*     */   private static final int FEMALE_PRONOUN_MATCHERS = 3;
/*     */   private static final int MALE_MATCHERS = 4;
/*     */   private static final int FEMALE_MATCHERS = 5;
/* 184 */   private static final int[][] UNIFIER = new int[6][6];
/*     */   private static final Matcher[][] MATCHERS;
/*     */   private static final Killer[][] KILLERS;
/*     */ 
/*     */   public MentionChainImpl(Mention mention, int offset, int id)
/*     */   {
/*  50 */     super(mention, offset, id);
/*  51 */     this.mMatcherDescriptor = typeToMatcherDescriptor(mention.entityType());
/*     */   }
/*     */ 
/*     */   public Matcher[] matchers()
/*     */   {
/*  61 */     if (this.mMatcherDescriptor < 0) return MATCHERS[1];
/*  62 */     return MATCHERS[this.mMatcherDescriptor];
/*     */   }
/*     */ 
/*     */   public Killer[] killers()
/*     */   {
/*  72 */     if (this.mMatcherDescriptor < 0) return KILLERS[1];
/*  73 */     return KILLERS[this.mMatcherDescriptor];
/*     */   }
/*     */ 
/*     */   protected void add(Mention mention)
/*     */   {
/*  86 */     int typeToMatchIndex = typeToMatcherDescriptor(mention.entityType());
/*  87 */     if ((this.mMatcherDescriptor >= 0) && (typeToMatchIndex >= 0)) {
/*  88 */       this.mMatcherDescriptor = UNIFIER[this.mMatcherDescriptor][typeToMatchIndex];
/*     */     }
/*     */ 
/*  91 */     if ((gender() == null) && (mention.gender() != null))
/*  92 */       setGender(mention.gender());
/*  93 */     for (String honorific : mention.honorifics())
/*  94 */       addHonorific(honorific);
/*     */   }
/*     */ 
/*     */   private static int typeToMatcherDescriptor(String entityType)
/*     */   {
/* 103 */     if (entityType.equals(Tags.PERSON_TAG))
/* 104 */       return 0;
/* 105 */     if (entityType.equals(Tags.LOCATION_TAG))
/* 106 */       return 1;
/* 107 */     if (entityType.equals(Tags.ORGANIZATION_TAG))
/* 108 */       return 1;
/* 109 */     if (entityType.equals(Tags.MALE_PRONOUN_TAG))
/* 110 */       return 2;
/* 111 */     if (entityType.equals(Tags.FEMALE_PRONOUN_TAG))
/* 112 */       return 3;
/* 113 */     return -1;
/*     */   }
/*     */ 
/*     */   private static final void descriptor(int descriptor)
/*     */   {
/* 123 */     subsume(descriptor, descriptor);
/*     */   }
/*     */ 
/*     */   private static void subsume(int descriptorLess, int descriptorMore)
/*     */   {
/* 135 */     unify(descriptorLess, descriptorMore, descriptorMore);
/*     */   }
/*     */ 
/*     */   private static void unify(int descriptor1, int descriptor2, int result)
/*     */   {
/* 147 */     UNIFIER[descriptor1][descriptor2] = result;
/* 148 */     UNIFIER[descriptor2][descriptor1] = result;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 186 */     descriptor(0);
/* 187 */     descriptor(1);
/* 188 */     descriptor(2);
/* 189 */     descriptor(3);
/* 190 */     descriptor(4);
/* 191 */     descriptor(5);
/* 192 */     subsume(2, 4);
/* 193 */     subsume(3, 5);
/* 194 */     subsume(0, 4);
/* 195 */     subsume(0, 5);
/* 196 */     unify(0, 2, 4);
/*     */ 
/* 198 */     unify(0, 3, 5);
/*     */ 
/* 206 */     MATCHERS = new Matcher[6][];
/*     */ 
/* 208 */     MATCHERS[0] = { new ExactPhraseMatch(1), new EntityTypeMatch(3, Tags.MALE_PRONOUN_TAG), new EntityTypeMatch(3, Tags.FEMALE_PRONOUN_TAG), new SequenceSubstringMatch(3), new SynonymMatch(1) };
/*     */ 
/* 216 */     MATCHERS[1] = { new ExactPhraseMatch(1), new SequenceSubstringMatch(3), new SynonymMatch(1) };
/*     */ 
/* 222 */     MATCHERS[2] = { new EntityTypeMatch(1, Tags.MALE_PRONOUN_TAG) };
/*     */ 
/* 226 */     MATCHERS[3] = { new EntityTypeMatch(1, Tags.FEMALE_PRONOUN_TAG) };
/*     */ 
/* 230 */     MATCHERS[4] = { new ExactPhraseMatch(1), new EntityTypeMatch(1, Tags.MALE_PRONOUN_TAG), new SequenceSubstringMatch(3), new SynonymMatch(1) };
/*     */ 
/* 237 */     MATCHERS[5] = { new ExactPhraseMatch(1), new EntityTypeMatch(1, Tags.FEMALE_PRONOUN_TAG), new SequenceSubstringMatch(3), new SynonymMatch(1) };
/*     */ 
/* 250 */     KILLERS = new Killer[6][];
/*     */ 
/* 252 */     KILLERS[0] = { new GenderKiller(), new HonorificConflictKiller() };
/*     */ 
/* 257 */     KILLERS[1] = { new GenderKiller() };
/*     */ 
/* 261 */     KILLERS[2] = { new GenderKiller() };
/*     */ 
/* 265 */     KILLERS[3] = { new GenderKiller() };
/*     */ 
/* 269 */     KILLERS[4] = { new GenderKiller(), new HonorificConflictKiller() };
/*     */ 
/* 274 */     KILLERS[5] = { new GenderKiller(), new HonorificConflictKiller() };
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.MentionChainImpl
 * JD-Core Version:    0.6.2
 */