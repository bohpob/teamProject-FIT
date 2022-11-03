package cz.cvut.fit.sp.chipin.exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Getter
public class ErrorResponse {
    private int code;
    private Date timestamp;
    private String message;
    private String details;

    public ErrorResponse(int code, Date timestamp, String message, String details) {
        super();
        this.code = code;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
