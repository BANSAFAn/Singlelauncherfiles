package org.tlauncher.tlauncher.minecraft.user;

import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;

public interface Parser<V extends org.tlauncher.tlauncher.minecraft.user.preq.Validatable> {
  V parseResponse(Logger paramLogger, String paramString) throws ParseException;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */