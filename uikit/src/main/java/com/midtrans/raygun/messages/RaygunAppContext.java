package com.midtrans.raygun.messages;

public class RaygunAppContext {
  private String identifier;

  public RaygunAppContext(String uuid) {
    identifier = uuid;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
