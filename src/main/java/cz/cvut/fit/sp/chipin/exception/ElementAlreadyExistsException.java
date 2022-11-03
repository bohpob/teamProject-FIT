package cz.cvut.fit.sp.chipin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ElementAlreadyExistsException extends RuntimeException {
    private final String message;

    public ElementAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

}
