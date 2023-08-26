package ch.qos.logback.core.status;

import java.util.List;

public interface StatusManager {
  void add(Status paramStatus);
  
  List<Status> getCopyOfStatusList();
  
  int getCount();
  
  boolean add(StatusListener paramStatusListener);
  
  void remove(StatusListener paramStatusListener);
  
  void clear();
  
  List<StatusListener> getCopyOfStatusListenerList();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\status\StatusManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */