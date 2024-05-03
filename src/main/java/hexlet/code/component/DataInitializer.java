package hexlet.code.component;

import hexlet.code.model.TaskStatuses;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusesRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
//@Profile("development")
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private final TaskStatusesRepository taskStatusesRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);

        taskStatusesRepository.save(generateStatus("Draft", "draft"));
        taskStatusesRepository.save(generateStatus("To review", "to_review"));
        taskStatusesRepository.save(generateStatus("To be fixed", "to_be_fixed"));
        taskStatusesRepository.save(generateStatus("To publish", "to_publish"));
        taskStatusesRepository.save(generateStatus("Published", "published"));

    }

    public TaskStatuses generateStatus(String name, String slug) {
        var taskStatus = new TaskStatuses();
        taskStatus.setName(name);
        taskStatus.setSlug(slug);
        return taskStatus;
    }
}
