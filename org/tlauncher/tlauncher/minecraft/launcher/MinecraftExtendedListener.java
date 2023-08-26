package org.tlauncher.tlauncher.minecraft.launcher;

public interface MinecraftExtendedListener extends MinecraftListener {
  void onMinecraftCollecting();
  
  void onMinecraftComparingAssets();
  
  void onMinecraftDownloading();
  
  void onMinecraftReconstructingAssets();
  
  void onMinecraftUnpackingNatives();
  
  void onMinecraftDeletingEntries();
  
  void onMinecraftConstructing();
  
  void onMinecraftLaunch();
  
  void onMinecraftPostLaunch();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\MinecraftExtendedListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */