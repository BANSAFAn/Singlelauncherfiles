package org.tlauncher.tlauncher.ui.editor;

import org.tlauncher.tlauncher.ui.block.Blockable;

public interface EditorField extends Blockable {
  String getSettingsValue();
  
  void setSettingsValue(String paramString);
  
  boolean isValueValid();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */