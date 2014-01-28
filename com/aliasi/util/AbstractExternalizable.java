/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Externalizable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class AbstractExternalizable
/*     */   implements Externalizable
/*     */ {
/*     */   static final long serialVersionUID = -3880451086025138660L;
/*     */   private Object mObjectRead;
/*     */ 
/*     */   protected abstract Object read(ObjectInput paramObjectInput)
/*     */     throws ClassNotFoundException, IOException;
/*     */ 
/*     */   public abstract void writeExternal(ObjectOutput paramObjectOutput)
/*     */     throws IOException;
/*     */ 
/*     */   public final void readExternal(ObjectInput objIn)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 141 */     this.mObjectRead = read(objIn);
/*     */   }
/*     */ 
/*     */   protected Object readResolve()
/*     */   {
/* 153 */     return this.mObjectRead;
/*     */   }
/*     */ 
/*     */   public static void compileTo(Compilable compilable, File file)
/*     */     throws IOException
/*     */   {
/* 166 */     FileOutputStream fileOut = null;
/* 167 */     BufferedOutputStream bufOut = null;
/* 168 */     ObjectOutputStream objOut = null;
/*     */     try {
/* 170 */       fileOut = new FileOutputStream(file);
/* 171 */       bufOut = new BufferedOutputStream(fileOut);
/* 172 */       objOut = new ObjectOutputStream(bufOut);
/* 173 */       compilable.compileTo(objOut);
/*     */     } finally {
/* 175 */       Streams.closeOutputStream(objOut);
/* 176 */       Streams.closeOutputStream(bufOut);
/* 177 */       Streams.closeOutputStream(fileOut);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void serializeTo(Serializable serializable, File file)
/*     */     throws IOException
/*     */   {
/* 193 */     FileOutputStream fileOut = null;
/* 194 */     BufferedOutputStream bufOut = null;
/* 195 */     ObjectOutputStream objOut = null;
/*     */     try {
/* 197 */       fileOut = new FileOutputStream(file);
/* 198 */       bufOut = new BufferedOutputStream(fileOut);
/* 199 */       objOut = new ObjectOutputStream(bufOut);
/* 200 */       objOut.writeObject(serializable);
/*     */     } finally {
/* 202 */       Streams.closeOutputStream(objOut);
/* 203 */       Streams.closeOutputStream(bufOut);
/* 204 */       Streams.closeOutputStream(fileOut);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void serializeOrCompile(Object obj, ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 220 */     if ((obj instanceof Serializable)) {
/* 221 */       out.writeObject(obj);
/* 222 */     } else if ((obj instanceof Compilable)) {
/* 223 */       ((Compilable)obj).compileTo(out);
/*     */     } else {
/* 225 */       String msg = "Object must be compilable or serializable. Found object with class=" + obj.getClass();
/*     */ 
/* 227 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void compileOrSerialize(Object obj, ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 246 */     if ((obj instanceof Compilable)) {
/* 247 */       ((Compilable)obj).compileTo(out);
/* 248 */     } else if ((obj instanceof Serializable)) {
/* 249 */       out.writeObject(obj);
/*     */     } else {
/* 251 */       String msg = "Object must be compilable or serializable. Found object with class=" + obj.getClass();
/*     */ 
/* 253 */       throw new NotSerializableException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object readObject(File file)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 303 */     FileInputStream fileIn = null;
/* 304 */     BufferedInputStream bufIn = null;
/* 305 */     ObjectInputStream objIn = null;
/*     */     try {
/* 307 */       fileIn = new FileInputStream(file);
/* 308 */       bufIn = new BufferedInputStream(fileIn);
/* 309 */       objIn = new ObjectInputStream(bufIn);
/* 310 */       return objIn.readObject();
/*     */     } finally {
/* 312 */       Streams.closeInputStream(objIn);
/* 313 */       Streams.closeInputStream(bufIn);
/* 314 */       Streams.closeInputStream(fileIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object readResourceObject(Class<?> clazz, String resourcePathName)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 342 */     InputStream in = null;
/* 343 */     ObjectInputStream objIn = null;
/*     */     try {
/* 345 */       in = clazz.getResourceAsStream(resourcePathName);
/* 346 */       objIn = new ObjectInputStream(in);
/* 347 */       return objIn.readObject();
/*     */     } finally {
/* 349 */       Streams.closeInputStream(objIn);
/* 350 */       Streams.closeInputStream(in);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object readResourceObject(String resourceAbsolutePathName)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 375 */     if (!resourceAbsolutePathName.startsWith("/")) {
/* 376 */       String msg = "This method requires an absolute resource name starting with a forward slash (/) Found resourcePathName=" + resourceAbsolutePathName;
/*     */ 
/* 378 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 380 */     return readResourceObject(AbstractExternalizable.class, resourceAbsolutePathName);
/*     */   }
/*     */ 
/*     */   public static Object compile(Compilable c)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 411 */     ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/* 412 */     ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
/* 413 */     c.compileTo(objOut);
/* 414 */     ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
/*     */ 
/* 416 */     ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*     */     try {
/* 418 */       return objIn.readObject();
/*     */     } catch (ClassNotFoundException e) {
/* 420 */       String msg = "Compile i/o class not found exception=" + e;
/* 421 */       throw new IOException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object serializeDeserialize(Serializable s)
/*     */     throws IOException
/*     */   {
/* 445 */     ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/* 446 */     ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
/* 447 */     objOut.writeObject(s);
/* 448 */     ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
/*     */ 
/* 450 */     ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*     */     try {
/* 452 */       return objIn.readObject();
/*     */     } catch (ClassNotFoundException e) {
/* 454 */       String msg = "Compile i/o class not found exception=" + e;
/* 455 */       throw new IOException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void writeInts(int[] xs, ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 470 */     objOut.writeInt(xs.length);
/* 471 */     for (int i = 0; i < xs.length; i++)
/* 472 */       objOut.writeInt(xs[i]);
/*     */   }
/*     */ 
/*     */   public static int[] readInts(ObjectInput objIn)
/*     */     throws IOException
/*     */   {
/* 486 */     int[] xs = new int[objIn.readInt()];
/* 487 */     for (int i = 0; i < xs.length; i++)
/* 488 */       xs[i] = objIn.readInt();
/* 489 */     return xs;
/*     */   }
/*     */ 
/*     */   public static void writeFloats(float[] xs, ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 503 */     objOut.writeInt(xs.length);
/* 504 */     for (int i = 0; i < xs.length; i++)
/* 505 */       objOut.writeFloat(xs[i]);
/*     */   }
/*     */ 
/*     */   public static void writeDoubles(double[] xs, ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 519 */     objOut.writeInt(xs.length);
/* 520 */     for (int i = 0; i < xs.length; i++)
/* 521 */       objOut.writeDouble(xs[i]);
/*     */   }
/*     */ 
/*     */   public static float[] readFloats(ObjectInput objIn)
/*     */     throws IOException
/*     */   {
/* 535 */     float[] xs = new float[objIn.readInt()];
/* 536 */     for (int i = 0; i < xs.length; i++)
/* 537 */       xs[i] = objIn.readFloat();
/* 538 */     return xs;
/*     */   }
/*     */ 
/*     */   public static double[] readDoubles(ObjectInput objIn)
/*     */     throws IOException
/*     */   {
/* 552 */     double[] xs = new double[objIn.readInt()];
/* 553 */     for (int i = 0; i < xs.length; i++)
/* 554 */       xs[i] = objIn.readDouble();
/* 555 */     return xs;
/*     */   }
/*     */ 
/*     */   public static void writeUTFs(String[] xs, ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 569 */     objOut.writeInt(xs.length);
/* 570 */     for (int i = 0; i < xs.length; i++)
/* 571 */       objOut.writeUTF(xs[i]);
/*     */   }
/*     */ 
/*     */   public static String[] readUTFs(ObjectInput objIn)
/*     */     throws IOException
/*     */   {
/* 585 */     String[] xs = new String[objIn.readInt()];
/* 586 */     for (int i = 0; i < xs.length; i++)
/* 587 */       xs[i] = objIn.readUTF();
/* 588 */     return xs;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.AbstractExternalizable
 * JD-Core Version:    0.6.2
 */