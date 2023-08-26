package org.tlauncher.tlauncher.ui.modpack.filter;

import org.tlauncher.modpack.domain.client.GameEntityDTO;

public interface GameEntityFilter extends Filter<GameEntityDTO> {
  boolean isProper(GameEntityDTO paramGameEntityDTO);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\GameEntityFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */