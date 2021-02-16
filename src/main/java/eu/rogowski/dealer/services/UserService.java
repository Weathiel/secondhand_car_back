package eu.rogowski.dealer.services;

import eu.rogowski.dealer.configuration.jwt.AuthTokenFilter;
import eu.rogowski.dealer.configuration.jwt.JwtUtils;
import eu.rogowski.dealer.exceptions.ResourceNotFoundException;
import eu.rogowski.dealer.models.Role;
import eu.rogowski.dealer.models.User;
import eu.rogowski.dealer.dto.UserDetailsImpl;
import eu.rogowski.dealer.dto.UserDTO;
import eu.rogowski.dealer.payload.JwtResponse;
import eu.rogowski.dealer.payload.ResponseJSON;
import eu.rogowski.dealer.repositories.RoleRepository;
import eu.rogowski.dealer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final AuthTokenFilter authTokenFilter;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final JwtUtils jwtUtils;

    public Page<User> getUserPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "userId"));
        return userRepository.findAll(pageable);
    }

    public List<User> getUserByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findAllByFirstNameAndLastName(firstName, lastName);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found!"));
    }

    public Integer getLengthOfUsers() {
        return userRepository.findAll().size();
    }

    public ResponseEntity<?> authenticateUser(UserDTO userDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();



        List list = userDetailsImpl.getAuthorities().stream().map(auth -> auth.getAuthority()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(userDetailsImpl.getId(), userDetailsImpl.getUsername(), userDetailsImpl.getPassword(), list, jwt));

    }

    public User getToken(HttpServletRequest httpServletRequest) {
        return userRepository.findByUsername(jwtUtils.getUsernameByToken(authTokenFilter.parseJwt(httpServletRequest)))
                .orElseThrow(() -> new UsernameNotFoundException("User for token wasn't found"));
    }

    public ResponseEntity registerUser(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        Set<Role> role = new HashSet();
        role.add(roleRepository.findByRoleId(Long.parseLong("1")).get());
        user.setRole(role);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());


        userRepository.save(user);

        return ResponseEntity.ok(new ResponseJSON("User successfully created!", 200));
    }

    public void delete(Long id) {
        userRepository.delete(userRepository.getOne(id));
    }

    public void update(Long id, UserDTO userDTO) {
        User user = userRepository.getOne(id);
        Set<Role> toTake = new HashSet<>();

        for(int i = 1; i<= userDTO.getRoleId(); i++){
            int j = i;
            if(i == 2)
                j++;
            Role role = roleRepository.findById((long) j).orElseThrow(() -> new ResourceNotFoundException());
            toTake.add(role);
        }
        user.setRole(toTake);

       User user1 = userRepository.save(user);
       for(Role role: user1.getRole()){
           System.out.println(role.getRolename());
       }
    }

    public void editUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUsername(userDTO.getUsername());
        userRepository.save(user);
    }

    public User updateVerificationToken(String mailTo) {
        User user = userRepository.findByEmail(mailTo).orElseThrow(() -> new UsernameNotFoundException("User wasn't found"));

        String accessToken = jwtUtils.generateJwtToken(mailTo);
        accessToken = accessToken.replaceAll("[^a-zA-Z0-9]", "");
        userRepository.save(user);

        return user;
    }

    public ResponseEntity enableUser(String username, String token) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Cannot find user by this email"));

/*        if (token.equals(user.getActivationToken())){
            user.setActivated(true);*/
            userRepository.save(user);
            return ResponseEntity.ok(new ResponseJSON("Successfully activated user", 200));
/*        }
       else{
            return ResponseEntity.badRequest().body(new ResponseJSON("Failed to match tokens", 402));
        }*/

    }

    public ResponseEntity filter(UserDTO userDTO) {

        return ResponseEntity.ok(userRepository.filterUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getUsername()));
    }
}
