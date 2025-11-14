package org.example.dto.chatDto;

import lombok.*;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<List<Long>> users;
    private List<Long> messages;
}
