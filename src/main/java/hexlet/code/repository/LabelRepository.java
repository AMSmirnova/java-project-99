package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LabelRepository
        extends JpaRepository<Label, Long>, JpaSpecificationExecutor<Label> {

    Optional<Label> findByName(String name);
    Optional<Set<Label>> findByIdIn(List<Long> ids);
}
