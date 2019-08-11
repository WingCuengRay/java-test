package com.h2rd.refactoring.web;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserRepository;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Path("/users")
@Service
public class UserResource {
  private UserRepository userDao;

  @Autowired
  public UserResource(UserRepository userRepository) {
    this.userDao = userRepository;
  }

  /**
   * Add a user
   *
   * @param user the user that is to be added
   * @return 200 with the user that was added if succeed, 400 if user is invalid or exists
   */
  @POST
  @Path("add/")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Response addUser(User user) {
    if (user == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    try {
      userDao.saveUser(user);
    } catch (IllegalArgumentException ex) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(user).build();
  }

  /**
   * Update a user according to his/her email
   *
   * @param user the user that needs to be updated
   * @return 200 with the list of user that was updated, 400 otherwise
   */
  @POST
  @Path("update/")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Response updateUser(User user) {
    if (user == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    try {
      userDao.updateUser(user);
    } catch (IllegalArgumentException ex) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(user).build();
  }

  /**
   * Delete a user
   *
   * @param user the user that need to be deleted
   * @return 200 and the user that was deleted if succeed, 400 otherwise
   */
  @POST
  @Path("delete/")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Response deleteUser(User user) {
    if (user == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    try {
      userDao.deleteUser(user);
    } catch (IllegalArgumentException ex) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(user).build();
  }

  /**
   * Get all users
   *
   * @return 200 with a list of user if succeed
   */
  @GET
  @Path("find/")
  @Produces(MediaType.APPLICATION_XML)
  public Response getAllUsers() {
    List<User> users = userDao.getAllUsers();
    GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) {
    };
    return Response.status(200).entity(usersEntity).build();
  }

  /**
   * Search users by name
   *
   * @param name the name of the user
   * @return 200 with the list of user with the same name if succeed, 400 otherwise
   */
  @GET
  @Path("search/")
  @Produces(MediaType.APPLICATION_XML)
  public Response searchUser(@QueryParam("name") String name) {
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    List<User> users = userDao.findUserByName(name);
    GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) {
    };
    return Response.ok().entity(usersEntity).build();
  }
}
