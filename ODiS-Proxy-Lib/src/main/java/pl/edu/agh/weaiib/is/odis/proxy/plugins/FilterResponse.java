package pl.edu.agh.weaiib.is.odis.proxy.plugins;

public class FilterResponse {

    private final boolean status;

    private final String message;

    public FilterResponse(boolean status){
        this.status = status;
        this.message = "";
    }

    public FilterResponse(boolean status, String message){
        this.status = status;
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
