package ch.qos.logback.core.spi;

import java.util.Map;

public interface PropertyContainer {
  String getProperty(String paramString);
  
  Map<String, String> getCopyOfPropertyMap();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\spi\PropertyContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */