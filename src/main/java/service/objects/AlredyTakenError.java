package service.objects;


public class AlredyTakenError {


    public AlredyTakenError() {
    }

    private final String error = "Username already taken";

    public String getError() {
        return error;
    }
}
