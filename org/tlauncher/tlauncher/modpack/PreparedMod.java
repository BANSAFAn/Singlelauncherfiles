package org.tlauncher.tlauncher.modpack;

import java.nio.file.Path;
import java.util.List;
import net.minecraft.launcher.versions.CompleteVersion;

public interface PreparedMod {
  List<Path> prepare(CompleteVersion paramCompleteVersion);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\modpack\PreparedMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */