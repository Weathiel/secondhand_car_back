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
public class Orders extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Float discount;

    @ManyToOne
    private Offers offers;

    @ManyToOne
    private User user;

    @OneToOne
    private Contract contract;

    public Orders(Long orderId, Float discount, Date createdDate,
                  Long offerId, Float mileage, @NotNull Float price, String color, String prod_country, Boolean englishCar, String image, String vin, @NotNull Boolean archivized, Date production_date,
                  Long carId, @NotNull String brand, @NotNull String model, String body_type, String gas_type, Date s_production_year, Date e_production_year, Integer engine_power,
                  Float deposit, Boolean done,
                  String username) {
        this.orderId = orderId;
        this.discount = discount;
        this.offers = new Offers(offerId, mileage, price, color, prod_country, englishCar, image, vin, archivized, production_date, carId, brand, model, body_type, gas_type, s_production_year, e_production_year, engine_power);
        this.user = new User(username);
        this.contract = new Contract(deposit, done);
        this.createdDate = createdDate;
    }

    public Orders(Long orderId, Float discount, Date createdDate,
                  Long offerId, Float mileage, @NotNull Float price, String color, String prod_country, Boolean englishCar, String image, String vin, @NotNull Boolean archivized, Date production_date,
                  Long carId, @NotNull String brand, @NotNull String model, String body_type, String gas_type, Date s_production_year, Date e_production_year, Integer engine_power,
                  Float deposit, Boolean done) {
        this.orderId = orderId;
        this.discount = discount;
        this.offers = new Offers(offerId, mileage, price, color, prod_country, englishCar, image, vin, archivized, production_date, carId, brand, model, body_type, gas_type, s_production_year, e_production_year, engine_power);
        this.user = null;
        this.contract = new Contract(deposit, done);
        this.createdDate = createdDate;
    }
}
