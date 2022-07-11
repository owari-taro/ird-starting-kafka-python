package com.example.serdes;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.kafka.common.serialization.Deserializer;

import com.example.event.User;
import com.google.gson.Gson;

public class UserDeserializer implements Deserializer<User> {

  private Gson gson = new Gson();

  @Override
  public User deserialize(String topic, byte[] data) {
    if (Objects.isNull(data)) {
      return null;
    }
    return gson.fromJson(new String(data, StandardCharsets.UTF_8), User.class);
  }
  
}
