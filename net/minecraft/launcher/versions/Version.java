package net.minecraft.launcher.versions;

import java.util.Date;
import net.minecraft.launcher.updater.VersionList;
import org.tlauncher.tlauncher.repository.Repo;

public interface Version {
  boolean isActivateSkinCapeForUserVersion();
  
  String getID();
  
  void setID(String paramString);
  
  String getJar();
  
  ReleaseType getReleaseType();
  
  Repo getSource();
  
  void setSource(Repo paramRepo);
  
  Date getUpdatedTime();
  
  Date getReleaseTime();
  
  VersionList getVersionList();
  
  void setVersionList(VersionList paramVersionList);
  
  boolean isSkinVersion();
  
  void setSkinVersion(boolean paramBoolean);
  
  String getUrl();
  
  void setUrl(String paramString);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\Version.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */