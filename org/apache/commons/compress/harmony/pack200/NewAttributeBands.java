/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.objectweb.asm.Label;
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
/*     */ public class NewAttributeBands
/*     */   extends BandSet
/*     */ {
/*     */   protected List<AttributeLayoutElement> attributeLayoutElements;
/*     */   private int[] backwardsCallCounts;
/*     */   private final CpBands cpBands;
/*     */   private final AttributeDefinitionBands.AttributeDefinition def;
/*     */   private boolean usedAtLeastOnce;
/*     */   private Integral lastPIntegral;
/*     */   
/*     */   public NewAttributeBands(int effort, CpBands cpBands, SegmentHeader header, AttributeDefinitionBands.AttributeDefinition def) throws IOException {
/*  50 */     super(effort, header);
/*  51 */     this.def = def;
/*  52 */     this.cpBands = cpBands;
/*  53 */     parseLayout();
/*     */   }
/*     */   
/*     */   public void addAttribute(NewAttribute attribute) {
/*  57 */     this.usedAtLeastOnce = true;
/*  58 */     InputStream stream = new ByteArrayInputStream(attribute.getBytes());
/*  59 */     for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements) {
/*  60 */       attributeLayoutElement.addAttributeToBand(attribute, stream);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
/*  66 */     for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements) {
/*  67 */       attributeLayoutElement.pack(outputStream);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAttributeName() {
/*  72 */     return this.def.name.getUnderlyingString();
/*     */   }
/*     */   
/*     */   public int getFlagIndex() {
/*  76 */     return this.def.index;
/*     */   }
/*     */   
/*     */   public int[] numBackwardsCalls() {
/*  80 */     return this.backwardsCallCounts;
/*     */   }
/*     */   
/*     */   public boolean isUsedAtLeastOnce() {
/*  84 */     return this.usedAtLeastOnce;
/*     */   }
/*     */   
/*     */   private void parseLayout() throws IOException {
/*  88 */     String layout = this.def.layout.getUnderlyingString();
/*  89 */     if (this.attributeLayoutElements == null) {
/*  90 */       this.attributeLayoutElements = new ArrayList<>();
/*  91 */       StringReader reader = new StringReader(layout);
/*     */       AttributeLayoutElement e;
/*  93 */       while ((e = readNextAttributeElement(reader)) != null) {
/*  94 */         this.attributeLayoutElements.add(e);
/*     */       }
/*  96 */       resolveCalls();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resolveCalls() {
/* 106 */     for (int i = 0; i < this.attributeLayoutElements.size(); i++) {
/* 107 */       AttributeLayoutElement element = this.attributeLayoutElements.get(i);
/* 108 */       if (element instanceof Callable) {
/* 109 */         Callable callable = (Callable)element;
/* 110 */         List<LayoutElement> body = callable.body;
/* 111 */         for (LayoutElement layoutElement : body)
/*     */         {
/* 113 */           resolveCallsForElement(i, callable, layoutElement);
/*     */         }
/*     */       } 
/*     */     } 
/* 117 */     int backwardsCallableIndex = 0;
/* 118 */     for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements) {
/* 119 */       if (attributeLayoutElement instanceof Callable) {
/* 120 */         Callable callable = (Callable)attributeLayoutElement;
/* 121 */         if (callable.isBackwardsCallable) {
/* 122 */           callable.setBackwardsCallableIndex(backwardsCallableIndex);
/* 123 */           backwardsCallableIndex++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 127 */     this.backwardsCallCounts = new int[backwardsCallableIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   private void resolveCallsForElement(int i, Callable currentCallable, LayoutElement layoutElement) {
/* 132 */     if (layoutElement instanceof Call) {
/* 133 */       Call call = (Call)layoutElement;
/* 134 */       int index = call.callableIndex;
/* 135 */       if (index == 0) {
/* 136 */         call.setCallable(currentCallable);
/* 137 */       } else if (index > 0) {
/* 138 */         for (int k = i + 1; k < this.attributeLayoutElements.size(); k++) {
/* 139 */           AttributeLayoutElement el = this.attributeLayoutElements.get(k);
/*     */           
/* 141 */           index--;
/* 142 */           if (el instanceof Callable && index == 0) {
/* 143 */             call.setCallable((Callable)el);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 149 */         for (int k = i - 1; k >= 0; k--) {
/* 150 */           AttributeLayoutElement el = this.attributeLayoutElements.get(k);
/*     */           
/* 152 */           index++;
/* 153 */           if (el instanceof Callable && index == 0) {
/* 154 */             call.setCallable((Callable)el);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 160 */     } else if (layoutElement instanceof Replication) {
/* 161 */       List<LayoutElement> children = ((Replication)layoutElement).layoutElements;
/* 162 */       for (LayoutElement child : children) {
/* 163 */         resolveCallsForElement(i, currentCallable, child);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private AttributeLayoutElement readNextAttributeElement(StringReader reader) throws IOException {
/* 169 */     reader.mark(1);
/* 170 */     int next = reader.read();
/* 171 */     if (next == -1) {
/* 172 */       return null;
/*     */     }
/* 174 */     if (next == 91) {
/* 175 */       return new Callable(readBody(getStreamUpToMatchingBracket(reader)));
/*     */     }
/* 177 */     reader.reset();
/* 178 */     return readNextLayoutElement(reader); } private LayoutElement readNextLayoutElement(StringReader reader) throws IOException { char uint_type; String str, int_type; List<UnionCase> unionCases; UnionCase c; List<LayoutElement> body; char next;
/*     */     int number;
/*     */     StringBuilder string;
/*     */     char nxt;
/* 182 */     int nextChar = reader.read();
/* 183 */     if (nextChar == -1) {
/* 184 */       return null;
/*     */     }
/*     */     
/* 187 */     switch (nextChar) {
/*     */       
/*     */       case 66:
/*     */       case 72:
/*     */       case 73:
/*     */       case 86:
/* 193 */         return new Integral(new String(new char[] { (char)nextChar }));
/*     */       case 70:
/*     */       case 83:
/* 196 */         return new Integral(new String(new char[] { (char)nextChar, (char)reader.read() }));
/*     */       case 80:
/* 198 */         reader.mark(1);
/* 199 */         if (reader.read() != 79) {
/* 200 */           reader.reset();
/* 201 */           this.lastPIntegral = new Integral("P" + (char)reader.read());
/* 202 */           return this.lastPIntegral;
/*     */         } 
/* 204 */         this.lastPIntegral = new Integral("PO" + (char)reader.read(), this.lastPIntegral);
/* 205 */         return this.lastPIntegral;
/*     */       case 79:
/* 207 */         reader.mark(1);
/* 208 */         if (reader.read() != 83) {
/* 209 */           reader.reset();
/* 210 */           return new Integral("O" + (char)reader.read(), this.lastPIntegral);
/*     */         } 
/* 212 */         return new Integral("OS" + (char)reader.read(), this.lastPIntegral);
/*     */ 
/*     */       
/*     */       case 78:
/* 216 */         uint_type = (char)reader.read();
/* 217 */         reader.read();
/* 218 */         str = readUpToMatchingBracket(reader);
/* 219 */         return new Replication("" + uint_type, str);
/*     */ 
/*     */       
/*     */       case 84:
/* 223 */         int_type = String.valueOf((char)reader.read());
/* 224 */         if (int_type.equals("S")) {
/* 225 */           int_type = int_type + (char)reader.read();
/*     */         }
/* 227 */         unionCases = new ArrayList<>();
/*     */         
/* 229 */         while ((c = readNextUnionCase(reader)) != null) {
/* 230 */           unionCases.add(c);
/*     */         }
/* 232 */         reader.read();
/* 233 */         reader.read();
/* 234 */         reader.read();
/* 235 */         body = null;
/* 236 */         reader.mark(1);
/* 237 */         next = (char)reader.read();
/* 238 */         if (next != ']') {
/* 239 */           reader.reset();
/* 240 */           body = readBody(getStreamUpToMatchingBracket(reader));
/*     */         } 
/* 242 */         return new Union(int_type, unionCases, body);
/*     */ 
/*     */       
/*     */       case 40:
/* 246 */         number = readNumber(reader).intValue();
/* 247 */         reader.read();
/* 248 */         return new Call(number);
/*     */       
/*     */       case 75:
/*     */       case 82:
/* 252 */         string = (new StringBuilder("")).append((char)nextChar).append((char)reader.read());
/* 253 */         nxt = (char)reader.read();
/* 254 */         string.append(nxt);
/* 255 */         if (nxt == 'N') {
/* 256 */           string.append((char)reader.read());
/*     */         }
/* 258 */         return new Reference(string.toString());
/*     */     } 
/* 260 */     return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnionCase readNextUnionCase(StringReader reader) throws IOException {
/* 271 */     reader.mark(2);
/* 272 */     reader.read();
/* 273 */     int next = reader.read();
/* 274 */     char ch = (char)next;
/* 275 */     if (ch == ')' || next == -1) {
/* 276 */       reader.reset();
/* 277 */       return null;
/*     */     } 
/* 279 */     reader.reset();
/* 280 */     reader.read();
/* 281 */     List<Integer> tags = new ArrayList<>();
/*     */     
/*     */     while (true) {
/* 284 */       Integer nextTag = readNumber(reader);
/* 285 */       if (nextTag != null) {
/* 286 */         tags.add(nextTag);
/* 287 */         reader.read();
/*     */       } 
/* 289 */       if (nextTag == null) {
/* 290 */         reader.read();
/* 291 */         reader.mark(1);
/* 292 */         ch = (char)reader.read();
/* 293 */         if (ch == ']') {
/* 294 */           return new UnionCase(tags);
/*     */         }
/* 296 */         reader.reset();
/* 297 */         return new UnionCase(tags, readBody(getStreamUpToMatchingBracket(reader)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface AttributeLayoutElement
/*     */   {
/*     */     void addAttributeToBand(NewAttribute param1NewAttribute, InputStream param1InputStream);
/*     */ 
/*     */     
/*     */     void pack(OutputStream param1OutputStream) throws IOException, Pack200Exception;
/*     */     
/*     */     void renumberBci(IntList param1IntList, Map<Label, Integer> param1Map);
/*     */   }
/*     */   
/*     */   public abstract class LayoutElement
/*     */     implements AttributeLayoutElement
/*     */   {
/*     */     protected int getLength(char uint_type) {
/* 317 */       int length = 0;
/* 318 */       switch (uint_type) {
/*     */         case 'B':
/* 320 */           length = 1;
/*     */           break;
/*     */         case 'H':
/* 323 */           length = 2;
/*     */           break;
/*     */         case 'I':
/* 326 */           length = 4;
/*     */           break;
/*     */         case 'V':
/* 329 */           length = 0;
/*     */           break;
/*     */       } 
/* 332 */       return length;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Integral
/*     */     extends LayoutElement
/*     */   {
/*     */     private final String tag;
/* 340 */     private final List band = new ArrayList();
/*     */     
/*     */     private final BHSDCodec defaultCodec;
/*     */     
/*     */     private Integral previousIntegral;
/*     */     private int previousPValue;
/*     */     
/*     */     public Integral(String tag) {
/* 348 */       this.tag = tag;
/* 349 */       this.defaultCodec = NewAttributeBands.this.getCodec(tag);
/*     */     }
/*     */     
/*     */     public Integral(String tag, Integral previousIntegral) {
/* 353 */       this.tag = tag;
/* 354 */       this.defaultCodec = NewAttributeBands.this.getCodec(tag);
/* 355 */       this.previousIntegral = previousIntegral;
/*     */     }
/*     */     
/*     */     public String getTag() {
/* 359 */       return this.tag;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
/* 364 */       Object val = null;
/* 365 */       int value = 0;
/* 366 */       if (this.tag.equals("B") || this.tag.equals("FB")) {
/* 367 */         value = NewAttributeBands.this.readInteger(1, inputStream) & 0xFF;
/* 368 */       } else if (this.tag.equals("SB")) {
/* 369 */         value = NewAttributeBands.this.readInteger(1, inputStream);
/* 370 */       } else if (this.tag.equals("H") || this.tag.equals("FH")) {
/* 371 */         value = NewAttributeBands.this.readInteger(2, inputStream) & 0xFFFF;
/* 372 */       } else if (this.tag.equals("SH")) {
/* 373 */         value = NewAttributeBands.this.readInteger(2, inputStream);
/* 374 */       } else if (this.tag.equals("I") || this.tag.equals("FI")) {
/* 375 */         value = NewAttributeBands.this.readInteger(4, inputStream);
/* 376 */       } else if (this.tag.equals("SI")) {
/* 377 */         value = NewAttributeBands.this.readInteger(4, inputStream);
/* 378 */       } else if (!this.tag.equals("V") && !this.tag.equals("FV") && !this.tag.equals("SV")) {
/*     */         
/* 380 */         if (this.tag.startsWith("PO") || this.tag.startsWith("OS")) {
/* 381 */           char uint_type = this.tag.substring(2).toCharArray()[0];
/* 382 */           int length = getLength(uint_type);
/* 383 */           value = NewAttributeBands.this.readInteger(length, inputStream);
/* 384 */           value += this.previousIntegral.previousPValue;
/* 385 */           val = attribute.getLabel(value);
/* 386 */           this.previousPValue = value;
/* 387 */         } else if (this.tag.startsWith("P")) {
/* 388 */           char uint_type = this.tag.substring(1).toCharArray()[0];
/* 389 */           int length = getLength(uint_type);
/* 390 */           value = NewAttributeBands.this.readInteger(length, inputStream);
/* 391 */           val = attribute.getLabel(value);
/* 392 */           this.previousPValue = value;
/* 393 */         } else if (this.tag.startsWith("O")) {
/* 394 */           char uint_type = this.tag.substring(1).toCharArray()[0];
/* 395 */           int length = getLength(uint_type);
/* 396 */           value = NewAttributeBands.this.readInteger(length, inputStream);
/* 397 */           value += this.previousIntegral.previousPValue;
/* 398 */           val = attribute.getLabel(value);
/* 399 */           this.previousPValue = value;
/*     */         } 
/* 401 */       }  if (val == null) {
/* 402 */         val = Integer.valueOf(value);
/*     */       }
/* 404 */       this.band.add(val);
/*     */     }
/*     */ 
/*     */     
/*     */     public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
/* 409 */       PackingUtils.log("Writing new attribute bands...");
/* 410 */       byte[] encodedBand = NewAttributeBands.this.encodeBandInt(this.tag, NewAttributeBands.this.integerListToArray(this.band), this.defaultCodec);
/* 411 */       outputStream.write(encodedBand);
/* 412 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + this.tag + "[" + this.band.size() + "]");
/*     */     }
/*     */     
/*     */     public int latestValue() {
/* 416 */       return ((Integer)this.band.get(this.band.size() - 1)).intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 421 */       if (this.tag.startsWith("O") || this.tag.startsWith("PO")) {
/* 422 */         renumberOffsetBci(this.previousIntegral.band, bciRenumbering, labelsToOffsets);
/* 423 */       } else if (this.tag.startsWith("P")) {
/* 424 */         for (int i = this.band.size() - 1; i >= 0; i--) {
/* 425 */           Object label = this.band.get(i);
/* 426 */           if (label instanceof Integer) {
/*     */             break;
/*     */           }
/* 429 */           if (label instanceof Label) {
/* 430 */             this.band.remove(i);
/* 431 */             Integer bytecodeIndex = labelsToOffsets.get(label);
/* 432 */             this.band.add(i, Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue())));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void renumberOffsetBci(List<Integer> relative, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 439 */       for (int i = this.band.size() - 1; i >= 0; i--) {
/* 440 */         Object label = this.band.get(i);
/* 441 */         if (label instanceof Integer) {
/*     */           break;
/*     */         }
/* 444 */         if (label instanceof Label) {
/* 445 */           this.band.remove(i);
/* 446 */           Integer bytecodeIndex = labelsToOffsets.get(label);
/*     */           
/* 448 */           Integer renumberedOffset = Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue()) - ((Integer)relative.get(i)).intValue());
/* 449 */           this.band.add(i, renumberedOffset);
/*     */         } 
/*     */       } 
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
/* 463 */     private final List<NewAttributeBands.LayoutElement> layoutElements = new ArrayList<>();
/*     */     
/*     */     public NewAttributeBands.Integral getCountElement() {
/* 466 */       return this.countElement;
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getLayoutElements() {
/* 470 */       return this.layoutElements;
/*     */     }
/*     */     
/*     */     public Replication(String tag, String contents) throws IOException {
/* 474 */       this.countElement = new NewAttributeBands.Integral(tag);
/* 475 */       StringReader stream = new StringReader(contents);
/*     */       NewAttributeBands.LayoutElement e;
/* 477 */       while ((e = NewAttributeBands.this.readNextLayoutElement(stream)) != null) {
/* 478 */         this.layoutElements.add(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
/* 484 */       this.countElement.addAttributeToBand(attribute, inputStream);
/* 485 */       int count = this.countElement.latestValue();
/* 486 */       for (int i = 0; i < count; i++) {
/* 487 */         for (NewAttributeBands.AttributeLayoutElement layoutElement : this.layoutElements) {
/* 488 */           layoutElement.addAttributeToBand(attribute, inputStream);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void pack(OutputStream out) throws IOException, Pack200Exception {
/* 495 */       this.countElement.pack(out);
/* 496 */       for (NewAttributeBands.AttributeLayoutElement layoutElement : this.layoutElements) {
/* 497 */         layoutElement.pack(out);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 503 */       for (NewAttributeBands.AttributeLayoutElement layoutElement : this.layoutElements) {
/* 504 */         layoutElement.renumberBci(bciRenumbering, labelsToOffsets);
/*     */       }
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
/*     */     
/*     */     public Union(String tag, List<NewAttributeBands.UnionCase> unionCases, List<NewAttributeBands.LayoutElement> body) {
/* 519 */       this.unionTag = new NewAttributeBands.Integral(tag);
/* 520 */       this.unionCases = unionCases;
/* 521 */       this.defaultCaseBody = body;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
/* 526 */       this.unionTag.addAttributeToBand(attribute, inputStream);
/* 527 */       long tag = this.unionTag.latestValue();
/* 528 */       boolean defaultCase = true;
/* 529 */       for (NewAttributeBands.UnionCase unionCase : this.unionCases) {
/* 530 */         if (unionCase.hasTag(tag)) {
/* 531 */           defaultCase = false;
/* 532 */           unionCase.addAttributeToBand(attribute, inputStream);
/*     */         } 
/*     */       } 
/* 535 */       if (defaultCase) {
/* 536 */         for (NewAttributeBands.LayoutElement layoutElement : this.defaultCaseBody) {
/* 537 */           layoutElement.addAttributeToBand(attribute, inputStream);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
/* 544 */       this.unionTag.pack(outputStream);
/* 545 */       for (NewAttributeBands.UnionCase unionCase : this.unionCases) {
/* 546 */         unionCase.pack(outputStream);
/*     */       }
/* 548 */       for (NewAttributeBands.LayoutElement element : this.defaultCaseBody) {
/* 549 */         element.pack(outputStream);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 555 */       for (NewAttributeBands.UnionCase unionCase : this.unionCases) {
/* 556 */         unionCase.renumberBci(bciRenumbering, labelsToOffsets);
/*     */       }
/* 558 */       for (NewAttributeBands.LayoutElement element : this.defaultCaseBody) {
/* 559 */         element.renumberBci(bciRenumbering, labelsToOffsets);
/*     */       }
/*     */     }
/*     */     
/*     */     public NewAttributeBands.Integral getUnionTag() {
/* 564 */       return this.unionTag;
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.UnionCase> getUnionCases() {
/* 568 */       return this.unionCases;
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getDefaultCaseBody() {
/* 572 */       return this.defaultCaseBody;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Call
/*     */     extends LayoutElement {
/*     */     private final int callableIndex;
/*     */     private NewAttributeBands.Callable callable;
/*     */     
/*     */     public Call(int callableIndex) {
/* 582 */       this.callableIndex = callableIndex;
/*     */     }
/*     */     
/*     */     public void setCallable(NewAttributeBands.Callable callable) {
/* 586 */       this.callable = callable;
/* 587 */       if (this.callableIndex < 1) {
/* 588 */         callable.setBackwardsCallable();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
/* 594 */       this.callable.addAttributeToBand(attribute, inputStream);
/* 595 */       if (this.callableIndex < 1) {
/* 596 */         this.callable.addBackwardsCall();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void pack(OutputStream outputStream) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public int getCallableIndex() {
/* 611 */       return this.callableIndex;
/*     */     }
/*     */     
/*     */     public NewAttributeBands.Callable getCallable() {
/* 615 */       return this.callable;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class Reference
/*     */     extends LayoutElement
/*     */   {
/*     */     private final String tag;
/*     */     
/*     */     private List<ConstantPoolEntry> band;
/*     */     
/*     */     private boolean nullsAllowed = false;
/*     */ 
/*     */     
/*     */     public Reference(String tag) {
/* 631 */       this.tag = tag;
/* 632 */       this.nullsAllowed = (tag.indexOf('N') != -1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
/* 637 */       int index = NewAttributeBands.this.readInteger(4, inputStream);
/* 638 */       if (this.tag.startsWith("RC")) {
/* 639 */         this.band.add(NewAttributeBands.this.cpBands.getCPClass(attribute.readClass(index)));
/* 640 */       } else if (this.tag.startsWith("RU")) {
/* 641 */         this.band.add(NewAttributeBands.this.cpBands.getCPUtf8(attribute.readUTF8(index)));
/* 642 */       } else if (this.tag.startsWith("RS")) {
/* 643 */         this.band.add(NewAttributeBands.this.cpBands.getCPSignature(attribute.readUTF8(index)));
/*     */       } else {
/* 645 */         this.band.add(NewAttributeBands.this.cpBands.getConstant(attribute.readConst(index)));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String getTag() {
/* 651 */       return this.tag;
/*     */     }
/*     */ 
/*     */     
/*     */     public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
/*     */       int[] ints;
/* 657 */       if (this.nullsAllowed) {
/* 658 */         ints = NewAttributeBands.this.cpEntryOrNullListToArray(this.band);
/*     */       } else {
/* 660 */         ints = NewAttributeBands.this.cpEntryListToArray(this.band);
/*     */       } 
/* 662 */       byte[] encodedBand = NewAttributeBands.this.encodeBandInt(this.tag, ints, Codec.UNSIGNED5);
/* 663 */       outputStream.write(encodedBand);
/* 664 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + this.tag + "[" + ints.length + "]");
/*     */     }
/*     */ 
/*     */     
/*     */     public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public class Callable
/*     */     implements AttributeLayoutElement
/*     */   {
/*     */     private final List<NewAttributeBands.LayoutElement> body;
/*     */     
/*     */     private boolean isBackwardsCallable;
/*     */     
/*     */     private int backwardsCallableIndex;
/*     */ 
/*     */     
/*     */     public Callable(List<NewAttributeBands.LayoutElement> body) {
/* 683 */       this.body = body;
/*     */     }
/*     */     
/*     */     public void setBackwardsCallableIndex(int backwardsCallableIndex) {
/* 687 */       this.backwardsCallableIndex = backwardsCallableIndex;
/*     */     }
/*     */     
/*     */     public void addBackwardsCall() {
/* 691 */       NewAttributeBands.this.backwardsCallCounts[this.backwardsCallableIndex] = NewAttributeBands.this.backwardsCallCounts[this.backwardsCallableIndex] + 1;
/*     */     }
/*     */     
/*     */     public boolean isBackwardsCallable() {
/* 695 */       return this.isBackwardsCallable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBackwardsCallable() {
/* 702 */       this.isBackwardsCallable = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
/* 707 */       for (NewAttributeBands.AttributeLayoutElement element : this.body) {
/* 708 */         element.addAttributeToBand(attribute, inputStream);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
/* 714 */       for (NewAttributeBands.AttributeLayoutElement element : this.body) {
/* 715 */         element.pack(outputStream);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 721 */       for (NewAttributeBands.AttributeLayoutElement element : this.body) {
/* 722 */         element.renumberBci(bciRenumbering, labelsToOffsets);
/*     */       }
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getBody() {
/* 727 */       return this.body;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class UnionCase
/*     */     extends LayoutElement
/*     */   {
/*     */     private final List<NewAttributeBands.LayoutElement> body;
/*     */     
/*     */     private final List<Integer> tags;
/*     */ 
/*     */     
/*     */     public UnionCase(List<Integer> tags) {
/* 741 */       this.tags = tags;
/* 742 */       this.body = Collections.EMPTY_LIST;
/*     */     }
/*     */     
/*     */     public boolean hasTag(long l) {
/* 746 */       return this.tags.contains(Integer.valueOf((int)l));
/*     */     }
/*     */     
/*     */     public UnionCase(List<Integer> tags, List<NewAttributeBands.LayoutElement> body) {
/* 750 */       this.tags = tags;
/* 751 */       this.body = body;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
/* 756 */       for (NewAttributeBands.LayoutElement element : this.body) {
/* 757 */         element.addAttributeToBand(attribute, inputStream);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
/* 763 */       for (NewAttributeBands.LayoutElement element : this.body) {
/* 764 */         element.pack(outputStream);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 770 */       for (NewAttributeBands.LayoutElement element : this.body) {
/* 771 */         element.renumberBci(bciRenumbering, labelsToOffsets);
/*     */       }
/*     */     }
/*     */     
/*     */     public List<NewAttributeBands.LayoutElement> getBody() {
/* 776 */       return this.body;
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
/*     */   private StringReader getStreamUpToMatchingBracket(StringReader reader) throws IOException {
/* 789 */     StringBuilder sb = new StringBuilder();
/* 790 */     int foundBracket = -1;
/* 791 */     while (foundBracket != 0) {
/* 792 */       int read = reader.read();
/* 793 */       if (read == -1) {
/*     */         break;
/*     */       }
/* 796 */       char c = (char)read;
/* 797 */       if (c == ']') {
/* 798 */         foundBracket++;
/*     */       }
/* 800 */       if (c == '[') {
/* 801 */         foundBracket--;
/*     */       }
/* 803 */       if (foundBracket != 0) {
/* 804 */         sb.append(c);
/*     */       }
/*     */     } 
/* 807 */     return new StringReader(sb.toString());
/*     */   }
/*     */   
/*     */   private int readInteger(int i, InputStream inputStream) {
/* 811 */     int result = 0;
/* 812 */     for (int j = 0; j < i; j++) {
/*     */       try {
/* 814 */         result = result << 8 | inputStream.read();
/* 815 */       } catch (IOException e) {
/* 816 */         throw new UncheckedIOException("Error reading unknown attribute", e);
/*     */       } 
/*     */     } 
/*     */     
/* 820 */     if (i == 1) {
/* 821 */       result = (byte)result;
/*     */     }
/* 823 */     if (i == 2) {
/* 824 */       result = (short)result;
/*     */     }
/* 826 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BHSDCodec getCodec(String layoutElement) {
/* 835 */     if (layoutElement.indexOf('O') >= 0) {
/* 836 */       return Codec.BRANCH5;
/*     */     }
/* 838 */     if (layoutElement.indexOf('P') >= 0) {
/* 839 */       return Codec.BCI5;
/*     */     }
/* 841 */     if (layoutElement.indexOf('S') >= 0 && layoutElement.indexOf("KS") < 0 && layoutElement
/* 842 */       .indexOf("RS") < 0) {
/* 843 */       return Codec.SIGNED5;
/*     */     }
/* 845 */     if (layoutElement.indexOf('B') >= 0) {
/* 846 */       return Codec.BYTE1;
/*     */     }
/* 848 */     return Codec.UNSIGNED5;
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
/*     */   private String readUpToMatchingBracket(StringReader reader) throws IOException {
/* 860 */     StringBuilder sb = new StringBuilder();
/* 861 */     int foundBracket = -1;
/* 862 */     while (foundBracket != 0) {
/* 863 */       int read = reader.read();
/* 864 */       if (read == -1) {
/*     */         break;
/*     */       }
/* 867 */       char c = (char)read;
/* 868 */       if (c == ']') {
/* 869 */         foundBracket++;
/*     */       }
/* 871 */       if (c == '[') {
/* 872 */         foundBracket--;
/*     */       }
/* 874 */       if (foundBracket != 0) {
/* 875 */         sb.append(c);
/*     */       }
/*     */     } 
/* 878 */     return sb.toString();
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
/* 889 */     stream.mark(1);
/* 890 */     char first = (char)stream.read();
/* 891 */     boolean negative = (first == '-');
/* 892 */     if (!negative) {
/* 893 */       stream.reset();
/*     */     }
/* 895 */     stream.mark(100);
/*     */     
/* 897 */     int length = 0; int i;
/* 898 */     while ((i = stream.read()) != -1 && Character.isDigit((char)i)) {
/* 899 */       length++;
/*     */     }
/* 901 */     stream.reset();
/* 902 */     if (length == 0) {
/* 903 */       return null;
/*     */     }
/* 905 */     char[] digits = new char[length];
/* 906 */     int read = stream.read(digits);
/* 907 */     if (read != digits.length) {
/* 908 */       throw new IOException("Error reading from the input stream");
/*     */     }
/* 910 */     return Integer.valueOf(Integer.parseInt((negative ? "-" : "") + new String(digits)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<LayoutElement> readBody(StringReader reader) throws IOException {
/* 921 */     List<LayoutElement> layoutElements = new ArrayList<>();
/*     */     LayoutElement e;
/* 923 */     while ((e = readNextLayoutElement(reader)) != null) {
/* 924 */       layoutElements.add(e);
/*     */     }
/* 926 */     return layoutElements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 936 */     for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements)
/* 937 */       attributeLayoutElement.renumberBci(bciRenumbering, labelsToOffsets); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\NewAttributeBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */