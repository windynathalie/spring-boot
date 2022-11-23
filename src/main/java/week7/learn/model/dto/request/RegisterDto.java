package week7.learn.model.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RegisterDto {

    @Email(message = "Must be email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 12, message = "Password must between 8 - 12 characters")
    private String password;

    @NotBlank(message = "First Name is required")
    private String firstName;

    private String lastName;

    @Size(min = 10, max = 15, message = "Phone Number must between 10 - 15 characters")
    private String phoneNumber;
}
