package by.gdev.util.os;

import by.gdev.util.model.GPUDriverVersion;
import by.gdev.util.model.GPUsDescriptionDTO;
import java.io.IOException;

public interface OSExecutor {
  String execute(String paramString, int paramInt) throws IOException, InterruptedException;
  
  GPUsDescriptionDTO getGPUInfo() throws IOException, InterruptedException;
  
  GPUDriverVersion getGPUDriverVersion() throws IOException, InterruptedException;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\os\OSExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */