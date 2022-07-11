package com.example.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.event.User;

public class UserSerdes implements Serde<User> {

  @Override
  public Serializer<User> serializer() {
    return new UserSerializer();
  }

  @Override
  public Deserializer<User> deserializer() {
    return new UserDeserializer();
  }
  
}
