/*     */ package set;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class ManagingIndex
/*     */ {
/*     */   private Set<Integer> result;
/*     */   private CreateIndex index;
/*     */   private TreeMap<Integer, String> title;
/*     */   private static TreeMap<Integer, String> doc_text;
/*     */   private HashMap<String, TreeMap<Integer, String>> map;
/*     */ 
/*     */   public ManagingIndex()
/*     */     throws IOException
/*     */   {
/*  29 */     this.result = new TreeSet();
/*  30 */     this.result.add(Integer.valueOf(0));
/*  31 */     XML_Parser parser = new XML_Parser();
/*  32 */     parser.ReadXml();
/*     */ 
/*  34 */     this.index = new CreateIndex();
/*  35 */     this.index.Index(parser.getDocNo(), parser.getText());
/*  36 */     this.index.Title(parser.getDocNo(), parser.getTitle());
/*  37 */     this.index.doc(parser.getDocNo(), parser.getText());
/*  38 */     this.title = this.index.getTitle();
/*  39 */     this.map = this.index.getIndex();
/*  40 */     doc_text = this.index.getDoc_text();
/*     */   }
/*     */ 
/*     */   public CreateIndex getIndex() {
/*  44 */     return this.index;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) throws IOException, ClassNotFoundException
/*     */   {
/*  49 */     String[] stop = { "a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an", "and", "any", "are", "as", "at", 
/*  50 */       "be", "because", "been", "but", "by", "can", "cannot", "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", 
/*  51 */       "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely", 
/*  52 */       "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", 
/*  53 */       "she", "should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", 
/*  54 */       "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
/*  55 */       "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "encyclopedia", "http", "www", "com", "wikipedia", "wiki", "free", "news", "headline", "amazon", "ebay" };
/*  56 */     List stopword = Arrays.asList(stop);
/*     */ 
/*  60 */     String p = "";
/*  61 */     String docs = "";
/*  62 */     Set out1 = new TreeSet();
/*  63 */     Set out2 = new TreeSet();
/*  64 */     Set out4 = new TreeSet();
/*  65 */     Set out3 = new TreeSet();
/*  66 */     Set out3_new = new TreeSet();
/*  67 */     TreeSet doc = new TreeSet();
/*  68 */     Set inp_query = new LinkedHashSet();
/*     */ 
/*  71 */     for (int i = 1; i <= 1400; i++)
/*     */     {
/*  73 */       docs = docs + i + " ";
/*  74 */       doc.add(Integer.valueOf(i));
/*     */     }
/*     */ 
/*  77 */     ManagingIndex manage = new ManagingIndex();
/*     */ 
/*  79 */     boolean flag_phrase = false;
/*  80 */     boolean flag_compoundPhrase = false;
/*  81 */     boolean flag_negate = false;
/*  82 */     boolean flag_complex = false;
/*  83 */     String x = "";
/*  84 */     String compound_query = "";
/*     */ 
/*  87 */     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/*  88 */     System.out.println("Enter the query");
/*  89 */     String input_query = br.readLine();
/*     */ 
/*  91 */     boolean flag_df = false;
/*  92 */     boolean flag_freq = false;
/*     */ 
/*  95 */     input_query = input_query.toLowerCase();
/*  96 */     input_query = input_query.trim();
/*  97 */     String temp = manage.callStemmer(input_query);
/*  98 */     input_query = temp;
/*  99 */     int count = 0;
/*     */ 
/* 101 */     String[] inp = input_query.split(" ");
/* 102 */     String[] copy_inp = inp;
/* 103 */     String df_query = input_query;
/* 104 */     String phrase = "";
/*     */ 
/* 108 */     if (inp[0].equals("tf"))
/*     */     {
/* 111 */       for (int i = 1; i < inp.length; i++)
/*     */       {
/* 113 */         if (doc_text.containsKey(Integer.valueOf(Integer.parseInt(inp[1].trim()))))
/*     */         {
/* 115 */           String str = (String)doc_text.get(Integer.valueOf(Integer.parseInt(inp[1].trim())));
/* 116 */           String delims = "[ ?\"<>;/://,''-.!|_-]+()[]&@%";
/* 117 */           StringTokenizer text = new StringTokenizer(str, delims);
/*     */ 
/* 121 */           while (text.hasMoreElements())
/*     */           {
/* 125 */             if (text.nextToken().equals(inp[2])) {
/* 126 */               count++;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 131 */           System.out.println(count);
/*     */         }
/*     */         else
/*     */         {
/* 135 */           System.out.println("this document does not exist");
/* 136 */         }System.exit(0);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     if (inp[0].equals("doc"))
/*     */     {
/* 144 */       if (doc_text.containsKey(Integer.valueOf(Integer.parseInt(inp[1].trim()))))
/* 145 */         System.out.println((String)doc_text.get(Integer.valueOf(Integer.parseInt(inp[1].trim()))));
/*     */       else
/* 147 */         System.out.println("No such document exists");
/* 148 */       System.exit(0);
/*     */     }
/* 151 */     else if (inp[0].equals("title"))
/*     */     {
/* 153 */       if (manage.title.containsKey(Integer.valueOf(Integer.parseInt(inp[1])))) {
/* 154 */         System.out.println((String)manage.title.get(Integer.valueOf(Integer.parseInt(inp[1]))));
/*     */       }
/*     */ 
/* 157 */       System.exit(0);
/*     */     }
/* 159 */     else if (inp[0].equals("df"))
/*     */     {
/* 161 */       flag_df = true;
/* 162 */       df_query = df_query.replace(inp[0], "");
/* 163 */       df_query.trim();
/*     */     }
/* 166 */     else if (inp[0].equals("freq"))
/*     */     {
/* 168 */       flag_freq = true;
/* 169 */       for (int i = 1; i < inp.length; i++)
/* 170 */         phrase = phrase + " " + inp[i];
/*     */     }
/* 172 */     phrase = phrase.replace("\"", "");
/*     */ 
/* 174 */     int point = 0;
/* 175 */     if ((flag_df) || (flag_freq))
/* 176 */       point = 1;
/* 177 */     for (int h = point; h < inp.length; h++)
/*     */     {
/* 179 */       String g = copy_inp[h];
/* 180 */       g = g.replace("\"", "");
/* 181 */       if (!stopword.contains(g));
/* 182 */       inp_query.add(inp[h]);
/*     */     }
/* 184 */     input_query = inp_query.toString();
/*     */ 
/* 187 */     input_query = input_query.replace(",", "");
/* 188 */     input_query = input_query.replace("[", "");
/* 189 */     input_query = input_query.replace("]", "");
/*     */ 
/* 192 */     String rank_query = input_query.replace("\"", "");
/*     */ 
/* 194 */     input_query = input_query.replace(" ", ",");
/*     */ 
/* 197 */     if ((input_query.charAt(0) == '!') && (input_query.contentEquals(",!")))
/*     */     {
/* 199 */       flag_complex = true;
/*     */ 
/* 201 */       List a = Arrays.asList(input_query.split(",!"));
/* 202 */       Iterator iterate = a.iterator();
/* 203 */       while (iterate.hasNext())
/*     */       {
/* 205 */         x = (String)iterate.next();
/* 206 */         x = x.replaceAll(",", " ");
/* 207 */         x = x.replace("\"", "");
/* 208 */         compound_query = compound_query + x + ",";
/*     */       }
/* 210 */       StringTokenizer Compoundquery = new StringTokenizer(compound_query, ",");
/* 211 */       out4 = manage.singleQuery(Compoundquery);
/* 212 */       out4.retainAll(doc);
/* 213 */       doc.removeAll(out4);
/*     */ 
/* 215 */       print(doc);
/*     */     }
/*     */ 
/* 219 */     if (input_query.charAt(0) == '!') {
/* 220 */       flag_negate = true;
/*     */     }
/* 222 */     input_query = input_query.replace("!", "");
/*     */ 
/* 225 */     if ((input_query.charAt(0) == '"') && (input_query.endsWith("\"")))
/*     */     {
/* 227 */       if (input_query.contains("\",\""))
/*     */       {
/* 231 */         List a = Arrays.asList(input_query.split("\",\""));
/* 232 */         Iterator iterate = a.iterator();
/* 233 */         while (iterate.hasNext())
/*     */         {
/* 235 */           x = (String)iterate.next();
/* 236 */           x = x.replaceAll(",", " ");
/* 237 */           x = x.replace("\"", "");
/* 238 */           compound_query = compound_query + x + ",";
/*     */         }
/*     */ 
/* 241 */         StringTokenizer Compoundquery = new StringTokenizer(compound_query, ",");
/* 242 */         out3 = manage.compoundPhrase(Compoundquery);
/* 243 */         flag_compoundPhrase = true;
/*     */       }
/*     */       else {
/* 246 */         flag_phrase = true;
/*     */       }
/*     */     }
/* 249 */     input_query = input_query.replace(",", " ");
/*     */ 
/* 251 */     String delims = "[ ?\"<>;/://,''-.!|_-]+()[]&@%";
/* 252 */     StringTokenizer query = new StringTokenizer(input_query, delims);
/*     */ 
/* 256 */     if (flag_negate)
/*     */     {
/* 259 */       if (flag_compoundPhrase)
/*     */       {
/* 261 */         if (out3.contains(" ")) {
/* 262 */           System.out.println("this word does not exist in any of the documents, thus its negation is all documents: \n" + docs);
/*     */         }
/*     */         else
/*     */         {
/* 266 */           while (!out3.isEmpty())
/* 267 */             out3_new.add(Integer.valueOf(Integer.parseInt((String)out3.iterator().next())));
/* 268 */           out3_new.retainAll(doc);
/* 269 */           doc.removeAll(out3_new);
/* 270 */           if (flag_df) {
/* 271 */             System.out.println(doc.size());
/*     */           }
/*     */           else
/*     */           {
/* 275 */             print(doc);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 281 */       if ((!flag_compoundPhrase) && (flag_phrase)) {
/* 282 */         p = manage.simplePhrase(query);
/*     */ 
/* 284 */         String[] r = p.split(" ");
/* 285 */         for (int a = 0; a < r.length; a++)
/*     */         {
/* 287 */           r[a] = r[a].replace(" ", "");
/* 288 */           if (!r[a].equals(" ")) {
/* 289 */             out2.add(Integer.valueOf(Integer.parseInt(r[a])));
/*     */           }
/*     */         }
/* 292 */         out2.retainAll(doc);
/* 293 */         doc.removeAll(out2);
/*     */ 
/* 295 */         print(doc);
/*     */       }
/*     */       else
/*     */       {
/* 302 */         out1 = manage.singleQuery(query);
/*     */ 
/* 304 */         if (out1.contains(Integer.valueOf(0))) {
/* 305 */           System.out.println("no documents found");
/*     */         }
/*     */         else {
/* 308 */           out1.retainAll(doc);
/* 309 */           doc.removeAll(out1);
/*     */ 
/* 311 */           if (flag_df) {
/* 312 */             System.out.println(doc.size());
/*     */           }
/*     */           else
/*     */           {
/* 316 */             print(doc);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 326 */       if (flag_compoundPhrase)
/*     */       {
/* 328 */         if (out3.contains(" ")) {
/* 329 */           System.out.println("Sorry, no matches");
/*     */         }
/* 331 */         else if (flag_df) {
/* 332 */           System.out.println(out3.size());
/*     */         }
/*     */         else {
/* 335 */           TreeSet send = new TreeSet();
/* 336 */           Iterator iterate = out3.iterator();
/* 337 */           while (iterate.hasNext())
/*     */           {
/* 339 */             String sample = (String)iterate.next();
/* 340 */             sample = sample.replace(" ", "");
/* 341 */             sample = sample.replace(",", "");
/*     */ 
/* 343 */             send.add(Integer.valueOf(Integer.parseInt(sample)));
/*     */           }
/*     */ 
/* 346 */           if (flag_freq)
/* 347 */             System.out.println(send.size());
/*     */           else {
/* 349 */             print(send);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 354 */       if (!flag_compoundPhrase)
/*     */       {
/* 356 */         if (flag_phrase)
/*     */         {
/* 358 */           QueryHandling handle = new QueryHandling();
/*     */ 
/* 360 */           p = manage.simplePhrase(query);
/*     */ 
/* 362 */           if (flag_freq)
/*     */           {
/* 365 */             int counts = handle.getCount();
/* 366 */             System.out.println(counts);
/* 367 */             System.exit(0);
/*     */           }
/* 369 */           else if (flag_df)
/*     */           {
/* 371 */             String[] df = p.split(" ");
/* 372 */             System.out.println(df.length);
/*     */           }
/*     */           else
/*     */           {
/* 377 */             String[] samp = p.split(" ");
/* 378 */             TreeSet samp_set = new TreeSet();
/* 379 */             for (int i = 0; i < samp.length; i++)
/*     */             {
/* 381 */               if (!samp[i].equals(""))
/* 382 */                 samp_set.add(Integer.valueOf(Integer.parseInt(samp[i])));
/*     */             }
/* 384 */             if (flag_freq)
/* 385 */               System.out.println(samp_set.size());
/*     */             else {
/* 387 */               print(samp_set);
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 393 */           out2 = manage.singleQuery(query);
/* 394 */           if (out2.contains(Integer.valueOf(0))) {
/* 395 */             System.out.println("no documents match");
/*     */           }
/* 397 */           else if (flag_freq) {
/* 398 */             System.out.println(out2.size());
/*     */           }
/* 408 */           else if (flag_df) {
/* 409 */             System.out.println(out2.size());
/*     */           }
/*     */           else
/* 412 */             print(out2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String callStemmer(String query)
/*     */   {
/* 422 */     String result = "";
/* 423 */     Stemming stemming = new Stemming();
/* 424 */     String[] array = query.split(" ");
/* 425 */     String ex = "";
/* 426 */     boolean flag_neg = false;
/* 427 */     boolean flag_freq = false;
/* 428 */     boolean flag_df = false;
/* 429 */     if (array[0].equals("tf"))
/* 430 */       result = "tf " + array[1] + " " + stemming.stem(array[2]);
/* 431 */     if (array[0].equals("freq"))
/*     */     {
/* 433 */       flag_freq = true;
/* 434 */       query = query.replace("freq ", "");
/*     */     }
/*     */ 
/* 438 */     if (array[0].equals("df"))
/*     */     {
/* 440 */       flag_df = true;
/* 441 */       query = query.replace("df ", "");
/* 442 */       query = query.trim();
/*     */     }
/*     */ 
/* 445 */     if (query.charAt(0) == '!')
/*     */     {
/* 447 */       query = query.replace("!", "");
/* 448 */       query = query.trim();
/* 449 */       flag_neg = true;
/*     */     }
/*     */ 
/* 452 */     if ((query.charAt(0) == '"') && (query.endsWith("\"")))
/*     */     {
/* 454 */       ex = query;
/* 455 */       ex = ex.replace(" ", ",");
/* 456 */       String[] test = (String[])null;
/* 457 */       if (ex.contains("\",\""))
/*     */       {
/* 459 */         test = ex.split("\",\"");
/* 460 */         for (int j = 0; j < test.length; j++)
/*     */         {
/* 462 */           String result1 = "";
/* 463 */           test[j] = test[j].replace("\"", "");
/* 464 */           array = test[j].split(",");
/* 465 */           for (int i = 0; i < array.length; i++)
/* 466 */             result1 = result1 + " " + stemming.stem(array[i]);
/* 467 */           result1 = result1.trim();
/* 468 */           result1 = "\"" + result1 + "\"";
/* 469 */           if (flag_neg) {
/* 470 */             result = result + result1 + " !";
/*     */           }
/* 472 */           result = result + " " + result1;
/*     */         }
/*     */ 
/* 477 */         if (flag_df)
/* 478 */           result = "df" + result;
/* 479 */         if (flag_freq)
/* 480 */           result = "freq" + result;
/* 481 */         result = result.trim();
/*     */       }
/*     */       else
/*     */       {
/* 486 */         query = query.replace("\"", "");
/* 487 */         array = query.split(" ");
/* 488 */         for (int i = 0; i < array.length; i++) {
/* 489 */           result = result + " " + stemming.stem(array[i]);
/*     */         }
/* 491 */         result = result.trim();
/* 492 */         result = "\"" + result + "\"";
/* 493 */         if (flag_neg)
/* 494 */           result = "!" + result;
/* 495 */         if (flag_freq)
/* 496 */           result = "freq " + result;
/* 497 */         if (flag_df)
/* 498 */           result = "df " + result;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 503 */       String[] temp = query.split(" ");
/* 504 */       for (int t = 0; t < temp.length; t++)
/* 505 */         if (flag_neg)
/* 506 */           result = result + " !" + stemming.stem(temp[t]);
/*     */         else
/* 508 */           result = result + " " + stemming.stem(temp[t]);
/* 509 */       result = result.trim();
/* 510 */       if (flag_df)
/* 511 */         result = "df " + result;
/* 512 */       if (flag_freq) {
/* 513 */         result = "freq " + result;
/*     */       }
/*     */     }
/*     */ 
/* 517 */     return result;
/*     */   }
/*     */ 
/*     */   public String titleContents(String titleNo)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 525 */     if (this.index.getIndex().keySet().contains(Integer.valueOf(Integer.parseInt(titleNo))))
/*     */     {
/* 527 */       TreeMap title_list = this.title;
/* 528 */       return (String)title_list.get(Integer.valueOf(Integer.parseInt(titleNo)));
/*     */     }
/*     */ 
/* 531 */     return "Sorry no such document exists";
/*     */   }
/*     */ 
/*     */   public Set<String> compoundPhrase(StringTokenizer Query)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 537 */     StringTokenizer query = Query;
/* 538 */     QueryHandling query_handle = new QueryHandling();
/*     */ 
/* 540 */     ManagingIndex manage = new ManagingIndex();
/* 541 */     Set final_result = new TreeSet();
/* 542 */     final_result.add(" ");
/*     */ 
/* 544 */     while (query.hasMoreElements())
/*     */     {
/* 546 */       String word = query.nextToken();
/* 547 */       StringTokenizer Word = new StringTokenizer(word, " ");
/* 548 */       String a = manage.simplePhrase(Word);
/* 549 */       if (!a.equals("Sorry, No documnents Match the Query"))
/*     */       {
/* 551 */         final_result.remove(" ");
/* 552 */         String[] s = a.split(" ");
/* 553 */         if (!final_result.contains(a))
/* 554 */           for (int i = 0; i < s.length; i++)
/* 555 */             final_result.add(s[i]);
/*     */       }
/*     */     }
/* 558 */     return final_result;
/*     */   }
/*     */ 
/*     */   public static void print(Set<Integer> docs)
/*     */   {
/* 564 */     Iterator it = docs.iterator();
/* 565 */     while (it.hasNext())
/*     */     {
/* 567 */       int no = ((Integer)it.next()).intValue();
/* 568 */       String disp = (String)doc_text.get(Integer.valueOf(no));
/* 569 */       if (disp.length() - 1 < 50)
/*     */       {
/* 571 */         disp = disp.substring(0, disp.length());
/*     */       }
/* 573 */       else disp = disp.substring(0, 50);
/*     */ 
/* 575 */       System.out.println(no + "        " + disp + "......");
/*     */     }
/*     */   }
/*     */ 
/*     */   public String simplePhrase(StringTokenizer Query)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 583 */     StringTokenizer query = Query;
/*     */ 
/* 585 */     String Final = "";
/* 586 */     int count = 0;
/* 587 */     String[] query_array = new String[5];
/* 588 */     String x = "";
/* 589 */     QueryHandling query_handle = new QueryHandling();
/* 590 */     boolean flag1 = false;
/* 591 */     boolean flag2 = false;
/*     */ 
/* 593 */     this.result.remove(Integer.valueOf(0));
/* 594 */     LinkedHashMap phrase_result = new LinkedHashMap();
/*     */ 
/* 596 */     if (query.countTokens() <= 2) {
/* 597 */       flag1 = true;
/*     */     }
/* 599 */     else if (query.countTokens() > 2)
/*     */     {
/* 602 */       if (query.countTokens() > 5) {
/* 603 */         System.out.println("phrase has more than 5 words, hence cannot return documents");
/*     */       }
/*     */       else {
/* 606 */         flag2 = true;
/* 607 */         count = query.countTokens();
/* 608 */         for (int j = 0; j < count; j++) {
/* 609 */           query_array[j] = query.nextToken();
/*     */         }
/* 611 */         for (int i = 0; i < count - 1; i++)
/*     */         {
/* 613 */           Final = query_array[i] + " " + query_array[(i + 1)];
/* 614 */           StringTokenizer new_query = new StringTokenizer(Final, " ");
/*     */ 
/* 617 */           phrase_result = query_handle.phrase(new_query, false);
/*     */         }
/*     */ 
/* 620 */         x = query_handle.compare(phrase_result);
/*     */       }
/*     */     }
/*     */ 
/* 624 */     phrase_result = query_handle.phrase(query, true);
/*     */ 
/* 626 */     if ((flag1) && (!phrase_result.containsKey(" "))) {
/* 627 */       return query_handle.getA();
/*     */     }
/*     */ 
/* 630 */     if (flag2)
/*     */     {
/* 632 */       return x;
/*     */     }
/*     */ 
/* 641 */     if ((flag1) && (!phrase_result.containsKey(" ")))
/*     */     {
/* 643 */       return query_handle.getA();
/*     */     }
/*     */ 
/* 647 */     return "Sorry, No documnents Match the Query";
/*     */   }
/*     */ 
/*     */   public Set<Integer> singleQuery(StringTokenizer Query)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 656 */     StringTokenizer query = Query;
/* 657 */     QueryHandling query_handle = new QueryHandling();
/* 658 */     if (query.countTokens() == 1)
/*     */     {
/* 660 */       this.result.remove(Integer.valueOf(0));
/* 661 */       this.result = query_handle.singleWord(query.nextToken());
/*     */     }
/* 663 */     else if (query.countTokens() > 1)
/*     */     {
/* 665 */       this.result.remove(Integer.valueOf(0));
/* 666 */       this.result = query_handle.doubleWord(query);
/*     */     }
/*     */ 
/* 670 */     return this.result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     set.ManagingIndex
 * JD-Core Version:    0.6.2
 */