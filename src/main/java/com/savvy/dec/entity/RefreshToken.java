package com.savvy.dec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Entity(name = "refreshtoken")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

//  public RefreshToken() {
//  }
//
//  public long getId() {
//    return id;
//  }
//
//  public void setId(long id) {
//    this.id = id;
//  }
//
//  public User getUser() {
//    return user;
//  }
//
//  public void setUser(User user) {
//    this.user = user;
//  }
//
//  public String getToken() {
//    return token;
//  }
//
//  public void setToken(String token) {
//    this.token = token;
//  }
//
//  public Instant getExpiryDate() {
//    return expiryDate;
//  }
//
//  public void setExpiryDate(Instant expiryDate) {
//    this.expiryDate = expiryDate;
//  }

}
