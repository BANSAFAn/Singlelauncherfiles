package org.apache.http;

public interface HeaderElement {
  String getName();
  
  String getValue();
  
  NameValuePair[] getParameters();
  
  NameValuePair getParameterByName(String paramString);
  
  int getParameterCount();
  
  NameValuePair getParameter(int paramInt);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\HeaderElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */