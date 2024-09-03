package Services.Impln;

import com.sample.createaccount.common.Constants;
import com.sample.createaccount.configurations.CustomErrorException;
import com.sample.createaccount.model.Response;
import com.sample.createaccount.model.User;
import com.sample.createaccount.repo.UsersDAO;
import com.sample.createaccount.services.impln.UserManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserManagementServiceImplTest {

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    @Mock
    private UsersDAO usersDAO;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setFullName("Jane Doe");
        testUser.setEmailId("jane.doe@example.com");
        testUser.setPassword("password123");
        testUser.setEncodedPassword("encodedPassword");
        testUser.setIsTermsAndConditionsAgreed(true);
    }

    @Test
    void testRegisterUser_Success() {
        when(usersDAO.findByEmailId(testUser.getEmailId())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(usersDAO.saveAndFlush(any(User.class))).thenReturn(testUser);

        Response response = userManagementService.registerUser(testUser);

        assertNotNull(response);
        assertEquals(Constants.REG_SUCCESS, response.getBusinessMessage());
        verify(usersDAO, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterUserWithNullPassword() {
        User user = new User();
        user.setPassword(null);

        // Call the method and expect an exception
        CustomErrorException thrownException = assertThrows(
                CustomErrorException.class,
                () -> userManagementService.registerUser(user),
                "Expected registerUser() to throw an exception"
        );

        // Verify the exception message
        assertEquals(Constants.EMPTY_PASSSWORD, thrownException.getMessage());
    }

    @Test
    void testRegisterUserWithEmptyPassword() {
        User user = new User();
        user.setPassword("");

        // Call the method and expect an exception
        CustomErrorException thrownException = assertThrows(
                CustomErrorException.class,
                () -> userManagementService.registerUser(user),
                "Expected registerUser() to throw an exception"
        );

        // Verify the exception message
        assertEquals(Constants.EMPTY_PASSSWORD, thrownException.getMessage());
    }

    @Test
    void testRegisterUser_EmailExists() {
        when(usersDAO.findByEmailId(testUser.getEmailId())).thenReturn(testUser);

        CustomErrorException thrown = assertThrows(CustomErrorException.class, () -> {
            userManagementService.registerUser(testUser);
        });

        assertEquals(Constants.EMAIL_EXISTS, thrown.getMessage());
    }

    @Test
    void testRegisterUser_EmptyPassword() {
        testUser.setPassword("");
        CustomErrorException thrown = assertThrows(CustomErrorException.class, () -> {
            userManagementService.registerUser(testUser);
        });

        assertEquals(Constants.EMPTY_PASSSWORD, thrown.getMessage());
    }

    @Test
    void testGetAllUsers() {
        when(usersDAO.findAll()).thenReturn(List.of(testUser));

        Response response = userManagementService.getAllUsers();

        assertNotNull(response);
        assertEquals(Constants.FETCHED_SUCCESS, response.getBusinessMessage());
        assertTrue(response.getData() instanceof List);
        assertFalse(((List<?>) response.getData()).isEmpty());
    }

    @Test
    void testGetUser_Success() {
        when(usersDAO.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        Response response = userManagementService.getUser(testUser.getId());

        assertNotNull(response);
        assertEquals(Constants.FETCHED_SUCCESS, response.getBusinessMessage());
        assertEquals(testUser, response.getData());
    }

    @Test
    void testGetUser_UserNotFound() {
        when(usersDAO.findById(testUser.getId())).thenReturn(Optional.empty());

        CustomErrorException thrown = assertThrows(CustomErrorException.class, () -> {
            userManagementService.getUser(testUser.getId());
        });

        assertEquals(Constants.MISSING_USER, thrown.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(usersDAO.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        Response response = userManagementService.deleteUser(testUser.getId());

        assertNotNull(response);
        assertEquals(Constants.DELETE_SUCCESS, response.getBusinessMessage());
        verify(usersDAO, times(1)).delete(testUser);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(usersDAO.findById(testUser.getId())).thenReturn(Optional.empty());

        CustomErrorException thrown = assertThrows(CustomErrorException.class, () -> {
            userManagementService.deleteUser(testUser.getId());
        });

        assertEquals(Constants.MISSING_USER, thrown.getMessage());
    }

    @Test
    void testUpdateUser_Success() {
        User updatedUser = new User();
        updatedUser.setFullName("Jane Smith");
        updatedUser.setEmailId("jane.smith@example.com");
        updatedUser.setIsTermsAndConditionsAgreed(false);

        when(usersDAO.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(usersDAO.saveAndFlush(any(User.class))).thenReturn(updatedUser);

        Response response = userManagementService.updateUser(testUser.getId(), updatedUser);

        assertNotNull(response);
        assertEquals(Constants.UPDATED_SUCCESS, response.getBusinessMessage());
    }

    @Test
    void testUpdateUserFields_Success() throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> updates = Map.of("fullName", "Jane Smith", "emailId", "jane.smith@example.com");

        when(usersDAO.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(usersDAO.saveAndFlush(any(User.class))).thenReturn(testUser);

        Response response = userManagementService.updateUserFields(testUser.getId(), updates);

        assertNotNull(response);
        assertEquals(Constants.UPDATED_SUCCESS, response.getBusinessMessage());
        assertEquals("Jane Smith", testUser.getFullName());
        assertEquals("jane.smith@example.com", testUser.getEmailId());
    }
}
