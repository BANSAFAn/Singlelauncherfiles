package org.apache.log4j.spi;

import java.io.InputStream;
import java.net.URL;

public interface Configurator {
  public static final String INHERITED = "inherited";
  
  public static final String NULL = "null";
  
  void doConfigure(InputStream paramInputStream, LoggerRepository paramLoggerRepository);
  
  void doConfigure(URL paramURL, LoggerRepository paramLoggerRepository);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\log4j\spi\Configurator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */