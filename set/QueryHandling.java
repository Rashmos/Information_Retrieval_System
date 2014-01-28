/*     */ package set;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class QueryHandling
/*     */ {
/*     */   private HashMap<String, TreeMap<Integer, String>> deserialized_HashMap;
/*     */   private LinkedHashMap<String, TreeMap<Integer, String>> phrase_HashMap;
/*     */   private CreateIndex create;
/*     */   private static int count;
/*  19 */   private String a = "";
/*     */ 
/*  21 */   public String getA() { return this.a; }
/*     */ 
/*     */   public void setA(String a)
/*     */   {
/*  25 */     this.a = a;
/*     */   }
/*     */ 
/*     */   public QueryHandling() throws IOException, ClassNotFoundException
/*     */   {
/*  30 */     this.create = new CreateIndex();
/*  31 */     XML_Parser parser = new XML_Parser();
/*  32 */     parser.ReadXml();
/*     */ 
/*  34 */     this.create.Index(parser.getDocNo(), parser.getText());
/*  35 */     this.create.Title(parser.getDocNo(), parser.getTitle());
/*  36 */     this.deserialized_HashMap = this.create.getIndex();
/*     */ 
/*  38 */     this.phrase_HashMap = new LinkedHashMap();
/*  39 */     TreeMap temp = new TreeMap();
/*  40 */     temp.put(Integer.valueOf(0), "");
/*  41 */     this.phrase_HashMap.put(" ", temp);
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, TreeMap<Integer, String>> getPhrase_HashMap() {
/*  45 */     return this.phrase_HashMap;
/*     */   }
/*     */ 
/*     */   public Set<Integer> singleWord(String single)
/*     */   {
/*  51 */     Set output = new TreeSet();
/*  52 */     output.add(Integer.valueOf(0));
/*  53 */     if (this.deserialized_HashMap.containsKey(single)) {
/*  54 */       output.remove(Integer.valueOf(0));
/*  55 */       output = ((TreeMap)this.deserialized_HashMap.get(single)).keySet();
/*     */     }
/*  57 */     return output;
/*     */   }
/*     */ 
/*     */   public Set<Integer> doubleWord(StringTokenizer Double)
/*     */   {
/*  62 */     Set output = new TreeSet();
/*  63 */     output.add(Integer.valueOf(0));
/*     */ 
/*  65 */     while (Double.hasMoreElements())
/*     */     {
/*  67 */       String word1 = Double.nextToken();
/*  68 */       if (this.deserialized_HashMap.containsKey(word1)) {
/*  69 */         output.remove(Integer.valueOf(0));
/*  70 */         output.addAll(((TreeMap)this.deserialized_HashMap.get(word1)).keySet());
/*     */       }
/*     */     }
/*  73 */     return output;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, TreeMap<Integer, String>> phrase(StringTokenizer Phrase, boolean flag) throws IOException, ClassNotFoundException
/*     */   {
/*  78 */     TreeSet doc_output1 = new TreeSet();
/*  79 */     TreeSet position_output1 = new TreeSet();
/*  80 */     TreeSet doc_output2 = new TreeSet();
/*  81 */     TreeSet position_output2 = new TreeSet();
/*  82 */     TreeMap phrase_treeMap = new TreeMap();
/*  83 */     String s = "";
/*     */ 
/*  85 */     doc_output1.add(Integer.valueOf(0));
/*  86 */     position_output1.add(" ");
/*  87 */     doc_output2.add(Integer.valueOf(0));
/*  88 */     position_output2.add(" ");
/*  89 */     TreeSet intersection = doc_output1;
/*  90 */     String outcome = "";
/*     */ 
/*  93 */     QueryHandling handle = new QueryHandling();
/*  94 */     for (int j = 0; j < Phrase.countTokens() - 1; j++)
/*     */     {
/*  96 */       String word1 = Phrase.nextToken();
/*  97 */       String word2 = Phrase.nextToken();
/*     */ 
/*  99 */       if ((this.deserialized_HashMap.containsKey(word1)) && (this.deserialized_HashMap.containsKey(word2))) {
/* 100 */         doc_output1.remove(Integer.valueOf(0));
/* 101 */         position_output1.remove(" ");
/*     */ 
/* 103 */         doc_output2.remove(Integer.valueOf(0));
/* 104 */         position_output2.remove(" ");
/*     */ 
/* 106 */         doc_output1.addAll(((TreeMap)this.deserialized_HashMap.get(word1)).keySet());
/* 107 */         doc_output2.addAll(((TreeMap)this.deserialized_HashMap.get(word2)).keySet());
/* 108 */         count = 0;
/* 109 */         if (intersection.retainAll(doc_output2))
/*     */         {
/* 111 */           Iterator z = intersection.iterator();
/*     */ 
/* 113 */           while (z.hasNext()) {
/* 114 */             int next = ((Integer)z.next()).intValue();
/*     */ 
/* 117 */             position_output1.add((String)((TreeMap)this.deserialized_HashMap.get(word1)).get(Integer.valueOf(next)));
/* 118 */             position_output2.add((String)((TreeMap)this.deserialized_HashMap.get(word2)).get(Integer.valueOf(next)));
/* 119 */             String pos1 = position_output1.toString();
/* 120 */             String pos2 = position_output2.toString();
/* 121 */             pos1 = pos1.replace("[", "");
/* 122 */             pos1 = pos1.replace("]", "");
/* 123 */             pos1 = pos1.trim();
/* 124 */             pos2 = pos2.replace("[", "");
/* 125 */             pos2 = pos2.replace("]", "");
/* 126 */             pos2 = pos2.trim();
/*     */ 
/* 128 */             String[] output1 = pos1.split(" ");
/* 129 */             String[] output2 = pos2.split(" ");
/* 130 */             TreeSet out1 = new TreeSet();
/* 131 */             TreeSet out2 = new TreeSet();
/*     */ 
/* 133 */             for (int i = 0; i < output1.length; i++)
/* 134 */               out1.add(Integer.valueOf(Integer.parseInt(output1[i]) + 1));
/* 135 */             for (int i = 0; i < output2.length; i++) {
/* 136 */               out2.add(Integer.valueOf(Integer.parseInt(output2[i])));
/*     */             }
/* 138 */             out2.retainAll(out1);
/*     */ 
/* 141 */             if (!out2.isEmpty())
/*     */             {
/* 143 */               this.phrase_HashMap.remove(" ");
/* 144 */               s = word1 + "," + word2;
/* 145 */               if ((this.phrase_HashMap.containsKey(s)) && (((TreeMap)this.phrase_HashMap.get(s)).containsKey(Integer.valueOf(next))))
/*     */               {
/* 147 */                 phrase_treeMap.put(Integer.valueOf(next), (String)phrase_treeMap.get(Integer.valueOf(next)) + "," + out2);
/* 148 */                 count += 1;
/*     */               }
/*     */               else
/*     */               {
/* 153 */                 phrase_treeMap.put(Integer.valueOf(next), out2);
/* 154 */                 count += 1;
/*     */               }
/*     */ 
/* 157 */               this.phrase_HashMap.put(s, phrase_treeMap);
/* 158 */               outcome = outcome + next + " ";
/*     */             }
/*     */ 
/* 161 */             position_output1.remove(((TreeMap)this.deserialized_HashMap.get(word1)).get(Integer.valueOf(next)));
/* 162 */             position_output2.remove(((TreeMap)this.deserialized_HashMap.get(word2)).get(Integer.valueOf(next)));
/*     */           }
/*     */ 
/* 165 */           this.a = outcome;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 178 */     return this.phrase_HashMap;
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 184 */     return count;
/*     */   }
/*     */ 
/*     */   public void dispPhrase(String out)
/*     */   {
/* 189 */     System.out.println(out);
/*     */   }
/*     */ 
/*     */   public String compare(LinkedHashMap<String, TreeMap<Integer, String>> phrase)
/*     */   {
/* 196 */     Set a = new TreeSet();
/* 197 */     Iterator iterator = phrase.keySet().iterator();
/* 198 */     Iterator iterator2 = phrase.keySet().iterator();
/* 199 */     a = ((TreeMap)phrase.get(iterator.next())).keySet();
/* 200 */     ArrayList posn_list = new ArrayList();
/* 201 */     int count = 0;
/* 202 */     String result1 = " ";
/* 203 */     int length = 0;
/* 204 */     String next_posn = "";
/*     */ 
/* 207 */     if (a.retainAll(((TreeMap)phrase.get(iterator.next())).keySet()));
/* 210 */     Iterator z = a.iterator();
/*     */     int k;
/* 211 */     for (; z.hasNext(); 
/* 224 */       k < length - 1)
/*     */     {
/* 213 */       int next = ((Integer)z.next()).intValue();
/*     */ 
/* 215 */       while (iterator2.hasNext())
/*     */       {
/* 217 */         posn_list.add((String)((TreeMap)phrase.get(iterator2.next())).get(Integer.valueOf(next)));
/* 218 */         length++;
/*     */       }
/*     */ 
/* 221 */       Iterator posn_iterator = posn_list.iterator();
/* 222 */       String pos1 = (String)posn_iterator.next();
/* 223 */       String pos2 = (String)posn_iterator.next();
/* 224 */       k = 0; continue;
/*     */ 
/* 226 */       TreeSet out1 = new TreeSet();
/* 227 */       TreeSet out2 = new TreeSet();
/* 228 */       if (count == 0)
/*     */       {
/* 231 */         pos1 = pos1.replace("[", "");
/* 232 */         pos1 = pos1.replace("]", "");
/* 233 */         pos1 = pos1.replace(" ", "");
/* 234 */         pos1 = pos1.trim();
/* 235 */         String[] output1 = pos1.split(",");
/* 236 */         count++;
/*     */ 
/* 238 */         for (int i = 0; i < output1.length; i++) {
/* 239 */           out1.add(Integer.valueOf(Integer.parseInt(output1[i]) + 1));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 244 */         next_posn = next_posn.replace("[", "");
/* 245 */         next_posn = next_posn.replace("]", "");
/* 246 */         next_posn = next_posn.replace(" ", "");
/* 247 */         next_posn = next_posn.trim();
/* 248 */         String[] output2 = next_posn.split(",");
/*     */ 
/* 250 */         for (int i = 0; i < output2.length; i++) {
/* 251 */           out1.add(Integer.valueOf(Integer.parseInt(output2[i]) + 1));
/*     */         }
/*     */       }
/* 254 */       pos2 = pos2.replace("[", "");
/* 255 */       pos2 = pos2.replace("]", "");
/* 256 */       pos2 = pos2.replace(" ", "");
/* 257 */       pos2 = pos2.trim();
/* 258 */       String[] output2 = pos2.split(",");
/*     */ 
/* 261 */       for (int i = 0; i < output2.length; i++) {
/* 262 */         out2.add(Integer.valueOf(Integer.parseInt(output2[i])));
/*     */       }
/* 264 */       out2.retainAll(out1);
/* 265 */       if (!result1.contains(next)) {
/* 266 */         result1 = result1 + next + " ";
/*     */       }
/* 268 */       next_posn = out2.toString();
/* 269 */       if (posn_iterator.hasNext())
/* 270 */         pos2 = (String)posn_iterator.next();
/* 224 */       k++;
/*     */     }
/*     */ 
/* 278 */     return result1;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     set.QueryHandling
 * JD-Core Version:    0.6.2
 */