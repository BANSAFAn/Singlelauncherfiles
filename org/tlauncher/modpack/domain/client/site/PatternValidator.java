/*   */ package org.tlauncher.modpack.domain.client.site;
/*   */ public class PatternValidator { public static final String NAME = "^[\\w\\s\\-]{3,150}$"; public static final String VERSION_NAME = "^[\\w\\-\\._]{3,150}$"; public static final String LANNAME = "^[a-z0-9\\-]{3,150}$";
/*   */   public static final String DESCRIPTION = "^([\\w\\sа-яА-ЯёЁ\\.\\-–:; «»·,*!+…'?%@)(/]|<b>|</b>|<i>|</i>|<u>|</u>|<p>|</p>|<p[^<>]*>|<em>|</em>|<br>|</br>|<br />|<strong>|</strong>|<ul>|</ul>|<li>|</li>|<h1[^<>]*>|</h1>|<h2[^<>]*>|</h2>|<h3[^<>]*>|</h3>|<img[^<>]*>|<span[^<>]*>|</span>){0,20000}$";
/*   */   
/* 5 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof PatternValidator)) return false;  PatternValidator other = (PatternValidator)o; return !!other.canEqual(this); } public static final String URI = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]{3,250}$"; public static final String MESSAGE = "^[а-яА-яa-zA-Z0-9\\s\\.\\-,!?]{0,1000}$"; public static final String COMMENT = "^([\\w\\sа-яА-ЯёЁ\\.\\-–:; «»·,*!+…'?%@)(/]|<b>|</b>|<i>|</i>|<u>|</u>|<em>|</em>){1,2000}$"; public static final String POST_COMMENT = "^(?!<em></em>|<i></i>|<u></u>|<b></b>)([\\w\\sа-яА-ЯёЁ\\.\\-–:; «»·,*!+…'?%@)(/]|<b>|</b>|<i>|</i>|<u>|</u>|<em>|</em>)+(?<!<em></em>|<i></i>|<u></u>|<b></b>){1,2000}$"; public static final String EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; protected boolean canEqual(Object other) { return other instanceof PatternValidator; } public int hashCode() { int result = 1; return 1; } public String toString() { return "PatternValidator()"; }
/*   */    }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\PatternValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */