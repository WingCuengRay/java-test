package test.com.h2rd.refactoring.integration;

import static org.junit.Assert.assertEquals;
import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.User.Role;
import java.util.EnumSet;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;

public class UserIntegrationTest extends JerseyTest {
  @Override
  protected TestContainerFactory getTestContainerFactory() {
    return new GrizzlyWebTestContainerFactory();
  }

  @Override
  protected DeploymentContext configureDeployment(){
    return ServletDeploymentContext
        .forPackages("com.h2rd.refactoring")
        .addListener(ContextLoaderListener.class)
        .contextParam("contextConfigLocation", "classpath:application-config.xml")
        .build();
  }

  @Test
  public void integrationTest() {
    User user = new User("fake user", "fake@user.com", EnumSet.of(Role.ADMIN, Role.MASTER));
    Response response = target("users/add").request().post(Entity.entity(user, MediaType.APPLICATION_XML_TYPE));
    assertEquals(200, response.getStatus());
    User responseUser = response.readEntity(User.class);
    assertEquals(user, responseUser);

    response = target("users/find").request().get();
    assertEquals(200, response.getStatus());
    List<User> responseUserList = response.readEntity(new GenericType<List<User>>(){});
    assertEquals(1, responseUserList.size());
    assertEquals(user, responseUserList.get(0));

    user.setName("Real user");
    response = target("users/update").request().post(Entity.entity(user, MediaType.APPLICATION_XML_TYPE));
    assertEquals(200, response.getStatus());
    responseUser = response.readEntity(User.class);
    assertEquals(user, responseUser);

    response = target("users/search").queryParam("name", "Real user").request().get();
    assertEquals(200, response.getStatus());
    responseUserList = response.readEntity(new GenericType<List<User>>(){});
    assertEquals(1, responseUserList.size());
    assertEquals(user, responseUserList.get(0));

    response = target("users/delete").request().post(Entity.entity(user, MediaType.APPLICATION_XML_TYPE));
    assertEquals(200, response.getStatus());
    responseUser = response.readEntity(User.class);
    assertEquals(user, responseUser);

    response = target("users/find").request().get();
    assertEquals(200, response.getStatus());
    responseUserList = response.readEntity(new GenericType<List<User>>(){});
    assertEquals(0, responseUserList.size());
  }
}
