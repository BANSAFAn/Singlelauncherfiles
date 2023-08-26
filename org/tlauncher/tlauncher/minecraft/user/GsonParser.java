/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import com.google.gson.FieldNamingPolicy;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.UUID;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.UUIDTypeAdapter;
/*    */ import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;
/*    */ 
/*    */ public class GsonParser<V extends Validatable>
/*    */   implements Parser<V> {
/*    */   private final Gson gson;
/*    */   private final Type type;
/*    */   
/*    */   public GsonParser(Gson gson, Type type) {
/* 19 */     this.gson = gson;
/* 20 */     this.type = type;
/*    */   }
/*    */   
/*    */   public static <V extends Validatable> GsonParser<V> defaultParser(Type type) {
/* 24 */     return new GsonParser<>((new GsonBuilder())
/* 25 */         .create(), type);
/*    */   }
/*    */   
/*    */   public static <V extends Validatable> GsonParser<V> withDashlessUUIDAdapter(Type type) {
/* 29 */     return new GsonParser<>((new GsonBuilder())
/*    */         
/* 31 */         .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
/* 32 */         .create(), type);
/*    */   }
/*    */   
/*    */   public static <V extends Validatable> GsonParser<V> lowerCaseWithUnderscores(Type type) {
/* 36 */     return new GsonParser<>((new GsonBuilder())
/*    */         
/* 38 */         .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
/* 39 */         .create(), type);
/*    */   }
/*    */   
/*    */   public static <V extends Validatable> GsonParser<V> upperCamelCase(Type type) {
/* 43 */     return new GsonParser<>((new GsonBuilder())
/*    */         
/* 45 */         .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
/* 46 */         .create(), type);
/*    */   }
/*    */   
/*    */   public static <V extends Validatable> GsonParser<V> withDeserializer(Type type, Object typeAdapter) {
/* 50 */     return new GsonParser<>((new GsonBuilder())
/*    */         
/* 52 */         .registerTypeAdapter(type, typeAdapter)
/* 53 */         .create(), type);
/*    */   }
/*    */   
/*    */   public V parseResponse(Logger logger, String response) throws ParseException {
/*    */     Validatable validatable;
/* 58 */     logger.trace("Parsing response");
/*    */     try {
/* 60 */       validatable = (Validatable)this.gson.fromJson(response, this.type);
/* 61 */     } catch (RuntimeException e) {
/* 62 */       throw new ParseException(e);
/*    */     } 
/* 64 */     logger.trace("Validating response");
/*    */     try {
/* 66 */       validatable.validate();
/* 67 */     } catch (ParseException e) {
/* 68 */       throw e;
/* 69 */     } catch (RuntimeException e) {
/* 70 */       throw new ParseException(e);
/*    */     } 
/* 72 */     return (V)validatable;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\GsonParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */