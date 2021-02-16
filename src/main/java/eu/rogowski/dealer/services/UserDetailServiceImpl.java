package eu.rogowski.dealer.services;

import eu.rogowski.dealer.dto.UserDetailsImpl;
import eu.rogowski.dealer.models.User;
import eu.rogowski.dealer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
                user.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
        return UserDetailsImpl.build(user.get());

    }
}
