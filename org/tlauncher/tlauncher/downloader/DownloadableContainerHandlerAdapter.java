package org.tlauncher.tlauncher.downloader;

public abstract class DownloadableContainerHandlerAdapter implements DownloadableContainerHandler {
  public void onAbort(DownloadableContainer c) {}
  
  public void onError(DownloadableContainer c, Downloadable d, Throwable e) {}
  
  public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {}
  
  public void onFullComplete(DownloadableContainer c) {}
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\DownloadableContainerHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */