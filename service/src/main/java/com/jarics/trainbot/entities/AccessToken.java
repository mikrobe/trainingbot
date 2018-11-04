
package com.jarics.trainbot.entities;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "token_type",
    "access_token",
    "athlete"
})
public class AccessToken {

  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("athlete")
  @Valid
  private Athlete athlete;
  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("token_type")
  public String getTokenType() {
    return tokenType;
  }

  @JsonProperty("token_type")
  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  @JsonProperty("access_token")
  public String getAccessToken() {
    return accessToken;
  }

  @JsonProperty("access_token")
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @JsonProperty("athlete")
  public Athlete getAthlete() {
    return athlete;
  }

  @JsonProperty("athlete")
  public void setAthlete(Athlete athlete) {
    this.athlete = athlete;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}

