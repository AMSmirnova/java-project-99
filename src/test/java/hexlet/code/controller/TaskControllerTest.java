package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusesRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    private Task testTask;
    private TaskStatus testTaskStatus;
    private User testUser;
    private Label testLabel;


    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testUser = userRepository.findByEmail("hexlet@example.com")
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        testTaskStatus = taskStatusesRepository.findBySlug("draft")
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
        testLabel = labelRepository.findByName("bug")
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        testTask.setLabels(Set.of(testLabel));

        taskRepository.save(testTask);
    }

    @AfterEach
    public void cleanUp() {
        taskRepository.deleteById(testTask.getId());
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/tasks").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        var tasks = taskRepository.findAll();
        for (var task : tasks) {
            assertThat(body).contains(String.valueOf(task.getId()));
            assertThat(body).contains(task.getName());
            assertThat(body).contains(task.getDescription());
        }
    }

    @Test
    public void testCreate() throws Exception {

        var data = new TaskCreateDTO();
        data.setTitle("title");
        data.setContent("content");
        data.setStatus(testTaskStatus.getSlug());
        data.setAssigneeId(testUser.getId());
        data.setIndex(123344);
        data.setTaskLabelIds(List.of(testLabel.getId()));

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(data.getTitle()).get();

        assertNotNull(task);
        assertThat(task.getIndex()).isEqualTo(data.getIndex());
        assertThat(task.getName()).isEqualTo(data.getTitle());
        assertThat(task.getDescription()).isEqualTo(data.getContent());
        assertThat(task.getTaskStatus().getName()).isEqualTo("Draft");
        assertThat(task.getAssignee().getId()).isEqualTo(data.getAssigneeId());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new TaskUpdateDTO();
        data.setTitle(JsonNullable.of("new name"));

        var request = put("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).get();
        assertThat(task.getName()).isEqualTo(data.getTitle().get());
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/tasks/{id}", testTask.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var task = taskRepository.findById(testTask.getId()).orElse(null);
        assertThat(task).isNull();
    }


    @Test
    public void testIndexFilterWithTitleCont() throws Exception {
        var titleCont = testTask.getName();

        var request = get("/api/tasks?titleCont=" + titleCont).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains(titleCont);
    }

    @Test
    public void testIndexFilterWithAssigneeId() throws Exception {
        var assigneeId = testTask.getAssignee().getId();

        var request = get("/api/tasks?assigneeId=" + assigneeId).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains(String.valueOf(assigneeId));
    }

    @Test
    public void testIndexFilterWithStatus() throws Exception {
        var status = testTask.getTaskStatus().getSlug();

        var request = get("/api/tasks?status=" + status).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains(status);
    }

    @Test
    public void testIndexFilterWithLabelId() throws Exception {
        var label = testTask.getLabels().iterator().next();
        var labelId = label.getId();

        var request = get("/api/tasks?labelId=" + labelId).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains(String.valueOf(labelId));
    }
}
