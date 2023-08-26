/*     */ package org.apache.http.conn.routing;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
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
/*     */ @Immutable
/*     */ public class BasicRouteDirector
/*     */   implements HttpRouteDirector
/*     */ {
/*     */   public int nextStep(RouteInfo plan, RouteInfo fact) {
/*  54 */     Args.notNull(plan, "Planned route");
/*     */     
/*  56 */     int step = -1;
/*     */     
/*  58 */     if (fact == null || fact.getHopCount() < 1) {
/*  59 */       step = firstStep(plan);
/*  60 */     } else if (plan.getHopCount() > 1) {
/*  61 */       step = proxiedStep(plan, fact);
/*     */     } else {
/*  63 */       step = directStep(plan, fact);
/*     */     } 
/*     */     
/*  66 */     return step;
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
/*     */   protected int firstStep(RouteInfo plan) {
/*  80 */     return (plan.getHopCount() > 1) ? 2 : 1;
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
/*     */   protected int directStep(RouteInfo plan, RouteInfo fact) {
/*  96 */     if (fact.getHopCount() > 1) {
/*  97 */       return -1;
/*     */     }
/*  99 */     if (!plan.getTargetHost().equals(fact.getTargetHost()))
/*     */     {
/* 101 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (plan.isSecure() != fact.isSecure()) {
/* 111 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 115 */     if (plan.getLocalAddress() != null && !plan.getLocalAddress().equals(fact.getLocalAddress()))
/*     */     {
/*     */       
/* 118 */       return -1;
/*     */     }
/*     */     
/* 121 */     return 0;
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
/*     */   protected int proxiedStep(RouteInfo plan, RouteInfo fact) {
/* 136 */     if (fact.getHopCount() <= 1) {
/* 137 */       return -1;
/*     */     }
/* 139 */     if (!plan.getTargetHost().equals(fact.getTargetHost())) {
/* 140 */       return -1;
/*     */     }
/* 142 */     int phc = plan.getHopCount();
/* 143 */     int fhc = fact.getHopCount();
/* 144 */     if (phc < fhc) {
/* 145 */       return -1;
/*     */     }
/*     */     
/* 148 */     for (int i = 0; i < fhc - 1; i++) {
/* 149 */       if (!plan.getHopTarget(i).equals(fact.getHopTarget(i))) {
/* 150 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 154 */     if (phc > fhc)
/*     */     {
/* 156 */       return 4;
/*     */     }
/*     */ 
/*     */     
/* 160 */     if ((fact.isTunnelled() && !plan.isTunnelled()) || (fact.isLayered() && !plan.isLayered()))
/*     */     {
/* 162 */       return -1;
/*     */     }
/*     */     
/* 165 */     if (plan.isTunnelled() && !fact.isTunnelled()) {
/* 166 */       return 3;
/*     */     }
/* 168 */     if (plan.isLayered() && !fact.isLayered()) {
/* 169 */       return 5;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     if (plan.isSecure() != fact.isSecure()) {
/* 176 */       return -1;
/*     */     }
/*     */     
/* 179 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\routing\BasicRouteDirector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */