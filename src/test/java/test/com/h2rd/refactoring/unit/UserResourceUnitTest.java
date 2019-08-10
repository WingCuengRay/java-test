package test.com.h2rd.refactoring.unit;

import static org.junit.Assert.assertEquals;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.User.Role;
import com.h2rd.refactoring.usermanagement.UserRepository;
import com.h2rd.refactoring.web.UserResource;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserResourceUnitTest {
    private UserResource userResource;
    private UserRepository userDao;

    @Before
    public void init(){
        userDao = Mockito.mock(UserRepository.class);
    }

    @Test
    public void getUsersTest() {
        userResource = new UserResource(userDao);

        User user = new User();
        user.setName("fake user");
        user.setEmail("fake@user.com");
        user.setRoles(EnumSet.of(Role.ADMIN, Role.MASTER));
        Mockito.when(userDao.getUserList()).thenReturn(Arrays.asList(user));

        Response response = userResource.getUsers();
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void saveUserTest(){
        userResource = new UserResource(userDao);

        Mockito.doThrow(new IllegalArgumentException()).when(userDao).saveUser(Mockito.any(User.class));
        Response response = userResource.addUser(new User("fake user", "fake@user.com", new HashSet<>()));
        assertEquals(400 ,response.getStatus());

        Mockito.doNothing().when(userDao).saveUser(Mockito.any(User.class));
        response = userResource.addUser(new User("fake user", "fake@user.com", EnumSet.of(Role.ADMIN, Role.MASTER)));
        assertEquals(200 ,response.getStatus());
    }

    @Test
    public void deleteUserTest(){
        userResource = new UserResource(userDao);

        Mockito.doThrow(new IllegalArgumentException()).when(userDao).deleteUser(Mockito.any(User.class));
        Response response = userResource.deleteUser(new User("fake user", "", EnumSet.of(Role.ADMIN, Role.MASTER)));
        assertEquals(400 ,response.getStatus());

        Mockito.doNothing().when(userDao).deleteUser(Mockito.any(User.class));
        response = userResource.deleteUser(new User("fake user", "fake@user.com", EnumSet.of(Role.ADMIN, Role.MASTER)));
        assertEquals(200 ,response.getStatus());
    }
}
