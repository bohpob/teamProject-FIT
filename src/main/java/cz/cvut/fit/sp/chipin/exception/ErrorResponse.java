package cz.cvut.fit.sp.chipin.exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Getter
public class ErrorResponse {
    private int code;
    private String date;
    private String message;
    private String details;

    public ErrorResponse(int code, String message, String details) {
        super();
        this.code = code;
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm");
        this.date = currentDate.format(formatter);
        this.message = message;
        this.details = details;
    }
}
