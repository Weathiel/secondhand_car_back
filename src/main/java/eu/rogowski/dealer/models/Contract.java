package eu.rogowski.dealer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    private Float deposit;

    private Boolean done = false;

    public Contract(Float deposit, Boolean done) {
        this.deposit = deposit;
        this.done = done;
    }
}
