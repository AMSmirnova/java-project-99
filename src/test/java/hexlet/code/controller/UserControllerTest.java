package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private JwtRequestPostProcessor token;
    private User testUser;

    @BeforeEach
    public  void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(testUser);
    }

    @AfterEach
    public void cleanUp() {
        var tasks = testUser.getTasks();

        if (tasks.isEmpty()) {
            userRepository.deleteById(testUser.getId());
        }
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/users").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        var users = userRepository.findAll();
        for (var user : users) {
            assertThat(body).contains(String.valueOf(user.getId()));
            assertThat(body).contains(user.getEmail());
        }
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getUserModel())
                .create();

        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(data.getEmail()).get();

        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(data.getFirstName());
        assertThat(user.getLastName()).isEqualTo(data.getLastName());
    }

    @Test
    public void testCreateWithInvalidEmail() throws Exception {
        var data = Map.of(
                "email", "hexlet",
                "password", "qwerty"
        );

        var request = post("/api/users").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithInvalidPassword() throws Exception {
        var data = Map.of(
                "email", "hexlet@gmail",
                "password", "q"
        );

        var request = post("/api/users").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdate() throws Exception {
        var data = new UserUpdateDTO();
        data.setFirstName(JsonNullable.of("new name"));

        var request = put("/api/users/" + testUser.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(data.getFirstName().get());
    }

    @Test
    public void testDestroy() throws Exception {
        var testToken = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        var request = delete("/api/users/{id}", testUser.getId()).with(testToken);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var user = userRepository.findById(testUser.getId()).orElse(null);
        assertThat(user).isNull();
    }

    @Test
    public void testDestroyNotCurrentUser() throws Exception {

        var request = delete("/api/users/{id}", testUser.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testShowWithoutAuth() throws Exception {
        var request = get("/api/users/{id}", testUser.getId());
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        var request = get("/api/users");
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateWithoutAuth() throws Exception {
        var data = Instancio.of(modelGenerator.getUserModel())
                .create();

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateWithoutAuth() throws Exception {
        var data = new UserUpdateDTO();
        data.setEmail(JsonNullable.of("hexlet@gmail.com"));

        var request = put("/api/users/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDestroyWithoutAuth() throws Exception {
        var request = delete("/api/labels/{id}", testUser.getId());
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }
}
