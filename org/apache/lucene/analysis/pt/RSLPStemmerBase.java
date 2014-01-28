/*     */ package org.apache.lucene.analysis.pt;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public abstract class RSLPStemmerBase
/*     */ {
/* 264 */   private static final Pattern headerPattern = Pattern.compile("^\\{\\s*\"([^\"]*)\",\\s*([0-9]+),\\s*(0|1),\\s*\\{(.*)\\},\\s*$");
/*     */ 
/* 266 */   private static final Pattern stripPattern = Pattern.compile("^\\{\\s*\"([^\"]*)\",\\s*([0-9]+)\\s*\\}\\s*(,|(\\}\\s*;))$");
/*     */ 
/* 268 */   private static final Pattern repPattern = Pattern.compile("^\\{\\s*\"([^\"]*)\",\\s*([0-9]+),\\s*\"([^\"]*)\"\\}\\s*(,|(\\}\\s*;))$");
/*     */ 
/* 270 */   private static final Pattern excPattern = Pattern.compile("^\\{\\s*\"([^\"]*)\",\\s*([0-9]+),\\s*\"([^\"]*)\",\\s*\\{(.*)\\}\\s*\\}\\s*(,|(\\}\\s*;))$");
/*     */ 
/*     */   protected static Map<String, Step> parse(Class<? extends RSLPStemmerBase> clazz, String resource)
/*     */   {
/*     */     try
/*     */     {
/* 249 */       InputStream is = clazz.getResourceAsStream(resource);
/* 250 */       LineNumberReader r = new LineNumberReader(new InputStreamReader(is, "UTF-8"));
/* 251 */       Map steps = new HashMap();
/*     */       String step;
/* 253 */       while ((step = readLine(r)) != null) {
/* 254 */         Step s = parseStep(r, step);
/* 255 */         steps.put(s.name, s);
/*     */       }
/* 257 */       r.close();
/* 258 */       return steps;
/*     */     } catch (IOException e) {
/* 260 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Step parseStep(LineNumberReader r, String header)
/*     */     throws IOException
/*     */   {
/* 274 */     Matcher matcher = headerPattern.matcher(header);
/* 275 */     if (!matcher.find()) {
/* 276 */       throw new RuntimeException("Illegal Step header specified at line " + r.getLineNumber());
/*     */     }
/* 278 */     assert (matcher.groupCount() == 4);
/* 279 */     String name = matcher.group(1);
/* 280 */     int min = Integer.parseInt(matcher.group(2));
/* 281 */     int type = Integer.parseInt(matcher.group(3));
/* 282 */     String[] suffixes = parseList(matcher.group(4));
/* 283 */     Rule[] rules = parseRules(r, type);
/* 284 */     return new Step(name, rules, min, suffixes);
/*     */   }
/*     */ 
/*     */   private static Rule[] parseRules(LineNumberReader r, int type) throws IOException {
/* 288 */     List rules = new ArrayList();
/*     */     String line;
/* 290 */     while ((line = readLine(r)) != null) {
/* 291 */       Matcher matcher = stripPattern.matcher(line);
/* 292 */       if (matcher.matches()) {
/* 293 */         rules.add(new Rule(matcher.group(1), Integer.parseInt(matcher.group(2)), ""));
/*     */       } else {
/* 295 */         matcher = repPattern.matcher(line);
/* 296 */         if (matcher.matches()) {
/* 297 */           rules.add(new Rule(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3)));
/*     */         } else {
/* 299 */           matcher = excPattern.matcher(line);
/* 300 */           if (matcher.matches()) {
/* 301 */             if (type == 0) {
/* 302 */               rules.add(new RuleWithSuffixExceptions(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3), parseList(matcher.group(4))));
/*     */             }
/*     */             else
/*     */             {
/* 307 */               rules.add(new RuleWithSetExceptions(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3), parseList(matcher.group(4))));
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 313 */             throw new RuntimeException("Illegal Step rule specified at line " + r.getLineNumber());
/*     */           }
/*     */         }
/*     */       }
/* 317 */       if (line.endsWith(";"))
/* 318 */         return (Rule[])rules.toArray(new Rule[rules.size()]);
/*     */     }
/* 320 */     return null;
/*     */   }
/*     */ 
/*     */   private static String[] parseList(String s) {
/* 324 */     if (s.length() == 0)
/* 325 */       return null;
/* 326 */     String[] list = s.split(",");
/* 327 */     for (int i = 0; i < list.length; i++)
/* 328 */       list[i] = parseString(list[i].trim());
/* 329 */     return list;
/*     */   }
/*     */ 
/*     */   private static String parseString(String s) {
/* 333 */     return s.substring(1, s.length() - 1);
/*     */   }
/*     */ 
/*     */   private static String readLine(LineNumberReader r) throws IOException {
/* 337 */     String line = null;
/* 338 */     while ((line = r.readLine()) != null) {
/* 339 */       line = line.trim();
/* 340 */       if ((line.length() > 0) && (line.charAt(0) != '#'))
/* 341 */         return line;
/*     */     }
/* 343 */     return line;
/*     */   }
/*     */ 
/*     */   protected static class Step
/*     */   {
/*     */     protected final String name;
/*     */     protected final RSLPStemmerBase.Rule[] rules;
/*     */     protected final int min;
/*     */     protected final char[][] suffixes;
/*     */ 
/*     */     public Step(String name, RSLPStemmerBase.Rule[] rules, int min, String[] suffixes)
/*     */     {
/* 196 */       this.name = name;
/* 197 */       this.rules = rules;
/* 198 */       if (min == 0) {
/* 199 */         min = 2147483647;
/* 200 */         for (RSLPStemmerBase.Rule r : rules)
/* 201 */           min = Math.min(min, r.min + r.suffix.length);
/*     */       }
/* 203 */       this.min = min;
/*     */ 
/* 205 */       if ((suffixes == null) || (suffixes.length == 0)) {
/* 206 */         this.suffixes = ((char[][])null);
/*     */       } else {
/* 208 */         this.suffixes = new char[suffixes.length][];
/* 209 */         for (int i = 0; i < suffixes.length; i++)
/* 210 */           this.suffixes[i] = suffixes[i].toCharArray();
/*     */       }
/*     */     }
/*     */ 
/*     */     public int apply(char[] s, int len)
/*     */     {
/* 218 */       if (len < this.min) {
/* 219 */         return len;
/*     */       }
/* 221 */       if (this.suffixes != null) {
/* 222 */         boolean found = false;
/*     */ 
/* 224 */         for (int i = 0; i < this.suffixes.length; i++) {
/* 225 */           if (StemmerUtil.endsWith(s, len, this.suffixes[i])) {
/* 226 */             found = true;
/* 227 */             break;
/*     */           }
/*     */         }
/* 230 */         if (!found) return len;
/*     */       }
/*     */ 
/* 233 */       for (int i = 0; i < this.rules.length; i++) {
/* 234 */         if (this.rules[i].matches(s, len)) {
/* 235 */           return this.rules[i].replace(s, len);
/*     */         }
/*     */       }
/* 238 */       return len;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class RuleWithSuffixExceptions extends RSLPStemmerBase.Rule
/*     */   {
/*     */     protected final char[][] exceptions;
/*     */ 
/*     */     public RuleWithSuffixExceptions(String suffix, int min, String replacement, String[] exceptions)
/*     */     {
/* 156 */       super(min, replacement);
/* 157 */       for (int i = 0; i < exceptions.length; i++) {
/* 158 */         if (!exceptions[i].endsWith(suffix))
/* 159 */           System.err.println("warning: useless exception '" + exceptions[i] + "' does not end with '" + suffix + "'");
/*     */       }
/* 161 */       this.exceptions = new char[exceptions.length][];
/* 162 */       for (int i = 0; i < exceptions.length; i++)
/* 163 */         this.exceptions[i] = exceptions[i].toCharArray();
/*     */     }
/*     */ 
/*     */     public boolean matches(char[] s, int len)
/*     */     {
/* 168 */       if (!super.matches(s, len)) {
/* 169 */         return false;
/*     */       }
/* 171 */       for (int i = 0; i < this.exceptions.length; i++) {
/* 172 */         if (StemmerUtil.endsWith(s, len, this.exceptions[i]))
/* 173 */           return false;
/*     */       }
/* 175 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class RuleWithSetExceptions extends RSLPStemmerBase.Rule
/*     */   {
/*     */     protected final CharArraySet exceptions;
/*     */ 
/*     */     public RuleWithSetExceptions(String suffix, int min, String replacement, String[] exceptions)
/*     */     {
/* 132 */       super(min, replacement);
/* 133 */       for (int i = 0; i < exceptions.length; i++) {
/* 134 */         if (!exceptions[i].endsWith(suffix))
/* 135 */           System.err.println("warning: useless exception '" + exceptions[i] + "' does not end with '" + suffix + "'");
/*     */       }
/* 137 */       this.exceptions = new CharArraySet(Version.LUCENE_31, Arrays.asList(exceptions), false);
/*     */     }
/*     */ 
/*     */     public boolean matches(char[] s, int len)
/*     */     {
/* 143 */       return (super.matches(s, len)) && (!this.exceptions.contains(s, 0, len));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class Rule
/*     */   {
/*     */     protected final char[] suffix;
/*     */     protected final char[] replacement;
/*     */     protected final int min;
/*     */ 
/*     */     public Rule(String suffix, int min, String replacement)
/*     */     {
/* 101 */       this.suffix = suffix.toCharArray();
/* 102 */       this.replacement = replacement.toCharArray();
/* 103 */       this.min = min;
/*     */     }
/*     */ 
/*     */     public boolean matches(char[] s, int len)
/*     */     {
/* 110 */       return (len - this.suffix.length >= this.min) && (StemmerUtil.endsWith(s, len, this.suffix));
/*     */     }
/*     */ 
/*     */     public int replace(char[] s, int len)
/*     */     {
/* 117 */       if (this.replacement.length > 0) {
/* 118 */         System.arraycopy(this.replacement, 0, s, len - this.suffix.length, this.replacement.length);
/*     */       }
/* 120 */       return len - this.suffix.length + this.replacement.length;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.pt.RSLPStemmerBase
 * JD-Core Version:    0.6.2
 */