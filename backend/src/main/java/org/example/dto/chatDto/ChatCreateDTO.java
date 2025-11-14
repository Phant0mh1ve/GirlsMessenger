package org.example.dto.chatDto;

import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatCreateDTO {
    private String name;
    private List<Long> users;
    private List<Long> messages;
}
