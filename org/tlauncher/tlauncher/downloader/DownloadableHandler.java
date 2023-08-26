package org.tlauncher.tlauncher.downloader;

public interface DownloadableHandler {
  void onStart(Downloadable paramDownloadable);
  
  void onAbort(Downloadable paramDownloadable);
  
  void onComplete(Downloadable paramDownloadable) throws RetryDownloadException;
  
  void onError(Downloadable paramDownloadable, Throwable paramThrowable);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\DownloadableHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */