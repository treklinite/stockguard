package apap.tk.stockguard.test;

import apap.tk.stockguard.model.UserModel;
import apap.tk.stockguard.model.UserModel.Role;
import apap.tk.stockguard.repository.UserDb;
import apap.tk.stockguard.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDb userDb;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserModel user = new UserModel();
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userDb.save(user)).thenReturn(user);

        UserModel createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        verify(userDb, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        UserModel user = new UserModel();
        user.setId(1);

        when(userDb.findById(1)).thenReturn(Optional.of(user));

        UserModel foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
        verify(userDb, times(1)).findById(1);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userDb.findById(1)).thenReturn(Optional.empty());

        UserModel foundUser = userService.getUserById(1);

        assertNull(foundUser);
        verify(userDb, times(1)).findById(1);
    }

    @Test
    void testUpdateUser() {
        UserModel user = new UserModel();
        user.setId(1);
        user.setUsername("newUsername");
        user.setEmail("newEmail");
        user.setRole(Role.MANAJER_TOKO);

        UserModel existingUser = new UserModel();
        existingUser.setId(1);

        when(userDb.findById(1)).thenReturn(Optional.of(existingUser));
        when(userDb.save(existingUser)).thenReturn(existingUser);

        UserModel updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals("newEmail", updatedUser.getEmail());
        assertEquals(Role.MANAJER_TOKO, updatedUser.getRole());
        verify(userDb, times(1)).findById(1);
        verify(userDb, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        UserModel user = new UserModel();
        user.setId(1);

        doNothing().when(userDb).delete(user);

        UserModel deletedUser = userService.deleteUser(user);

        assertNotNull(deletedUser);
        assertEquals(1, deletedUser.getId());
        verify(userDb, times(1)).delete(user);
    }

    @Test
    void testGetAllUsers() {
        UserModel user1 = new UserModel();
        UserModel user2 = new UserModel();

        when(userDb.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserModel> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userDb, times(1)).findAll();
    }

    @Test
    void testIsUsernameExistExceptCurrent() {
        UserModel user = new UserModel();
        user.setId(1);
        user.setUsername("existingUsername");

        when(userDb.findByUsername("existingUsername")).thenReturn(user);

        boolean exists = userService.isUsernameExistExceptCurrent("existingUsername", 2);

        assertTrue(exists);
        verify(userDb, times(1)).findByUsername("existingUsername");
    }

    @Test
    void testIsUsernameExistExceptCurrentSameUser() {
        UserModel user = new UserModel();
        user.setId(1);
        user.setUsername("existingUsername");

        when(userDb.findByUsername("existingUsername")).thenReturn(user);

        boolean exists = userService.isUsernameExistExceptCurrent("existingUsername", 1);

        assertFalse(exists);
        verify(userDb, times(1)).findByUsername("existingUsername");
    }

    @Test
    void testIsUsernameExistExceptCurrentNotFound() {
        when(userDb.findByUsername("nonExistingUsername")).thenReturn(null);

        boolean exists = userService.isUsernameExistExceptCurrent("nonExistingUsername", 1);

        assertFalse(exists);
        verify(userDb, times(1)).findByUsername("nonExistingUsername");
    }
}
