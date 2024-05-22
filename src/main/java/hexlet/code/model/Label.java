package hexlet.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "labels")
@Setter
@Getter
public class Label implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;


    @Column(unique = true)
    @Size(min = 3, max = 1000)
    @NotBlank
    private String name;

    @CreationTimestamp
    private LocalDate createdAt;

}
