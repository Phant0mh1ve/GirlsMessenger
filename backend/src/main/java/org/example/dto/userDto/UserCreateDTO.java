package org.example.dto.userDto;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String username;
    @Email
    private String email;
    @Length(max = 72)
    private String password;
}
