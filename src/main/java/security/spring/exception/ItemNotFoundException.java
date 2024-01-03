package security.spring.exception;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String message) {
        super(message);
    }
}
