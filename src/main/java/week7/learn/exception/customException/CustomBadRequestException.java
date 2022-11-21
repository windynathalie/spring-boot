package week7.learn.exception.customException;

public class CustomBadRequestException extends Exception {

    public CustomBadRequestException(String message) {
        super(message);
    }

}
