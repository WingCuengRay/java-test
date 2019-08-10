package com.h2rd.refactoring.usermanagement;

public class UserRepositoryFactory {
  private static UserRepository userRepository;

  public static UserRepository getInstance(){
    if(userRepository == null){
      userRepository = new SimpleUserDao();
    }

    return userRepository;
  }
}
