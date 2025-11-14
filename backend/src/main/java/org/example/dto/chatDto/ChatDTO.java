package org.example.dto.chatDto;

import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO{
    private Long id;
    private String name;
    private List<Long> users;
    private List<Long> messages;
}
