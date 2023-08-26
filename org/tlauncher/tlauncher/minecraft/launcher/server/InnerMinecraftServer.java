package org.tlauncher.tlauncher.minecraft.launcher.server;

import java.io.IOException;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.tlauncher.entity.server.Server;

public interface InnerMinecraftServer {
  void initInnerServers();
  
  void prepareInnerServer() throws IOException;
  
  void searchRemovedServers() throws IOException;
  
  void addPageServer(Server paramServer) throws IOException;
  
  void addPageServerToModpack(Server paramServer, Version paramVersion) throws IOException;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\server\InnerMinecraftServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */