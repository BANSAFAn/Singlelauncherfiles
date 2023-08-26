/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import javax.swing.ComboBoxModel;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.ListCellRenderer;
/*    */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*    */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*    */ import org.tlauncher.tlauncher.ui.swing.DefaultConverterCellRenderer;
/*    */ import org.tlauncher.tlauncher.ui.swing.SimpleComboBoxModel;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.TlauncherCustomBox;
/*    */ import org.tlauncher.util.Reflect;
/*    */ 
/*    */ public class ExtendedComboBox<T> extends TlauncherCustomBox<T> {
/*    */   private static final long serialVersionUID = -4509947341182373649L;
/*    */   private StringConverter<T> converter;
/*    */   
/*    */   public ExtendedComboBox(ListCellRenderer<T> renderer) {
/* 18 */     setModel((ComboBoxModel)new SimpleComboBoxModel());
/* 19 */     setRenderer(renderer);
/* 20 */     setOpaque(false);
/* 21 */     setFont(getFont().deriveFont(TLauncherFrame.fontSize));
/*    */     
/* 23 */     ((JComponent)Reflect.cast(getEditor().getEditorComponent(), JComponent.class)).setOpaque(false);
/*    */   }
/*    */   
/*    */   public ExtendedComboBox(StringConverter<T> converter) {
/* 27 */     this((ListCellRenderer<T>)new DefaultConverterCellRenderer(converter));
/* 28 */     this.converter = converter;
/*    */   }
/*    */   
/*    */   public ExtendedComboBox() {
/* 32 */     this((ListCellRenderer<T>)null);
/*    */   }
/*    */   
/*    */   public SimpleComboBoxModel<T> getSimpleModel() {
/* 36 */     return (SimpleComboBoxModel<T>)getModel();
/*    */   }
/*    */   
/*    */   public T getValueAt(int i) {
/* 40 */     Object value = getItemAt(i);
/* 41 */     return returnAs(value);
/*    */   }
/*    */   
/*    */   public T getSelectedValue() {
/* 45 */     Object selected = getSelectedItem();
/* 46 */     return returnAs(selected);
/*    */   }
/*    */   
/*    */   public void setSelectedValue(T value) {
/* 50 */     setSelectedItem(value);
/*    */   }
/*    */   
/*    */   public void setSelectedValue(String string) {
/* 54 */     T value = convert(string);
/* 55 */     if (value == null) {
/*    */       return;
/*    */     }
/* 58 */     setSelectedValue(value);
/*    */   }
/*    */   
/*    */   public StringConverter<T> getConverter() {
/* 62 */     return this.converter;
/*    */   }
/*    */   
/*    */   public void setConverter(StringConverter<T> converter) {
/* 66 */     this.converter = converter;
/*    */   }
/*    */   
/*    */   protected String convert(T obj) {
/* 70 */     T from = returnAs(obj);
/*    */     
/* 72 */     if (this.converter != null)
/* 73 */       return this.converter.toValue(from); 
/* 74 */     return (from == null) ? null : from.toString();
/*    */   }
/*    */   
/*    */   protected T convert(String from) {
/* 78 */     if (this.converter == null)
/* 79 */       return null; 
/* 80 */     return (T)this.converter.fromString(from);
/*    */   }
/*    */ 
/*    */   
/*    */   private T returnAs(Object obj) {
/*    */     try {
/* 86 */       return (T)obj;
/* 87 */     } catch (ClassCastException ce) {
/* 88 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\ExtendedComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */