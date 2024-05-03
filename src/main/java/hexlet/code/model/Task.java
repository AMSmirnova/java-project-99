package hexlet.code.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "tasks")
@Getter
@Setter
@Entity
public class Task implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @ManyToOne(cascade = CascadeType.DETACH)
    private TaskStatuses taskStatus;

    private int index;
    private String description;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User assignee;

    @CreatedDate
    private LocalDate createdAt;
}
