/*    */ package org.tlauncher.tlauncher.ui.swing.icon;
/*    */ 
/*    */ public class ImageIconPicturePosition extends ImageIcon {
/*    */   private static final long serialVersionUID = 4790815662595114089L;
/*    */   private int position;
/*    */   
/*  7 */   public void setPosition(int position) { this.position = position; } public String toString() { return "ImageIconPicturePosition(position=" + getPosition() + ")"; }
/*  8 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ImageIconPicturePosition)) return false;  ImageIconPicturePosition other = (ImageIconPicturePosition)o; return !other.canEqual(this) ? false : (!super.equals(o) ? false : (!(getPosition() != other.getPosition()))); } protected boolean canEqual(Object other) { return other instanceof ImageIconPicturePosition; } public int hashCode() { int PRIME = 59; result = super.hashCode(); return result * 59 + getPosition(); }
/*    */ 
/*    */   
/*    */   public int getPosition() {
/* 12 */     return this.position;
/*    */   } public ImageIconPicturePosition(byte[] imageData, int position) {
/* 14 */     super(imageData);
/* 15 */     this.position = position;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\icon\ImageIconPicturePosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */