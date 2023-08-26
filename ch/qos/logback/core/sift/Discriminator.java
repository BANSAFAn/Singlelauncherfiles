package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.LifeCycle;

public interface Discriminator<E> extends LifeCycle {
  String getDiscriminatingValue(E paramE);
  
  String getKey();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\sift\Discriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */