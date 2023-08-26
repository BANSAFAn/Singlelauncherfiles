/*    */ package org.tlauncher.modpack.domain.client.site;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class CommonPage<T> {
/*    */   private Integer current;
/*    */   private Integer allPages;
/*    */   
/* 10 */   public Integer getCurrent() { return this.current; } private boolean next; private List<T> content; private Long allElements; public Integer getAllPages() { return this.allPages; } public boolean isNext() { return this.next; } public List<T> getContent() { return this.content; } public Long getAllElements() { return this.allElements; } public void setCurrent(Integer current) { this.current = current; } public void setAllPages(Integer allPages) { this.allPages = allPages; } public void setNext(boolean next) { this.next = next; } public void setContent(List<T> content) { this.content = content; } public void setAllElements(Long allElements) { this.allElements = allElements; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof CommonPage)) return false;  CommonPage<?> other = (CommonPage)o; if (!other.canEqual(this)) return false;  if (isNext() != other.isNext()) return false;  Object this$current = getCurrent(), other$current = other.getCurrent(); if ((this$current == null) ? (other$current != null) : !this$current.equals(other$current)) return false;  Object this$allPages = getAllPages(), other$allPages = other.getAllPages(); if ((this$allPages == null) ? (other$allPages != null) : !this$allPages.equals(other$allPages)) return false;  Object this$allElements = getAllElements(), other$allElements = other.getAllElements(); if ((this$allElements == null) ? (other$allElements != null) : !this$allElements.equals(other$allElements)) return false;  Object<T> this$content = (Object<T>)getContent(); Object<?> other$content = (Object<?>)other.getContent(); return !((this$content == null) ? (other$content != null) : !this$content.equals(other$content)); } protected boolean canEqual(Object other) { return other instanceof CommonPage; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isNext() ? 79 : 97); Object $current = getCurrent(); result = result * 59 + (($current == null) ? 43 : $current.hashCode()); Object $allPages = getAllPages(); result = result * 59 + (($allPages == null) ? 43 : $allPages.hashCode()); Object $allElements = getAllElements(); result = result * 59 + (($allElements == null) ? 43 : $allElements.hashCode()); Object<T> $content = (Object<T>)getContent(); return result * 59 + (($content == null) ? 43 : $content.hashCode()); } public String toString() { return "CommonPage(current=" + getCurrent() + ", allPages=" + getAllPages() + ", next=" + isNext() + ", content=" + getContent() + ", allElements=" + getAllElements() + ")"; } public CommonPage(Integer current, Integer allPages, boolean next, List<T> content, Long allElements) {
/* 11 */     this.current = current; this.allPages = allPages; this.next = next; this.content = content; this.allElements = allElements;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> CommonPage() {
/* 21 */     this.current = Integer.valueOf(0);
/* 22 */     this.allPages = Integer.valueOf(0);
/* 23 */     this.next = false;
/* 24 */     this.content = new ArrayList<>();
/* 25 */     this.allElements = Long.valueOf(0L);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 29 */     return !(!Objects.isNull(this.content) && !this.content.isEmpty());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\CommonPage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */