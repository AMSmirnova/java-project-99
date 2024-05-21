package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private LabelRepository labelRepository;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);
    }

    @AfterEach
    public void cleanUp() {
        labelRepository.deleteById(testLabel.getId());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/labels/{id}", testLabel.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThat(body).contains(testLabel.getName());
    }

    @Test
    public void testShowLabelNotFound() throws Exception {
        labelRepository.deleteById(testLabel.getId());

        var request = get("/api/labels/{id}", testLabel.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/labels").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        var labels = labelRepository.findAll();
        for (var label : labels) {
            assertThat(body).contains(String.valueOf(label.getId()));
            assertThat(body).contains(label.getName());
        }
    }

    @Test
    public void testCreate() throws Exception {
        var data = Map.of("name", "newLabel");

        var request = post("/api/labels").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var label = labelRepository.findByName(data.get("name")).orElse(null);

        assertThat(label).isNotNull();
        assertThat(label.getName()).isEqualTo(data.get("name"));
    }

    @Test
    public void testCreateWithInvalidName() throws Exception {
        var data = Map.of("name", "ne");

        var request = post("/api/labels").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new LabelUpdateDTO();
        data.setName(JsonNullable.of("newName"));

        var request = put("/api/labels/{id}", testLabel.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedLabel = labelRepository.findByName("newName").get();

        assertThat(updatedLabel).isNotNull();
        assertThat(updatedLabel.getName()).isEqualTo("newName");
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/labels/{id}", testLabel.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var label = labelRepository.findById(testLabel.getId()).orElse(null);
        assertThat(label).isNull();
    }

    @Test
    public void testShowWithoutAuth() throws Exception {
        var request = get("/api/labels/{id}", testLabel.getId());
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        var request = get("/api/labels");
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateWithoutAuth() throws Exception {
        var data = Map.of("name", "newLabel");
        var request = post("/api/labels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateWithoutAuth() throws Exception {
        var data = new LabelUpdateDTO();
        data.setName(JsonNullable.of("newName"));

        var request = put("/api/labels/{id}", testLabel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDestroyWithoutAuth() throws Exception {
        var request = delete("/api/labels/{id}", testLabel.getId());
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }
}
