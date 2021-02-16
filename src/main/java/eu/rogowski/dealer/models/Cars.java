package eu.rogowski.dealer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    private String body_type;

    private String gas_type;

    @Temporal(TemporalType.DATE)
    private Date s_production_year;

    @Temporal(TemporalType.DATE)
    private Date e_production_year;

    private Integer engine_power;

    public Cars(Long carId, @NotNull String brand, @NotNull String model, String body_type, String gas_type, Date s_production_year, Date e_production_year, Integer engine_power) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.body_type = body_type;
        this.gas_type = gas_type;
        this.s_production_year = s_production_year;
        this.e_production_year = e_production_year;
        this.engine_power = engine_power;
    }
}
