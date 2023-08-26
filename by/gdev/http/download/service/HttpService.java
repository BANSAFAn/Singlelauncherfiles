package by.gdev.http.download.service;

import by.gdev.http.download.model.RequestMetadata;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface HttpService {
  RequestMetadata getRequestByUrlAndSave(String paramString, Path paramPath) throws IOException;
  
  RequestMetadata getMetaByUrl(String paramString) throws IOException;
  
  String getRequestByUrl(String paramString) throws IOException;
  
  String getRequestByUrl(String paramString, Map<String, String> paramMap) throws IOException;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\service\HttpService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */