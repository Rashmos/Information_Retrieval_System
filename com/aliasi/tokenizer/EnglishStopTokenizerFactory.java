/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import java.io.ObjectInput;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class EnglishStopTokenizerFactory extends StopTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 4616272325206021322L;
/*  97 */   static final Set<String> STOP_SET = new HashSet();
/*     */ 
/*     */   public EnglishStopTokenizerFactory(TokenizerFactory factory)
/*     */   {
/*  72 */     super(factory, STOP_SET);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/*  76 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  99 */     STOP_SET.add("a");
/* 100 */     STOP_SET.add("be");
/* 101 */     STOP_SET.add("had");
/* 102 */     STOP_SET.add("it");
/* 103 */     STOP_SET.add("only");
/* 104 */     STOP_SET.add("she");
/* 105 */     STOP_SET.add("was");
/* 106 */     STOP_SET.add("about");
/* 107 */     STOP_SET.add("because");
/* 108 */     STOP_SET.add("has");
/* 109 */     STOP_SET.add("its");
/* 110 */     STOP_SET.add("of");
/* 111 */     STOP_SET.add("some");
/* 112 */     STOP_SET.add("we");
/* 113 */     STOP_SET.add("after");
/* 114 */     STOP_SET.add("been");
/* 115 */     STOP_SET.add("have");
/* 116 */     STOP_SET.add("last");
/* 117 */     STOP_SET.add("on");
/* 118 */     STOP_SET.add("such");
/* 119 */     STOP_SET.add("were");
/* 120 */     STOP_SET.add("all");
/* 121 */     STOP_SET.add("but");
/* 122 */     STOP_SET.add("he");
/* 123 */     STOP_SET.add("more");
/* 124 */     STOP_SET.add("one");
/* 125 */     STOP_SET.add("than");
/* 126 */     STOP_SET.add("when");
/* 127 */     STOP_SET.add("also");
/* 128 */     STOP_SET.add("by");
/* 129 */     STOP_SET.add("her");
/* 130 */     STOP_SET.add("most");
/* 131 */     STOP_SET.add("or");
/* 132 */     STOP_SET.add("that");
/* 133 */     STOP_SET.add("which");
/* 134 */     STOP_SET.add("an");
/* 135 */     STOP_SET.add("can");
/* 136 */     STOP_SET.add("his");
/* 137 */     STOP_SET.add("mr");
/* 138 */     STOP_SET.add("other");
/* 139 */     STOP_SET.add("the");
/* 140 */     STOP_SET.add("who");
/* 141 */     STOP_SET.add("any");
/* 142 */     STOP_SET.add("co");
/* 143 */     STOP_SET.add("if");
/* 144 */     STOP_SET.add("mrs");
/* 145 */     STOP_SET.add("out");
/* 146 */     STOP_SET.add("their");
/* 147 */     STOP_SET.add("will");
/* 148 */     STOP_SET.add("and");
/* 149 */     STOP_SET.add("corp");
/* 150 */     STOP_SET.add("in");
/* 151 */     STOP_SET.add("ms");
/* 152 */     STOP_SET.add("over");
/* 153 */     STOP_SET.add("there");
/* 154 */     STOP_SET.add("with");
/* 155 */     STOP_SET.add("are");
/* 156 */     STOP_SET.add("could");
/* 157 */     STOP_SET.add("inc");
/* 158 */     STOP_SET.add("mz");
/* 159 */     STOP_SET.add("s");
/* 160 */     STOP_SET.add("they");
/* 161 */     STOP_SET.add("would");
/* 162 */     STOP_SET.add("as");
/* 163 */     STOP_SET.add("for");
/* 164 */     STOP_SET.add("into");
/* 165 */     STOP_SET.add("no");
/* 166 */     STOP_SET.add("so");
/* 167 */     STOP_SET.add("this");
/* 168 */     STOP_SET.add("up");
/* 169 */     STOP_SET.add("at");
/* 170 */     STOP_SET.add("from");
/* 171 */     STOP_SET.add("is");
/* 172 */     STOP_SET.add("not");
/* 173 */     STOP_SET.add("says");
/* 174 */     STOP_SET.add("to");
/*     */   }
/*     */ 
/*     */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<EnglishStopTokenizerFactory>
/*     */   {
/*     */     static final long serialVersionUID = 3382872690562205086L;
/*     */ 
/*     */     public Serializer(EnglishStopTokenizerFactory factory)
/*     */     {
/*  83 */       super();
/*     */     }
/*     */     public Serializer() {
/*  86 */       this(null);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in, TokenizerFactory baseFactory) {
/*  90 */       return new EnglishStopTokenizerFactory(baseFactory);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.EnglishStopTokenizerFactory
 * JD-Core Version:    0.6.2
 */