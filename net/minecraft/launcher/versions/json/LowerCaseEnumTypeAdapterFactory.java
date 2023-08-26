/*    */ package net.minecraft.launcher.versions.json;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonToken;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LowerCaseEnumTypeAdapterFactory
/*    */   implements TypeAdapterFactory
/*    */ {
/*    */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/*    */     // Byte code:
/*    */     //   0: aload_2
/*    */     //   1: invokevirtual getRawType : ()Ljava/lang/Class;
/*    */     //   4: astore_3
/*    */     //   5: aload_3
/*    */     //   6: invokevirtual isEnum : ()Z
/*    */     //   9: ifne -> 14
/*    */     //   12: aconst_null
/*    */     //   13: areturn
/*    */     //   14: new java/util/HashMap
/*    */     //   17: dup
/*    */     //   18: invokespecial <init> : ()V
/*    */     //   21: astore #4
/*    */     //   23: aload_3
/*    */     //   24: invokevirtual getEnumConstants : ()[Ljava/lang/Object;
/*    */     //   27: astore #5
/*    */     //   29: aload #5
/*    */     //   31: arraylength
/*    */     //   32: istore #6
/*    */     //   34: iconst_0
/*    */     //   35: istore #7
/*    */     //   37: iload #7
/*    */     //   39: iload #6
/*    */     //   41: if_icmpge -> 72
/*    */     //   44: aload #5
/*    */     //   46: iload #7
/*    */     //   48: aaload
/*    */     //   49: astore #8
/*    */     //   51: aload #4
/*    */     //   53: aload #8
/*    */     //   55: invokestatic toLowercase : (Ljava/lang/Object;)Ljava/lang/String;
/*    */     //   58: aload #8
/*    */     //   60: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   65: pop
/*    */     //   66: iinc #7, 1
/*    */     //   69: goto -> 37
/*    */     //   72: new net/minecraft/launcher/versions/json/LowerCaseEnumTypeAdapterFactory$1
/*    */     //   75: dup
/*    */     //   76: aload_0
/*    */     //   77: aload #4
/*    */     //   79: invokespecial <init> : (Lnet/minecraft/launcher/versions/json/LowerCaseEnumTypeAdapterFactory;Ljava/util/Map;)V
/*    */     //   82: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     //   #20	-> 5
/*    */     //   #21	-> 12
/*    */     //   #23	-> 14
/*    */     //   #25	-> 23
/*    */     //   #26	-> 51
/*    */     //   #25	-> 66
/*    */     //   #29	-> 72
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   51	15	8	constant	Ljava/lang/Object;
/*    */     //   0	83	0	this	Lnet/minecraft/launcher/versions/json/LowerCaseEnumTypeAdapterFactory;
/*    */     //   0	83	1	gson	Lcom/google/gson/Gson;
/*    */     //   0	83	2	type	Lcom/google/gson/reflect/TypeToken;
/*    */     //   5	78	3	rawType	Ljava/lang/Class;
/*    */     //   23	60	4	lowercaseToConstant	Ljava/util/Map;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	83	2	type	Lcom/google/gson/reflect/TypeToken<TT;>;
/*    */     //   5	78	3	rawType	Ljava/lang/Class<*>;
/*    */     //   23	60	4	lowercaseToConstant	Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;
/*    */   }
/*    */   
/*    */   private static String toLowercase(Object o) {
/* 51 */     return o.toString().toLowerCase(Locale.US);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\json\LowerCaseEnumTypeAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */