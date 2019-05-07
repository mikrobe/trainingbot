package com.jarics.trainbot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jarics.trainbot.services.AthletesRanking;
import com.jarics.trainbot.services.EventTypes;
import com.jarics.trainbot.services.MLClasses;
import lombok.EqualsAndHashCode;
import org.dizitart.no2.objects.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class AthleteFTP implements Serializable, UserDetails {
    @Id private long id;
    private double bikeFtp;
    private double runFtp;
    private double swimFtp;
    private EventTypes eventType = EventTypes.olympic;
    private AthletesRanking athletesRanking = AthletesRanking.beginner;
    private MLClasses classification = MLClasses.unknown;
    private String username;
    private String password;
    private String tokenType;
    private String accessToken;

    public double getBikeFtp() {
        return bikeFtp;
    }

    public void setBikeFtp(double bikeFtp) {
        this.bikeFtp = bikeFtp;
    }

    public double getRunFtp() {
        return runFtp;
    }

    public void setRunFtp(double runFtp) {
        this.runFtp = runFtp;
    }

    public double getSwimFtp() {
        return swimFtp;
    }

    public void setSwimFtp(double swimFtp) {
        this.swimFtp = swimFtp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MLClasses getClassification() {
        return classification;
    }

    public void setClassification(MLClasses classification) {
        this.classification = classification;
    }

    public AthletesRanking getAthletesRanking() {
        return athletesRanking;
    }

    public void setAthletesRanking(AthletesRanking athletesRanking) {
        this.athletesRanking = athletesRanking;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    public void setEventType(EventTypes eventType) {
        this.eventType = eventType;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}



