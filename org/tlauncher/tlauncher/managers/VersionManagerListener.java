package org.tlauncher.tlauncher.managers;

public interface VersionManagerListener {
  void onVersionsRefreshing(VersionManager paramVersionManager);
  
  void onVersionsRefreshingFailed(VersionManager paramVersionManager);
  
  void onVersionsRefreshed(VersionManager paramVersionManager);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\VersionManagerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */