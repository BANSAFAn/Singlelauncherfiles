/*     */ package org.tlauncher.tlauncher.minecraft.auth;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class AuthenticatorDatabase {
/*     */   public AuthenticatorDatabase(Map<String, Account> map) {
/*  15 */     if (map == null)
/*  16 */       throw new NullPointerException(); 
/*  17 */     this.accounts = map;
/*     */   }
/*     */   private final Map<String, Account> accounts;
/*     */   public AuthenticatorDatabase() {
/*  21 */     this(new LinkedHashMap<>());
/*     */   }
/*     */   
/*     */   public Collection<Account> getAccounts() {
/*  25 */     return Collections.unmodifiableCollection(this.accounts.values());
/*     */   }
/*     */   
/*     */   public Account getByUUID(String uuid) {
/*  29 */     for (Account account : this.accounts.values()) {
/*  30 */       if (StringUtils.equals(account.getUUID(), uuid))
/*  31 */         return account; 
/*     */     } 
/*  33 */     return null;
/*     */   }
/*     */   
/*     */   public Account getByUsername(String username) {
/*  37 */     if (username == null) {
/*  38 */       throw new NullPointerException();
/*     */     }
/*  40 */     for (Account acc : this.accounts.values()) {
/*  41 */       if (username.equals(acc.getUsername()))
/*  42 */         return acc; 
/*     */     } 
/*  44 */     return null;
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
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<AuthenticatorDatabase>, JsonSerializer<AuthenticatorDatabase>
/*     */   {
/*     */     public AuthenticatorDatabase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/*  87 */       Map<String, Account> services = new LinkedHashMap<>();
/*  88 */       Map<String, Map<String, Object>> credentials = deserializeCredentials((JsonObject)json, context);
/*     */       
/*  90 */       for (Map.Entry<String, Map<String, Object>> en : credentials.entrySet()) {
/*  91 */         services.put(en.getKey(), new Account(en.getValue()));
/*     */       }
/*  93 */       return new AuthenticatorDatabase(services);
/*     */     }
/*     */     
/*     */     Map<String, Map<String, Object>> deserializeCredentials(JsonObject json, JsonDeserializationContext context) {
/*  97 */       Map<String, Map<String, Object>> result = new LinkedHashMap<>();
/*     */       
/*  99 */       for (Map.Entry<String, JsonElement> authEntry : (Iterable<Map.Entry<String, JsonElement>>)json.entrySet()) {
/* 100 */         Map<String, Object> credentials = new LinkedHashMap<>();
/* 101 */         for (Map.Entry<String, JsonElement> credentialsEntry : (Iterable<Map.Entry<String, JsonElement>>)((JsonObject)authEntry.getValue()).entrySet())
/* 102 */           credentials.put(credentialsEntry.getKey(), deserializeCredential(credentialsEntry.getValue())); 
/* 103 */         result.put(authEntry.getKey(), credentials);
/*     */       } 
/*     */       
/* 106 */       return result;
/*     */     }
/*     */     
/*     */     private Object deserializeCredential(JsonElement element) {
/* 110 */       if (element instanceof JsonObject) {
/* 111 */         Map<String, Object> result = new LinkedHashMap<>();
/* 112 */         for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)((JsonObject)element).entrySet())
/* 113 */           result.put(entry.getKey(), deserializeCredential(entry.getValue())); 
/* 114 */         return result;
/*     */       } 
/*     */       
/* 117 */       if (element instanceof com.google.gson.JsonArray) {
/* 118 */         List<Object> result = new ArrayList();
/* 119 */         for (JsonElement entry : element)
/* 120 */           result.add(deserializeCredential(entry)); 
/* 121 */         return result;
/*     */       } 
/*     */       
/* 124 */       return element.getAsString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement serialize(AuthenticatorDatabase src, Type typeOfSrc, JsonSerializationContext context) {
/* 130 */       Map<String, Map<String, Object>> credentials = new LinkedHashMap<>();
/* 131 */       for (Map.Entry<String, Account> en : (Iterable<Map.Entry<String, Account>>)src.accounts.entrySet()) {
/* 132 */         credentials.put(en.getKey(), ((Account)en.getValue()).createMap());
/*     */       }
/* 134 */       return context.serialize(credentials);
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanFreeAccount() {
/* 139 */     List<String> list = new ArrayList<>();
/* 140 */     for (Map.Entry<String, Account> account : this.accounts.entrySet()) {
/* 141 */       if (((Account)account.getValue()).getType().equals(Account.AccountType.FREE)) {
/* 142 */         list.add(account.getKey());
/*     */       }
/*     */     } 
/* 145 */     for (String string : list) {
/* 146 */       this.accounts.remove(string);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Account getByUsernameType(String username, String type) {
/* 152 */     if (username == null || type == null) {
/* 153 */       throw new NullPointerException();
/*     */     }
/* 155 */     for (Account acc : this.accounts.values()) {
/* 156 */       if (username.equals(acc.getUsername()) && type.equals(acc.getType().name()))
/* 157 */         return acc; 
/* 158 */     }  return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\AuthenticatorDatabase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */