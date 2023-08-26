package org.tlauncher.tlauncher.ui.listener;

public interface ManagerListerner<T> {
  void startedDownloading();
  
  void downloadedData(T paramT);
  
  void preparedGame();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\ManagerListerner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */