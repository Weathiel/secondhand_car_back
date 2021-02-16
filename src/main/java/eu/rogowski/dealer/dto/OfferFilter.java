package eu.rogowski.dealer.dto;

import eu.rogowski.dealer.models.Cars;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OfferFilter {
    Cars car;
    String vin;
    long minMilleage;
    long maxMilleage;
    long minValue;
    long maxValue;
    boolean englishCar = false;

    String prod_country;
    String date;
    String maxDate;
}
