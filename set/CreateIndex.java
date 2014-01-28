/*     */ package set;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class CreateIndex
/*     */ {
/*  20 */   public HashMap<String, TreeMap<Integer, String>> index = new HashMap(); private TreeMap<Integer, String> title = new TreeMap(); private TreeMap<Integer, String> doc_text = new TreeMap();
/*     */   private StringTokenizer text_for_index;
/*  20 */   private String[] stop = { "a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "encyclopedia", "http", "www", "com", "wikipedia", "wiki", "free", "news", "headline", "amazon", "ebay" }; private List<String> stopword = Arrays.asList(this.stop);
/*     */ 
/*     */   public TreeMap<Integer, String> getDoc_text()
/*     */   {
/*  38 */     return this.doc_text;
/*     */   }
/*     */ 
/*     */   public HashMap<String, TreeMap<Integer, String>> getIndex() {
/*  42 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void doc(ArrayList<Integer> doc_no, ArrayList<String> Text) throws IOException
/*     */   {
/*  47 */     Iterator doc_iterator = doc_no.iterator();
/*  48 */     Iterator text_iterator = Text.iterator();
/*  49 */     while (doc_iterator.hasNext())
/*  50 */       this.doc_text.put((Integer)doc_iterator.next(), (String)text_iterator.next());
/*     */   }
/*     */ 
/*     */   public void Title(ArrayList<Integer> doc_no, ArrayList<String> Title)
/*     */     throws IOException
/*     */   {
/*  57 */     Iterator doc_iterator = doc_no.iterator();
/*  58 */     Iterator title_iterator = Title.iterator();
/*  59 */     while (doc_iterator.hasNext())
/*  60 */       this.title.put((Integer)doc_iterator.next(), (String)title_iterator.next());
/*     */   }
/*     */ 
/*     */   public void Index(ArrayList<Integer> doc_no, ArrayList<String> Text)
/*     */     throws IOException
/*     */   {
/*  67 */     Stemming stem = new Stemming();
/*     */ 
/*  69 */     long start_time = System.currentTimeMillis();
/*  70 */     Iterator doc_iterator = doc_no.iterator();
/*  71 */     Iterator text_iterator = Text.iterator();
/*     */ 
/*  73 */     for (; doc_iterator.hasNext(); 
/*  81 */       this.text_for_index.hasMoreTokens())
/*     */     {
/*  75 */       int Doc_No = ((Integer)doc_iterator.next()).intValue();
/*  76 */       String content = (String)text_iterator.next();
/*  77 */       content = content.toLowerCase();
/*  78 */       String delims = "[ ?\"<>;/://,''-.!|_-]+()[]&@%";
/*  79 */       this.text_for_index = new StringTokenizer(content, delims);
/*  80 */       int position = 1;
/*  81 */       continue;
/*     */ 
/*  83 */       String next_word = this.text_for_index.nextToken();
/*  84 */       if (stopword(next_word))
/*     */       {
/*  86 */         next_word = stem.stem(next_word);
/*  87 */         if (this.index.containsKey(next_word))
/*     */         {
/*  89 */           TreeMap temp = (TreeMap)this.index.get(next_word);
/*  90 */           if (temp.containsKey(Integer.valueOf(Doc_No)))
/*     */           {
/*  92 */             temp.put(Integer.valueOf(Doc_No), (String)temp.get(Integer.valueOf(Doc_No)) + position + " ");
/*     */           }
/*  94 */           else temp.put(Integer.valueOf(Doc_No), position + " ");
/*     */         }
/*     */         else
/*     */         {
/*  98 */           TreeMap temp2 = new TreeMap();
/*  99 */           temp2.put(Integer.valueOf(Doc_No), position + " ");
/* 100 */           this.index.put(next_word, temp2);
/*     */         }
/*     */ 
/* 103 */         position++;
/*     */       }
/*     */     }
/*     */ 
/* 107 */     long end_time = System.currentTimeMillis();
/* 108 */     long time = end_time - start_time;
/* 109 */     System.out.println("Index creation time = " + time + " Milliseconds");
/*     */   }
/*     */ 
/*     */   public TreeMap<Integer, String> getTitle()
/*     */   {
/* 114 */     return this.title;
/*     */   }
/*     */ 
/*     */   public boolean stopword(String word)
/*     */   {
/* 119 */     if (this.stopword.contains(word))
/* 120 */       return false;
/* 121 */     return true;
/*     */   }
/*     */ 
/*     */   public void displayIndex(HashMap<String, TreeMap<Integer, String>> map)
/*     */   {
/* 126 */     Iterator index_key = map.keySet().iterator();
/* 127 */     Iterator index_values = map.values().iterator();
/* 128 */     for (int i = 0; i < map.size(); i++)
/*     */     {
/* 131 */       String word = (String)index_key.next();
/* 132 */       System.out.println(word);
/* 133 */       TreeMap doc = (TreeMap)index_values.next();
/* 134 */       for (Map.Entry entry : doc.entrySet())
/* 135 */         System.out.println("Key: " + entry.getKey() + ". Value: " + (String)entry.getValue());
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     set.CreateIndex
 * JD-Core Version:    0.6.2
 */