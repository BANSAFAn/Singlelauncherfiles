package com.google.gson;

import com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
  <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\TypeAdapterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */