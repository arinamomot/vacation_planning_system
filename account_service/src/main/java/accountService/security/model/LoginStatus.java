package accountService.security.model;

public class LoginStatus {

    private boolean loggedIn;
    private String username;
    private String errorMessage;
    private boolean success;
    private Integer userId;

    public LoginStatus() {
    }

    public LoginStatus(boolean loggedIn, boolean success, String username, String errorMessage, Integer userId) {
        this.loggedIn = loggedIn;
        this.username = username;
        this.errorMessage = errorMessage;
        this.success = success;
        this.userId = userId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}

