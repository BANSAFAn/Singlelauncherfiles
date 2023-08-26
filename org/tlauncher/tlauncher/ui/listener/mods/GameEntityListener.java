package org.tlauncher.tlauncher.ui.listener.mods;

import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

public interface GameEntityListener {
  void activationStarted(GameEntityDTO paramGameEntityDTO);
  
  void activation(GameEntityDTO paramGameEntityDTO);
  
  void activationError(GameEntityDTO paramGameEntityDTO, Throwable paramThrowable);
  
  void processingStarted(GameEntityDTO paramGameEntityDTO, VersionDTO paramVersionDTO);
  
  void installEntity(GameEntityDTO paramGameEntityDTO, GameType paramGameType);
  
  void installEntity(CompleteVersion paramCompleteVersion);
  
  void removeEntity(GameEntityDTO paramGameEntityDTO);
  
  void removeCompleteVersion(CompleteVersion paramCompleteVersion);
  
  void installError(GameEntityDTO paramGameEntityDTO, VersionDTO paramVersionDTO, Throwable paramThrowable);
  
  void populateStatus(GameEntityDTO paramGameEntityDTO, GameType paramGameType, boolean paramBoolean);
  
  void updateVersion(CompleteVersion paramCompleteVersion1, CompleteVersion paramCompleteVersion2);
  
  default void updateVersionStorageAndScene(CompleteVersion v, CompleteVersion newVersion) {}
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\GameEntityListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */