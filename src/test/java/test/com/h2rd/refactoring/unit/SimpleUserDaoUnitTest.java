package test.com.h2rd.refactoring.unit;

import static org.junit.Assert.assertEquals;

import com.h2rd.refactoring.usermanagement.SimpleUserDao;
import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.User.Role;
import java.util.EnumSet;
import java.util.List;
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
        assertEquals(1, userDao.getAllUsers().size());
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
        assertEquals(1, userDao.getAllUsers().size());
        userDao.deleteUser(user);
        assertEquals(0, userDao.getAllUsers().size());
    }

    @Test
    public void updateExistUserTest() {
        User user = new User();
        user.setName("Fake Name");
        user.setEmail("fake@email.com");
        user.setRoles(EnumSet.of(Role.ADMIN, Role.MASTER));

        userDao.saveUser(user);
        assertEquals(1, userDao.getAllUsers().size());

        user.setName("Real Name");
        userDao.updateUser(user);

        List<User> userList = userDao.getAllUsers();
        assertEquals(1, userList.size());
        assertEquals(user, userList.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void  updateNonExistUserTest(){
        User user = new User();
        user.setName("Fake Name");
        user.setEmail("fake@email.com");
        user.setRoles(EnumSet.of(Role.ADMIN, Role.MASTER));
        userDao.updateUser(user);
    }
}