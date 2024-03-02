package library.backend.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookNotFoundException extends RuntimeException {
    private String message;
    public BookNotFoundException(String message){
        super(message);
        this.message = message;
    }
}
