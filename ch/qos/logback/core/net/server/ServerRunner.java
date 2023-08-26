package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAware;
import java.io.IOException;

public interface ServerRunner<T extends Client> extends ContextAware, Runnable {
  boolean isRunning();
  
  void stop() throws IOException;
  
  void accept(ClientVisitor<T> paramClientVisitor);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\net\server\ServerRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */