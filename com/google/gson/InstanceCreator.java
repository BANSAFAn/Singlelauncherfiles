package com.google.gson;

import java.lang.reflect.Type;

public interface InstanceCreator<T> {
  T createInstance(Type paramType);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\InstanceCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */