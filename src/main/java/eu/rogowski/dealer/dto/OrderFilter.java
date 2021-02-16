package eu.rogowski.dealer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderFilter {
    Float maxValue;
    Float minValue;
    Float minDiscount;
    Float maxDiscount;
    String vin;
    String username;
    Boolean done = false;
}
