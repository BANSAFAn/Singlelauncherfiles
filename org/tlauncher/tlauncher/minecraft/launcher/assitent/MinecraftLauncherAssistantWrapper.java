package org.tlauncher.tlauncher.minecraft.launcher.assitent;

import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;

public abstract class MinecraftLauncherAssistantWrapper implements MinecraftLauncherAssistantInterface {
  public void collectInfo(MinecraftLauncher launcher) throws MinecraftException {}
  
  public void collectResources(MinecraftLauncher launcher) throws MinecraftException {}
  
  public void constructJavaArguments(MinecraftLauncher launcher) throws MinecraftException {}
  
  public void constructProgramArguments(MinecraftLauncher launcher) throws MinecraftException {}
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\assitent\MinecraftLauncherAssistantWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */