package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private final TaskStatusRepository taskStatusesRepository;

    @Autowired
    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addAdminUser();

        generateStatus("Draft", "draft");
        generateStatus("To review", "to_review");
        generateStatus("To be fixed", "to_be_fixed");
        generateStatus("To publish", "to_publish");
        generateStatus("Published", "published");

        addDefaultLabels();
    }

    public void addAdminUser() {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);
    }

    public void addDefaultLabels() {
        var label1 = new Label();
        label1.setName("feature");
        labelRepository.save(label1);

        var label2 = new Label();
        label2.setName("bug");
        labelRepository.save(label2);
    }

    public void generateStatus(String name, String slug) {
        var taskStatus = new TaskStatus();
        taskStatus.setName(name);
        taskStatus.setSlug(slug);
        taskStatusesRepository.save(taskStatus);
    }
}
