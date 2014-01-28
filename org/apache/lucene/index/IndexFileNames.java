/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ final class IndexFileNames
/*     */ {
/*     */   static final String SEGMENTS = "segments";
/*     */   static final String SEGMENTS_GEN = "segments.gen";
/*     */   static final String DELETABLE = "deletable";
/*     */   static final String NORMS_EXTENSION = "nrm";
/*     */   static final String FREQ_EXTENSION = "frq";
/*     */   static final String PROX_EXTENSION = "prx";
/*     */   static final String TERMS_EXTENSION = "tis";
/*     */   static final String TERMS_INDEX_EXTENSION = "tii";
/*     */   static final String FIELDS_INDEX_EXTENSION = "fdx";
/*     */   static final String FIELDS_EXTENSION = "fdt";
/*     */   static final String VECTORS_FIELDS_EXTENSION = "tvf";
/*     */   static final String VECTORS_DOCUMENTS_EXTENSION = "tvd";
/*     */   static final String VECTORS_INDEX_EXTENSION = "tvx";
/*     */   static final String COMPOUND_FILE_EXTENSION = "cfs";
/*     */   static final String COMPOUND_FILE_STORE_EXTENSION = "cfx";
/*     */   static final String DELETES_EXTENSION = "del";
/*     */   static final String FIELD_INFOS_EXTENSION = "fnm";
/*     */   static final String PLAIN_NORMS_EXTENSION = "f";
/*     */   static final String SEPARATE_NORMS_EXTENSION = "s";
/*     */   static final String GEN_EXTENSION = "gen";
/*  94 */   static final String[] INDEX_EXTENSIONS = { "cfs", "fnm", "fdx", "fdt", "tii", "tis", "frq", "prx", "del", "tvx", "tvd", "tvf", "gen", "nrm", "cfx" };
/*     */ 
/* 114 */   static final String[] INDEX_EXTENSIONS_IN_COMPOUND_FILE = { "fnm", "fdx", "fdt", "tii", "tis", "frq", "prx", "tvx", "tvd", "tvf", "nrm" };
/*     */ 
/* 128 */   static final String[] STORE_INDEX_EXTENSIONS = { "tvx", "tvf", "tvd", "fdx", "fdt" };
/*     */ 
/* 136 */   static final String[] NON_STORE_INDEX_EXTENSIONS = { "fnm", "frq", "prx", "tis", "tii", "nrm" };
/*     */ 
/* 146 */   static final String[] COMPOUND_EXTENSIONS = { "fnm", "frq", "prx", "fdx", "fdt", "tii", "tis" };
/*     */ 
/* 157 */   static final String[] VECTOR_EXTENSIONS = { "tvx", "tvd", "tvf" };
/*     */ 
/*     */   static final String fileNameFromGeneration(String base, String extension, long gen)
/*     */   {
/* 174 */     if (gen == -1L)
/* 175 */       return null;
/* 176 */     if (gen == 0L) {
/* 177 */       return base + extension;
/*     */     }
/* 179 */     return base + "_" + Long.toString(gen, 36) + extension;
/*     */   }
/*     */ 
/*     */   static final boolean isDocStoreFile(String fileName)
/*     */   {
/* 189 */     if (fileName.endsWith("cfx"))
/* 190 */       return true;
/* 191 */     for (int i = 0; i < STORE_INDEX_EXTENSIONS.length; i++)
/* 192 */       if (fileName.endsWith(STORE_INDEX_EXTENSIONS[i]))
/* 193 */         return true;
/* 194 */     return false;
/*     */   }
/*     */ 
/*     */   static String segmentFileName(String segmentName, String ext) {
/* 198 */     return segmentName + "." + ext;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexFileNames
 * JD-Core Version:    0.6.2
 */