package org.tlauncher.tlauncher.ui.versions;

import java.util.List;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.managers.VersionManager;

public interface VersionHandlerListener {
  void onVersionRefreshing(VersionManager paramVersionManager);
  
  void onVersionRefreshed(VersionManager paramVersionManager);
  
  void onVersionSelected(List<VersionSyncInfo> paramList);
  
  void onVersionDeselected();
  
  void onVersionDownload(List<VersionSyncInfo> paramList);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionHandlerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */