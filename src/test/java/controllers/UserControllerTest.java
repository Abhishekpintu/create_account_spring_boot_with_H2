package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.createaccount.CreateaccountApplication;
import com.sample.createaccount.model.Response;
import com.sample.createaccount.model.User;
import com.sample.createaccount.services.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CreateaccountApplication.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private UserManagementService userManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Response response;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setFullName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("Password123");
        user.setEncodedPassword("encodedPassword");
        user.setIsTermsAndConditionsAgreed(true);

        response = new Response();
        response.setData(user);
    }


    @Test
    void testGetAllUsers() throws Exception {
        when(userManagementService.getAllUsers()).thenReturn(response);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUser() throws Exception {
        when(userManagementService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userManagementService.deleteUser(1L)).thenReturn(response);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userManagementService.updateUser(any(Long.class), any(User.class))).thenReturn(response);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUserFields() throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put("fullName", "Jane Doe");

        when(userManagementService.updateUserFields(any(Long.class), any(Map.class))).thenReturn(response);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fields)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUser() throws Exception {
        when(userManagementService.registerUser(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }
}

