package th.co.infinitait.comvisitor.exception;

public enum MessageLevel {
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR");

    private String code;
    public String getCode() {
        return code;
    }
    MessageLevel(String code) {
        this.code = code;
    }
}
