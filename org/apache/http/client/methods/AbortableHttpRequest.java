package org.apache.http.client.methods;

import java.io.IOException;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;

@Deprecated
public interface AbortableHttpRequest {
  void setConnectionRequest(ClientConnectionRequest paramClientConnectionRequest) throws IOException;
  
  void setReleaseTrigger(ConnectionReleaseTrigger paramConnectionReleaseTrigger) throws IOException;
  
  void abort();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\methods\AbortableHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */