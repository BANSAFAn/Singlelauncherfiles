/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ public class ServerCommandEntity {
/*    */   private String requestType;
/*    */   private String urn;
/*    */   
/*  7 */   public void setRequestType(String requestType) { this.requestType = requestType; } private String body; private Map<String, String> queries; public void setUrn(String urn) { this.urn = urn; } public void setBody(String body) { this.body = body; } public void setQueries(Map<String, String> queries) { this.queries = queries; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ServerCommandEntity)) return false;  ServerCommandEntity other = (ServerCommandEntity)o; if (!other.canEqual(this)) return false;  Object this$requestType = getRequestType(), other$requestType = other.getRequestType(); if ((this$requestType == null) ? (other$requestType != null) : !this$requestType.equals(other$requestType)) return false;  Object this$urn = getUrn(), other$urn = other.getUrn(); if ((this$urn == null) ? (other$urn != null) : !this$urn.equals(other$urn)) return false;  Object this$body = getBody(), other$body = other.getBody(); if ((this$body == null) ? (other$body != null) : !this$body.equals(other$body)) return false;  Object<String, String> this$queries = (Object<String, String>)getQueries(), other$queries = (Object<String, String>)other.getQueries(); return !((this$queries == null) ? (other$queries != null) : !this$queries.equals(other$queries)); } protected boolean canEqual(Object other) { return other instanceof ServerCommandEntity; } public int hashCode() { int PRIME = 59; result = 1; Object $requestType = getRequestType(); result = result * 59 + (($requestType == null) ? 43 : $requestType.hashCode()); Object $urn = getUrn(); result = result * 59 + (($urn == null) ? 43 : $urn.hashCode()); Object $body = getBody(); result = result * 59 + (($body == null) ? 43 : $body.hashCode()); Object<String, String> $queries = (Object<String, String>)getQueries(); return result * 59 + (($queries == null) ? 43 : $queries.hashCode()); } public String toString() { return "ServerCommandEntity(requestType=" + getRequestType() + ", urn=" + getUrn() + ", body=" + getBody() + ", queries=" + getQueries() + ")"; }
/*    */   
/*  9 */   public String getRequestType() { return this.requestType; }
/* 10 */   public String getUrn() { return this.urn; }
/* 11 */   public String getBody() { return this.body; } public Map<String, String> getQueries() {
/* 12 */     return this.queries;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\ServerCommandEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */