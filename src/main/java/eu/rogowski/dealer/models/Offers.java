package eu.rogowski.dealer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"vin"}))
public class Offers extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    private Float mileage;

    @NotNull
    private Float price;

    private String color;

    private String prod_country;

    private Boolean englishCar = false;

    private String image;

    @NotNull
    private String vin;

    @NotNull
    private Boolean archivized = false;

    private Date production_date;

    @ManyToOne
    private Cars cars;

    public Offers(Long offerId, Float mileage, @NotNull Float price, String color, String prod_country, Boolean englishCar, String image, @NotNull String vin, @NotNull Boolean archivized, Date production_date,
                  Long carId, @NotNull String brand, @NotNull String model, String body_type, String gas_type, Date s_production_year, Date e_production_year, Integer engine_power) {
        this.offerId = offerId;
        this.mileage = mileage;
        this.price = price;
        this.color = color;
        this.prod_country = prod_country;
        this.englishCar = englishCar;
        this.image = image;
        this.vin = vin;
        this.archivized = archivized;
        this.production_date = production_date;
        this.cars = new Cars(carId, brand, model, body_type, gas_type, s_production_year, e_production_year, engine_power);
    }
}
