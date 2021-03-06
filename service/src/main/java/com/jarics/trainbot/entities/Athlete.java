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
    "id",
    "username",
    "resource_state",
    "firstname",
    "lastname",
    "city",
    "state",
    "country",
    "sex",
    "premium",
    "summit",
    "created_at",
    "updated_at",
    "badge_type_id",
    "profile_medium",
    "profile",
    "friend",
    "follower",
    "email"
})
public class Athlete {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("username")
  private String username;
  @JsonProperty("resource_state")
  private Integer resourceState;
  @JsonProperty("firstname")
  private String firstname;
  @JsonProperty("lastname")
  private String lastname;
  @JsonProperty("city")
  private String city;
  @JsonProperty("state")
  private String state;
  @JsonProperty("country")
  private String country;
  @JsonProperty("sex")
  private String sex;
  @JsonProperty("premium")
  private Boolean premium;
  @JsonProperty("summit")
  private Boolean summit;
  @JsonProperty("created_at")
  private String createdAt;
  @JsonProperty("updated_at")
  private String updatedAt;
  @JsonProperty("badge_type_id")
  private Integer badgeTypeId;
  @JsonProperty("profile_medium")
  private String profileMedium;
  @JsonProperty("profile")
  private String profile;
  @JsonProperty("friend")
  private Object friend;
  @JsonProperty("follower")
  private Object follower;
  @JsonProperty("email")
  private String email;
  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  @JsonProperty("username")
  public void setUsername(String username) {
    this.username = username;
  }

  @JsonProperty("resource_state")
  public Integer getResourceState() {
    return resourceState;
  }

  @JsonProperty("resource_state")
  public void setResourceState(Integer resourceState) {
    this.resourceState = resourceState;
  }

  @JsonProperty("firstname")
  public String getFirstname() {
    return firstname;
  }

  @JsonProperty("firstname")
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  @JsonProperty("lastname")
  public String getLastname() {
    return lastname;
  }

  @JsonProperty("lastname")
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(String city) {
    this.city = city;
  }

  @JsonProperty("state")
  public String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(String state) {
    this.state = state;
  }

  @JsonProperty("country")
  public String getCountry() {
    return country;
  }

  @JsonProperty("country")
  public void setCountry(String country) {
    this.country = country;
  }

  @JsonProperty("sex")
  public String getSex() {
    return sex;
  }

  @JsonProperty("sex")
  public void setSex(String sex) {
    this.sex = sex;
  }

  @JsonProperty("premium")
  public Boolean getPremium() {
    return premium;
  }

  @JsonProperty("premium")
  public void setPremium(Boolean premium) {
    this.premium = premium;
  }

  @JsonProperty("summit")
  public Boolean getSummit() {
    return summit;
  }

  @JsonProperty("summit")
  public void setSummit(Boolean summit) {
    this.summit = summit;
  }

  @JsonProperty("created_at")
  public String getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("created_at")
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  @JsonProperty("updated_at")
  public String getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updated_at")
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  @JsonProperty("badge_type_id")
  public Integer getBadgeTypeId() {
    return badgeTypeId;
  }

  @JsonProperty("badge_type_id")
  public void setBadgeTypeId(Integer badgeTypeId) {
    this.badgeTypeId = badgeTypeId;
  }

  @JsonProperty("profile_medium")
  public String getProfileMedium() {
    return profileMedium;
  }

  @JsonProperty("profile_medium")
  public void setProfileMedium(String profileMedium) {
    this.profileMedium = profileMedium;
  }

  @JsonProperty("profile")
  public String getProfile() {
    return profile;
  }

  @JsonProperty("profile")
  public void setProfile(String profile) {
    this.profile = profile;
  }

  @JsonProperty("friend")
  public Object getFriend() {
    return friend;
  }

  @JsonProperty("friend")
  public void setFriend(Object friend) {
    this.friend = friend;
  }

  @JsonProperty("follower")
  public Object getFollower() {
    return follower;
  }

  @JsonProperty("follower")
  public void setFollower(Object follower) {
    this.follower = follower;
  }

  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
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
