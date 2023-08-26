package org.apache.commons.io.file;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@FunctionalInterface
public interface PathFilter {
  FileVisitResult accept(Path paramPath, BasicFileAttributes paramBasicFileAttributes);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\PathFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */