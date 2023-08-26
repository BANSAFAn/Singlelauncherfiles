/*   */ package org.tlauncher.modpack.domain.client.share;
/*   */ public class SiteParams {
/*   */   private GameType type;
/*   */   private String lang;
/*   */   private Integer page;
/*   */   private Integer pageSize;
/*   */   
/* 8 */   public GameType getType() { return this.type; } private Object gameVersion; private List<String> categories; private String search; private String jwt; private NameIdDTO minecraftVersionType; public String getLang() { return this.lang; } public Integer getPage() { return this.page; } public Integer getPageSize() { return this.pageSize; } public Object getGameVersion() { return this.gameVersion; } public List<String> getCategories() { return this.categories; } public String getSearch() { return this.search; } public String getJwt() { return this.jwt; } public NameIdDTO getMinecraftVersionType() { return this.minecraftVersionType; } public void setType(GameType type) { this.type = type; } public void setLang(String lang) { this.lang = lang; } public void setPage(Integer page) { this.page = page; } public void setPageSize(Integer pageSize) { this.pageSize = pageSize; } public void setGameVersion(Object gameVersion) { this.gameVersion = gameVersion; } public void setCategories(List<String> categories) { this.categories = categories; } public void setSearch(String search) { this.search = search; } public void setJwt(String jwt) { this.jwt = jwt; } public void setMinecraftVersionType(NameIdDTO minecraftVersionType) { this.minecraftVersionType = minecraftVersionType; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof SiteParams)) return false;  SiteParams other = (SiteParams)o; if (!other.canEqual(this)) return false;  Object this$page = getPage(), other$page = other.getPage(); if ((this$page == null) ? (other$page != null) : !this$page.equals(other$page)) return false;  Object this$pageSize = getPageSize(), other$pageSize = other.getPageSize(); if ((this$pageSize == null) ? (other$pageSize != null) : !this$pageSize.equals(other$pageSize)) return false;  Object this$type = getType(), other$type = other.getType(); if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type)) return false;  Object this$lang = getLang(), other$lang = other.getLang(); if ((this$lang == null) ? (other$lang != null) : !this$lang.equals(other$lang)) return false;  Object this$gameVersion = getGameVersion(), other$gameVersion = other.getGameVersion(); if ((this$gameVersion == null) ? (other$gameVersion != null) : !this$gameVersion.equals(other$gameVersion)) return false;  Object<String> this$categories = (Object<String>)getCategories(), other$categories = (Object<String>)other.getCategories(); if ((this$categories == null) ? (other$categories != null) : !this$categories.equals(other$categories)) return false;  Object this$search = getSearch(), other$search = other.getSearch(); if ((this$search == null) ? (other$search != null) : !this$search.equals(other$search)) return false;  Object this$jwt = getJwt(), other$jwt = other.getJwt(); if ((this$jwt == null) ? (other$jwt != null) : !this$jwt.equals(other$jwt)) return false;  Object this$minecraftVersionType = getMinecraftVersionType(), other$minecraftVersionType = other.getMinecraftVersionType(); return !((this$minecraftVersionType == null) ? (other$minecraftVersionType != null) : !this$minecraftVersionType.equals(other$minecraftVersionType)); } protected boolean canEqual(Object other) { return other instanceof SiteParams; } public int hashCode() { int PRIME = 59; result = 1; Object $page = getPage(); result = result * 59 + (($page == null) ? 43 : $page.hashCode()); Object $pageSize = getPageSize(); result = result * 59 + (($pageSize == null) ? 43 : $pageSize.hashCode()); Object $type = getType(); result = result * 59 + (($type == null) ? 43 : $type.hashCode()); Object $lang = getLang(); result = result * 59 + (($lang == null) ? 43 : $lang.hashCode()); Object $gameVersion = getGameVersion(); result = result * 59 + (($gameVersion == null) ? 43 : $gameVersion.hashCode()); Object<String> $categories = (Object<String>)getCategories(); result = result * 59 + (($categories == null) ? 43 : $categories.hashCode()); Object $search = getSearch(); result = result * 59 + (($search == null) ? 43 : $search.hashCode()); Object $jwt = getJwt(); result = result * 59 + (($jwt == null) ? 43 : $jwt.hashCode()); Object $minecraftVersionType = getMinecraftVersionType(); return result * 59 + (($minecraftVersionType == null) ? 43 : $minecraftVersionType.hashCode()); } public String toString() { return "SiteParams(type=" + getType() + ", lang=" + getLang() + ", page=" + getPage() + ", pageSize=" + getPageSize() + ", gameVersion=" + getGameVersion() + ", categories=" + getCategories() + ", search=" + getSearch() + ", jwt=" + getJwt() + ", minecraftVersionType=" + getMinecraftVersionType() + ")"; } public SiteParams(GameType type, String lang, Integer page, Integer pageSize, Object gameVersion, List<String> categories, String search, String jwt, NameIdDTO minecraftVersionType) {
/* 9 */     this.type = type; this.lang = lang; this.page = page; this.pageSize = pageSize; this.gameVersion = gameVersion; this.categories = categories; this.search = search; this.jwt = jwt; this.minecraftVersionType = minecraftVersionType;
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\SiteParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */