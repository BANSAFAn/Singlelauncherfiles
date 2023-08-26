package net.minecraft.launcher.process;

public interface JavaProcessListener {
  void onJavaProcessLog(JavaProcess paramJavaProcess, String paramString);
  
  void onJavaProcessEnded(JavaProcess paramJavaProcess);
  
  void onJavaProcessError(JavaProcess paramJavaProcess, Throwable paramThrowable);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\process\JavaProcessListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */