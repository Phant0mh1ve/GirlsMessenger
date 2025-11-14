package org.example.dto.messageDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private String text;
    private Long chat;
    private Long sender;
    private List<Long> attachments = new ArrayList<>();
    LocalDateTime createdAt;
    boolean isUpdated;
}
