package test.com.h2rd.refactoring.integration;

import static org.junit.Assert.assertEquals;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.User.Role;
import com.h2rd.refactoring.web.UserResource;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.spi.container.servlet.WebComponent;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly2.web.GrizzlyWebTestContainerFactory;
import java.util.EnumSet;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Test;

public class UserIntegrationTest extends JerseyTest {

  public static class AppConfig extends DefaultResourceConfig {
    public AppConfig() {
      super(UserResource.class);
    }
  }

  @Override
  protected TestContainerFactory getTestContainerFactory() {
    return new GrizzlyWebTestContainerFactory();
  }

  @Override
  public WebAppDescriptor configure() {
    return new WebAppDescriptor.Builder()
        .initParam(WebComponent.RESOURCE_CONFIG_CLASS,
            AppConfig.class.getName())
        .build();
  }

  @Test
  public void integrationTest() {
    User user = new User("fake user", "fake@user.com", EnumSet.of(Role.ADMIN, Role.MASTER));
    WebResource resource = resource().path("users/add");
    User newUser = resource.type(MediaType.APPLICATION_XML_TYPE).post(User.class, user);
    assertEquals(user, newUser);

    resource = resource().path("users/find");
    List<User> listOfUser = resource.type(MediaType.APPLICATION_XML_TYPE).get(new GenericType<List<User>>() {});
    assertEquals(1, listOfUser.size());
    assertEquals(user, listOfUser.get(0));

    resource = resource().path("users/search").queryParam("name", "fake user");
    listOfUser = resource.type(MediaType.APPLICATION_XML_TYPE).get(new GenericType<List<User>>() {});
    assertEquals(1, listOfUser.size());
    assertEquals(user, listOfUser.get(0));

    resource = resource().path("users/delete");
    User deletedUser = resource.type(MediaType.APPLICATION_XML_TYPE).post(User.class, user);
    assertEquals(user, deletedUser);

    resource = resource().path("users/find");
    listOfUser = resource.type(MediaType.APPLICATION_XML_TYPE).get(new GenericType<List<User>>() {});
    assertEquals(0, listOfUser.size());
  }

}
