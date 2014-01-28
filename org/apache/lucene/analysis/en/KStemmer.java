/*      */ package org.apache.lucene.analysis.en;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import org.apache.lucene.analysis.CharArrayMap;
/*      */ import org.apache.lucene.analysis.util.OpenStringBuilder;
/*      */ import org.apache.lucene.util.Version;
/*      */ 
/*      */ public class KStemmer
/*      */ {
/*      */   private static final int MaxWordLen = 50;
/*   75 */   private static final String[] exceptionWords = { "aide", "bathe", "caste", "cute", "dame", "dime", "doge", "done", "dune", "envelope", "gage", "grille", "grippe", "lobe", "mane", "mare", "nape", "node", "pane", "pate", "plane", "pope", "programme", "quite", "ripe", "rote", "rune", "sage", "severe", "shoppe", "sine", "slime", "snipe", "steppe", "suite", "swinge", "tare", "tine", "tope", "tripe", "twine" };
/*      */ 
/*   82 */   private static final String[][] directConflations = { { "aging", "age" }, { "going", "go" }, { "goes", "go" }, { "lying", "lie" }, { "using", "use" }, { "owing", "owe" }, { "suing", "sue" }, { "dying", "die" }, { "tying", "tie" }, { "vying", "vie" }, { "aged", "age" }, { "used", "use" }, { "vied", "vie" }, { "cued", "cue" }, { "died", "die" }, { "eyed", "eye" }, { "hued", "hue" }, { "iced", "ice" }, { "lied", "lie" }, { "owed", "owe" }, { "sued", "sue" }, { "toed", "toe" }, { "tied", "tie" }, { "does", "do" }, { "doing", "do" }, { "aeronautical", "aeronautics" }, { "mathematical", "mathematics" }, { "political", "politics" }, { "metaphysical", "metaphysics" }, { "cylindrical", "cylinder" }, { "nazism", "nazi" }, { "ambiguity", "ambiguous" }, { "barbarity", "barbarous" }, { "credulity", "credulous" }, { "generosity", "generous" }, { "spontaneity", "spontaneous" }, { "unanimity", "unanimous" }, { "voracity", "voracious" }, { "fled", "flee" }, { "miscarriage", "miscarry" } };
/*      */ 
/*   97 */   private static final String[][] countryNationality = { { "afghan", "afghanistan" }, { "african", "africa" }, { "albanian", "albania" }, { "algerian", "algeria" }, { "american", "america" }, { "andorran", "andorra" }, { "angolan", "angola" }, { "arabian", "arabia" }, { "argentine", "argentina" }, { "armenian", "armenia" }, { "asian", "asia" }, { "australian", "australia" }, { "austrian", "austria" }, { "azerbaijani", "azerbaijan" }, { "azeri", "azerbaijan" }, { "bangladeshi", "bangladesh" }, { "belgian", "belgium" }, { "bermudan", "bermuda" }, { "bolivian", "bolivia" }, { "bosnian", "bosnia" }, { "botswanan", "botswana" }, { "brazilian", "brazil" }, { "british", "britain" }, { "bulgarian", "bulgaria" }, { "burmese", "burma" }, { "californian", "california" }, { "cambodian", "cambodia" }, { "canadian", "canada" }, { "chadian", "chad" }, { "chilean", "chile" }, { "chinese", "china" }, { "colombian", "colombia" }, { "croat", "croatia" }, { "croatian", "croatia" }, { "cuban", "cuba" }, { "cypriot", "cyprus" }, { "czechoslovakian", "czechoslovakia" }, { "danish", "denmark" }, { "egyptian", "egypt" }, { "equadorian", "equador" }, { "eritrean", "eritrea" }, { "estonian", "estonia" }, { "ethiopian", "ethiopia" }, { "european", "europe" }, { "fijian", "fiji" }, { "filipino", "philippines" }, { "finnish", "finland" }, { "french", "france" }, { "gambian", "gambia" }, { "georgian", "georgia" }, { "german", "germany" }, { "ghanian", "ghana" }, { "greek", "greece" }, { "grenadan", "grenada" }, { "guamian", "guam" }, { "guatemalan", "guatemala" }, { "guinean", "guinea" }, { "guyanan", "guyana" }, { "haitian", "haiti" }, { "hawaiian", "hawaii" }, { "holland", "dutch" }, { "honduran", "honduras" }, { "hungarian", "hungary" }, { "icelandic", "iceland" }, { "indonesian", "indonesia" }, { "iranian", "iran" }, { "iraqi", "iraq" }, { "iraqui", "iraq" }, { "irish", "ireland" }, { "israeli", "israel" }, { "italian", "italy" }, { "jamaican", "jamaica" }, { "japanese", "japan" }, { "jordanian", "jordan" }, { "kampuchean", "cambodia" }, { "kenyan", "kenya" }, { "korean", "korea" }, { "kuwaiti", "kuwait" }, { "lankan", "lanka" }, { "laotian", "laos" }, { "latvian", "latvia" }, { "lebanese", "lebanon" }, { "liberian", "liberia" }, { "libyan", "libya" }, { "lithuanian", "lithuania" }, { "macedonian", "macedonia" }, { "madagascan", "madagascar" }, { "malaysian", "malaysia" }, { "maltese", "malta" }, { "mauritanian", "mauritania" }, { "mexican", "mexico" }, { "micronesian", "micronesia" }, { "moldovan", "moldova" }, { "monacan", "monaco" }, { "mongolian", "mongolia" }, { "montenegran", "montenegro" }, { "moroccan", "morocco" }, { "myanmar", "burma" }, { "namibian", "namibia" }, { "nepalese", "nepal" }, { "nicaraguan", "nicaragua" }, { "nigerian", "nigeria" }, { "norwegian", "norway" }, { "omani", "oman" }, { "pakistani", "pakistan" }, { "panamanian", "panama" }, { "papuan", "papua" }, { "paraguayan", "paraguay" }, { "peruvian", "peru" }, { "portuguese", "portugal" }, { "romanian", "romania" }, { "rumania", "romania" }, { "rumanian", "romania" }, { "russian", "russia" }, { "rwandan", "rwanda" }, { "samoan", "samoa" }, { "scottish", "scotland" }, { "serb", "serbia" }, { "serbian", "serbia" }, { "siam", "thailand" }, { "siamese", "thailand" }, { "slovakia", "slovak" }, { "slovakian", "slovak" }, { "slovenian", "slovenia" }, { "somali", "somalia" }, { "somalian", "somalia" }, { "spanish", "spain" }, { "swedish", "sweden" }, { "swiss", "switzerland" }, { "syrian", "syria" }, { "taiwanese", "taiwan" }, { "tanzanian", "tanzania" }, { "texan", "texas" }, { "thai", "thailand" }, { "tunisian", "tunisia" }, { "turkish", "turkey" }, { "ugandan", "uganda" }, { "ukrainian", "ukraine" }, { "uruguayan", "uruguay" }, { "uzbek", "uzbekistan" }, { "venezuelan", "venezuela" }, { "vietnamese", "viet" }, { "virginian", "virginia" }, { "yemeni", "yemen" }, { "yugoslav", "yugoslavia" }, { "yugoslavian", "yugoslavia" }, { "zambian", "zambia" }, { "zealander", "zealand" }, { "zimbabwean", "zimbabwe" } };
/*      */ 
/*  179 */   private static final String[] supplementDict = { "aids", "applicator", "capacitor", "digitize", "electromagnet", "ellipsoid", "exosphere", "extensible", "ferromagnet", "graphics", "hydromagnet", "polygraph", "toroid", "superconduct", "backscatter", "connectionism" };
/*      */ 
/*  184 */   private static final String[] properNouns = { "abrams", "achilles", "acropolis", "adams", "agnes", "aires", "alexander", "alexis", "alfred", "algiers", "alps", "amadeus", "ames", "amos", "andes", "angeles", "annapolis", "antilles", "aquarius", "archimedes", "arkansas", "asher", "ashly", "athens", "atkins", "atlantis", "avis", "bahamas", "bangor", "barbados", "barger", "bering", "brahms", "brandeis", "brussels", "bruxelles", "cairns", "camoros", "camus", "carlos", "celts", "chalker", "charles", "cheops", "ching", "christmas", "cocos", "collins", "columbus", "confucius", "conners", "connolly", "copernicus", "cramer", "cyclops", "cygnus", "cyprus", "dallas", "damascus", "daniels", "davies", "davis", "decker", "denning", "dennis", "descartes", "dickens", "doris", "douglas", "downs", "dreyfus", "dukakis", "dulles", "dumfries", "ecclesiastes", "edwards", "emily", "erasmus", "euphrates", "evans", "everglades", "fairbanks", "federales", "fisher", "fitzsimmons", "fleming", "forbes", "fowler", "france", "francis", "goering", "goodling", "goths", "grenadines", "guiness", "hades", "harding", "harris", "hastings", "hawkes", "hawking", "hayes", "heights", "hercules", "himalayas", "hippocrates", "hobbs", "holmes", "honduras", "hopkins", "hughes", "humphreys", "illinois", "indianapolis", "inverness", "iris", "iroquois", "irving", "isaacs", "italy", "james", "jarvis", "jeffreys", "jesus", "jones", "josephus", "judas", "julius", "kansas", "keynes", "kipling", "kiwanis", "lansing", "laos", "leeds", "levis", "leviticus", "lewis", "louis", "maccabees", "madras", "maimonides", "maldive", "massachusetts", "matthews", "mauritius", "memphis", "mercedes", "midas", "mingus", "minneapolis", "mohammed", "moines", "morris", "moses", "myers", "myknos", "nablus", "nanjing", "nantes", "naples", "neal", "netherlands", "nevis", "nostradamus", "oedipus", "olympus", "orleans", "orly", "papas", "paris", "parker", "pauling", "peking", "pershing", "peter", "peters", "philippines", "phineas", "pisces", "pryor", "pythagoras", "queens", "rabelais", "ramses", "reynolds", "rhesus", "rhodes", "richards", "robins", "rodgers", "rogers", "rubens", "sagittarius", "seychelles", "socrates", "texas", "thames", "thomas", "tiberias", "tunis", "venus", "vilnius", "wales", "warner", "wilkins", "williams", "wyoming", "xmas", "yonkers", "zeus", "frances", "aarhus", "adonis", "andrews", "angus", "antares", "aquinas", "arcturus", "ares", "artemis", "augustus", "ayers", "barnabas", "barnes", "becker", "bejing", "biggs", "billings", "boeing", "boris", "borroughs", "briggs", "buenos", "calais", "caracas", "cassius", "cerberus", "ceres", "cervantes", "chantilly", "chartres", "chester", "connally", "conner", "coors", "cummings", "curtis", "daedalus", "dionysus", "dobbs", "dolores", "edmonds" };
/*      */ 
/*  236 */   private static final CharArrayMap<DictEntry> dict_ht = initializeDictHash();
/*      */ 
/*  244 */   private final OpenStringBuilder word = new OpenStringBuilder();
/*      */   private int j;
/*      */   private int k;
/*  563 */   DictEntry matchedEntry = null;
/*      */ 
/*  997 */   private static char[] ization = "ization".toCharArray();
/*  998 */   private static char[] ition = "ition".toCharArray();
/*  999 */   private static char[] ation = "ation".toCharArray();
/* 1000 */   private static char[] ication = "ication".toCharArray();
/*      */   String result;
/*      */ 
/*      */   private char finalChar()
/*      */   {
/*  258 */     return this.word.charAt(this.k);
/*      */   }
/*      */ 
/*      */   private char penultChar() {
/*  262 */     return this.word.charAt(this.k - 1);
/*      */   }
/*      */ 
/*      */   private boolean isVowel(int index) {
/*  266 */     return !isCons(index);
/*      */   }
/*      */ 
/*      */   private boolean isCons(int index)
/*      */   {
/*  272 */     char ch = this.word.charAt(index);
/*      */ 
/*  274 */     if ((ch == 'a') || (ch == 'e') || (ch == 'i') || (ch == 'o') || (ch == 'u')) return false;
/*  275 */     if ((ch != 'y') || (index == 0)) return true;
/*  276 */     return !isCons(index - 1);
/*      */   }
/*      */ 
/*      */   private static CharArrayMap<DictEntry> initializeDictHash()
/*      */   {
/*  283 */     CharArrayMap d = new CharArrayMap(Version.LUCENE_31, 1000, false);
/*      */ 
/*  286 */     d = new CharArrayMap(Version.LUCENE_31, 1000, false);
/*  287 */     for (int i = 0; i < exceptionWords.length; i++) {
/*  288 */       if (!d.containsKey(exceptionWords[i])) {
/*  289 */         DictEntry entry = new DictEntry(exceptionWords[i], true);
/*  290 */         d.put(exceptionWords[i], entry);
/*      */       } else {
/*  292 */         System.out.println("Warning: Entry [" + exceptionWords[i] + "] already in dictionary 1");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  297 */     for (int i = 0; i < directConflations.length; i++) {
/*  298 */       if (!d.containsKey(directConflations[i][0])) {
/*  299 */         DictEntry entry = new DictEntry(directConflations[i][1], false);
/*  300 */         d.put(directConflations[i][0], entry);
/*      */       } else {
/*  302 */         System.out.println("Warning: Entry [" + directConflations[i][0] + "] already in dictionary 2");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  307 */     for (int i = 0; i < countryNationality.length; i++) {
/*  308 */       if (!d.containsKey(countryNationality[i][0])) {
/*  309 */         DictEntry entry = new DictEntry(countryNationality[i][1], false);
/*  310 */         d.put(countryNationality[i][0], entry);
/*      */       } else {
/*  312 */         System.out.println("Warning: Entry [" + countryNationality[i][0] + "] already in dictionary 3");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  317 */     DictEntry defaultEntry = new DictEntry(null, false);
/*      */ 
/*  320 */     String[] array = KStemData1.data;
/*      */ 
/*  322 */     for (int i = 0; i < array.length; i++) {
/*  323 */       if (!d.containsKey(array[i]))
/*  324 */         d.put(array[i], defaultEntry);
/*      */       else {
/*  326 */         System.out.println("Warning: Entry [" + array[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  331 */     array = KStemData2.data;
/*  332 */     for (int i = 0; i < array.length; i++) {
/*  333 */       if (!d.containsKey(array[i]))
/*  334 */         d.put(array[i], defaultEntry);
/*      */       else {
/*  336 */         System.out.println("Warning: Entry [" + array[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  341 */     array = KStemData3.data;
/*  342 */     for (int i = 0; i < array.length; i++) {
/*  343 */       if (!d.containsKey(array[i]))
/*  344 */         d.put(array[i], defaultEntry);
/*      */       else {
/*  346 */         System.out.println("Warning: Entry [" + array[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  351 */     array = KStemData4.data;
/*  352 */     for (int i = 0; i < array.length; i++) {
/*  353 */       if (!d.containsKey(array[i]))
/*  354 */         d.put(array[i], defaultEntry);
/*      */       else {
/*  356 */         System.out.println("Warning: Entry [" + array[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  361 */     array = KStemData5.data;
/*  362 */     for (int i = 0; i < array.length; i++) {
/*  363 */       if (!d.containsKey(array[i]))
/*  364 */         d.put(array[i], defaultEntry);
/*      */       else {
/*  366 */         System.out.println("Warning: Entry [" + array[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  371 */     array = KStemData6.data;
/*  372 */     for (int i = 0; i < array.length; i++) {
/*  373 */       if (!d.containsKey(array[i]))
/*  374 */         d.put(array[i], defaultEntry);
/*      */       else {
/*  376 */         System.out.println("Warning: Entry [" + array[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  381 */     array = KStemData7.data;
/*  382 */     for (int i = 0; i < array.length; i++) {
/*  383 */       if (!d.containsKey(array[i]))
/*  384 */         d.put(array[i], defaultEntry);
/*      */       else {
/*  386 */         System.out.println("Warning: Entry [" + array[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  391 */     for (int i = 0; i < KStemData8.data.length; i++) {
/*  392 */       if (!d.containsKey(KStemData8.data[i]))
/*  393 */         d.put(KStemData8.data[i], defaultEntry);
/*      */       else {
/*  395 */         System.out.println("Warning: Entry [" + KStemData8.data[i] + "] already in dictionary 4");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  400 */     for (int i = 0; i < supplementDict.length; i++) {
/*  401 */       if (!d.containsKey(supplementDict[i]))
/*  402 */         d.put(supplementDict[i], defaultEntry);
/*      */       else {
/*  404 */         System.out.println("Warning: Entry [" + supplementDict[i] + "] already in dictionary 5");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  409 */     for (int i = 0; i < properNouns.length; i++) {
/*  410 */       if (!d.containsKey(properNouns[i]))
/*  411 */         d.put(properNouns[i], defaultEntry);
/*      */       else {
/*  413 */         System.out.println("Warning: Entry [" + properNouns[i] + "] already in dictionary 6");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  418 */     return d;
/*      */   }
/*      */ 
/*      */   private boolean isAlpha(char ch) {
/*  422 */     return (ch >= 'a') && (ch <= 'z');
/*      */   }
/*      */ 
/*      */   private int stemLength()
/*      */   {
/*  427 */     return this.j + 1;
/*      */   }
/*      */ 
/*      */   private boolean endsIn(char[] s) {
/*  431 */     if (s.length > this.k) return false;
/*      */ 
/*  433 */     int r = this.word.length() - s.length;
/*  434 */     this.j = this.k;
/*  435 */     int r1 = r; for (int i = 0; i < s.length; r1++) {
/*  436 */       if (s[i] != this.word.charAt(r1)) return false;
/*  435 */       i++;
/*      */     }
/*      */ 
/*  438 */     this.j = (r - 1);
/*  439 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean endsIn(char a, char b) {
/*  443 */     if (2 > this.k) return false;
/*      */ 
/*  445 */     if ((this.word.charAt(this.k - 1) == a) && (this.word.charAt(this.k) == b)) {
/*  446 */       this.j = (this.k - 2);
/*  447 */       return true;
/*      */     }
/*  449 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean endsIn(char a, char b, char c) {
/*  453 */     if (3 > this.k) return false;
/*  454 */     if ((this.word.charAt(this.k - 2) == a) && (this.word.charAt(this.k - 1) == b) && (this.word.charAt(this.k) == c))
/*      */     {
/*  456 */       this.j = (this.k - 3);
/*  457 */       return true;
/*      */     }
/*  459 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean endsIn(char a, char b, char c, char d) {
/*  463 */     if (4 > this.k) return false;
/*  464 */     if ((this.word.charAt(this.k - 3) == a) && (this.word.charAt(this.k - 2) == b) && (this.word.charAt(this.k - 1) == c) && (this.word.charAt(this.k) == d))
/*      */     {
/*  466 */       this.j = (this.k - 4);
/*  467 */       return true;
/*      */     }
/*  469 */     return false;
/*      */   }
/*      */ 
/*      */   private DictEntry wordInDict()
/*      */   {
/*  479 */     if (this.matchedEntry != null) return this.matchedEntry;
/*  480 */     DictEntry e = (DictEntry)dict_ht.get(this.word.getArray(), 0, this.word.length());
/*  481 */     if ((e != null) && (!e.exception)) {
/*  482 */       this.matchedEntry = e;
/*      */     }
/*      */ 
/*  485 */     return e;
/*      */   }
/*      */ 
/*      */   private void plural()
/*      */   {
/*  490 */     if (this.word.charAt(this.k) == 's')
/*  491 */       if (endsIn('i', 'e', 's')) {
/*  492 */         this.word.setLength(this.j + 3);
/*  493 */         this.k -= 1;
/*  494 */         if (lookup())
/*  495 */           return;
/*  496 */         this.k += 1;
/*  497 */         this.word.unsafeWrite('s');
/*  498 */         setSuffix("y");
/*  499 */         lookup(); } else {
/*  500 */         if (endsIn('e', 's'))
/*      */         {
/*  502 */           this.word.setLength(this.j + 2);
/*  503 */           this.k -= 1;
/*      */ 
/*  518 */           boolean tryE = (this.j > 0) && ((this.word.charAt(this.j) != 's') || (this.word.charAt(this.j - 1) != 's'));
/*      */ 
/*  520 */           if ((tryE) && (lookup())) return;
/*      */ 
/*  524 */           this.word.setLength(this.j + 1);
/*  525 */           this.k -= 1;
/*  526 */           if (lookup()) return;
/*      */ 
/*  529 */           this.word.unsafeWrite('e');
/*  530 */           this.k += 1;
/*      */ 
/*  532 */           if (!tryE) lookup();
/*  533 */           return;
/*      */         }
/*  535 */         if ((this.word.length() > 3) && (penultChar() != 's') && (!endsIn('o', 'u', 's')))
/*      */         {
/*  538 */           this.word.setLength(this.k);
/*  539 */           this.k -= 1;
/*  540 */           lookup();
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void setSuffix(String s)
/*      */   {
/*  547 */     setSuff(s, s.length());
/*      */   }
/*      */ 
/*      */   private void setSuff(String s, int len)
/*      */   {
/*  552 */     this.word.setLength(this.j + 1);
/*  553 */     for (int l = 0; l < len; l++) {
/*  554 */       this.word.unsafeWrite(s.charAt(l));
/*      */     }
/*  556 */     this.k = (this.j + len);
/*      */   }
/*      */ 
/*      */   private boolean lookup()
/*      */   {
/*  576 */     this.matchedEntry = ((DictEntry)dict_ht.get(this.word.getArray(), 0, this.word.size()));
/*  577 */     return this.matchedEntry != null;
/*      */   }
/*      */ 
/*      */   private void pastTense()
/*      */   {
/*  588 */     if (this.word.length() <= 4) return;
/*      */ 
/*  590 */     if (endsIn('i', 'e', 'd')) {
/*  591 */       this.word.setLength(this.j + 3);
/*  592 */       this.k -= 1;
/*  593 */       if (lookup())
/*  594 */         return;
/*  595 */       this.k += 1;
/*  596 */       this.word.unsafeWrite('d');
/*  597 */       setSuffix("y");
/*  598 */       lookup();
/*  599 */       return;
/*      */     }
/*      */ 
/*  603 */     if ((endsIn('e', 'd')) && (vowelInStem()))
/*      */     {
/*  605 */       this.word.setLength(this.j + 2);
/*  606 */       this.k = (this.j + 1);
/*      */ 
/*  608 */       DictEntry entry = wordInDict();
/*  609 */       if ((entry != null) && (!entry.exception))
/*      */       {
/*  613 */         return;
/*      */       }
/*      */ 
/*  616 */       this.word.setLength(this.j + 1);
/*  617 */       this.k = this.j;
/*  618 */       if (lookup()) return;
/*      */ 
/*  627 */       if (doubleC(this.k)) {
/*  628 */         this.word.setLength(this.k);
/*  629 */         this.k -= 1;
/*  630 */         if (lookup()) return;
/*  631 */         this.word.unsafeWrite(this.word.charAt(this.k));
/*  632 */         this.k += 1;
/*  633 */         lookup();
/*  634 */         return;
/*      */       }
/*      */ 
/*  641 */       if ((this.word.charAt(0) == 'u') && (this.word.charAt(1) == 'n')) {
/*  642 */         this.word.unsafeWrite('e');
/*  643 */         this.word.unsafeWrite('d');
/*  644 */         this.k += 2;
/*      */ 
/*  646 */         return;
/*      */       }
/*      */ 
/*  654 */       this.word.setLength(this.j + 1);
/*  655 */       this.word.unsafeWrite('e');
/*  656 */       this.k = (this.j + 1);
/*      */ 
/*  658 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean doubleC(int i)
/*      */   {
/*  664 */     if (i < 1) return false;
/*      */ 
/*  666 */     if (this.word.charAt(i) != this.word.charAt(i - 1)) return false;
/*  667 */     return isCons(i);
/*      */   }
/*      */ 
/*      */   private boolean vowelInStem() {
/*  671 */     for (int i = 0; i < stemLength(); i++) {
/*  672 */       if (isVowel(i)) return true;
/*      */     }
/*  674 */     return false;
/*      */   }
/*      */ 
/*      */   private void aspect()
/*      */   {
/*  686 */     if (this.word.length() <= 5) return;
/*      */ 
/*  689 */     if ((endsIn('i', 'n', 'g')) && (vowelInStem()))
/*      */     {
/*  692 */       this.word.setCharAt(this.j + 1, 'e');
/*  693 */       this.word.setLength(this.j + 2);
/*  694 */       this.k = (this.j + 1);
/*      */ 
/*  696 */       DictEntry entry = wordInDict();
/*  697 */       if ((entry != null) && 
/*  698 */         (!entry.exception)) {
/*  699 */         return;
/*      */       }
/*      */ 
/*  703 */       this.word.setLength(this.k);
/*  704 */       this.k -= 1;
/*      */ 
/*  706 */       if (lookup()) return;
/*      */ 
/*  709 */       if (doubleC(this.k)) {
/*  710 */         this.k -= 1;
/*  711 */         this.word.setLength(this.k + 1);
/*  712 */         if (lookup()) return;
/*  713 */         this.word.unsafeWrite(this.word.charAt(this.k));
/*      */ 
/*  720 */         this.k += 1;
/*  721 */         lookup();
/*  722 */         return;
/*      */       }
/*      */ 
/*  737 */       if ((this.j > 0) && (isCons(this.j)) && (isCons(this.j - 1))) {
/*  738 */         this.k = this.j;
/*  739 */         this.word.setLength(this.k + 1);
/*      */ 
/*  741 */         return;
/*      */       }
/*      */ 
/*  744 */       this.word.setLength(this.j + 1);
/*  745 */       this.word.unsafeWrite('e');
/*  746 */       this.k = (this.j + 1);
/*      */ 
/*  748 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ityEndings()
/*      */   {
/*  759 */     int old_k = this.k;
/*      */ 
/*  761 */     if (endsIn('i', 't', 'y')) {
/*  762 */       this.word.setLength(this.j + 1);
/*  763 */       this.k = this.j;
/*  764 */       if (lookup()) return;
/*  765 */       this.word.unsafeWrite('e');
/*  766 */       this.k = (this.j + 1);
/*  767 */       if (lookup()) return;
/*  768 */       this.word.setCharAt(this.j + 1, 'i');
/*  769 */       this.word.append("ty");
/*  770 */       this.k = old_k;
/*      */ 
/*  775 */       if ((this.j > 0) && (this.word.charAt(this.j - 1) == 'i') && (this.word.charAt(this.j) == 'l')) {
/*  776 */         this.word.setLength(this.j - 1);
/*  777 */         this.word.append("le");
/*  778 */         this.k = this.j;
/*  779 */         lookup();
/*  780 */         return;
/*      */       }
/*      */ 
/*  784 */       if ((this.j > 0) && (this.word.charAt(this.j - 1) == 'i') && (this.word.charAt(this.j) == 'v')) {
/*  785 */         this.word.setLength(this.j + 1);
/*  786 */         this.word.unsafeWrite('e');
/*  787 */         this.k = (this.j + 1);
/*  788 */         lookup();
/*  789 */         return;
/*      */       }
/*      */ 
/*  792 */       if ((this.j > 0) && (this.word.charAt(this.j - 1) == 'a') && (this.word.charAt(this.j) == 'l')) {
/*  793 */         this.word.setLength(this.j + 1);
/*  794 */         this.k = this.j;
/*  795 */         lookup();
/*  796 */         return;
/*      */       }
/*      */ 
/*  806 */       if (lookup()) return;
/*      */ 
/*  809 */       this.word.setLength(this.j + 1);
/*  810 */       this.k = this.j;
/*      */ 
/*  812 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void nceEndings()
/*      */   {
/*  818 */     int old_k = this.k;
/*      */ 
/*  821 */     if (endsIn('n', 'c', 'e')) {
/*  822 */       char word_char = this.word.charAt(this.j);
/*  823 */       if ((word_char != 'e') && (word_char != 'a')) return;
/*  824 */       this.word.setLength(this.j);
/*  825 */       this.word.unsafeWrite('e');
/*  826 */       this.k = this.j;
/*  827 */       if (lookup()) return;
/*  828 */       this.word.setLength(this.j);
/*      */ 
/*  832 */       this.k = (this.j - 1);
/*  833 */       if (lookup()) return;
/*  834 */       this.word.unsafeWrite(word_char);
/*  835 */       this.word.append("nce");
/*  836 */       this.k = old_k;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void nessEndings()
/*      */   {
/*  844 */     if (endsIn('n', 'e', 's', 's'))
/*      */     {
/*  848 */       this.word.setLength(this.j + 1);
/*  849 */       this.k = this.j;
/*  850 */       if (this.word.charAt(this.j) == 'i') this.word.setCharAt(this.j, 'y');
/*  851 */       lookup();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ismEndings()
/*      */   {
/*  858 */     if (endsIn('i', 's', 'm'))
/*      */     {
/*  862 */       this.word.setLength(this.j + 1);
/*  863 */       this.k = this.j;
/*  864 */       lookup();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mentEndings()
/*      */   {
/*  871 */     int old_k = this.k;
/*      */ 
/*  873 */     if (endsIn('m', 'e', 'n', 't')) {
/*  874 */       this.word.setLength(this.j + 1);
/*  875 */       this.k = this.j;
/*  876 */       if (lookup()) return;
/*  877 */       this.word.append("ment");
/*  878 */       this.k = old_k;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void izeEndings()
/*      */   {
/*  886 */     int old_k = this.k;
/*      */ 
/*  888 */     if (endsIn('i', 'z', 'e')) {
/*  889 */       this.word.setLength(this.j + 1);
/*  890 */       this.k = this.j;
/*  891 */       if (lookup()) return;
/*  892 */       this.word.unsafeWrite('i');
/*      */ 
/*  894 */       if (doubleC(this.j)) {
/*  895 */         this.word.setLength(this.j);
/*  896 */         this.k = (this.j - 1);
/*  897 */         if (lookup()) return;
/*  898 */         this.word.unsafeWrite(this.word.charAt(this.j - 1));
/*      */       }
/*      */ 
/*  901 */       this.word.setLength(this.j + 1);
/*  902 */       this.word.unsafeWrite('e');
/*  903 */       this.k = (this.j + 1);
/*  904 */       if (lookup()) return;
/*  905 */       this.word.setLength(this.j + 1);
/*  906 */       this.word.append("ize");
/*  907 */       this.k = old_k;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ncyEndings()
/*      */   {
/*  915 */     if (endsIn('n', 'c', 'y')) {
/*  916 */       if ((this.word.charAt(this.j) != 'e') && (this.word.charAt(this.j) != 'a')) return;
/*  917 */       this.word.setCharAt(this.j + 2, 't');
/*  918 */       this.word.setLength(this.j + 3);
/*  919 */       this.k = (this.j + 2);
/*      */ 
/*  921 */       if (lookup()) return;
/*      */ 
/*  923 */       this.word.setCharAt(this.j + 2, 'c');
/*  924 */       this.word.unsafeWrite('e');
/*  925 */       this.k = (this.j + 3);
/*  926 */       lookup();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void bleEndings()
/*      */   {
/*  933 */     int old_k = this.k;
/*      */ 
/*  936 */     if (endsIn('b', 'l', 'e')) {
/*  937 */       if ((this.word.charAt(this.j) != 'a') && (this.word.charAt(this.j) != 'i')) return;
/*  938 */       char word_char = this.word.charAt(this.j);
/*  939 */       this.word.setLength(this.j);
/*  940 */       this.k = (this.j - 1);
/*  941 */       if (lookup()) return;
/*  942 */       if (doubleC(this.k)) {
/*  943 */         this.word.setLength(this.k);
/*  944 */         this.k -= 1;
/*  945 */         if (lookup()) return;
/*  946 */         this.k += 1;
/*  947 */         this.word.unsafeWrite(this.word.charAt(this.k - 1));
/*      */       }
/*  949 */       this.word.setLength(this.j);
/*  950 */       this.word.unsafeWrite('e');
/*  951 */       this.k = this.j;
/*  952 */       if (lookup()) return;
/*  953 */       this.word.setLength(this.j);
/*  954 */       this.word.append("ate");
/*      */ 
/*  956 */       this.k = (this.j + 2);
/*  957 */       if (lookup()) return;
/*  958 */       this.word.setLength(this.j);
/*  959 */       this.word.unsafeWrite(word_char);
/*  960 */       this.word.append("ble");
/*  961 */       this.k = old_k;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void icEndings()
/*      */   {
/*  973 */     if (endsIn('i', 'c')) {
/*  974 */       this.word.setLength(this.j + 3);
/*  975 */       this.word.append("al");
/*  976 */       this.k = (this.j + 4);
/*  977 */       if (lookup()) return;
/*      */ 
/*  979 */       this.word.setCharAt(this.j + 1, 'y');
/*  980 */       this.word.setLength(this.j + 2);
/*  981 */       this.k = (this.j + 1);
/*  982 */       if (lookup()) return;
/*      */ 
/*  984 */       this.word.setCharAt(this.j + 1, 'e');
/*  985 */       if (lookup()) return;
/*      */ 
/*  987 */       this.word.setLength(this.j + 1);
/*  988 */       this.k = this.j;
/*  989 */       if (lookup()) return;
/*  990 */       this.word.append("ic");
/*  991 */       this.k = (this.j + 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ionEndings()
/*      */   {
/* 1008 */     int old_k = this.k;
/* 1009 */     if (!endsIn('i', 'o', 'n')) {
/* 1010 */       return;
/*      */     }
/*      */ 
/* 1013 */     if (endsIn(ization))
/*      */     {
/* 1017 */       this.word.setLength(this.j + 3);
/* 1018 */       this.word.unsafeWrite('e');
/* 1019 */       this.k = (this.j + 3);
/* 1020 */       lookup();
/* 1021 */       return;
/*      */     }
/*      */ 
/* 1024 */     if (endsIn(ition)) {
/* 1025 */       this.word.setLength(this.j + 1);
/* 1026 */       this.word.unsafeWrite('e');
/* 1027 */       this.k = (this.j + 1);
/* 1028 */       if (lookup())
/*      */       {
/* 1032 */         return;
/*      */       }
/*      */ 
/* 1035 */       this.word.setLength(this.j + 1);
/* 1036 */       this.word.append("ition");
/* 1037 */       this.k = old_k;
/*      */     }
/* 1039 */     else if (endsIn(ation)) {
/* 1040 */       this.word.setLength(this.j + 3);
/* 1041 */       this.word.unsafeWrite('e');
/* 1042 */       this.k = (this.j + 3);
/* 1043 */       if (lookup()) {
/* 1044 */         return;
/*      */       }
/* 1046 */       this.word.setLength(this.j + 1);
/* 1047 */       this.word.unsafeWrite('e');
/*      */ 
/* 1051 */       this.k = (this.j + 1);
/* 1052 */       if (lookup()) return;
/*      */ 
/* 1054 */       this.word.setLength(this.j + 1);
/*      */ 
/* 1058 */       this.k = this.j;
/* 1059 */       if (lookup()) return;
/*      */ 
/* 1062 */       this.word.setLength(this.j + 1);
/* 1063 */       this.word.append("ation");
/* 1064 */       this.k = old_k;
/*      */     }
/*      */ 
/* 1074 */     if (endsIn(ication)) {
/* 1075 */       this.word.setLength(this.j + 1);
/* 1076 */       this.word.unsafeWrite('y');
/* 1077 */       this.k = (this.j + 1);
/* 1078 */       if (lookup())
/*      */       {
/* 1082 */         return;
/*      */       }
/*      */ 
/* 1085 */       this.word.setLength(this.j + 1);
/* 1086 */       this.word.append("ication");
/* 1087 */       this.k = old_k;
/*      */     }
/*      */ 
/* 1093 */     this.j = (this.k - 3);
/*      */ 
/* 1095 */     this.word.setLength(this.j + 1);
/* 1096 */     this.word.unsafeWrite('e');
/* 1097 */     this.k = (this.j + 1);
/* 1098 */     if (lookup()) {
/* 1099 */       return;
/*      */     }
/* 1101 */     this.word.setLength(this.j + 1);
/* 1102 */     this.k = this.j;
/* 1103 */     if (lookup()) {
/* 1104 */       return;
/*      */     }
/*      */ 
/* 1107 */     this.word.setLength(this.j + 1);
/* 1108 */     this.word.append("ion");
/* 1109 */     this.k = old_k;
/*      */   }
/*      */ 
/*      */   private void erAndOrEndings()
/*      */   {
/* 1122 */     int old_k = this.k;
/*      */ 
/* 1124 */     if (this.word.charAt(this.k) != 'r') return;
/*      */ 
/* 1128 */     if (endsIn('i', 'z', 'e', 'r'))
/*      */     {
/* 1132 */       this.word.setLength(this.j + 4);
/* 1133 */       this.k = (this.j + 3);
/* 1134 */       lookup();
/* 1135 */       return;
/*      */     }
/*      */ 
/* 1138 */     if ((endsIn('e', 'r')) || (endsIn('o', 'r'))) {
/* 1139 */       char word_char = this.word.charAt(this.j + 1);
/* 1140 */       if (doubleC(this.j)) {
/* 1141 */         this.word.setLength(this.j);
/* 1142 */         this.k = (this.j - 1);
/* 1143 */         if (lookup()) return;
/* 1144 */         this.word.unsafeWrite(this.word.charAt(this.j - 1));
/*      */       }
/*      */ 
/* 1147 */       if (this.word.charAt(this.j) == 'i') {
/* 1148 */         this.word.setCharAt(this.j, 'y');
/* 1149 */         this.word.setLength(this.j + 1);
/* 1150 */         this.k = this.j;
/* 1151 */         if (lookup())
/* 1152 */           return;
/* 1153 */         this.word.setCharAt(this.j, 'i');
/* 1154 */         this.word.unsafeWrite('e');
/*      */       }
/*      */ 
/* 1157 */       if (this.word.charAt(this.j) == 'e') {
/* 1158 */         this.word.setLength(this.j);
/* 1159 */         this.k = (this.j - 1);
/* 1160 */         if (lookup()) return;
/* 1161 */         this.word.unsafeWrite('e');
/*      */       }
/*      */ 
/* 1164 */       this.word.setLength(this.j + 2);
/* 1165 */       this.k = (this.j + 1);
/* 1166 */       if (lookup()) return;
/* 1167 */       this.word.setLength(this.j + 1);
/* 1168 */       this.k = this.j;
/* 1169 */       if (lookup()) return;
/* 1170 */       this.word.unsafeWrite('e');
/* 1171 */       this.k = (this.j + 1);
/* 1172 */       if (lookup()) return;
/* 1173 */       this.word.setLength(this.j + 1);
/* 1174 */       this.word.unsafeWrite(word_char);
/* 1175 */       this.word.unsafeWrite('r');
/* 1176 */       this.k = old_k;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void lyEndings()
/*      */   {
/* 1189 */     int old_k = this.k;
/*      */ 
/* 1191 */     if (endsIn('l', 'y'))
/*      */     {
/* 1193 */       this.word.setCharAt(this.j + 2, 'e');
/*      */ 
/* 1195 */       if (lookup()) return;
/* 1196 */       this.word.setCharAt(this.j + 2, 'y');
/*      */ 
/* 1198 */       this.word.setLength(this.j + 1);
/* 1199 */       this.k = this.j;
/*      */ 
/* 1201 */       if (lookup()) return;
/*      */ 
/* 1203 */       if ((this.j > 0) && (this.word.charAt(this.j - 1) == 'a') && (this.word.charAt(this.j) == 'l'))
/*      */       {
/* 1212 */         return;
/* 1213 */       }this.word.append("ly");
/* 1214 */       this.k = old_k;
/*      */ 
/* 1216 */       if ((this.j > 0) && (this.word.charAt(this.j - 1) == 'a') && (this.word.charAt(this.j) == 'b'))
/*      */       {
/* 1225 */         this.word.setCharAt(this.j + 2, 'e');
/* 1226 */         this.k = (this.j + 2);
/* 1227 */         return;
/*      */       }
/*      */ 
/* 1230 */       if (this.word.charAt(this.j) == 'i') {
/* 1231 */         this.word.setLength(this.j);
/* 1232 */         this.word.unsafeWrite('y');
/* 1233 */         this.k = this.j;
/* 1234 */         if (lookup()) return;
/* 1235 */         this.word.setLength(this.j);
/* 1236 */         this.word.append("ily");
/* 1237 */         this.k = old_k;
/*      */       }
/*      */ 
/* 1240 */       this.word.setLength(this.j + 1);
/*      */ 
/* 1242 */       this.k = this.j;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void alEndings()
/*      */   {
/* 1253 */     int old_k = this.k;
/*      */ 
/* 1255 */     if (this.word.length() < 4) return;
/* 1256 */     if (endsIn('a', 'l')) {
/* 1257 */       this.word.setLength(this.j + 1);
/* 1258 */       this.k = this.j;
/* 1259 */       if (lookup()) {
/* 1260 */         return;
/*      */       }
/* 1262 */       if (doubleC(this.j)) {
/* 1263 */         this.word.setLength(this.j);
/* 1264 */         this.k = (this.j - 1);
/* 1265 */         if (lookup()) return;
/* 1266 */         this.word.unsafeWrite(this.word.charAt(this.j - 1));
/*      */       }
/*      */ 
/* 1269 */       this.word.setLength(this.j + 1);
/* 1270 */       this.word.unsafeWrite('e');
/* 1271 */       this.k = (this.j + 1);
/* 1272 */       if (lookup()) return;
/*      */ 
/* 1274 */       this.word.setLength(this.j + 1);
/* 1275 */       this.word.append("um");
/*      */ 
/* 1277 */       this.k = (this.j + 2);
/* 1278 */       if (lookup()) return;
/*      */ 
/* 1280 */       this.word.setLength(this.j + 1);
/* 1281 */       this.word.append("al");
/* 1282 */       this.k = old_k;
/*      */ 
/* 1284 */       if ((this.j > 0) && (this.word.charAt(this.j - 1) == 'i') && (this.word.charAt(this.j) == 'c')) {
/* 1285 */         this.word.setLength(this.j - 1);
/* 1286 */         this.k = (this.j - 2);
/* 1287 */         if (lookup()) return;
/*      */ 
/* 1289 */         this.word.setLength(this.j - 1);
/* 1290 */         this.word.unsafeWrite('y');
/* 1291 */         this.k = (this.j - 1);
/* 1292 */         if (lookup()) return;
/*      */ 
/* 1294 */         this.word.setLength(this.j - 1);
/* 1295 */         this.word.append("ic");
/* 1296 */         this.k = this.j;
/*      */ 
/* 1300 */         lookup();
/* 1301 */         return;
/*      */       }
/*      */ 
/* 1304 */       if (this.word.charAt(this.j) == 'i') {
/* 1305 */         this.word.setLength(this.j);
/* 1306 */         this.k = (this.j - 1);
/* 1307 */         if (lookup()) return;
/* 1308 */         this.word.append("ial");
/* 1309 */         this.k = old_k;
/* 1310 */         lookup();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void iveEndings()
/*      */   {
/* 1322 */     int old_k = this.k;
/*      */ 
/* 1324 */     if (endsIn('i', 'v', 'e')) {
/* 1325 */       this.word.setLength(this.j + 1);
/* 1326 */       this.k = this.j;
/* 1327 */       if (lookup()) return;
/*      */ 
/* 1329 */       this.word.unsafeWrite('e');
/* 1330 */       this.k = (this.j + 1);
/* 1331 */       if (lookup()) return;
/* 1332 */       this.word.setLength(this.j + 1);
/* 1333 */       this.word.append("ive");
/* 1334 */       if ((this.j > 0) && (this.word.charAt(this.j - 1) == 'a') && (this.word.charAt(this.j) == 't')) {
/* 1335 */         this.word.setCharAt(this.j - 1, 'e');
/* 1336 */         this.word.setLength(this.j);
/* 1337 */         this.k = (this.j - 1);
/* 1338 */         if (lookup()) return;
/* 1339 */         this.word.setLength(this.j - 1);
/* 1340 */         if (lookup()) return;
/*      */ 
/* 1342 */         this.word.append("ative");
/* 1343 */         this.k = old_k;
/*      */       }
/*      */ 
/* 1347 */       this.word.setCharAt(this.j + 2, 'o');
/* 1348 */       this.word.setCharAt(this.j + 3, 'n');
/* 1349 */       if (lookup()) return;
/*      */ 
/* 1351 */       this.word.setCharAt(this.j + 2, 'v');
/* 1352 */       this.word.setCharAt(this.j + 3, 'e');
/* 1353 */       this.k = old_k;
/*      */     }
/*      */   }
/*      */ 
/*      */   String stem(String term)
/*      */   {
/* 1362 */     boolean changed = stem(term.toCharArray(), term.length());
/* 1363 */     if (!changed) return term;
/* 1364 */     return asString();
/*      */   }
/*      */ 
/*      */   String asString()
/*      */   {
/* 1371 */     String s = getString();
/* 1372 */     if (s != null) return s;
/* 1373 */     return this.word.toString();
/*      */   }
/*      */ 
/*      */   CharSequence asCharSequence() {
/* 1377 */     return this.result != null ? this.result : this.word;
/*      */   }
/*      */ 
/*      */   String getString() {
/* 1381 */     return this.result;
/*      */   }
/*      */ 
/*      */   char[] getChars() {
/* 1385 */     return this.word.getArray();
/*      */   }
/*      */ 
/*      */   int getLength() {
/* 1389 */     return this.word.length();
/*      */   }
/*      */ 
/*      */   private boolean matched()
/*      */   {
/* 1401 */     return this.matchedEntry != null;
/*      */   }
/*      */ 
/*      */   boolean stem(char[] term, int len)
/*      */   {
/* 1409 */     this.result = null;
/*      */ 
/* 1411 */     this.k = (len - 1);
/* 1412 */     if ((this.k <= 1) || (this.k >= 49)) {
/* 1413 */       return false;
/*      */     }
/*      */ 
/* 1418 */     DictEntry entry = (DictEntry)dict_ht.get(term, 0, len);
/* 1419 */     if (entry != null) {
/* 1420 */       if (entry.root != null) {
/* 1421 */         this.result = entry.root;
/* 1422 */         return true;
/*      */       }
/* 1424 */       return false;
/*      */     }
/*      */ 
/* 1435 */     this.word.reset();
/*      */ 
/* 1437 */     this.word.reserve(len + 10);
/* 1438 */     for (int i = 0; i < len; i++) {
/* 1439 */       char ch = term[i];
/* 1440 */       if (!isAlpha(ch)) return false;
/*      */ 
/* 1443 */       this.word.unsafeWrite(ch);
/*      */     }
/*      */ 
/* 1446 */     this.matchedEntry = null;
/*      */ 
/* 1459 */     plural();
/* 1460 */     if (!matched()) {
/* 1461 */       pastTense();
/* 1462 */       if (!matched()) {
/* 1463 */         aspect();
/* 1464 */         if (!matched()) {
/* 1465 */           ityEndings();
/* 1466 */           if (!matched()) {
/* 1467 */             nessEndings();
/* 1468 */             if (!matched()) {
/* 1469 */               ionEndings();
/* 1470 */               if (!matched()) {
/* 1471 */                 erAndOrEndings();
/* 1472 */                 if (!matched()) {
/* 1473 */                   lyEndings();
/* 1474 */                   if (!matched()) {
/* 1475 */                     alEndings();
/* 1476 */                     if (!matched()) {
/* 1477 */                       entry = wordInDict();
/* 1478 */                       iveEndings();
/* 1479 */                       if (!matched()) {
/* 1480 */                         izeEndings();
/* 1481 */                         if (!matched()) {
/* 1482 */                           mentEndings();
/* 1483 */                           if (!matched()) {
/* 1484 */                             bleEndings();
/* 1485 */                             if (!matched()) {
/* 1486 */                               ismEndings();
/* 1487 */                               if (!matched()) {
/* 1488 */                                 icEndings();
/* 1489 */                                 if (!matched()) {
/* 1490 */                                   ncyEndings();
/* 1491 */                                   if (!matched()) {
/* 1492 */                                     nceEndings();
/* 1493 */                                     matched();
/*      */                                   }
/*      */                                 }
/*      */                               }
/*      */                             }
/*      */                           }
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1501 */     entry = this.matchedEntry;
/* 1502 */     if (entry != null) {
/* 1503 */       this.result = entry.root;
/*      */     }
/*      */ 
/* 1522 */     return true;
/*      */   }
/*      */ 
/*      */   static class DictEntry
/*      */   {
/*      */     boolean exception;
/*      */     String root;
/*      */ 
/*      */     DictEntry(String root, boolean isException)
/*      */     {
/*  231 */       this.root = root;
/*  232 */       this.exception = isException;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.en.KStemmer
 * JD-Core Version:    0.6.2
 */