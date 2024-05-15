package hexlet.code.dto.label;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;


@Setter
@Getter
public class LabelUpdateDto {

    private JsonNullable<String> name;
}
