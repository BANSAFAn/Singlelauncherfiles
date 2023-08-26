/*     */ package org.apache.commons.codec.language.bm;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class Rule
/*     */ {
/*     */   public static final class Phoneme
/*     */     implements PhonemeExpr
/*     */   {
/*  85 */     public static final Comparator<Phoneme> COMPARATOR = new Comparator<Phoneme>()
/*     */       {
/*     */         public int compare(Rule.Phoneme o1, Rule.Phoneme o2) {
/*  88 */           for (int i = 0; i < o1.phonemeText.length(); i++) {
/*  89 */             if (i >= o2.phonemeText.length()) {
/*  90 */               return 1;
/*     */             }
/*  92 */             int c = o1.phonemeText.charAt(i) - o2.phonemeText.charAt(i);
/*  93 */             if (c != 0) {
/*  94 */               return c;
/*     */             }
/*     */           } 
/*     */           
/*  98 */           if (o1.phonemeText.length() < o2.phonemeText.length()) {
/*  99 */             return -1;
/*     */           }
/*     */           
/* 102 */           return 0;
/*     */         }
/*     */       };
/*     */     
/*     */     private final StringBuilder phonemeText;
/*     */     private final Languages.LanguageSet languages;
/*     */     
/*     */     public Phoneme(CharSequence phonemeText, Languages.LanguageSet languages) {
/* 110 */       this.phonemeText = new StringBuilder(phonemeText);
/* 111 */       this.languages = languages;
/*     */     }
/*     */     
/*     */     public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight) {
/* 115 */       this(phonemeLeft.phonemeText, phonemeLeft.languages);
/* 116 */       this.phonemeText.append(phonemeRight.phonemeText);
/*     */     }
/*     */     
/*     */     public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight, Languages.LanguageSet languages) {
/* 120 */       this(phonemeLeft.phonemeText, languages);
/* 121 */       this.phonemeText.append(phonemeRight.phonemeText);
/*     */     }
/*     */     
/*     */     public Phoneme append(CharSequence str) {
/* 125 */       this.phonemeText.append(str);
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public Languages.LanguageSet getLanguages() {
/* 130 */       return this.languages;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<Phoneme> getPhonemes() {
/* 135 */       return Collections.singleton(this);
/*     */     }
/*     */     
/*     */     public CharSequence getPhonemeText() {
/* 139 */       return this.phonemeText;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Phoneme join(Phoneme right) {
/* 147 */       return new Phoneme(this.phonemeText.toString() + right.phonemeText.toString(), this.languages.restrictTo(right.languages));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class PhonemeList
/*     */     implements PhonemeExpr
/*     */   {
/*     */     private final List<Rule.Phoneme> phonemes;
/*     */ 
/*     */     
/*     */     public PhonemeList(List<Rule.Phoneme> phonemes) {
/* 160 */       this.phonemes = phonemes;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Rule.Phoneme> getPhonemes() {
/* 165 */       return this.phonemes;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public static final RPattern ALL_STRINGS_RMATCHER = new RPattern()
/*     */     {
/*     */       public boolean isMatch(CharSequence input) {
/* 179 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public static final String ALL = "ALL";
/*     */   
/*     */   private static final String DOUBLE_QUOTE = "\"";
/*     */   
/*     */   private static final String HASH_INCLUDE = "#include";
/* 189 */   private static final Map<NameType, Map<RuleType, Map<String, Map<String, List<Rule>>>>> RULES = new EnumMap<NameType, Map<RuleType, Map<String, Map<String, List<Rule>>>>>(NameType.class); private final RPattern lContext;
/*     */   private final String pattern;
/*     */   
/*     */   static {
/* 193 */     for (NameType s : NameType.values()) {
/* 194 */       Map<RuleType, Map<String, Map<String, List<Rule>>>> rts = new EnumMap<RuleType, Map<String, Map<String, List<Rule>>>>(RuleType.class);
/*     */ 
/*     */       
/* 197 */       for (RuleType rt : RuleType.values()) {
/* 198 */         Map<String, Map<String, List<Rule>>> rs = new HashMap<String, Map<String, List<Rule>>>();
/*     */         
/* 200 */         Languages ls = Languages.getInstance(s);
/* 201 */         for (String l : ls.getLanguages()) {
/*     */           try {
/* 203 */             rs.put(l, parseRules(createScanner(s, rt, l), createResourceName(s, rt, l)));
/* 204 */           } catch (IllegalStateException e) {
/* 205 */             throw new IllegalStateException("Problem processing " + createResourceName(s, rt, l), e);
/*     */           } 
/*     */         } 
/* 208 */         if (!rt.equals(RuleType.RULES)) {
/* 209 */           rs.put("common", parseRules(createScanner(s, rt, "common"), createResourceName(s, rt, "common")));
/*     */         }
/*     */         
/* 212 */         rts.put(rt, Collections.unmodifiableMap(rs));
/*     */       } 
/*     */       
/* 215 */       RULES.put(s, Collections.unmodifiableMap(rts));
/*     */     } 
/*     */   }
/*     */   private final PhonemeExpr phoneme; private final RPattern rContext;
/*     */   private static boolean contains(CharSequence chars, char input) {
/* 220 */     for (int i = 0; i < chars.length(); i++) {
/* 221 */       if (chars.charAt(i) == input) {
/* 222 */         return true;
/*     */       }
/*     */     } 
/* 225 */     return false;
/*     */   }
/*     */   
/*     */   private static String createResourceName(NameType nameType, RuleType rt, String lang) {
/* 229 */     return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", new Object[] { nameType.getName(), rt.getName(), lang });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Scanner createScanner(NameType nameType, RuleType rt, String lang) {
/* 234 */     String resName = createResourceName(nameType, rt, lang);
/* 235 */     InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
/*     */     
/* 237 */     if (rulesIS == null) {
/* 238 */       throw new IllegalArgumentException("Unable to load resource: " + resName);
/*     */     }
/*     */     
/* 241 */     return new Scanner(rulesIS, "UTF-8");
/*     */   }
/*     */   
/*     */   private static Scanner createScanner(String lang) {
/* 245 */     String resName = String.format("org/apache/commons/codec/language/bm/%s.txt", new Object[] { lang });
/* 246 */     InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
/*     */     
/* 248 */     if (rulesIS == null) {
/* 249 */       throw new IllegalArgumentException("Unable to load resource: " + resName);
/*     */     }
/*     */     
/* 252 */     return new Scanner(rulesIS, "UTF-8");
/*     */   }
/*     */   
/*     */   private static boolean endsWith(CharSequence input, CharSequence suffix) {
/* 256 */     if (suffix.length() > input.length()) {
/* 257 */       return false;
/*     */     }
/* 259 */     for (int i = input.length() - 1, j = suffix.length() - 1; j >= 0; i--, j--) {
/* 260 */       if (input.charAt(i) != suffix.charAt(j)) {
/* 261 */         return false;
/*     */       }
/*     */     } 
/* 264 */     return true;
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
/*     */   public static List<Rule> getInstance(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
/* 280 */     Map<String, List<Rule>> ruleMap = getInstanceMap(nameType, rt, langs);
/* 281 */     List<Rule> allRules = new ArrayList<Rule>();
/* 282 */     for (List<Rule> rules : ruleMap.values()) {
/* 283 */       allRules.addAll(rules);
/*     */     }
/* 285 */     return allRules;
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
/*     */   public static List<Rule> getInstance(NameType nameType, RuleType rt, String lang) {
/* 300 */     return getInstance(nameType, rt, Languages.LanguageSet.from(new HashSet<String>(Arrays.asList(new String[] { lang }))));
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
/*     */   public static Map<String, List<Rule>> getInstanceMap(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
/* 317 */     return langs.isSingleton() ? getInstanceMap(nameType, rt, langs.getAny()) : getInstanceMap(nameType, rt, "any");
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
/*     */   public static Map<String, List<Rule>> getInstanceMap(NameType nameType, RuleType rt, String lang) {
/* 335 */     Map<String, List<Rule>> rules = (Map<String, List<Rule>>)((Map)((Map)RULES.get(nameType)).get(rt)).get(lang);
/*     */     
/* 337 */     if (rules == null) {
/* 338 */       throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", new Object[] { nameType.getName(), rt.getName(), lang }));
/*     */     }
/*     */ 
/*     */     
/* 342 */     return rules;
/*     */   }
/*     */   
/*     */   private static Phoneme parsePhoneme(String ph) {
/* 346 */     int open = ph.indexOf("[");
/* 347 */     if (open >= 0) {
/* 348 */       if (!ph.endsWith("]")) {
/* 349 */         throw new IllegalArgumentException("Phoneme expression contains a '[' but does not end in ']'");
/*     */       }
/* 351 */       String before = ph.substring(0, open);
/* 352 */       String in = ph.substring(open + 1, ph.length() - 1);
/* 353 */       Set<String> langs = new HashSet<String>(Arrays.asList(in.split("[+]")));
/*     */       
/* 355 */       return new Phoneme(before, Languages.LanguageSet.from(langs));
/*     */     } 
/* 357 */     return new Phoneme(ph, Languages.ANY_LANGUAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   private static PhonemeExpr parsePhonemeExpr(String ph) {
/* 362 */     if (ph.startsWith("(")) {
/* 363 */       if (!ph.endsWith(")")) {
/* 364 */         throw new IllegalArgumentException("Phoneme starts with '(' so must end with ')'");
/*     */       }
/*     */       
/* 367 */       List<Phoneme> phs = new ArrayList<Phoneme>();
/* 368 */       String body = ph.substring(1, ph.length() - 1);
/* 369 */       for (String part : body.split("[|]")) {
/* 370 */         phs.add(parsePhoneme(part));
/*     */       }
/* 372 */       if (body.startsWith("|") || body.endsWith("|")) {
/* 373 */         phs.add(new Phoneme("", Languages.ANY_LANGUAGE));
/*     */       }
/*     */       
/* 376 */       return new PhonemeList(phs);
/*     */     } 
/* 378 */     return parsePhoneme(ph);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, List<Rule>> parseRules(Scanner scanner, final String location) {
/* 383 */     Map<String, List<Rule>> lines = new HashMap<String, List<Rule>>();
/* 384 */     int currentLine = 0;
/*     */     
/* 386 */     boolean inMultilineComment = false;
/* 387 */     while (scanner.hasNextLine()) {
/* 388 */       currentLine++;
/* 389 */       String rawLine = scanner.nextLine();
/* 390 */       String line = rawLine;
/*     */       
/* 392 */       if (inMultilineComment) {
/* 393 */         if (line.endsWith("*/"))
/* 394 */           inMultilineComment = false; 
/*     */         continue;
/*     */       } 
/* 397 */       if (line.startsWith("/*")) {
/* 398 */         inMultilineComment = true;
/*     */         continue;
/*     */       } 
/* 401 */       int cmtI = line.indexOf("//");
/* 402 */       if (cmtI >= 0) {
/* 403 */         line = line.substring(0, cmtI);
/*     */       }
/*     */ 
/*     */       
/* 407 */       line = line.trim();
/*     */       
/* 409 */       if (line.length() == 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 413 */       if (line.startsWith("#include")) {
/*     */         
/* 415 */         String incl = line.substring("#include".length()).trim();
/* 416 */         if (incl.contains(" ")) {
/* 417 */           throw new IllegalArgumentException("Malformed import statement '" + rawLine + "' in " + location);
/*     */         }
/*     */         
/* 420 */         lines.putAll(parseRules(createScanner(incl), location + "->" + incl));
/*     */         
/*     */         continue;
/*     */       } 
/* 424 */       String[] parts = line.split("\\s+");
/* 425 */       if (parts.length != 4) {
/* 426 */         throw new IllegalArgumentException("Malformed rule statement split into " + parts.length + " parts: " + rawLine + " in " + location);
/*     */       }
/*     */       
/*     */       try {
/* 430 */         String pat = stripQuotes(parts[0]);
/* 431 */         String lCon = stripQuotes(parts[1]);
/* 432 */         String rCon = stripQuotes(parts[2]);
/* 433 */         PhonemeExpr ph = parsePhonemeExpr(stripQuotes(parts[3]));
/* 434 */         final int cLine = currentLine;
/* 435 */         Rule r = new Rule(pat, lCon, rCon, ph) {
/* 436 */             private final int myLine = cLine;
/* 437 */             private final String loc = location;
/*     */ 
/*     */             
/*     */             public String toString() {
/* 441 */               StringBuilder sb = new StringBuilder();
/* 442 */               sb.append("Rule");
/* 443 */               sb.append("{line=").append(this.myLine);
/* 444 */               sb.append(", loc='").append(this.loc).append('\'');
/* 445 */               sb.append('}');
/* 446 */               return sb.toString();
/*     */             }
/*     */           };
/* 449 */         String patternKey = r.pattern.substring(0, 1);
/* 450 */         List<Rule> rules = lines.get(patternKey);
/* 451 */         if (rules == null) {
/* 452 */           rules = new ArrayList<Rule>();
/* 453 */           lines.put(patternKey, rules);
/*     */         } 
/* 455 */         rules.add(r);
/* 456 */       } catch (IllegalArgumentException e) {
/* 457 */         throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     return lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RPattern pattern(final String regex) {
/* 477 */     boolean startsWith = regex.startsWith("^");
/* 478 */     boolean endsWith = regex.endsWith("$");
/* 479 */     final String content = regex.substring(startsWith ? 1 : 0, endsWith ? (regex.length() - 1) : regex.length());
/* 480 */     boolean boxes = content.contains("[");
/*     */     
/* 482 */     if (!boxes) {
/* 483 */       if (startsWith && endsWith) {
/*     */         
/* 485 */         if (content.length() == 0)
/*     */         {
/* 487 */           return new RPattern()
/*     */             {
/*     */               public boolean isMatch(CharSequence input) {
/* 490 */                 return (input.length() == 0);
/*     */               }
/*     */             };
/*     */         }
/* 494 */         return new RPattern()
/*     */           {
/*     */             public boolean isMatch(CharSequence input) {
/* 497 */               return input.equals(content);
/*     */             }
/*     */           };
/*     */       } 
/* 501 */       if ((startsWith || endsWith) && content.length() == 0)
/*     */       {
/* 503 */         return ALL_STRINGS_RMATCHER; } 
/* 504 */       if (startsWith)
/*     */       {
/* 506 */         return new RPattern()
/*     */           {
/*     */             public boolean isMatch(CharSequence input) {
/* 509 */               return Rule.startsWith(input, content);
/*     */             }
/*     */           }; } 
/* 512 */       if (endsWith)
/*     */       {
/* 514 */         return new RPattern()
/*     */           {
/*     */             public boolean isMatch(CharSequence input) {
/* 517 */               return Rule.endsWith(input, content);
/*     */             }
/*     */           };
/*     */       }
/*     */     } else {
/* 522 */       boolean startsWithBox = content.startsWith("[");
/* 523 */       boolean endsWithBox = content.endsWith("]");
/*     */       
/* 525 */       if (startsWithBox && endsWithBox) {
/* 526 */         String boxContent = content.substring(1, content.length() - 1);
/* 527 */         if (!boxContent.contains("[")) {
/*     */           
/* 529 */           boolean negate = boxContent.startsWith("^");
/* 530 */           if (negate) {
/* 531 */             boxContent = boxContent.substring(1);
/*     */           }
/* 533 */           final String bContent = boxContent;
/* 534 */           final boolean shouldMatch = !negate;
/*     */           
/* 536 */           if (startsWith && endsWith)
/*     */           {
/* 538 */             return new RPattern()
/*     */               {
/*     */                 public boolean isMatch(CharSequence input) {
/* 541 */                   return (input.length() == 1 && Rule.contains(bContent, input.charAt(0)) == shouldMatch);
/*     */                 }
/*     */               }; } 
/* 544 */           if (startsWith)
/*     */           {
/* 546 */             return new RPattern()
/*     */               {
/*     */                 public boolean isMatch(CharSequence input) {
/* 549 */                   return (input.length() > 0 && Rule.contains(bContent, input.charAt(0)) == shouldMatch);
/*     */                 }
/*     */               }; } 
/* 552 */           if (endsWith)
/*     */           {
/* 554 */             return new RPattern()
/*     */               {
/*     */                 public boolean isMatch(CharSequence input) {
/* 557 */                   return (input.length() > 0 && Rule.contains(bContent, input.charAt(input.length() - 1)) == shouldMatch);
/*     */                 }
/*     */               };
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 566 */     return new RPattern() {
/* 567 */         Pattern pattern = Pattern.compile(regex);
/*     */ 
/*     */         
/*     */         public boolean isMatch(CharSequence input) {
/* 571 */           Matcher matcher = this.pattern.matcher(input);
/* 572 */           return matcher.find();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static boolean startsWith(CharSequence input, CharSequence prefix) {
/* 578 */     if (prefix.length() > input.length()) {
/* 579 */       return false;
/*     */     }
/* 581 */     for (int i = 0; i < prefix.length(); i++) {
/* 582 */       if (input.charAt(i) != prefix.charAt(i)) {
/* 583 */         return false;
/*     */       }
/*     */     } 
/* 586 */     return true;
/*     */   }
/*     */   
/*     */   private static String stripQuotes(String str) {
/* 590 */     if (str.startsWith("\"")) {
/* 591 */       str = str.substring(1);
/*     */     }
/*     */     
/* 594 */     if (str.endsWith("\"")) {
/* 595 */       str = str.substring(0, str.length() - 1);
/*     */     }
/*     */     
/* 598 */     return str;
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
/*     */   public Rule(String pattern, String lContext, String rContext, PhonemeExpr phoneme) {
/* 622 */     this.pattern = pattern;
/* 623 */     this.lContext = pattern(lContext + "$");
/* 624 */     this.rContext = pattern("^" + rContext);
/* 625 */     this.phoneme = phoneme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RPattern getLContext() {
/* 634 */     return this.lContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 643 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PhonemeExpr getPhoneme() {
/* 652 */     return this.phoneme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RPattern getRContext() {
/* 661 */     return this.rContext;
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
/*     */   public boolean patternAndContextMatches(CharSequence input, int i) {
/* 676 */     if (i < 0) {
/* 677 */       throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
/*     */     }
/*     */     
/* 680 */     int patternLength = this.pattern.length();
/* 681 */     int ipl = i + patternLength;
/*     */     
/* 683 */     if (ipl > input.length())
/*     */     {
/* 685 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 690 */     if (!input.subSequence(i, ipl).equals(this.pattern))
/* 691 */       return false; 
/* 692 */     if (!this.rContext.isMatch(input.subSequence(ipl, input.length()))) {
/* 693 */       return false;
/*     */     }
/* 695 */     return this.lContext.isMatch(input.subSequence(0, i));
/*     */   }
/*     */   
/*     */   public static interface RPattern {
/*     */     boolean isMatch(CharSequence param1CharSequence);
/*     */   }
/*     */   
/*     */   public static interface PhonemeExpr {
/*     */     Iterable<Rule.Phoneme> getPhonemes();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\codec\language\bm\Rule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */