/*     */ package org.apache.lucene.document;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class DateTools
/*     */ {
/*  54 */   private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */ 
/*  56 */   private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.US);
/*  57 */   private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyyMM", Locale.US);
/*  58 */   private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.US);
/*  59 */   private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("yyyyMMddHH", Locale.US);
/*  60 */   private static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm", Locale.US);
/*  61 */   private static final SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
/*  62 */   private static final SimpleDateFormat MILLISECOND_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US);
/*     */ 
/*  75 */   private static final Calendar calInstance = Calendar.getInstance(GMT);
/*     */ 
/*     */   public static synchronized String dateToString(Date date, Resolution resolution)
/*     */   {
/*  90 */     return timeToString(date.getTime(), resolution);
/*     */   }
/*     */ 
/*     */   public static synchronized String timeToString(long time, Resolution resolution)
/*     */   {
/* 103 */     calInstance.setTimeInMillis(round(time, resolution));
/* 104 */     Date date = calInstance.getTime();
/*     */ 
/* 106 */     if (resolution == Resolution.YEAR)
/* 107 */       return YEAR_FORMAT.format(date);
/* 108 */     if (resolution == Resolution.MONTH)
/* 109 */       return MONTH_FORMAT.format(date);
/* 110 */     if (resolution == Resolution.DAY)
/* 111 */       return DAY_FORMAT.format(date);
/* 112 */     if (resolution == Resolution.HOUR)
/* 113 */       return HOUR_FORMAT.format(date);
/* 114 */     if (resolution == Resolution.MINUTE)
/* 115 */       return MINUTE_FORMAT.format(date);
/* 116 */     if (resolution == Resolution.SECOND)
/* 117 */       return SECOND_FORMAT.format(date);
/* 118 */     if (resolution == Resolution.MILLISECOND) {
/* 119 */       return MILLISECOND_FORMAT.format(date);
/*     */     }
/*     */ 
/* 122 */     throw new IllegalArgumentException("unknown resolution " + resolution);
/*     */   }
/*     */ 
/*     */   public static synchronized long stringToTime(String dateString)
/*     */     throws ParseException
/*     */   {
/* 136 */     return stringToDate(dateString).getTime();
/*     */   }
/*     */ 
/*     */   public static synchronized Date stringToDate(String dateString)
/*     */     throws ParseException
/*     */   {
/* 150 */     if (dateString.length() == 4)
/* 151 */       return YEAR_FORMAT.parse(dateString);
/* 152 */     if (dateString.length() == 6)
/* 153 */       return MONTH_FORMAT.parse(dateString);
/* 154 */     if (dateString.length() == 8)
/* 155 */       return DAY_FORMAT.parse(dateString);
/* 156 */     if (dateString.length() == 10)
/* 157 */       return HOUR_FORMAT.parse(dateString);
/* 158 */     if (dateString.length() == 12)
/* 159 */       return MINUTE_FORMAT.parse(dateString);
/* 160 */     if (dateString.length() == 14)
/* 161 */       return SECOND_FORMAT.parse(dateString);
/* 162 */     if (dateString.length() == 17) {
/* 163 */       return MILLISECOND_FORMAT.parse(dateString);
/*     */     }
/* 165 */     throw new ParseException("Input is not valid date string: " + dateString, 0);
/*     */   }
/*     */ 
/*     */   public static synchronized Date round(Date date, Resolution resolution)
/*     */   {
/* 178 */     return new Date(round(date.getTime(), resolution));
/*     */   }
/*     */ 
/*     */   public static synchronized long round(long time, Resolution resolution)
/*     */   {
/* 192 */     calInstance.setTimeInMillis(time);
/*     */ 
/* 194 */     if (resolution == Resolution.YEAR) {
/* 195 */       calInstance.set(2, 0);
/* 196 */       calInstance.set(5, 1);
/* 197 */       calInstance.set(11, 0);
/* 198 */       calInstance.set(12, 0);
/* 199 */       calInstance.set(13, 0);
/* 200 */       calInstance.set(14, 0);
/* 201 */     } else if (resolution == Resolution.MONTH) {
/* 202 */       calInstance.set(5, 1);
/* 203 */       calInstance.set(11, 0);
/* 204 */       calInstance.set(12, 0);
/* 205 */       calInstance.set(13, 0);
/* 206 */       calInstance.set(14, 0);
/* 207 */     } else if (resolution == Resolution.DAY) {
/* 208 */       calInstance.set(11, 0);
/* 209 */       calInstance.set(12, 0);
/* 210 */       calInstance.set(13, 0);
/* 211 */       calInstance.set(14, 0);
/* 212 */     } else if (resolution == Resolution.HOUR) {
/* 213 */       calInstance.set(12, 0);
/* 214 */       calInstance.set(13, 0);
/* 215 */       calInstance.set(14, 0);
/* 216 */     } else if (resolution == Resolution.MINUTE) {
/* 217 */       calInstance.set(13, 0);
/* 218 */       calInstance.set(14, 0);
/* 219 */     } else if (resolution == Resolution.SECOND) {
/* 220 */       calInstance.set(14, 0);
/* 221 */     } else if (resolution != Resolution.MILLISECOND)
/*     */     {
/* 224 */       throw new IllegalArgumentException("unknown resolution " + resolution);
/*     */     }
/* 226 */     return calInstance.getTimeInMillis();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  66 */     YEAR_FORMAT.setTimeZone(GMT);
/*  67 */     MONTH_FORMAT.setTimeZone(GMT);
/*  68 */     DAY_FORMAT.setTimeZone(GMT);
/*  69 */     HOUR_FORMAT.setTimeZone(GMT);
/*  70 */     MINUTE_FORMAT.setTimeZone(GMT);
/*  71 */     SECOND_FORMAT.setTimeZone(GMT);
/*  72 */     MILLISECOND_FORMAT.setTimeZone(GMT);
/*     */   }
/*     */ 
/*     */   public static class Resolution
/*     */   {
/* 232 */     public static final Resolution YEAR = new Resolution("year");
/* 233 */     public static final Resolution MONTH = new Resolution("month");
/* 234 */     public static final Resolution DAY = new Resolution("day");
/* 235 */     public static final Resolution HOUR = new Resolution("hour");
/* 236 */     public static final Resolution MINUTE = new Resolution("minute");
/* 237 */     public static final Resolution SECOND = new Resolution("second");
/* 238 */     public static final Resolution MILLISECOND = new Resolution("millisecond");
/*     */     private String resolution;
/*     */ 
/*     */     private Resolution()
/*     */     {
/*     */     }
/*     */ 
/*     */     private Resolution(String resolution)
/*     */     {
/* 246 */       this.resolution = resolution;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 251 */       return this.resolution;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.DateTools
 * JD-Core Version:    0.6.2
 */