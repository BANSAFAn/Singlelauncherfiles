package by.gdev.http.download.service;

import by.gdev.http.download.exeption.StatusExeption;
import by.gdev.http.upload.download.downloader.DownloaderContainer;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface Downloader {
  void addContainer(DownloaderContainer paramDownloaderContainer) throws IOException;
  
  void startDownload(boolean paramBoolean) throws InterruptedException, ExecutionException, StatusExeption, IOException;
  
  void cancelDownload();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\service\Downloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */