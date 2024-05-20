package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskStatusRepository statusRepository;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private TaskStatus testStatus;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        statusRepository.save(testStatus);
    }

    @AfterEach
    public void cleanUp() {
        statusRepository.deleteById(testStatus.getId());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/task_statuses/{id}", testStatus.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThat(body).contains(testStatus.getName());
        assertThat(body).contains(testStatus.getSlug());
    }

    @Test
    public void testShowStatusNotFound() throws Exception {
        statusRepository.deleteById(testStatus.getId());

        var request = get("/api/task_statuses/{id}", testStatus.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/task_statuses").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        var statuses = statusRepository.findAll();
        for (var status : statuses) {
            assertThat(body).contains(String.valueOf(status.getId()));
            assertThat(body).contains(status.getName());
        }
    }

    @Test
    public void testCreate() throws Exception {
        var data = Map.of(
                "name", "newStatus",
                "slug", "newSlug"
        );

        var request = post("/api/task_statuses").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var status = statusRepository.findBySlug(data.get("slug")).orElse(null);

        assertThat(status).isNotNull();
        assertThat(status.getName()).isEqualTo(data.get("name"));
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new TaskStatusUpdateDto();
        data.setSlug(JsonNullable.of("new_slug"));

        var request = put("/api/task_statuses/{id}", testStatus.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedStatus = statusRepository.findBySlug("new_slug").get();

        assertThat(updatedStatus).isNotNull();
        assertThat(updatedStatus.getName()).isEqualTo(testStatus.getName());
        assertThat(updatedStatus.getSlug()).isEqualTo("new_slug");
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/task_statuses/{id}", testStatus.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var status = statusRepository.findById(testStatus.getId()).orElse(null);
        assertThat(status).isNull();
    }

    @Test
    public void testShowWithoutAuth() throws Exception {
        var request = get("/api/task_statuses/{id}", testStatus.getId());
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        var request = get("/api/task_statuses");
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateWithoutAuth() throws Exception {
        var data = Map.of(
                "name", "newStatus",
                "slug", "newSlug"
        );
        var request = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateWithoutAuth() throws Exception {
        var data = new TaskStatusUpdateDto();
        data.setSlug(JsonNullable.of("new_slug"));

        var request = put("/api/task_statuses/{id}", testStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDestroyWithoutAuth() throws Exception {
        var request = delete("/api/task_statuses/{id}", testStatus.getId());
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }
}

