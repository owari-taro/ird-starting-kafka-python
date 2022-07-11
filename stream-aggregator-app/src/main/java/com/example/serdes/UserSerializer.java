package com.example.serdes;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.kafka.common.serialization.Serializer;

import com.example.event.User;
import com.google.gson.Gson;

public class UserSerializer implements Serializer<User> {
  private Gson gson = new Gson();

  @Override
  public byte[] serialize(String topic, User data) {
    if (Objects.isNull(data)) {
      return new byte[]{};
    }
    return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
  }
}
