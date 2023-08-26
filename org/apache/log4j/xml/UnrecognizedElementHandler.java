package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public interface UnrecognizedElementHandler {
  boolean parseUnrecognizedElement(Element paramElement, Properties paramProperties) throws Exception;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\log4j\xml\UnrecognizedElementHandler.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */