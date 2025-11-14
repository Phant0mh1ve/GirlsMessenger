package org.example.dto.userDto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String username;
    private String email;
    private String passwordHash;
}
