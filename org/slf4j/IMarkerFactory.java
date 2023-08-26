package org.slf4j;

public interface IMarkerFactory {
  Marker getMarker(String paramString);
  
  boolean exists(String paramString);
  
  boolean detachMarker(String paramString);
  
  Marker getDetachedMarker(String paramString);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\IMarkerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */