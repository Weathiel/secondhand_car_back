package eu.rogowski.dealer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor
public class OffersDTO {

    private Long offerId;

    private Float mileage;

    private Float price;

    private String color;

    private String prod_country;

    private Boolean english_car;

    private Boolean archivized;

    private Date production_date;

    private Long carId;

    private String vin;

}
