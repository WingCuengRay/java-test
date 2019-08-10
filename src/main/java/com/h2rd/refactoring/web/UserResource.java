package com.h2rd.refactoring.web;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserRepository;
import com.h2rd.refactoring.usermanagement.UserRepositoryFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Path("/users")
@Repository
public class UserResource{
    private UserRepository userDao;

    public UserResource(){
//        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
//            "classpath:/application-config.xml"
//        });
//        userDao = context.getBean(SimpleUserDao.class);
        userDao = UserRepositoryFactory.getInstance();
    }

    @Autowired
    public UserResource(UserRepository userRepository){
        this.userDao = userRepository;
    }

    @POST
    @Path("add/")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response addUser(User user) {
        try{
          userDao.saveUser(user);
        } catch (IllegalArgumentException ex){
          return Response.status(Status.BAD_REQUEST).build();
        }

      return Response.ok().entity(user).build();
    }

    @POST
    @Path("update/")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response updateUser(User user) {

        try{
          userDao.updateUser(user);
        } catch (IllegalArgumentException ex){
          return Response.status(Status.BAD_REQUEST).build();
        }

        return Response.ok().entity(user).build();
    }

    @POST
    @Path("delete/")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteUser(User user) {
        try{
          userDao.deleteUser(user);
        } catch (IllegalArgumentException ex){
          return Response.status(Status.BAD_REQUEST).build();
        }

        return Response.ok().entity(user).build();
    }

    @GET
    @Path("find/")
    @Produces(MediaType.APPLICATION_XML)
    public Response getUsers() {
    	List<User> users = userDao.getUserList();
      GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) {};
      return Response.status(200).entity(usersEntity).build();
    }

    @GET
    @Path("search/")
    @Produces(MediaType.APPLICATION_XML)
    public Response findUser(@QueryParam("name") String name) {
        List<User> users = userDao.findUserByName(name);
        GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) {};
        return Response.ok().entity(usersEntity).build();
    }
}
