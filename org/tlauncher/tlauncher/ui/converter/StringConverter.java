package org.tlauncher.tlauncher.ui.converter;

public interface StringConverter<T> {
  T fromString(String paramString);
  
  String toString(T paramT);
  
  String toValue(T paramT);
  
  Class<T> getObjectClass();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\StringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */