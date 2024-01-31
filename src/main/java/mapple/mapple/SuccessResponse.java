package mapple.mapple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {

    private String message;
    private Object data;

    public SuccessResponse(String message) {
        this.message = message;
        this.data = null;
    }
}
