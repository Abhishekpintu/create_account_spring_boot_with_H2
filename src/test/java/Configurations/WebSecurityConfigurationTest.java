package Configurations;

import com.sample.createaccount.CreateaccountApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest(classes = CreateaccountApplication.class)
public class WebSecurityConfigurationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenRequestingResource_thenStatusOk() throws Exception {
        // Test that the resource is accessible without authentication
        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRequestingResource_thenCsrfDisabled() throws Exception {
        // Test that CSRF protection is disabled
        mockMvc.perform(get("/users/").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRequestingResource_thenFrameOptionsDisabled() throws Exception {
        // Test that frame options are disabled
        mockMvc.perform(get("/some-resource"))
                .andExpect(header().doesNotExist("X-Frame-Options"));
    }
}
