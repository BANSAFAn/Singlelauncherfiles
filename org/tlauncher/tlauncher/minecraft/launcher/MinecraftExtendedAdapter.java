package org.tlauncher.tlauncher.minecraft.launcher;

import org.tlauncher.tlauncher.minecraft.crash.Crash;

public class MinecraftExtendedAdapter implements MinecraftExtendedListener {
  public void onMinecraftCollecting() {}
  
  public void onMinecraftComparingAssets() {}
  
  public void onMinecraftDownloading() {}
  
  public void onMinecraftReconstructingAssets() {}
  
  public void onMinecraftUnpackingNatives() {}
  
  public void onMinecraftDeletingEntries() {}
  
  public void onMinecraftConstructing() {}
  
  public void onMinecraftPrepare() {}
  
  public void onMinecraftAbort() {}
  
  public void onMinecraftLaunch() {}
  
  public void onMinecraftClose() {}
  
  public void onMinecraftError(Throwable e) {}
  
  public void onMinecraftKnownError(MinecraftException e) {}
  
  public void onMinecraftCrash(Crash crash) {}
  
  public void onMinecraftPostLaunch() {}
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\MinecraftExtendedAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */