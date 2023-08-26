/*     */ package com.google.gson.internal;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class $Gson$Types
/*     */ {
/*  45 */   static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
/*     */   
/*     */   private $Gson$Types() {
/*  48 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
/*  59 */     return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GenericArrayType arrayOf(Type componentType) {
/*  69 */     return new GenericArrayTypeImpl(componentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WildcardType subtypeOf(Type bound) {
/*     */     Type[] upperBounds;
/*  80 */     if (bound instanceof WildcardType) {
/*  81 */       upperBounds = ((WildcardType)bound).getUpperBounds();
/*     */     } else {
/*  83 */       upperBounds = new Type[] { bound };
/*     */     } 
/*  85 */     return new WildcardTypeImpl(upperBounds, EMPTY_TYPE_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WildcardType supertypeOf(Type bound) {
/*     */     Type[] lowerBounds;
/*  95 */     if (bound instanceof WildcardType) {
/*  96 */       lowerBounds = ((WildcardType)bound).getLowerBounds();
/*     */     } else {
/*  98 */       lowerBounds = new Type[] { bound };
/*     */     } 
/* 100 */     return new WildcardTypeImpl(new Type[] { Object.class }, lowerBounds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type canonicalize(Type type) {
/* 109 */     if (type instanceof Class) {
/* 110 */       Class<?> c = (Class)type;
/* 111 */       return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;
/*     */     } 
/* 113 */     if (type instanceof ParameterizedType) {
/* 114 */       ParameterizedType p = (ParameterizedType)type;
/* 115 */       return new ParameterizedTypeImpl(p.getOwnerType(), p
/* 116 */           .getRawType(), p.getActualTypeArguments());
/*     */     } 
/* 118 */     if (type instanceof GenericArrayType) {
/* 119 */       GenericArrayType g = (GenericArrayType)type;
/* 120 */       return new GenericArrayTypeImpl(g.getGenericComponentType());
/*     */     } 
/* 122 */     if (type instanceof WildcardType) {
/* 123 */       WildcardType w = (WildcardType)type;
/* 124 */       return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
/*     */     } 
/*     */ 
/*     */     
/* 128 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class<?> getRawType(Type type) {
/* 133 */     if (type instanceof Class)
/*     */     {
/* 135 */       return (Class)type;
/*     */     }
/* 137 */     if (type instanceof ParameterizedType) {
/* 138 */       ParameterizedType parameterizedType = (ParameterizedType)type;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       Type rawType = parameterizedType.getRawType();
/* 144 */       $Gson$Preconditions.checkArgument(rawType instanceof Class);
/* 145 */       return (Class)rawType;
/*     */     } 
/* 147 */     if (type instanceof GenericArrayType) {
/* 148 */       Type componentType = ((GenericArrayType)type).getGenericComponentType();
/* 149 */       return Array.newInstance(getRawType(componentType), 0).getClass();
/*     */     } 
/* 151 */     if (type instanceof TypeVariable)
/*     */     {
/*     */       
/* 154 */       return Object.class;
/*     */     }
/* 156 */     if (type instanceof WildcardType) {
/* 157 */       return getRawType(((WildcardType)type).getUpperBounds()[0]);
/*     */     }
/*     */     
/* 160 */     String className = (type == null) ? "null" : type.getClass().getName();
/* 161 */     throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean equal(Object a, Object b) {
/* 167 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Type a, Type b) {
/* 174 */     if (a == b)
/*     */     {
/* 176 */       return true;
/*     */     }
/* 178 */     if (a instanceof Class)
/*     */     {
/* 180 */       return a.equals(b);
/*     */     }
/* 182 */     if (a instanceof ParameterizedType) {
/* 183 */       if (!(b instanceof ParameterizedType)) {
/* 184 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 188 */       ParameterizedType pa = (ParameterizedType)a;
/* 189 */       ParameterizedType pb = (ParameterizedType)b;
/* 190 */       return (equal(pa.getOwnerType(), pb.getOwnerType()) && pa
/* 191 */         .getRawType().equals(pb.getRawType()) && 
/* 192 */         Arrays.equals((Object[])pa.getActualTypeArguments(), (Object[])pb.getActualTypeArguments()));
/*     */     } 
/* 194 */     if (a instanceof GenericArrayType) {
/* 195 */       if (!(b instanceof GenericArrayType)) {
/* 196 */         return false;
/*     */       }
/*     */       
/* 199 */       GenericArrayType ga = (GenericArrayType)a;
/* 200 */       GenericArrayType gb = (GenericArrayType)b;
/* 201 */       return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
/*     */     } 
/* 203 */     if (a instanceof WildcardType) {
/* 204 */       if (!(b instanceof WildcardType)) {
/* 205 */         return false;
/*     */       }
/*     */       
/* 208 */       WildcardType wa = (WildcardType)a;
/* 209 */       WildcardType wb = (WildcardType)b;
/* 210 */       return (Arrays.equals((Object[])wa.getUpperBounds(), (Object[])wb.getUpperBounds()) && 
/* 211 */         Arrays.equals((Object[])wa.getLowerBounds(), (Object[])wb.getLowerBounds()));
/*     */     } 
/* 213 */     if (a instanceof TypeVariable) {
/* 214 */       if (!(b instanceof TypeVariable)) {
/* 215 */         return false;
/*     */       }
/* 217 */       TypeVariable<?> va = (TypeVariable)a;
/* 218 */       TypeVariable<?> vb = (TypeVariable)b;
/* 219 */       return (va.getGenericDeclaration() == vb.getGenericDeclaration() && va
/* 220 */         .getName().equals(vb.getName()));
/*     */     } 
/*     */ 
/*     */     
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static int hashCodeOrZero(Object o) {
/* 229 */     return (o != null) ? o.hashCode() : 0;
/*     */   }
/*     */   
/*     */   public static String typeToString(Type type) {
/* 233 */     return (type instanceof Class) ? ((Class)type).getName() : type.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
/* 242 */     if (toResolve == rawType) {
/* 243 */       return context;
/*     */     }
/*     */ 
/*     */     
/* 247 */     if (toResolve.isInterface()) {
/* 248 */       Class<?>[] interfaces = rawType.getInterfaces();
/* 249 */       for (int i = 0, length = interfaces.length; i < length; i++) {
/* 250 */         if (interfaces[i] == toResolve)
/* 251 */           return rawType.getGenericInterfaces()[i]; 
/* 252 */         if (toResolve.isAssignableFrom(interfaces[i])) {
/* 253 */           return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 259 */     if (!rawType.isInterface()) {
/* 260 */       while (rawType != Object.class) {
/* 261 */         Class<?> rawSupertype = rawType.getSuperclass();
/* 262 */         if (rawSupertype == toResolve)
/* 263 */           return rawType.getGenericSuperclass(); 
/* 264 */         if (toResolve.isAssignableFrom(rawSupertype)) {
/* 265 */           return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
/*     */         }
/* 267 */         rawType = rawSupertype;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 272 */     return toResolve;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
/* 283 */     if (context instanceof WildcardType)
/*     */     {
/* 285 */       context = ((WildcardType)context).getUpperBounds()[0];
/*     */     }
/* 287 */     $Gson$Preconditions.checkArgument(supertype.isAssignableFrom(contextRawType));
/* 288 */     return resolve(context, contextRawType, 
/* 289 */         getGenericSupertype(context, contextRawType, supertype));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getArrayComponentType(Type array) {
/* 297 */     return (array instanceof GenericArrayType) ? (
/* 298 */       (GenericArrayType)array).getGenericComponentType() : (
/* 299 */       (Class)array).getComponentType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getCollectionElementType(Type context, Class<?> contextRawType) {
/* 307 */     Type collectionType = getSupertype(context, contextRawType, Collection.class);
/*     */     
/* 309 */     if (collectionType instanceof WildcardType) {
/* 310 */       collectionType = ((WildcardType)collectionType).getUpperBounds()[0];
/*     */     }
/* 312 */     if (collectionType instanceof ParameterizedType) {
/* 313 */       return ((ParameterizedType)collectionType).getActualTypeArguments()[0];
/*     */     }
/* 315 */     return Object.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type[] getMapKeyAndValueTypes(Type context, Class<?> contextRawType) {
/* 328 */     if (context == Properties.class) {
/* 329 */       return new Type[] { String.class, String.class };
/*     */     }
/*     */     
/* 332 */     Type mapType = getSupertype(context, contextRawType, Map.class);
/*     */     
/* 334 */     if (mapType instanceof ParameterizedType) {
/* 335 */       ParameterizedType mapParameterizedType = (ParameterizedType)mapType;
/* 336 */       return mapParameterizedType.getActualTypeArguments();
/*     */     } 
/* 338 */     return new Type[] { Object.class, Object.class };
/*     */   }
/*     */   
/*     */   public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
/* 342 */     return resolve(context, contextRawType, toResolve, new HashMap<TypeVariable<?>, Type>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Type resolve(Type context, Class<?> contextRawType, Type toResolve, Map<TypeVariable<?>, Type> visitedTypeVariables) {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore #4
/*     */     //   3: aload_2
/*     */     //   4: instanceof java/lang/reflect/TypeVariable
/*     */     //   7: ifeq -> 90
/*     */     //   10: aload_2
/*     */     //   11: checkcast java/lang/reflect/TypeVariable
/*     */     //   14: astore #5
/*     */     //   16: aload_3
/*     */     //   17: aload #5
/*     */     //   19: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   24: checkcast java/lang/reflect/Type
/*     */     //   27: astore #6
/*     */     //   29: aload #6
/*     */     //   31: ifnull -> 49
/*     */     //   34: aload #6
/*     */     //   36: getstatic java/lang/Void.TYPE : Ljava/lang/Class;
/*     */     //   39: if_acmpne -> 46
/*     */     //   42: aload_2
/*     */     //   43: goto -> 48
/*     */     //   46: aload #6
/*     */     //   48: areturn
/*     */     //   49: aload_3
/*     */     //   50: aload #5
/*     */     //   52: getstatic java/lang/Void.TYPE : Ljava/lang/Class;
/*     */     //   55: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   60: pop
/*     */     //   61: aload #4
/*     */     //   63: ifnonnull -> 70
/*     */     //   66: aload #5
/*     */     //   68: astore #4
/*     */     //   70: aload_0
/*     */     //   71: aload_1
/*     */     //   72: aload #5
/*     */     //   74: invokestatic resolveTypeVariable : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/reflect/TypeVariable;)Ljava/lang/reflect/Type;
/*     */     //   77: astore_2
/*     */     //   78: aload_2
/*     */     //   79: aload #5
/*     */     //   81: if_acmpne -> 87
/*     */     //   84: goto -> 482
/*     */     //   87: goto -> 3
/*     */     //   90: aload_2
/*     */     //   91: instanceof java/lang/Class
/*     */     //   94: ifeq -> 154
/*     */     //   97: aload_2
/*     */     //   98: checkcast java/lang/Class
/*     */     //   101: invokevirtual isArray : ()Z
/*     */     //   104: ifeq -> 154
/*     */     //   107: aload_2
/*     */     //   108: checkcast java/lang/Class
/*     */     //   111: astore #5
/*     */     //   113: aload #5
/*     */     //   115: invokevirtual getComponentType : ()Ljava/lang/Class;
/*     */     //   118: astore #6
/*     */     //   120: aload_0
/*     */     //   121: aload_1
/*     */     //   122: aload #6
/*     */     //   124: aload_3
/*     */     //   125: invokestatic resolve : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/Map;)Ljava/lang/reflect/Type;
/*     */     //   128: astore #7
/*     */     //   130: aload #6
/*     */     //   132: aload #7
/*     */     //   134: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   137: ifeq -> 145
/*     */     //   140: aload #5
/*     */     //   142: goto -> 150
/*     */     //   145: aload #7
/*     */     //   147: invokestatic arrayOf : (Ljava/lang/reflect/Type;)Ljava/lang/reflect/GenericArrayType;
/*     */     //   150: astore_2
/*     */     //   151: goto -> 482
/*     */     //   154: aload_2
/*     */     //   155: instanceof java/lang/reflect/GenericArrayType
/*     */     //   158: ifeq -> 210
/*     */     //   161: aload_2
/*     */     //   162: checkcast java/lang/reflect/GenericArrayType
/*     */     //   165: astore #5
/*     */     //   167: aload #5
/*     */     //   169: invokeinterface getGenericComponentType : ()Ljava/lang/reflect/Type;
/*     */     //   174: astore #6
/*     */     //   176: aload_0
/*     */     //   177: aload_1
/*     */     //   178: aload #6
/*     */     //   180: aload_3
/*     */     //   181: invokestatic resolve : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/Map;)Ljava/lang/reflect/Type;
/*     */     //   184: astore #7
/*     */     //   186: aload #6
/*     */     //   188: aload #7
/*     */     //   190: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   193: ifeq -> 201
/*     */     //   196: aload #5
/*     */     //   198: goto -> 206
/*     */     //   201: aload #7
/*     */     //   203: invokestatic arrayOf : (Ljava/lang/reflect/Type;)Ljava/lang/reflect/GenericArrayType;
/*     */     //   206: astore_2
/*     */     //   207: goto -> 482
/*     */     //   210: aload_2
/*     */     //   211: instanceof java/lang/reflect/ParameterizedType
/*     */     //   214: ifeq -> 368
/*     */     //   217: aload_2
/*     */     //   218: checkcast java/lang/reflect/ParameterizedType
/*     */     //   221: astore #5
/*     */     //   223: aload #5
/*     */     //   225: invokeinterface getOwnerType : ()Ljava/lang/reflect/Type;
/*     */     //   230: astore #6
/*     */     //   232: aload_0
/*     */     //   233: aload_1
/*     */     //   234: aload #6
/*     */     //   236: aload_3
/*     */     //   237: invokestatic resolve : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/Map;)Ljava/lang/reflect/Type;
/*     */     //   240: astore #7
/*     */     //   242: aload #7
/*     */     //   244: aload #6
/*     */     //   246: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   249: ifne -> 256
/*     */     //   252: iconst_1
/*     */     //   253: goto -> 257
/*     */     //   256: iconst_0
/*     */     //   257: istore #8
/*     */     //   259: aload #5
/*     */     //   261: invokeinterface getActualTypeArguments : ()[Ljava/lang/reflect/Type;
/*     */     //   266: astore #9
/*     */     //   268: iconst_0
/*     */     //   269: istore #10
/*     */     //   271: aload #9
/*     */     //   273: arraylength
/*     */     //   274: istore #11
/*     */     //   276: iload #10
/*     */     //   278: iload #11
/*     */     //   280: if_icmpge -> 340
/*     */     //   283: aload_0
/*     */     //   284: aload_1
/*     */     //   285: aload #9
/*     */     //   287: iload #10
/*     */     //   289: aaload
/*     */     //   290: aload_3
/*     */     //   291: invokestatic resolve : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/Map;)Ljava/lang/reflect/Type;
/*     */     //   294: astore #12
/*     */     //   296: aload #12
/*     */     //   298: aload #9
/*     */     //   300: iload #10
/*     */     //   302: aaload
/*     */     //   303: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   306: ifne -> 334
/*     */     //   309: iload #8
/*     */     //   311: ifne -> 327
/*     */     //   314: aload #9
/*     */     //   316: invokevirtual clone : ()Ljava/lang/Object;
/*     */     //   319: checkcast [Ljava/lang/reflect/Type;
/*     */     //   322: astore #9
/*     */     //   324: iconst_1
/*     */     //   325: istore #8
/*     */     //   327: aload #9
/*     */     //   329: iload #10
/*     */     //   331: aload #12
/*     */     //   333: aastore
/*     */     //   334: iinc #10, 1
/*     */     //   337: goto -> 276
/*     */     //   340: iload #8
/*     */     //   342: ifeq -> 362
/*     */     //   345: aload #7
/*     */     //   347: aload #5
/*     */     //   349: invokeinterface getRawType : ()Ljava/lang/reflect/Type;
/*     */     //   354: aload #9
/*     */     //   356: invokestatic newParameterizedTypeWithOwner : (Ljava/lang/reflect/Type;Ljava/lang/reflect/Type;[Ljava/lang/reflect/Type;)Ljava/lang/reflect/ParameterizedType;
/*     */     //   359: goto -> 364
/*     */     //   362: aload #5
/*     */     //   364: astore_2
/*     */     //   365: goto -> 482
/*     */     //   368: aload_2
/*     */     //   369: instanceof java/lang/reflect/WildcardType
/*     */     //   372: ifeq -> 482
/*     */     //   375: aload_2
/*     */     //   376: checkcast java/lang/reflect/WildcardType
/*     */     //   379: astore #5
/*     */     //   381: aload #5
/*     */     //   383: invokeinterface getLowerBounds : ()[Ljava/lang/reflect/Type;
/*     */     //   388: astore #6
/*     */     //   390: aload #5
/*     */     //   392: invokeinterface getUpperBounds : ()[Ljava/lang/reflect/Type;
/*     */     //   397: astore #7
/*     */     //   399: aload #6
/*     */     //   401: arraylength
/*     */     //   402: iconst_1
/*     */     //   403: if_icmpne -> 439
/*     */     //   406: aload_0
/*     */     //   407: aload_1
/*     */     //   408: aload #6
/*     */     //   410: iconst_0
/*     */     //   411: aaload
/*     */     //   412: aload_3
/*     */     //   413: invokestatic resolve : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/Map;)Ljava/lang/reflect/Type;
/*     */     //   416: astore #8
/*     */     //   418: aload #8
/*     */     //   420: aload #6
/*     */     //   422: iconst_0
/*     */     //   423: aaload
/*     */     //   424: if_acmpeq -> 436
/*     */     //   427: aload #8
/*     */     //   429: invokestatic supertypeOf : (Ljava/lang/reflect/Type;)Ljava/lang/reflect/WildcardType;
/*     */     //   432: astore_2
/*     */     //   433: goto -> 482
/*     */     //   436: goto -> 476
/*     */     //   439: aload #7
/*     */     //   441: arraylength
/*     */     //   442: iconst_1
/*     */     //   443: if_icmpne -> 476
/*     */     //   446: aload_0
/*     */     //   447: aload_1
/*     */     //   448: aload #7
/*     */     //   450: iconst_0
/*     */     //   451: aaload
/*     */     //   452: aload_3
/*     */     //   453: invokestatic resolve : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/Map;)Ljava/lang/reflect/Type;
/*     */     //   456: astore #8
/*     */     //   458: aload #8
/*     */     //   460: aload #7
/*     */     //   462: iconst_0
/*     */     //   463: aaload
/*     */     //   464: if_acmpeq -> 476
/*     */     //   467: aload #8
/*     */     //   469: invokestatic subtypeOf : (Ljava/lang/reflect/Type;)Ljava/lang/reflect/WildcardType;
/*     */     //   472: astore_2
/*     */     //   473: goto -> 482
/*     */     //   476: aload #5
/*     */     //   478: astore_2
/*     */     //   479: goto -> 482
/*     */     //   482: aload #4
/*     */     //   484: ifnull -> 497
/*     */     //   487: aload_3
/*     */     //   488: aload #4
/*     */     //   490: aload_2
/*     */     //   491: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   496: pop
/*     */     //   497: aload_2
/*     */     //   498: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #348	-> 0
/*     */     //   #350	-> 3
/*     */     //   #351	-> 10
/*     */     //   #352	-> 16
/*     */     //   #353	-> 29
/*     */     //   #355	-> 34
/*     */     //   #359	-> 49
/*     */     //   #360	-> 61
/*     */     //   #361	-> 66
/*     */     //   #364	-> 70
/*     */     //   #365	-> 78
/*     */     //   #366	-> 84
/*     */     //   #369	-> 87
/*     */     //   #370	-> 107
/*     */     //   #371	-> 113
/*     */     //   #372	-> 120
/*     */     //   #373	-> 130
/*     */     //   #374	-> 140
/*     */     //   #375	-> 145
/*     */     //   #376	-> 151
/*     */     //   #378	-> 154
/*     */     //   #379	-> 161
/*     */     //   #380	-> 167
/*     */     //   #381	-> 176
/*     */     //   #382	-> 186
/*     */     //   #383	-> 196
/*     */     //   #384	-> 201
/*     */     //   #385	-> 207
/*     */     //   #387	-> 210
/*     */     //   #388	-> 217
/*     */     //   #389	-> 223
/*     */     //   #390	-> 232
/*     */     //   #391	-> 242
/*     */     //   #393	-> 259
/*     */     //   #394	-> 268
/*     */     //   #395	-> 283
/*     */     //   #396	-> 296
/*     */     //   #397	-> 309
/*     */     //   #398	-> 314
/*     */     //   #399	-> 324
/*     */     //   #401	-> 327
/*     */     //   #394	-> 334
/*     */     //   #405	-> 340
/*     */     //   #406	-> 345
/*     */     //   #407	-> 362
/*     */     //   #408	-> 365
/*     */     //   #410	-> 368
/*     */     //   #411	-> 375
/*     */     //   #412	-> 381
/*     */     //   #413	-> 390
/*     */     //   #415	-> 399
/*     */     //   #416	-> 406
/*     */     //   #417	-> 418
/*     */     //   #418	-> 427
/*     */     //   #419	-> 433
/*     */     //   #421	-> 436
/*     */     //   #422	-> 446
/*     */     //   #423	-> 458
/*     */     //   #424	-> 467
/*     */     //   #425	-> 473
/*     */     //   #428	-> 476
/*     */     //   #429	-> 479
/*     */     //   #436	-> 482
/*     */     //   #437	-> 487
/*     */     //   #439	-> 497
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   16	71	5	typeVariable	Ljava/lang/reflect/TypeVariable;
/*     */     //   29	58	6	previouslyResolved	Ljava/lang/reflect/Type;
/*     */     //   113	41	5	original	Ljava/lang/Class;
/*     */     //   120	34	6	componentType	Ljava/lang/reflect/Type;
/*     */     //   130	24	7	newComponentType	Ljava/lang/reflect/Type;
/*     */     //   167	43	5	original	Ljava/lang/reflect/GenericArrayType;
/*     */     //   176	34	6	componentType	Ljava/lang/reflect/Type;
/*     */     //   186	24	7	newComponentType	Ljava/lang/reflect/Type;
/*     */     //   296	38	12	resolvedTypeArgument	Ljava/lang/reflect/Type;
/*     */     //   271	69	10	t	I
/*     */     //   276	64	11	length	I
/*     */     //   223	145	5	original	Ljava/lang/reflect/ParameterizedType;
/*     */     //   232	136	6	ownerType	Ljava/lang/reflect/Type;
/*     */     //   242	126	7	newOwnerType	Ljava/lang/reflect/Type;
/*     */     //   259	109	8	changed	Z
/*     */     //   268	100	9	args	[Ljava/lang/reflect/Type;
/*     */     //   418	18	8	lowerBound	Ljava/lang/reflect/Type;
/*     */     //   458	18	8	upperBound	Ljava/lang/reflect/Type;
/*     */     //   381	101	5	original	Ljava/lang/reflect/WildcardType;
/*     */     //   390	92	6	originalLowerBound	[Ljava/lang/reflect/Type;
/*     */     //   399	83	7	originalUpperBound	[Ljava/lang/reflect/Type;
/*     */     //   0	499	0	context	Ljava/lang/reflect/Type;
/*     */     //   0	499	1	contextRawType	Ljava/lang/Class;
/*     */     //   0	499	2	toResolve	Ljava/lang/reflect/Type;
/*     */     //   0	499	3	visitedTypeVariables	Ljava/util/Map;
/*     */     //   3	496	4	resolving	Ljava/lang/reflect/TypeVariable;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   16	71	5	typeVariable	Ljava/lang/reflect/TypeVariable<*>;
/*     */     //   113	41	5	original	Ljava/lang/Class<*>;
/*     */     //   0	499	1	contextRawType	Ljava/lang/Class<*>;
/*     */     //   0	499	3	visitedTypeVariables	Ljava/util/Map<Ljava/lang/reflect/TypeVariable<*>;Ljava/lang/reflect/Type;>;
/*     */     //   3	496	4	resolving	Ljava/lang/reflect/TypeVariable<*>;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
/* 443 */     Class<?> declaredByRaw = declaringClassOf(unknown);
/*     */ 
/*     */     
/* 446 */     if (declaredByRaw == null) {
/* 447 */       return unknown;
/*     */     }
/*     */     
/* 450 */     Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
/* 451 */     if (declaredBy instanceof ParameterizedType) {
/* 452 */       int index = indexOf((Object[])declaredByRaw.getTypeParameters(), unknown);
/* 453 */       return ((ParameterizedType)declaredBy).getActualTypeArguments()[index];
/*     */     } 
/*     */     
/* 456 */     return unknown;
/*     */   }
/*     */   
/*     */   private static int indexOf(Object[] array, Object toFind) {
/* 460 */     for (int i = 0, length = array.length; i < length; i++) {
/* 461 */       if (toFind.equals(array[i])) {
/* 462 */         return i;
/*     */       }
/*     */     } 
/* 465 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
/* 473 */     GenericDeclaration genericDeclaration = (GenericDeclaration)typeVariable.getGenericDeclaration();
/* 474 */     return (genericDeclaration instanceof Class) ? 
/* 475 */       (Class)genericDeclaration : 
/* 476 */       null;
/*     */   }
/*     */   
/*     */   static void checkNotPrimitive(Type type) {
/* 480 */     $Gson$Preconditions.checkArgument((!(type instanceof Class) || !((Class)type).isPrimitive()));
/*     */   }
/*     */   
/*     */   private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
/*     */     private final Type ownerType;
/*     */     private final Type rawType;
/*     */     private final Type[] typeArguments;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
/* 490 */       if (rawType instanceof Class) {
/* 491 */         Class<?> rawTypeAsClass = (Class)rawType;
/*     */         
/* 493 */         boolean isStaticOrTopLevelClass = (Modifier.isStatic(rawTypeAsClass.getModifiers()) || rawTypeAsClass.getEnclosingClass() == null);
/* 494 */         $Gson$Preconditions.checkArgument((ownerType != null || isStaticOrTopLevelClass));
/*     */       } 
/*     */       
/* 497 */       this.ownerType = (ownerType == null) ? null : $Gson$Types.canonicalize(ownerType);
/* 498 */       this.rawType = $Gson$Types.canonicalize(rawType);
/* 499 */       this.typeArguments = (Type[])typeArguments.clone();
/* 500 */       for (int t = 0, length = this.typeArguments.length; t < length; t++) {
/* 501 */         $Gson$Preconditions.checkNotNull(this.typeArguments[t]);
/* 502 */         $Gson$Types.checkNotPrimitive(this.typeArguments[t]);
/* 503 */         this.typeArguments[t] = $Gson$Types.canonicalize(this.typeArguments[t]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Type[] getActualTypeArguments() {
/* 508 */       return (Type[])this.typeArguments.clone();
/*     */     }
/*     */     
/*     */     public Type getRawType() {
/* 512 */       return this.rawType;
/*     */     }
/*     */     
/*     */     public Type getOwnerType() {
/* 516 */       return this.ownerType;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 520 */       return (other instanceof ParameterizedType && 
/* 521 */         $Gson$Types.equals(this, (ParameterizedType)other));
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 525 */       return Arrays.hashCode((Object[])this.typeArguments) ^ this.rawType
/* 526 */         .hashCode() ^ 
/* 527 */         $Gson$Types.hashCodeOrZero(this.ownerType);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 531 */       int length = this.typeArguments.length;
/* 532 */       if (length == 0) {
/* 533 */         return $Gson$Types.typeToString(this.rawType);
/*     */       }
/*     */       
/* 536 */       StringBuilder stringBuilder = new StringBuilder(30 * (length + 1));
/* 537 */       stringBuilder.append($Gson$Types.typeToString(this.rawType)).append("<").append($Gson$Types.typeToString(this.typeArguments[0]));
/* 538 */       for (int i = 1; i < length; i++) {
/* 539 */         stringBuilder.append(", ").append($Gson$Types.typeToString(this.typeArguments[i]));
/*     */       }
/* 541 */       return stringBuilder.append(">").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class GenericArrayTypeImpl
/*     */     implements GenericArrayType, Serializable {
/*     */     private final Type componentType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public GenericArrayTypeImpl(Type componentType) {
/* 551 */       this.componentType = $Gson$Types.canonicalize(componentType);
/*     */     }
/*     */     
/*     */     public Type getGenericComponentType() {
/* 555 */       return this.componentType;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 559 */       return (o instanceof GenericArrayType && 
/* 560 */         $Gson$Types.equals(this, (GenericArrayType)o));
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 564 */       return this.componentType.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 568 */       return $Gson$Types.typeToString(this.componentType) + "[]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WildcardTypeImpl
/*     */     implements WildcardType, Serializable
/*     */   {
/*     */     private final Type upperBound;
/*     */     
/*     */     private final Type lowerBound;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
/* 584 */       $Gson$Preconditions.checkArgument((lowerBounds.length <= 1));
/* 585 */       $Gson$Preconditions.checkArgument((upperBounds.length == 1));
/*     */       
/* 587 */       if (lowerBounds.length == 1) {
/* 588 */         $Gson$Preconditions.checkNotNull(lowerBounds[0]);
/* 589 */         $Gson$Types.checkNotPrimitive(lowerBounds[0]);
/* 590 */         $Gson$Preconditions.checkArgument((upperBounds[0] == Object.class));
/* 591 */         this.lowerBound = $Gson$Types.canonicalize(lowerBounds[0]);
/* 592 */         this.upperBound = Object.class;
/*     */       } else {
/*     */         
/* 595 */         $Gson$Preconditions.checkNotNull(upperBounds[0]);
/* 596 */         $Gson$Types.checkNotPrimitive(upperBounds[0]);
/* 597 */         this.lowerBound = null;
/* 598 */         this.upperBound = $Gson$Types.canonicalize(upperBounds[0]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Type[] getUpperBounds() {
/* 603 */       return new Type[] { this.upperBound };
/*     */     }
/*     */     
/*     */     public Type[] getLowerBounds() {
/* 607 */       (new Type[1])[0] = this.lowerBound; return (this.lowerBound != null) ? new Type[1] : $Gson$Types.EMPTY_TYPE_ARRAY;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 611 */       return (other instanceof WildcardType && 
/* 612 */         $Gson$Types.equals(this, (WildcardType)other));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 617 */       return ((this.lowerBound != null) ? (31 + this.lowerBound.hashCode()) : 1) ^ 31 + this.upperBound
/* 618 */         .hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 622 */       if (this.lowerBound != null)
/* 623 */         return "? super " + $Gson$Types.typeToString(this.lowerBound); 
/* 624 */       if (this.upperBound == Object.class) {
/* 625 */         return "?";
/*     */       }
/* 627 */       return "? extends " + $Gson$Types.typeToString(this.upperBound);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\internal\$Gson$Types.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */