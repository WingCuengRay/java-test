package com.h2rd.refactoring.usermanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SimpleUserDao implements UserRepository {
    private static final int MINIMUM_NUM_OF_ROLE = 1;

    private final List<User> userList;

    public SimpleUserDao(){
        userList = Collections.synchronizedList(new ArrayList<>());
    }

    public void saveUser(User user) {
        if(!isValidUser(user) || isUserExist(user)) {
            throw new IllegalArgumentException("Invalid user information to save");
        }

        userList.add(user);
    }

    public List<User> getAllUsers() {
        List<User> copy = new ArrayList<>(userList.size());
        synchronized (userList){
            copy.addAll(userList);
        }

        return copy;
    }

    public void deleteUser(User userToDelete) {
        if(userToDelete == null)
            return;

        synchronized (userList) {
            for (User user : userList) {
                if (user.getEmail().equals(userToDelete.getEmail())) {
                    userList.remove(user);
                    break;
                }
            }
        }
    }

    public void updateUser(User userToUpdate) {
        if(!isValidUser(userToUpdate) || !isUserExist(userToUpdate)){
            throw new IllegalArgumentException("Invalid user information to update");
        }

        synchronized (userList){
            for(User each : userList){
                if(each.getEmail().equals(userToUpdate.getEmail())){
                    each.setName(userToUpdate.getName());
                    each.setRoles(userToUpdate.getRoles());
                    break;
                }
            }
        }
    }

    public User findUserByEmail(String email) {
        if(StringUtils.isBlank(email)){
            return null;
        }

        User target = null;
        synchronized (userList) {
            for(User each: userList) {
                if(each.getEmail().equals(email)) {
                    target = new User(each.getName(), each.getEmail(), each.getRoles());
                    break;
                }
            }
        }

        return target;
    }

    public List<User> findUserByName(final String name){
        List<User> found = new ArrayList<>();
        synchronized (userList){
            for(User each : userList){
                if(each.getName().equals(name))
                    found.add(each);
            }
        }

        return found;
    }

    private boolean isUserExist(User user){
        if(user.getEmail() == null)
            return false;

        synchronized (userList){
            for(User each : userList){
                if(each.getEmail().equals(user.getEmail()))
                    return true;
            }
        }

        return false;
    }

    private boolean isValidUser(User user){
        if(user == null || StringUtils.isBlank(user.getEmail()) ||  user.getRoles() == null || user.getRoles().size() < MINIMUM_NUM_OF_ROLE)
            return false;
        else
            return true;
    }
}
