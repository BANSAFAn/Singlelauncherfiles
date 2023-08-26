package org.tlauncher.tlauncher.configuration;

import java.io.IOException;
import org.tlauncher.tlauncher.entity.ConfigEnum;

public interface AbstractConfiguration {
  String get(String paramString);
  
  int getInteger(String paramString);
  
  double getDouble(String paramString);
  
  float getFloat(String paramString);
  
  long getLong(String paramString);
  
  boolean getBoolean(String paramString);
  
  boolean getBoolean(ConfigEnum paramConfigEnum);
  
  String getDefault(String paramString);
  
  int getDefaultInteger(String paramString);
  
  double getDefaultDouble(String paramString);
  
  float getDefaultFloat(String paramString);
  
  long getDefaultLong(String paramString);
  
  boolean getDefaultBoolean(String paramString);
  
  void set(String paramString, Object paramObject);
  
  void set(ConfigEnum paramConfigEnum, Object paramObject);
  
  void clear();
  
  void save() throws IOException;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\AbstractConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */