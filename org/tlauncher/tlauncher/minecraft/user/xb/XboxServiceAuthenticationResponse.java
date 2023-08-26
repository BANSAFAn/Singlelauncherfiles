/*    */ package org.tlauncher.tlauncher.minecraft.user.xb;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;
/*    */ 
/*    */ public class XboxServiceAuthenticationResponse implements Validatable {
/*    */   private final String token;
/*    */   
/*    */   public XboxServiceAuthenticationResponse(String token, String uhs) {
/* 15 */     this.token = token;
/* 16 */     this.uhs = uhs;
/*    */   }
/*    */   private final String uhs;
/*    */   public boolean equals(Object o) {
/* 20 */     if (this == o)
/* 21 */       return true; 
/* 22 */     if (o == null || getClass() != o.getClass())
/* 23 */       return false; 
/* 24 */     XboxServiceAuthenticationResponse that = (XboxServiceAuthenticationResponse)o;
/* 25 */     if (!Objects.equals(this.token, that.token))
/* 26 */       return false; 
/* 27 */     return Objects.equals(this.uhs, that.uhs);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 31 */     int result = (this.token != null) ? this.token.hashCode() : 0;
/* 32 */     return 31 * result + ((this.uhs != null) ? this.uhs.hashCode() : 0);
/*    */   }
/*    */   
/*    */   public String getToken() {
/* 36 */     return this.token;
/*    */   }
/*    */   
/*    */   public String getUHS() {
/* 40 */     return this.uhs;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 44 */     return "XboxServiceAuthenticationResponse{token='" + this.token + '\'' + ", uhs='" + this.uhs + '\'' + '}';
/*    */   }
/*    */   
/*    */   public void validate() {
/* 48 */     Validatable.notEmpty(this.token, "token");
/* 49 */     Validatable.notEmpty(this.uhs, "uhs");
/*    */   }
/*    */   
/*    */   public static class Deserializer implements JsonDeserializer<XboxServiceAuthenticationResponse> {
/*    */     public XboxServiceAuthenticationResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 54 */       JsonObject o = json.getAsJsonObject();
/* 55 */       return new XboxServiceAuthenticationResponse(o
/*    */           
/* 57 */           .get("Token").getAsString(), o
/*    */           
/* 59 */           .get("DisplayClaims").getAsJsonObject()
/* 60 */           .get("xui").getAsJsonArray()
/* 61 */           .get(0).getAsJsonObject()
/* 62 */           .get("uhs").getAsString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\xb\XboxServiceAuthenticationResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */