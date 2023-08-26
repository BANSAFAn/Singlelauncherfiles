package org.tlauncher.tlauncher.ui.listener.mods;

import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

public class GameEntityPanel extends ExtendedPanel implements GameEntityListener {
  public void activationStarted(GameEntityDTO e) {}
  
  public void activation(GameEntityDTO e) {}
  
  public void activationError(GameEntityDTO e, Throwable t) {}
  
  public void processingStarted(GameEntityDTO e, VersionDTO version) {}
  
  public void installEntity(GameEntityDTO e, GameType type) {}
  
  public void installEntity(CompleteVersion e) {}
  
  public void removeEntity(GameEntityDTO e) {}
  
  public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {}
  
  public void populateStatus(GameEntityDTO status, GameType type, boolean state) {}
  
  public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {}
  
  public void removeCompleteVersion(CompleteVersion e) {}
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\GameEntityPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */