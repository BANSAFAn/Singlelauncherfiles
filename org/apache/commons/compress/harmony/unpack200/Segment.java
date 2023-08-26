/*     */ package org.apache.commons.compress.harmony.unpack200;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.zip.CRC32;
/*     */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPField;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethod;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.ClassConstantPool;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFile;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.InnerClassesAttribute;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.SourceFileAttribute;
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
/*     */ public class Segment
/*     */ {
/*     */   public static final int LOG_LEVEL_VERBOSE = 2;
/*     */   public static final int LOG_LEVEL_STANDARD = 1;
/*     */   public static final int LOG_LEVEL_QUIET = 0;
/*     */   private SegmentHeader header;
/*     */   private CpBands cpBands;
/*     */   private AttrDefinitionBands attrDefinitionBands;
/*     */   private IcBands icBands;
/*     */   private ClassBands classBands;
/*     */   private BcBands bcBands;
/*     */   private FileBands fileBands;
/*     */   private boolean overrideDeflateHint;
/*     */   private boolean deflateHint;
/*     */   private boolean doPreRead;
/*     */   private int logLevel;
/*     */   private PrintWriter logStream;
/*     */   private byte[][] classFilesContents;
/*     */   private boolean[] fileDeflate;
/*     */   private boolean[] fileIsClass;
/*     */   private InputStream internalBuffer;
/*     */   
/*     */   private ClassFile buildClassFile(int classNum) {
/* 111 */     ClassFile classFile = new ClassFile();
/* 112 */     int[] major = this.classBands.getClassVersionMajor();
/* 113 */     int[] minor = this.classBands.getClassVersionMinor();
/* 114 */     if (major != null) {
/* 115 */       classFile.major = major[classNum];
/* 116 */       classFile.minor = minor[classNum];
/*     */     } else {
/* 118 */       classFile.major = this.header.getDefaultClassMajorVersion();
/* 119 */       classFile.minor = this.header.getDefaultClassMinorVersion();
/*     */     } 
/*     */     
/* 122 */     ClassConstantPool cp = classFile.pool;
/* 123 */     int fullNameIndexInCpClass = this.classBands.getClassThisInts()[classNum];
/* 124 */     String fullName = this.cpBands.getCpClass()[fullNameIndexInCpClass];
/*     */     
/* 126 */     int i = fullName.lastIndexOf("/") + 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     List<Attribute> classAttributes = this.classBands.getClassAttributes()[classNum];
/* 132 */     SourceFileAttribute sourceFileAttribute = null;
/* 133 */     for (Attribute classAttribute : classAttributes) {
/* 134 */       if (classAttribute.isSourceFileAttribute()) {
/* 135 */         sourceFileAttribute = (SourceFileAttribute)classAttribute;
/*     */       }
/*     */     } 
/*     */     
/* 139 */     if (sourceFileAttribute == null) {
/*     */ 
/*     */ 
/*     */       
/* 143 */       AttributeLayout SOURCE_FILE = this.attrDefinitionBands.getAttributeDefinitionMap().getAttributeLayout("SourceFile", 0);
/* 144 */       if (SOURCE_FILE.matches(this.classBands.getRawClassFlags()[classNum])) {
/* 145 */         int firstDollar = -1;
/* 146 */         for (int k = 0; k < fullName.length(); k++) {
/* 147 */           if (fullName.charAt(k) <= '$') {
/* 148 */             firstDollar = k;
/*     */           }
/*     */         } 
/* 151 */         String fileName = null;
/*     */         
/* 153 */         if (firstDollar > -1 && i <= firstDollar) {
/* 154 */           fileName = fullName.substring(i, firstDollar) + ".java";
/*     */         } else {
/* 156 */           fileName = fullName.substring(i) + ".java";
/*     */         } 
/* 158 */         sourceFileAttribute = new SourceFileAttribute(this.cpBands.cpUTF8Value(fileName, false));
/* 159 */         classFile.attributes = new Attribute[] { (Attribute)cp.add((ClassFileEntry)sourceFileAttribute) };
/*     */       } else {
/* 161 */         classFile.attributes = new Attribute[0];
/*     */       } 
/*     */     } else {
/* 164 */       classFile.attributes = new Attribute[] { (Attribute)cp.add((ClassFileEntry)sourceFileAttribute) };
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     List<Attribute> classAttributesWithoutSourceFileAttribute = new ArrayList<>(classAttributes.size());
/* 172 */     for (int index = 0; index < classAttributes.size(); index++) {
/* 173 */       Attribute attrib = classAttributes.get(index);
/* 174 */       if (!attrib.isSourceFileAttribute()) {
/* 175 */         classAttributesWithoutSourceFileAttribute.add(attrib);
/*     */       }
/*     */     } 
/* 178 */     Attribute[] originalAttributes = classFile.attributes;
/* 179 */     classFile
/* 180 */       .attributes = new Attribute[originalAttributes.length + classAttributesWithoutSourceFileAttribute.size()];
/* 181 */     System.arraycopy(originalAttributes, 0, classFile.attributes, 0, originalAttributes.length);
/* 182 */     for (int j = 0; j < classAttributesWithoutSourceFileAttribute.size(); j++) {
/* 183 */       Attribute attrib = classAttributesWithoutSourceFileAttribute.get(j);
/* 184 */       cp.add((ClassFileEntry)attrib);
/* 185 */       classFile.attributes[originalAttributes.length + j] = attrib;
/*     */     } 
/*     */ 
/*     */     
/* 189 */     ClassFileEntry cfThis = cp.add((ClassFileEntry)this.cpBands.cpClassValue(fullNameIndexInCpClass));
/* 190 */     ClassFileEntry cfSuper = cp.add((ClassFileEntry)this.cpBands.cpClassValue(this.classBands.getClassSuperInts()[classNum]));
/*     */     
/* 192 */     ClassFileEntry[] cfInterfaces = new ClassFileEntry[(this.classBands.getClassInterfacesInts()[classNum]).length];
/* 193 */     for (i = 0; i < cfInterfaces.length; i++) {
/* 194 */       cfInterfaces[i] = cp.add((ClassFileEntry)this.cpBands.cpClassValue(this.classBands.getClassInterfacesInts()[classNum][i]));
/*     */     }
/*     */     
/* 197 */     ClassFileEntry[] cfFields = new ClassFileEntry[this.classBands.getClassFieldCount()[classNum]];
/*     */     
/* 199 */     for (i = 0; i < cfFields.length; i++) {
/* 200 */       int descriptorIndex = this.classBands.getFieldDescrInts()[classNum][i];
/* 201 */       int nameIndex = this.cpBands.getCpDescriptorNameInts()[descriptorIndex];
/* 202 */       int typeIndex = this.cpBands.getCpDescriptorTypeInts()[descriptorIndex];
/* 203 */       CPUTF8 name = this.cpBands.cpUTF8Value(nameIndex);
/* 204 */       CPUTF8 descriptor = this.cpBands.cpSignatureValue(typeIndex);
/* 205 */       cfFields[i] = cp.add((ClassFileEntry)new CPField(name, descriptor, this.classBands.getFieldFlags()[classNum][i], this.classBands
/* 206 */             .getFieldAttributes()[classNum][i]));
/*     */     } 
/*     */     
/* 209 */     ClassFileEntry[] cfMethods = new ClassFileEntry[this.classBands.getClassMethodCount()[classNum]];
/*     */     
/* 211 */     for (i = 0; i < cfMethods.length; i++) {
/* 212 */       int descriptorIndex = this.classBands.getMethodDescrInts()[classNum][i];
/* 213 */       int nameIndex = this.cpBands.getCpDescriptorNameInts()[descriptorIndex];
/* 214 */       int typeIndex = this.cpBands.getCpDescriptorTypeInts()[descriptorIndex];
/* 215 */       CPUTF8 name = this.cpBands.cpUTF8Value(nameIndex);
/* 216 */       CPUTF8 descriptor = this.cpBands.cpSignatureValue(typeIndex);
/* 217 */       cfMethods[i] = cp.add((ClassFileEntry)new CPMethod(name, descriptor, this.classBands.getMethodFlags()[classNum][i], this.classBands
/* 218 */             .getMethodAttributes()[classNum][i]));
/*     */     } 
/* 220 */     cp.addNestedEntries();
/*     */ 
/*     */     
/* 223 */     boolean addInnerClassesAttr = false;
/* 224 */     IcTuple[] ic_local = getClassBands().getIcLocal()[classNum];
/* 225 */     boolean ic_local_sent = (ic_local != null);
/* 226 */     InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute("InnerClasses");
/* 227 */     IcTuple[] ic_relevant = getIcBands().getRelevantIcTuples(fullName, cp);
/* 228 */     List<IcTuple> ic_stored = computeIcStored(ic_local, ic_relevant);
/* 229 */     for (IcTuple icStored : ic_stored) {
/* 230 */       int innerClassIndex = icStored.thisClassIndex();
/* 231 */       int outerClassIndex = icStored.outerClassIndex();
/* 232 */       int simpleClassNameIndex = icStored.simpleClassNameIndex();
/*     */       
/* 234 */       String innerClassString = icStored.thisClassString();
/* 235 */       String outerClassString = icStored.outerClassString();
/* 236 */       String simpleClassName = icStored.simpleClassName();
/*     */       
/* 238 */       CPClass innerClass = null;
/* 239 */       CPUTF8 innerName = null;
/* 240 */       CPClass outerClass = null;
/*     */ 
/*     */       
/* 243 */       innerClass = (innerClassIndex != -1) ? this.cpBands.cpClassValue(innerClassIndex) : this.cpBands.cpClassValue(innerClassString);
/* 244 */       if (!icStored.isAnonymous())
/*     */       {
/* 246 */         innerName = (simpleClassNameIndex != -1) ? this.cpBands.cpUTF8Value(simpleClassNameIndex) : this.cpBands.cpUTF8Value(simpleClassName);
/*     */       }
/*     */       
/* 249 */       if (icStored.isMember())
/*     */       {
/* 251 */         outerClass = (outerClassIndex != -1) ? this.cpBands.cpClassValue(outerClassIndex) : this.cpBands.cpClassValue(outerClassString);
/*     */       }
/* 253 */       int flags = icStored.F;
/* 254 */       innerClassesAttribute.addInnerClassesEntry(innerClass, outerClass, innerName, flags);
/* 255 */       addInnerClassesAttr = true;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     if (ic_local_sent && ic_local.length == 0) {
/* 260 */       addInnerClassesAttr = false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 265 */     if (!ic_local_sent && ic_relevant.length == 0) {
/* 266 */       addInnerClassesAttr = false;
/*     */     }
/*     */     
/* 269 */     if (addInnerClassesAttr) {
/*     */ 
/*     */       
/* 272 */       Attribute[] originalAttrs = classFile.attributes;
/* 273 */       Attribute[] newAttrs = new Attribute[originalAttrs.length + 1];
/* 274 */       System.arraycopy(originalAttrs, 0, newAttrs, 0, originalAttrs.length);
/* 275 */       newAttrs[newAttrs.length - 1] = (Attribute)innerClassesAttribute;
/* 276 */       classFile.attributes = newAttrs;
/* 277 */       cp.addWithNestedEntries((ClassFileEntry)innerClassesAttribute);
/*     */     } 
/*     */     
/* 280 */     cp.resolve(this);
/*     */ 
/*     */     
/* 283 */     classFile.accessFlags = (int)this.classBands.getClassFlags()[classNum];
/* 284 */     classFile.thisClass = cp.indexOf(cfThis);
/* 285 */     classFile.superClass = cp.indexOf(cfSuper);
/*     */     
/* 287 */     classFile.interfaces = new int[cfInterfaces.length];
/* 288 */     for (i = 0; i < cfInterfaces.length; i++) {
/* 289 */       classFile.interfaces[i] = cp.indexOf(cfInterfaces[i]);
/*     */     }
/* 291 */     classFile.fields = cfFields;
/* 292 */     classFile.methods = cfMethods;
/* 293 */     return classFile;
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
/*     */   private List<IcTuple> computeIcStored(IcTuple[] ic_local, IcTuple[] ic_relevant) {
/* 305 */     List<IcTuple> result = new ArrayList<>(ic_relevant.length);
/* 306 */     List<IcTuple> duplicates = new ArrayList<>(ic_relevant.length);
/* 307 */     Set<IcTuple> isInResult = new HashSet<>(ic_relevant.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     if (ic_local != null) {
/* 314 */       for (IcTuple element : ic_local) {
/* 315 */         if (isInResult.add(element)) {
/* 316 */           result.add(element);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 322 */     for (IcTuple element : ic_relevant) {
/* 323 */       if (isInResult.add(element)) {
/* 324 */         result.add(element);
/*     */       } else {
/* 326 */         duplicates.add(element);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 331 */     duplicates.forEach(result::remove);
/*     */     
/* 333 */     return result;
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
/*     */   private void readSegment(InputStream in) throws IOException, Pack200Exception {
/* 345 */     log(2, "-------");
/* 346 */     this.cpBands = new CpBands(this);
/* 347 */     this.cpBands.read(in);
/* 348 */     this.attrDefinitionBands = new AttrDefinitionBands(this);
/* 349 */     this.attrDefinitionBands.read(in);
/* 350 */     this.icBands = new IcBands(this);
/* 351 */     this.icBands.read(in);
/* 352 */     this.classBands = new ClassBands(this);
/* 353 */     this.classBands.read(in);
/* 354 */     this.bcBands = new BcBands(this);
/* 355 */     this.bcBands.read(in);
/* 356 */     this.fileBands = new FileBands(this);
/* 357 */     this.fileBands.read(in);
/*     */     
/* 359 */     this.fileBands.processFileBits();
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
/*     */   private void parseSegment() throws IOException, Pack200Exception {
/* 371 */     this.header.unpack();
/* 372 */     this.cpBands.unpack();
/* 373 */     this.attrDefinitionBands.unpack();
/* 374 */     this.icBands.unpack();
/* 375 */     this.classBands.unpack();
/* 376 */     this.bcBands.unpack();
/* 377 */     this.fileBands.unpack();
/*     */     
/* 379 */     int classNum = 0;
/* 380 */     int numberOfFiles = this.header.getNumberOfFiles();
/* 381 */     String[] fileName = this.fileBands.getFileName();
/* 382 */     int[] fileOptions = this.fileBands.getFileOptions();
/* 383 */     SegmentOptions options = this.header.getOptions();
/*     */     
/* 385 */     this.classFilesContents = new byte[numberOfFiles][];
/* 386 */     this.fileDeflate = new boolean[numberOfFiles];
/* 387 */     this.fileIsClass = new boolean[numberOfFiles];
/*     */     
/* 389 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 390 */     DataOutputStream dos = new DataOutputStream(bos);
/*     */     
/* 392 */     for (int i = 0; i < numberOfFiles; i++) {
/* 393 */       String name = fileName[i];
/*     */       
/* 395 */       boolean nameIsEmpty = (name == null || name.equals(""));
/* 396 */       boolean isClass = ((fileOptions[i] & 0x2) == 2 || nameIsEmpty);
/* 397 */       if (isClass && nameIsEmpty) {
/* 398 */         name = this.cpBands.getCpClass()[this.classBands.getClassThisInts()[classNum]] + ".class";
/* 399 */         fileName[i] = name;
/*     */       } 
/*     */       
/* 402 */       if (!this.overrideDeflateHint) {
/* 403 */         this.fileDeflate[i] = ((fileOptions[i] & 0x1) == 1 || options.shouldDeflate());
/*     */       } else {
/* 405 */         this.fileDeflate[i] = this.deflateHint;
/*     */       } 
/*     */       
/* 408 */       this.fileIsClass[i] = isClass;
/*     */       
/* 410 */       if (isClass) {
/* 411 */         ClassFile classFile = buildClassFile(classNum);
/* 412 */         classFile.write(dos);
/* 413 */         dos.flush();
/*     */         
/* 415 */         this.classFilesContents[classNum] = bos.toByteArray();
/* 416 */         bos.reset();
/*     */         
/* 418 */         classNum++;
/*     */       } 
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
/*     */   public void unpack(InputStream in, JarOutputStream out) throws IOException, Pack200Exception {
/* 432 */     unpackRead(in);
/* 433 */     unpackProcess();
/* 434 */     unpackWrite(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void unpackRead(InputStream in) throws IOException, Pack200Exception {
/* 441 */     if (!in.markSupported()) {
/* 442 */       in = new BufferedInputStream(in);
/*     */     }
/*     */     
/* 445 */     this.header = new SegmentHeader(this);
/* 446 */     this.header.read(in);
/*     */     
/* 448 */     int size = (int)this.header.getArchiveSize() - this.header.getArchiveSizeOffset();
/*     */     
/* 450 */     if (this.doPreRead && this.header.getArchiveSize() != 0L) {
/* 451 */       byte[] data = new byte[size];
/* 452 */       in.read(data);
/* 453 */       this.internalBuffer = new BufferedInputStream(new ByteArrayInputStream(data));
/*     */     } else {
/* 455 */       readSegment(in);
/*     */     } 
/*     */   }
/*     */   
/*     */   void unpackProcess() throws IOException, Pack200Exception {
/* 460 */     if (this.internalBuffer != null) {
/* 461 */       readSegment(this.internalBuffer);
/*     */     }
/* 463 */     parseSegment();
/*     */   }
/*     */   
/*     */   void unpackWrite(JarOutputStream out) throws IOException {
/* 467 */     writeJar(out);
/* 468 */     if (this.logStream != null) {
/* 469 */       this.logStream.close();
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
/*     */   
/*     */   public void writeJar(JarOutputStream out) throws IOException {
/* 483 */     String[] fileName = this.fileBands.getFileName();
/* 484 */     int[] fileModtime = this.fileBands.getFileModtime();
/* 485 */     long[] fileSize = this.fileBands.getFileSize();
/* 486 */     byte[][] fileBits = this.fileBands.getFileBits();
/*     */ 
/*     */     
/* 489 */     int classNum = 0;
/* 490 */     int numberOfFiles = this.header.getNumberOfFiles();
/* 491 */     long archiveModtime = this.header.getArchiveModtime();
/*     */     
/* 493 */     for (int i = 0; i < numberOfFiles; i++) {
/* 494 */       String name = fileName[i];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 501 */       long modtime = 1000L * (archiveModtime + fileModtime[i]);
/* 502 */       boolean deflate = this.fileDeflate[i];
/*     */       
/* 504 */       JarEntry entry = new JarEntry(name);
/* 505 */       if (deflate) {
/* 506 */         entry.setMethod(8);
/*     */       } else {
/* 508 */         entry.setMethod(0);
/* 509 */         CRC32 crc = new CRC32();
/* 510 */         if (this.fileIsClass[i]) {
/* 511 */           crc.update(this.classFilesContents[classNum]);
/* 512 */           entry.setSize((this.classFilesContents[classNum]).length);
/*     */         } else {
/* 514 */           crc.update(fileBits[i]);
/* 515 */           entry.setSize(fileSize[i]);
/*     */         } 
/* 517 */         entry.setCrc(crc.getValue());
/*     */       } 
/*     */       
/* 520 */       entry.setTime(modtime - TimeZone.getDefault().getRawOffset());
/* 521 */       out.putNextEntry(entry);
/*     */ 
/*     */       
/* 524 */       if (this.fileIsClass[i]) {
/* 525 */         entry.setSize((this.classFilesContents[classNum]).length);
/* 526 */         out.write(this.classFilesContents[classNum]);
/* 527 */         classNum++;
/*     */       } else {
/* 529 */         entry.setSize(fileSize[i]);
/* 530 */         out.write(fileBits[i]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public SegmentConstantPool getConstantPool() {
/* 536 */     return this.cpBands.getConstantPool();
/*     */   }
/*     */   
/*     */   public SegmentHeader getSegmentHeader() {
/* 540 */     return this.header;
/*     */   }
/*     */   
/*     */   public void setPreRead(boolean value) {
/* 544 */     this.doPreRead = value;
/*     */   }
/*     */   
/*     */   protected AttrDefinitionBands getAttrDefinitionBands() {
/* 548 */     return this.attrDefinitionBands;
/*     */   }
/*     */   
/*     */   protected ClassBands getClassBands() {
/* 552 */     return this.classBands;
/*     */   }
/*     */   
/*     */   protected CpBands getCpBands() {
/* 556 */     return this.cpBands;
/*     */   }
/*     */   
/*     */   protected IcBands getIcBands() {
/* 560 */     return this.icBands;
/*     */   }
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 564 */     this.logLevel = logLevel;
/*     */   }
/*     */   
/*     */   public void setLogStream(OutputStream logStream) {
/* 568 */     this.logStream = new PrintWriter(new OutputStreamWriter(logStream, Charset.defaultCharset()), false);
/*     */   }
/*     */   
/*     */   public void log(int logLevel, String message) {
/* 572 */     if (this.logLevel >= logLevel) {
/* 573 */       this.logStream.println(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideDeflateHint(boolean deflateHint) {
/* 583 */     this.overrideDeflateHint = true;
/* 584 */     this.deflateHint = deflateHint;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\Segment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */