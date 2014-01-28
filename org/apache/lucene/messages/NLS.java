/*     */ package org.apache.lucene.messages;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class NLS
/*     */ {
/*  48 */   private static Map<String, Class<Object>> bundles = new HashMap(0);
/*     */ 
/*     */   public static String getLocalizedMessage(String key)
/*     */   {
/*  56 */     return getLocalizedMessage(key, Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static String getLocalizedMessage(String key, Locale locale) {
/*  60 */     Object message = getResourceBundleObject(key, locale);
/*  61 */     if (message == null) {
/*  62 */       return "Message with key:" + key + " and locale: " + locale + " not found.";
/*     */     }
/*     */ 
/*  65 */     return message.toString();
/*     */   }
/*     */ 
/*     */   public static String getLocalizedMessage(String key, Locale locale, Object[] args)
/*     */   {
/*  70 */     String str = getLocalizedMessage(key, locale);
/*     */ 
/*  72 */     if (args.length > 0) {
/*  73 */       str = MessageFormat.format(str, args);
/*     */     }
/*     */ 
/*  76 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getLocalizedMessage(String key, Object[] args) {
/*  80 */     return getLocalizedMessage(key, Locale.getDefault(), args);
/*     */   }
/*     */ 
/*     */   protected static void initializeMessages(String bundleName, Class clazz)
/*     */   {
/*     */     try
/*     */     {
/*  95 */       load(clazz);
/*  96 */       if (!bundles.containsKey(bundleName))
/*  97 */         bundles.put(bundleName, clazz);
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object getResourceBundleObject(String messageKey, Locale locale)
/*     */   {
/* 108 */     for (Iterator it = bundles.keySet().iterator(); it.hasNext(); ) {
/* 109 */       Class clazz = (Class)bundles.get(it.next());
/* 110 */       ResourceBundle resourceBundle = ResourceBundle.getBundle(clazz.getName(), locale);
/*     */ 
/* 112 */       if (resourceBundle != null) {
/*     */         try {
/* 114 */           Object obj = resourceBundle.getObject(messageKey);
/* 115 */           if (obj != null)
/* 116 */             return obj;
/*     */         }
/*     */         catch (MissingResourceException e)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   private static void load(Class<Object> clazz)
/*     */   {
/* 130 */     Field[] fieldArray = clazz.getDeclaredFields();
/*     */ 
/* 132 */     boolean isFieldAccessible = (clazz.getModifiers() & 0x1) != 0;
/*     */ 
/* 135 */     int len = fieldArray.length;
/* 136 */     Map fields = new HashMap(len * 2);
/* 137 */     for (int i = 0; i < len; i++) {
/* 138 */       fields.put(fieldArray[i].getName(), fieldArray[i]);
/* 139 */       loadfieldValue(fieldArray[i], isFieldAccessible, clazz);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void loadfieldValue(Field field, boolean isFieldAccessible, Class<Object> clazz)
/*     */   {
/* 149 */     int MOD_EXPECTED = 9;
/* 150 */     int MOD_MASK = MOD_EXPECTED | 0x10;
/* 151 */     if ((field.getModifiers() & MOD_MASK) != MOD_EXPECTED) {
/* 152 */       return;
/*     */     }
/*     */ 
/* 155 */     if (!isFieldAccessible)
/* 156 */       makeAccessible(field);
/*     */     try {
/* 158 */       field.set(null, field.getName());
/* 159 */       validateMessage(field.getName(), clazz);
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/*     */     }
/*     */     catch (IllegalAccessException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void validateMessage(String key, Class<Object> clazz)
/*     */   {
/*     */     try
/*     */     {
/* 174 */       ResourceBundle resourceBundle = ResourceBundle.getBundle(clazz.getName(), Locale.getDefault());
/*     */ 
/* 176 */       if (resourceBundle != null) {
/* 177 */         Object obj = resourceBundle.getObject(key);
/* 178 */         if (obj == null)
/* 179 */           System.err.println("WARN: Message with key:" + key + " and locale: " + Locale.getDefault() + " not found.");
/*     */       }
/*     */     }
/*     */     catch (MissingResourceException e) {
/* 183 */       System.err.println("WARN: Message with key:" + key + " and locale: " + Locale.getDefault() + " not found.");
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void makeAccessible(Field field)
/*     */   {
/* 197 */     if (System.getSecurityManager() == null)
/* 198 */       field.setAccessible(true);
/*     */     else
/* 200 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Object run() {
/* 202 */           this.val$field.setAccessible(true);
/* 203 */           return null;
/*     */         }
/*     */       });
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.messages.NLS
 * JD-Core Version:    0.6.2
 */