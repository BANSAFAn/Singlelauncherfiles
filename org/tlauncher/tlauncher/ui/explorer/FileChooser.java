package org.tlauncher.tlauncher.ui.explorer;

import java.awt.Component;
import java.io.File;
import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;

public interface FileChooser {
  void setFileFilter(CommonFilter paramCommonFilter);
  
  int showSaveDialog(Component paramComponent);
  
  File getSelectedFile();
  
  void setSelectedFile(File paramFile);
  
  void setCurrentDirectory(File paramFile);
  
  int showDialog(Component paramComponent);
  
  File[] getSelectedFiles();
  
  void setMultiSelectionEnabled(boolean paramBoolean);
  
  int showDialog(Component paramComponent, String paramString);
  
  void setDialogTitle(String paramString);
  
  void setFileSelectionMode(int paramInt);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\FileChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */