package org.tlauncher.tlauncher.downloader;

public abstract class DownloaderAdapterListener implements DownloaderListener {
  public void onDownloaderStart(Downloader d, int files) {}
  
  public void onDownloaderAbort(Downloader d) {}
  
  public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {}
  
  public void onDownloaderFileComplete(Downloader d, Downloadable file) {}
  
  public void onDownloaderComplete(Downloader d) {}
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\DownloaderAdapterListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */