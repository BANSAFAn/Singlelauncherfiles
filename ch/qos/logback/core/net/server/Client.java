package ch.qos.logback.core.net.server;

import java.io.Closeable;

public interface Client extends Runnable, Closeable {
  void close();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\net\server\Client.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */