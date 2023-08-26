package org.tlauncher.tlauncher.updater.bootstrapper;

import java.io.IOException;
import java.util.List;
import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedBootInfo;

public interface PreparedEnvironmentComponent {
  List<String> getLibrariesForRunning() throws IOException;
  
  DownloadedBootInfo validateLibraryAndJava();
  
  void preparedLibrariesAndJava(DownloadedBootInfo paramDownloadedBootInfo);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\PreparedEnvironmentComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */