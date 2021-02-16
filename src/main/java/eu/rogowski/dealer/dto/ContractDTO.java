package eu.rogowski.dealer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContractDTO {
    private Long contractId;

    private boolean done;

    private Float deposit;
}
