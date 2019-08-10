package com.h2rd.refactoring.usermanagement;

import java.util.List;

public interface UserRepository {
  void saveUser(User user);
  List<User> getUserList();
  void deleteUser(User userToDelete);
  void updateUser(User userToUpdate);
  User findUserByEmail(String email);
  List<User> findUserByName(String name);
}
