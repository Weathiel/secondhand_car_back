package eu.rogowski.dealer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserDTO {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String city;

    private String token;

    private String address;

    private String phoneNumber;

    private Long roleId;
}
