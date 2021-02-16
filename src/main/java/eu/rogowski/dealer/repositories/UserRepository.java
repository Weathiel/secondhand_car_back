package eu.rogowski.dealer.repositories;

import eu.rogowski.dealer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByFirstNameAndLastName(String firstName, String lastName);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT new User(u.username, u.firstName, u.lastName, u.email, u.city, u.address, u.phoneNumber) FROM User u WHERE u.username LIKE %:username% AND u.lastName LIKE %:lastname% AND u.firstName LIKE %:firstname%")
    List<User> filterUser(@Param("firstname") String firstname, @Param("lastname") String lastname, @Param("username") String username);

    @Query("SELECT count(u) FROM User u JOIN u.orders o JOIN o.offers off " +
            "WHERE off.vin = :vin")
    long countUsersForOffer(@Param("vin") String vin);
    void delete(User u);
}
