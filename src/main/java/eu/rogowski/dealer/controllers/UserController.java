package eu.rogowski.dealer.controllers;

import eu.rogowski.dealer.models.User;
import eu.rogowski.dealer.dto.UserDTO;
import eu.rogowski.dealer.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @GetMapping(params = {"page", "size"})
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public List<User> getUserPage(@RequestParam Integer page,
                                  @RequestParam Integer size) {
        return userService.getUserPage(page, size).toList();
    }

    @GetMapping("/length")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public Integer getLenghtOfUsers() {
        return userService.getLengthOfUsers();
    }

    @GetMapping(params = {"firstName", "lastName"})
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public List<User> getUsersByNames(@RequestParam String firstName,
                                      @RequestParam String lastName) {
        return userService.getUserByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping(params = "username")
    public User getUsersByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/getToken")
    public User getToken(HttpServletRequest httpServletRequest) {
        return userService.getToken(httpServletRequest);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
        return userService.authenticateUser(userDTO);
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity regiser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public ResponseEntity filterUser(@RequestBody UserDTO userDTO){
        return userService.filter(userDTO);
    }

    @PutMapping("/{username}")
    public void updateProfile(@PathVariable String username,
                              @RequestBody UserDTO userDTO){
        userService.editUser(username, userDTO);
    }

}
