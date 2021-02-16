package eu.rogowski.dealer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.criterion.Order;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "password", "email"}))
@Getter @Setter @NoArgsConstructor
public class User extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String city;

    @NotNull
    private String address;

    @NotNull
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    Set<Orders> orders = new HashSet<>(0);

    @ManyToMany
    @JoinColumn()
    private Set<Role> role = new HashSet<>(0);

    public User(@NotNull String username) {
        this.username = username;
    }

    public User(@NotNull String username, @NotNull String firstName, @NotNull String lastName, @NotNull String email, @NotNull String city, @NotNull String address, @NotNull String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
