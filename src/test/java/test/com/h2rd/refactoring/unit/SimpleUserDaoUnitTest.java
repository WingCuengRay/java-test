package test.com.h2rd.refactoring.unit;

import static org.junit.Assert.assertEquals;

import com.h2rd.refactoring.usermanagement.SimpleUserDao;
import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.User.Role;
import java.util.Arrays;
import java.util.EnumSet;
import org.junit.Before;
import org.junit.Test;

public class SimpleUserDaoUnitTest {

    private SimpleUserDao userDao;

    @Before
    public void init(){
        userDao = new SimpleUserDao();
    }

    @Test
    public void saveUserTest() {
        User user = new User();
        user.setName("Fake Name");
        user.setEmail("fake@email.com");
        user.setRoles(EnumSet.of(Role.ADMIN, Role.MASTER));
        userDao.saveUser(user);

        User found = userDao.findUserByEmail(user.getEmail());
        assertEquals(1, userDao.getUserList().size());
        assertEquals(user, found);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveUserWithoutRoleTest(){
        User user = new User();
        user.setName("Fake Name");
        user.setEmail("fake@email.com");
        userDao.saveUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveUserWithoutEmailTest(){
        User user = new User();
        user.setName("Fake Name");
        user.setRoles(EnumSet.of(Role.ADMIN, Role.MASTER));
        userDao.saveUser(user);
    }

    @Test
    public void deleteUserTest() {
        User user = new User();
        user.setName("Fake Name");
        user.setEmail("fake@email.com");
        user.setRoles(EnumSet.of(Role.ADMIN, Role.MASTER));

        userDao.saveUser(user);
        assertEquals(1, userDao.getUserList().size());
        userDao.deleteUser(user);
        assertEquals(0, userDao.getUserList().size());
    }
}