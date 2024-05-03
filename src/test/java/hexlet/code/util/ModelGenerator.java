package hexlet.code.util;

import hexlet.code.model.Task;
import hexlet.code.model.TaskStatuses;
import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {
    private Model<User> userModel;

    private Model<TaskStatuses> taskStatusesModel;

    private Model<Task> taskModel;

    @Autowired
    private Faker faker;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> passwordEncoder
                        .encode(faker.internet().password()))
                .toModel();

        taskStatusesModel = Instancio.of(TaskStatuses.class)
                .ignore(Select.field(TaskStatuses::getId))
                .ignore(Select.field(TaskStatuses::getCreatedAt))
                .supply(Select.field(TaskStatuses::getName), () -> faker.lorem().word())
                .supply(Select.field(TaskStatuses::getSlug), () -> faker.lorem().word())
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getCreatedAt))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getTaskStatus))
                .supply(Select.field(Task::getName), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .supply(Select.field(Task::getIndex), () -> faker.hashCode())
                .toModel();

    }
}
