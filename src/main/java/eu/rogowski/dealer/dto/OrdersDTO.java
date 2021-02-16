package eu.rogowski.dealer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdersDTO {
    private Long orderId;

    private Float discount;

    private Long offerId;

    private Long userId;

    private Long paymentId;

    private Long contractId;

    private ContractDTO contract;

    private String username;

}
