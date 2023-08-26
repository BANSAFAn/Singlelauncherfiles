package org.slf4j;

import java.io.Serializable;
import java.util.Iterator;

public interface Marker extends Serializable {
  public static final String ANY_MARKER = "*";
  
  public static final String ANY_NON_NULL_MARKER = "+";
  
  String getName();
  
  void add(Marker paramMarker);
  
  boolean remove(Marker paramMarker);
  
  boolean hasChildren();
  
  boolean hasReferences();
  
  Iterator iterator();
  
  boolean contains(Marker paramMarker);
  
  boolean contains(String paramString);
  
  boolean equals(Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\Marker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */