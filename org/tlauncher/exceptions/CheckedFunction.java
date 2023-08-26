package org.tlauncher.exceptions;

@FunctionalInterface
public interface CheckedFunction<T, R> {
  R apply(T paramT) throws Exception;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\exceptions\CheckedFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */