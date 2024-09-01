package repo;

import com.sample.createaccount.CreateaccountApplication;
import com.sample.createaccount.model.User;
import com.sample.createaccount.repo.UsersDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CreateaccountApplication.class)
public class UsersDAOTest {

    @Autowired
    private UsersDAO usersDAO;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Prepare test user
        testUser = new User();
        testUser.setFullName("Jane Doe");
        testUser.setEmailId("jane.doe@example.com");
        testUser.setEncodedPassword("encodedPassword");
        testUser.setIsTermsAndConditionsAgreed(true);

        // Save user to the repository
        usersDAO.save(testUser);
    }

    @Test
    void testFindByEmailId() {
        // When
        User foundUser = usersDAO.findByEmailId(testUser.getEmailId());

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmailId()).isEqualTo(testUser.getEmailId());
        assertThat(foundUser.getFullName()).isEqualTo(testUser.getFullName());
    }

    // Add additional test methods as needed
}
