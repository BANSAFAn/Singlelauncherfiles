package org.tlauncher.tlauncher.updater.client;

public abstract class UpdaterAdapter implements UpdaterListener {
  public void onUpdaterRequesting(Updater u) {}
  
  public void onUpdaterErrored(Updater.SearchFailed failed) {}
  
  public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {}
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\UpdaterAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */