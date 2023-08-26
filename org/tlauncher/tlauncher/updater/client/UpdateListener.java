package org.tlauncher.tlauncher.updater.client;

public interface UpdateListener {
  void onUpdateError(Update paramUpdate, Throwable paramThrowable);
  
  void onUpdateDownloading(Update paramUpdate);
  
  void onUpdateDownloadError(Update paramUpdate, Throwable paramThrowable);
  
  void onUpdateReady(Update paramUpdate);
  
  void onUpdateApplying(Update paramUpdate);
  
  void onUpdateApplyError(Update paramUpdate, Throwable paramThrowable);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\UpdateListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */