package eu.rogowski.dealer.payload;

import eu.rogowski.dealer.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private Long id;

    private String username;

    private String password;

    private List role;

    private String token;

    public JwtResponse(Long id, String username, String password, List role, String token) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.token = token;
    }
}
