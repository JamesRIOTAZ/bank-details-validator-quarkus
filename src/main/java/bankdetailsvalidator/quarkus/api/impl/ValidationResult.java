package bankdetailsvalidator.quarkus.api.impl;

public class ValidationResult {

    private StringBuilder message;
    private boolean complete;
    private boolean checkPassed;

    public ValidationResult(){
        complete = false;
        checkPassed = false;
        message = new StringBuilder();
    }

    protected boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isCheckPassed() {
        return checkPassed;
    }

    public void setCheckPassed(boolean checkPassed) {
        this.checkPassed = checkPassed;
    }

    public void updateMessage(String testName){
        message.append(testName).append(isCheckPassed() ? " OK. " : " failed. ");
    }

    protected String getMessage(){
        return message.toString().trim();
    }
}
