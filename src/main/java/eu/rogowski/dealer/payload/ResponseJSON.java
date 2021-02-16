package eu.rogowski.dealer.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseJSON {
    String message;
    int responseCode;

    public ResponseJSON(String message, int responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }
}
