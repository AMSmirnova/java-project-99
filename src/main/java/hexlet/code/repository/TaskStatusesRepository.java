package hexlet.code.repository;

import hexlet.code.model.TaskStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskStatusesRepository
        extends JpaRepository<TaskStatuses, Long>, JpaSpecificationExecutor<TaskStatuses> {

    Optional<TaskStatuses> findBySlug(String slug);

}
