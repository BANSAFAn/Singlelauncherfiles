/*     */ package org.apache.commons.compress.harmony.unpack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.compress.harmony.pack200.BHSDCodec;
/*     */ import org.apache.commons.compress.harmony.pack200.Codec;
/*     */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPDouble;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPFieldRef;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPFloat;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPInteger;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPInterfaceMethodRef;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPLong;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethodRef;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPNameAndType;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPString;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.NewAttribute;
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
/*     */ public class NewAttributeBands
/*     */   extends BandSet
/*     */ {
/*     */   private final AttributeLayout attributeLayout;
/*     */   private int backwardsCallCount;
/*     */   protected List<AttributeLayoutElement> attributeLayoutElements;
/*     */   
/*     */   public NewAttributeBands(Segment segment, AttributeLayout attributeLayout) throws IOException {
/*  55 */     super(segment);
/*  56 */     this.attributeLayout = attributeLayout;
/*  57 */     parseLayout();
/*  58 */     attributeLayout.setBackwardsCallCount(this.backwardsCallCount);
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
/*     */   public void read(InputStream in) throws IOException, Pack200Exception {}
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
/*     */   public List<Attribute> parseAttributes(InputStream in, int occurrenceCount) throws IOException, Pack200Exception {
/*  82 */     for (AttributeLayoutElement element : this.attributeLayoutElements) {
/*  83 */       element.readBands(in, occurrenceCount);
/*     */     }
/*     */     
/*  86 */     List<Attribute> attributes = new ArrayList<>(occurrenceCount);
/*  87 */     for (int i = 0; i < occurrenceCount; i++) {
/*  88 */       attributes.add(getOneAttribute(i, this.attributeLayoutElements));
/*     */     }
/*  90 */     return attributes;
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
/*     */   private Attribute getOneAttribute(int index, List<AttributeLayoutElement> elements) {
/* 102 */     NewAttribute attribute = new NewAttribute(this.segment.getCpBands().cpUTF8Value(this.attributeLayout.getName()), this.attributeLayout.getIndex());
/* 103 */     for (AttributeLayoutElement element : elements) {
/* 104 */       element.addToAttribute(index, attribute);
/*     */     }
/* 106 */     return (Attribute)attribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseLayout() throws IOException {
/* 115 */     if (this.attributeLayoutElements == null) {
/* 116 */       this.attributeLayoutElements = new ArrayList<>();
/* 117 */       StringReader stream = new StringReader(this.attributeLayout.getLayout());
/*     */       AttributeLayoutElement e;
/* 119 */       while ((e = readNextAttributeElement(stream)) != null) {
/* 120 */         this.attributeLayoutElements.add(e);
/*     */       }
/* 122 */       resolveCalls();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resolveCalls() {
/* 130 */     int backwardsCalls = 0;
/* 131 */     for (int i = 0; i < this.attributeLayoutElements.size(); i++) {
/* 132 */       AttributeLayoutElement element = this.attributeLayoutElements.get(i);
/* 133 */       if (element instanceof Callable) {
/* 134 */         Callable callable = (Callable)element;
/* 135 */         if (i == 0) {
/* 136 */           callable.setFirstCallable(true);
/*     */         }
/*     */         
/* 139 */         for (LayoutElement layoutElement : callable.body)
/*     */         {
/* 141 */           backwardsCalls += resolveCallsForElement(i, callable, layoutElement);
/*     */         }
/*     */       } 
/*     */     } 
/* 145 */     this.backwardsCallCount = backwardsCalls;
/*     */   }
/*     */   
/*     */   private int resolveCallsForElement(int i, Callable currentCallable, LayoutElement layoutElement) {
/* 149 */     int backwardsCalls = 0;
/* 150 */     if (layoutElement instanceof Call) {
/* 151 */       Call call = (Call)layoutElement;
/* 152 */       int index = call.callableIndex;
/* 153 */       if (index == 0) {
/* 154 */         backwardsCalls++;
/* 155 */         call.setCallable(currentCallable);
/* 156 */       } else if (index > 0) {
/* 157 */         for (int k = i + 1; k < this.attributeLayoutElements.size(); k++) {
/* 158 */           AttributeLayoutElement el = this.attributeLayoutElements.get(k);
/*     */           
/* 160 */           index--;
/* 161 */           if (el instanceof Callable && index == 0) {
/* 162 */             call.setCallable((Callable)el);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 168 */         backwardsCalls++;
/* 169 */         for (int k = i - 1; k >= 0; k--) {
/* 170 */           AttributeLayoutElement el = this.attributeLayoutElements.get(k);
/*     */           
/* 172 */           index++;
/* 173 */           if (el instanceof Callable && index == 0) {
/* 174 */             call.setCallable((Callable)el);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 180 */     } else if (layoutElement instanceof Replication) {
/* 181 */       List<LayoutElement> children = ((Replication)layoutElement).layoutElements;
/* 182 */       for (LayoutElement child : children) {
/* 183 */         backwardsCalls += resolveCallsForElement(i, currentCallable, child);
/*     */       }
/*     */     } 
/* 186 */     return backwardsCalls;
/*     */   }
/*     */   
/*     */   private AttributeLayoutElement readNextAttributeElement(StringReader stream) throws IOException {
/* 190 */     stream.mark(1);
/* 191 */     int next = stream.read();
/* 192 */     if (next == -1) {
/* 193 */       return null;
/*     */     }
/* 195 */     if (next == 91) {
/* 196 */       return new Callable(readBody(getStreamUpToMatchingBracket(stream)));
/*     */     }
/* 198 */     stream.reset();
/* 199 */     return readNextLayoutElement(stream); } private LayoutElement readNextLayoutElement(StringReader stream) throws IOException { char uint_type; String str, int_type; List<UnionCase> unionCases; UnionCase c; List<LayoutElement> body; char next;
/*     */     int number;
/*     */     StringBuilder string;
/*     */     char nxt;
/* 203 */     int nextChar = stream.read();
/* 204 */     if (nextChar == -1) {
/* 205 */       return null;
/*     */     }
/* 207 */     switch (nextChar) {
/*     */       
/*     */       case 66:
/*     */       case 72:
/*     */       case 73:
/*     */       case 86:
/* 213 */         return new Integral(new String(new char[] { (char)nextChar }));
/*     */       case 70:
/*     */       case 83:
/* 216 */         return new Integral(new String(new char[] { (char)nextChar, (char)stream.read() }));
/*     */       case 80:
/* 218 */         stream.mark(1);
/* 219 */         if (stream.read() != 79) {
/* 220 */           stream.reset();
/* 221 */           return new Integral("P" + (char)stream.read());
/*     */         } 
/* 223 */         return new Integral("PO" + (char)stream.read());
/*     */       case 79:
/* 225 */         stream.mark(1);
/* 226 */         if (stream.read() != 83) {
/* 227 */           stream.reset();
/* 228 */           return new Integral("O" + (char)stream.read());
/*     */         } 
/* 230 */         return new Integral("OS" + (char)stream.read());
/*     */ 
/*     */       
/*     */       case 78:
/* 234 */         uint_type = (char)stream.read();
/* 235 */         stream.read();
/* 236 */         str = readUpToMatchingBracket(stream);
/* 237 */         return new Replication("" + uint_type, str);
/*     */ 
/*     */       
/*     */       case 84:
/* 241 */         int_type = "" + (char)stream.read();
/* 242 */         if (int_type.equals("S")) {
/* 243 */           int_type = int_type + (char)stream.read();
/*     */         }
/* 245 */         unionCases = new ArrayList<>();
/*     */         
/* 247 */         while ((c = readNextUnionCase(stream)) != null) {
/* 248 */           unionCases.add(c);
/*     */         }
/* 250 */         stream.read();
/* 251 */         stream.read();
/* 252 */         stream.read();
/* 253 */         body = null;
/* 254 */         stream.mark(1);
/* 255 */         next = (char)stream.read();
/* 256 */         if (next != ']') {
/* 257 */           stream.reset();
/* 258 */           body = readBody(getStreamUpToMatchingBracket(stream));
/*     */         } 
/* 260 */         return new Union(int_type, unionCases, body);
/*     */ 
/*     */       
/*     */       case 40:
/* 264 */         number = readNumber(stream).intValue();
/* 265 */         stream.read();
/* 266 */         return new Call(number);
/*     */       
/*     */       case 75:
/*     */       case 82:
/* 270 */         string = (new StringBuilder("")).append((char)nextChar).append((char)stream.read());
/* 271 */         nxt = (char)stream.read();
/* 272 */         string.append(nxt);
/* 273 */         if (nxt == 'N') {
/* 274 */           string.append((char)stream.read());
/*     */         }
/* 276 */         return new Reference(string.toString());
/*     */     } 
/* 278 */     return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnionCase readNextUnionCase(StringReader stream) throws IOException {
/* 289 */     stream.mark(2);
/* 290 */     stream.read();
/* 291 */     int next = stream.read();
/* 292 */     char ch = (char)next;
/* 293 */     if (ch == ')' || next == -1) {
/* 294 */       stream.reset();
/* 295 */       return null;
/*     */     } 
/* 297 */     stream.reset();
/* 298 */     stream.read();
/* 299 */     List<Integer> tags = new ArrayList<>();
/*     */     
/*     */     while (true) {
/* 302 */       Integer nextTag = readNumber(stream);
/* 303 */       if (nextTag != null) {
/* 304 */         tags.add(nextTag);
/* 305 */         stream.read();
/*     */       } 
/* 307 */       if (nextTag == null) {
/* 308 */         stream.read();
/* 309 */         stream.mark(1);
/* 310 */         ch = (char)stream.read();
/* 311 */         if (ch == ']') {
/* 312 */           return new UnionCase(tags);
/*     */         }
/* 314 */         stream.reset();
/* 315 */         return new UnionCase(tags, readBody(getStreamUpToMatchingBracket(stream)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface AttributeLayoutElement
/*     */   {
/*     */     void readBands(InputStream param1InputStream, int param1Int) throws IOException, Pack200Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void addToAttribute(int param1Int, NewAttribute param1NewAttribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class LayoutElement
/*     */     implements AttributeLayoutElement
/*     */   {
/*     */     private LayoutElement() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected int getLength(char uint_type) {
/* 347 */       int length = 0;
/* 348 */       switch (uint_type) {
/*     */         case 'B':
/* 350 */           length = 1;
/*     */           break;
/*     */         case 'H':
/* 353 */           length = 2;
/*     */           break;
/*     */         case 'I':
/* 356 */           length = 4;
/*     */           break;
/*     */         case 'V':
/* 359 */           length = 0;
/*     */           break;
/*     */       } 
/* 362 */       return length;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Integral
/*     */     extends LayoutElement
/*     */   {
/*     */     private final String tag;
/*     */     private int[] band;
/*     */     
/*     */     public Integral(String tag) {
/* 373 */       this.tag = tag;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
/* 378 */       this.band = NewAttributeBands.this.decodeBandInt(NewAttributeBands.this.attributeLayout.getName() + "_" + this.tag, in, NewAttributeBands.this.getCodec(this.tag), count);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addToAttribute(int n, NewAttribute attribute) {
/* 383 */       int value = this.band[n];
/* 384 */       if (this.tag.equals("B") || this.tag.equals("FB")) {
/* 385 */         attribute.addInteger(1, value);
/* 386 */       } else if (this.tag.equals("SB")) {
/* 387 */         attribute.addInteger(1, (byte)value);
/* 388 */       } else if (this.tag.equals("H") || this.tag.equals("FH")) {
/* 389 */         attribute.addInteger(2, value);
/* 390 */       } else if (this.tag.equals("SH")) {
/* 391 */         attribute.addInteger(2, (short)value);
/* 392 */       } else if (this.tag.equals("I") || this.tag.equals("FI")) {
/* 393 */         attribute.addInteger(4, value);
/* 394 */       } else if (this.tag.equals("SI")) {
/* 395 */         attribute.addInteger(4, value);
/* 396 */       } else if (!this.tag.equals("V") && !this.tag.equals("FV") && !this.tag.equals("SV")) {
/*     */ 
/*     */         
/* 399 */         if (this.tag.startsWith("PO")) {
/* 400 */           char uint_type = this.tag.substring(2).toCharArray()[0];
/* 401 */           int length = getLength(uint_type);
/* 402 */           attribute.addBCOffset(length, value);
/* 403 */         } else if (this.tag.startsWith("P")) {
/* 404 */           char uint_type = this.tag.substring(1).toCharArray()[0];
/* 405 */           int length = getLength(uint_type);
/* 406 */           attribute.addBCIndex(length, value);
/* 407 */         } else if (this.tag.startsWith("OS")) {
/* 408 */           char uint_type = this.tag.substring(2).toCharArray()[0];
/* 409 */           int length = getLength(uint_type);
/* 410 */           if (length == 1) {
/* 411 */             value = (byte)value;
/* 412 */           } else if (length == 2) {
/* 413 */             value = (short)value;
/* 414 */           } else if (length == 4) {
/* 415 */             value = value;
/*     */           } 
/* 417 */           attribute.addBCLength(length, value);
/* 418 */         } else if (this.tag.startsWith("O")) {
/* 419 */           char uint_type = this.tag.substring(1).toCharArray()[0];
/* 420 */           int length = getLength(uint_type);
/* 421 */           attribute.addBCLength(length, value);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     int getValue(int index) {
/* 426 */       return this.band[index];
/*     */     }
/*     */     
/*     */     public String getTag() {
/* 430 */       return this.tag;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class Replication
/*     */     extends LayoutElement
/*     */   {
/*     */     private final NewAttributeBands.Integral countElement;
/*     */ 
/*     */     
/* 442 */     private final List<NewAttributeBands.LayoutElement> layoutElements = new ArrayList<>();
/*     */     
/*     */     public Replication(String tag, String contents) throws IOException {
/* 445 */       this.countElement = new NewAttributeBands.Integral(tag);
/* 446 */       StringReader stream = new StringReader(contents);
/*     */       NewAttributeBands.LayoutElement e;
/* 448 */       while ((e = NewAttributeBands.this.readNextLayoutElement(stream)) != null) {
/* 449 */         this.layoutElements.add(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
/* 455 */       this.countElement.readBands(in, count);
/* 456 */       int arrayCount = 0;
/* 457 */       for (int i = 0; i < count; i++) {
/* 458 */         arrayCount += this.countElement.getValue(i);
/*     */       }
/* 460 */       for (NewAttributeBands.LayoutElement layoutElement : this.layoutElements) {
/* 461 */         layoutElement.readBands(in, arrayCount);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void addToAttribute(int index, NewAttribute attribute) {
/* 468 */       this.countElement.addToAttribute(index, attribute);
/*     */ 
/*     */       
/* 471 */       int offset = 0;
/* 472 */       for (int i = 0; i < index; i++) {
/* 473 */         offset += this.countElement.getValue(i);
/*     */       }
/* 475 */       long numElements = this.countElement.getValue(index);
/* 476 */       for (int j = offset; j < offset + numElements; j++) {
/* 477 */         for (NewAttributeBands.LayoutElement layoutElement : this.layoutElements) {
/* 478 */           layoutElement.addToAttribute(j, attribute);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public NewAttributeBands.Integral getCountElement() {
/* 484 */       return this.countElement;
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getLayoutElements() {
/* 488 */       return this.layoutElements;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class Union
/*     */     extends LayoutElement
/*     */   {
/*     */     private final NewAttributeBands.Integral unionTag;
/*     */     
/*     */     private final List<NewAttributeBands.UnionCase> unionCases;
/*     */     private final List<NewAttributeBands.LayoutElement> defaultCaseBody;
/*     */     private int[] caseCounts;
/*     */     private int defaultCount;
/*     */     
/*     */     public Union(String tag, List<NewAttributeBands.UnionCase> unionCases, List<NewAttributeBands.LayoutElement> body) {
/* 504 */       this.unionTag = new NewAttributeBands.Integral(tag);
/* 505 */       this.unionCases = unionCases;
/* 506 */       this.defaultCaseBody = body;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
/* 511 */       this.unionTag.readBands(in, count);
/* 512 */       int[] values = this.unionTag.band;
/*     */       
/* 514 */       this.caseCounts = new int[this.unionCases.size()];
/* 515 */       for (int i = 0; i < this.caseCounts.length; i++) {
/* 516 */         NewAttributeBands.UnionCase unionCase = this.unionCases.get(i);
/* 517 */         for (int value : values) {
/* 518 */           if (unionCase.hasTag(value)) {
/* 519 */             this.caseCounts[i] = this.caseCounts[i] + 1;
/*     */           }
/*     */         } 
/* 522 */         unionCase.readBands(in, this.caseCounts[i]);
/*     */       } 
/*     */       
/* 525 */       for (int value : values) {
/* 526 */         boolean found = false;
/* 527 */         for (NewAttributeBands.UnionCase unionCase : this.unionCases) {
/* 528 */           if (unionCase.hasTag(value)) {
/* 529 */             found = true;
/*     */           }
/*     */         } 
/* 532 */         if (!found) {
/* 533 */           this.defaultCount++;
/*     */         }
/*     */       } 
/* 536 */       if (this.defaultCaseBody != null) {
/* 537 */         for (NewAttributeBands.LayoutElement element : this.defaultCaseBody) {
/* 538 */           element.readBands(in, this.defaultCount);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addToAttribute(int n, NewAttribute attribute) {
/* 545 */       this.unionTag.addToAttribute(n, attribute);
/* 546 */       int offset = 0;
/* 547 */       int[] tagBand = this.unionTag.band;
/* 548 */       int tag = this.unionTag.getValue(n);
/* 549 */       boolean defaultCase = true;
/* 550 */       for (NewAttributeBands.UnionCase unionCase : this.unionCases) {
/* 551 */         if (unionCase.hasTag(tag)) {
/* 552 */           defaultCase = false;
/* 553 */           for (int j = 0; j < n; j++) {
/* 554 */             if (unionCase.hasTag(tagBand[j])) {
/* 555 */               offset++;
/*     */             }
/*     */           } 
/* 558 */           unionCase.addToAttribute(offset, attribute);
/*     */         } 
/*     */       } 
/* 561 */       if (defaultCase) {
/*     */         
/* 563 */         int defaultOffset = 0;
/* 564 */         for (int j = 0; j < n; j++) {
/* 565 */           boolean found = false;
/* 566 */           for (NewAttributeBands.UnionCase unionCase : this.unionCases) {
/* 567 */             if (unionCase.hasTag(tagBand[j])) {
/* 568 */               found = true;
/*     */             }
/*     */           } 
/* 571 */           if (!found) {
/* 572 */             defaultOffset++;
/*     */           }
/*     */         } 
/* 575 */         if (this.defaultCaseBody != null) {
/* 576 */           for (NewAttributeBands.LayoutElement element : this.defaultCaseBody) {
/* 577 */             element.addToAttribute(defaultOffset, attribute);
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public NewAttributeBands.Integral getUnionTag() {
/* 584 */       return this.unionTag;
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.UnionCase> getUnionCases() {
/* 588 */       return this.unionCases;
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getDefaultCaseBody() {
/* 592 */       return this.defaultCaseBody;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Call
/*     */     extends LayoutElement
/*     */   {
/*     */     private final int callableIndex;
/*     */     private NewAttributeBands.Callable callable;
/*     */     
/*     */     public Call(int callableIndex) {
/* 603 */       this.callableIndex = callableIndex;
/*     */     }
/*     */     
/*     */     public void setCallable(NewAttributeBands.Callable callable) {
/* 607 */       this.callable = callable;
/* 608 */       if (this.callableIndex < 1) {
/* 609 */         callable.setBackwardsCallable();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void readBands(InputStream in, int count) {
/* 620 */       if (this.callableIndex > 0) {
/* 621 */         this.callable.addCount(count);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addToAttribute(int n, NewAttribute attribute) {
/* 627 */       this.callable.addNextToAttribute(attribute);
/*     */     }
/*     */     
/*     */     public int getCallableIndex() {
/* 631 */       return this.callableIndex;
/*     */     }
/*     */     
/*     */     public NewAttributeBands.Callable getCallable() {
/* 635 */       return this.callable;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class Reference
/*     */     extends LayoutElement
/*     */   {
/*     */     private final String tag;
/*     */     
/*     */     private Object band;
/*     */     
/*     */     private final int length;
/*     */ 
/*     */     
/*     */     public Reference(String tag) {
/* 651 */       this.tag = tag;
/* 652 */       this.length = getLength(tag.charAt(tag.length() - 1));
/*     */     }
/*     */ 
/*     */     
/*     */     public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
/* 657 */       if (this.tag.startsWith("KI")) {
/* 658 */         this.band = NewAttributeBands.this.parseCPIntReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 659 */       } else if (this.tag.startsWith("KJ")) {
/* 660 */         this.band = NewAttributeBands.this.parseCPLongReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 661 */       } else if (this.tag.startsWith("KF")) {
/* 662 */         this.band = NewAttributeBands.this.parseCPFloatReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 663 */       } else if (this.tag.startsWith("KD")) {
/* 664 */         this.band = NewAttributeBands.this.parseCPDoubleReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 665 */       } else if (this.tag.startsWith("KS")) {
/* 666 */         this.band = NewAttributeBands.this.parseCPStringReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 667 */       } else if (this.tag.startsWith("RC")) {
/* 668 */         this.band = NewAttributeBands.this.parseCPClassReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 669 */       } else if (this.tag.startsWith("RS")) {
/* 670 */         this.band = NewAttributeBands.this.parseCPSignatureReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 671 */       } else if (this.tag.startsWith("RD")) {
/* 672 */         this.band = NewAttributeBands.this.parseCPDescriptorReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 673 */       } else if (this.tag.startsWith("RF")) {
/* 674 */         this.band = NewAttributeBands.this.parseCPFieldRefReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 675 */       } else if (this.tag.startsWith("RM")) {
/* 676 */         this.band = NewAttributeBands.this.parseCPMethodRefReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 677 */       } else if (this.tag.startsWith("RI")) {
/* 678 */         this.band = NewAttributeBands.this.parseCPInterfaceMethodRefReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/* 679 */       } else if (this.tag.startsWith("RU")) {
/* 680 */         this.band = NewAttributeBands.this.parseCPUTF8References(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void addToAttribute(int n, NewAttribute attribute) {
/* 686 */       if (this.tag.startsWith("KI")) {
/* 687 */         attribute.addToBody(this.length, ((CPInteger[])this.band)[n]);
/* 688 */       } else if (this.tag.startsWith("KJ")) {
/* 689 */         attribute.addToBody(this.length, ((CPLong[])this.band)[n]);
/* 690 */       } else if (this.tag.startsWith("KF")) {
/* 691 */         attribute.addToBody(this.length, ((CPFloat[])this.band)[n]);
/* 692 */       } else if (this.tag.startsWith("KD")) {
/* 693 */         attribute.addToBody(this.length, ((CPDouble[])this.band)[n]);
/* 694 */       } else if (this.tag.startsWith("KS")) {
/* 695 */         attribute.addToBody(this.length, ((CPString[])this.band)[n]);
/* 696 */       } else if (this.tag.startsWith("RC")) {
/* 697 */         attribute.addToBody(this.length, ((CPClass[])this.band)[n]);
/* 698 */       } else if (this.tag.startsWith("RS")) {
/* 699 */         attribute.addToBody(this.length, ((CPUTF8[])this.band)[n]);
/* 700 */       } else if (this.tag.startsWith("RD")) {
/* 701 */         attribute.addToBody(this.length, ((CPNameAndType[])this.band)[n]);
/* 702 */       } else if (this.tag.startsWith("RF")) {
/* 703 */         attribute.addToBody(this.length, ((CPFieldRef[])this.band)[n]);
/* 704 */       } else if (this.tag.startsWith("RM")) {
/* 705 */         attribute.addToBody(this.length, ((CPMethodRef[])this.band)[n]);
/* 706 */       } else if (this.tag.startsWith("RI")) {
/* 707 */         attribute.addToBody(this.length, ((CPInterfaceMethodRef[])this.band)[n]);
/* 708 */       } else if (this.tag.startsWith("RU")) {
/* 709 */         attribute.addToBody(this.length, ((CPUTF8[])this.band)[n]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getTag() {
/* 714 */       return this.tag;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Callable
/*     */     implements AttributeLayoutElement
/*     */   {
/*     */     private final List<NewAttributeBands.LayoutElement> body;
/*     */     private boolean isBackwardsCallable;
/*     */     private boolean isFirstCallable;
/*     */     private int count;
/*     */     private int index;
/*     */     
/*     */     public Callable(List<NewAttributeBands.LayoutElement> body) {
/* 728 */       this.body = body;
/*     */     }
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
/*     */     public void addNextToAttribute(NewAttribute attribute) {
/* 741 */       for (NewAttributeBands.LayoutElement element : this.body) {
/* 742 */         element.addToAttribute(this.index, attribute);
/*     */       }
/* 744 */       this.index++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addCount(int count) {
/* 753 */       this.count += count;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
/* 758 */       if (this.isFirstCallable) {
/* 759 */         count += this.count;
/*     */       } else {
/* 761 */         count = this.count;
/*     */       } 
/* 763 */       for (NewAttributeBands.LayoutElement element : this.body) {
/* 764 */         element.readBands(in, count);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addToAttribute(int n, NewAttribute attribute) {
/* 770 */       if (this.isFirstCallable) {
/*     */         
/* 772 */         for (NewAttributeBands.LayoutElement element : this.body) {
/* 773 */           element.addToAttribute(this.index, attribute);
/*     */         }
/* 775 */         this.index++;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isBackwardsCallable() {
/* 780 */       return this.isBackwardsCallable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBackwardsCallable() {
/* 787 */       this.isBackwardsCallable = true;
/*     */     }
/*     */     
/*     */     public void setFirstCallable(boolean isFirstCallable) {
/* 791 */       this.isFirstCallable = isFirstCallable;
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getBody() {
/* 795 */       return this.body;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class UnionCase
/*     */     extends LayoutElement
/*     */   {
/*     */     private List<NewAttributeBands.LayoutElement> body;
/*     */     
/*     */     private final List<Integer> tags;
/*     */ 
/*     */     
/*     */     public UnionCase(List<Integer> tags) {
/* 809 */       this.tags = tags;
/*     */     }
/*     */     
/*     */     public boolean hasTag(int i) {
/* 813 */       return this.tags.contains(Integer.valueOf(i));
/*     */     }
/*     */     
/*     */     public boolean hasTag(long l) {
/* 817 */       return this.tags.contains(Integer.valueOf((int)l));
/*     */     }
/*     */     
/*     */     public UnionCase(List<Integer> tags, List<NewAttributeBands.LayoutElement> body) {
/* 821 */       this.tags = tags;
/* 822 */       this.body = body;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
/* 827 */       if (this.body != null) {
/* 828 */         for (NewAttributeBands.LayoutElement element : this.body) {
/* 829 */           element.readBands(in, count);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addToAttribute(int index, NewAttribute attribute) {
/* 836 */       if (this.body != null) {
/* 837 */         for (NewAttributeBands.LayoutElement element : this.body) {
/* 838 */           element.addToAttribute(index, attribute);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getBody() {
/* 844 */       return (this.body == null) ? Collections.EMPTY_LIST : this.body;
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
/*     */   
/*     */   private StringReader getStreamUpToMatchingBracket(StringReader stream) throws IOException {
/* 857 */     StringBuilder sb = new StringBuilder();
/* 858 */     int foundBracket = -1;
/* 859 */     while (foundBracket != 0) {
/* 860 */       int read = stream.read();
/* 861 */       if (read == -1) {
/*     */         break;
/*     */       }
/* 864 */       char c = (char)read;
/* 865 */       if (c == ']') {
/* 866 */         foundBracket++;
/*     */       }
/* 868 */       if (c == '[') {
/* 869 */         foundBracket--;
/*     */       }
/* 871 */       if (foundBracket != 0) {
/* 872 */         sb.append(c);
/*     */       }
/*     */     } 
/* 875 */     return new StringReader(sb.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BHSDCodec getCodec(String layoutElement) {
/* 885 */     if (layoutElement.indexOf('O') >= 0) {
/* 886 */       return Codec.BRANCH5;
/*     */     }
/* 888 */     if (layoutElement.indexOf('P') >= 0) {
/* 889 */       return Codec.BCI5;
/*     */     }
/* 891 */     if (layoutElement.indexOf('S') >= 0 && layoutElement.indexOf("KS") < 0 && layoutElement
/* 892 */       .indexOf("RS") < 0) {
/* 893 */       return Codec.SIGNED5;
/*     */     }
/* 895 */     if (layoutElement.indexOf('B') >= 0) {
/* 896 */       return Codec.BYTE1;
/*     */     }
/* 898 */     return Codec.UNSIGNED5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String readUpToMatchingBracket(StringReader stream) throws IOException {
/* 909 */     StringBuilder sb = new StringBuilder();
/* 910 */     int foundBracket = -1;
/* 911 */     while (foundBracket != 0) {
/* 912 */       int read = stream.read();
/* 913 */       if (read == -1) {
/*     */         break;
/*     */       }
/* 916 */       char c = (char)read;
/* 917 */       if (c == ']') {
/* 918 */         foundBracket++;
/*     */       }
/* 920 */       if (c == '[') {
/* 921 */         foundBracket--;
/*     */       }
/* 923 */       if (foundBracket != 0) {
/* 924 */         sb.append(c);
/*     */       }
/*     */     } 
/* 927 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Integer readNumber(StringReader stream) throws IOException {
/* 938 */     stream.mark(1);
/* 939 */     char first = (char)stream.read();
/* 940 */     boolean negative = (first == '-');
/* 941 */     if (!negative) {
/* 942 */       stream.reset();
/*     */     }
/* 944 */     stream.mark(100);
/*     */     
/* 946 */     int length = 0; int i;
/* 947 */     while ((i = stream.read()) != -1 && Character.isDigit((char)i)) {
/* 948 */       length++;
/*     */     }
/* 950 */     stream.reset();
/* 951 */     if (length == 0) {
/* 952 */       return null;
/*     */     }
/* 954 */     char[] digits = new char[length];
/* 955 */     int read = stream.read(digits);
/* 956 */     if (read != digits.length) {
/* 957 */       throw new IOException("Error reading from the input stream");
/*     */     }
/* 959 */     return Integer.valueOf(Integer.parseInt((negative ? "-" : "") + new String(digits)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<LayoutElement> readBody(StringReader stream) throws IOException {
/* 970 */     List<LayoutElement> layoutElements = new ArrayList<>();
/*     */     LayoutElement e;
/* 972 */     while ((e = readNextLayoutElement(stream)) != null) {
/* 973 */       layoutElements.add(e);
/*     */     }
/* 975 */     return layoutElements;
/*     */   }
/*     */   
/*     */   public int getBackwardsCallCount() {
/* 979 */     return this.backwardsCallCount;
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
/*     */   public void setBackwardsCalls(int[] backwardsCalls) throws IOException {
/* 991 */     int index = 0;
/* 992 */     parseLayout();
/* 993 */     for (AttributeLayoutElement element : this.attributeLayoutElements) {
/* 994 */       if (element instanceof Callable && ((Callable)element).isBackwardsCallable()) {
/* 995 */         ((Callable)element).addCount(backwardsCalls[index]);
/* 996 */         index++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unpack() throws IOException, Pack200Exception {}
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\NewAttributeBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */