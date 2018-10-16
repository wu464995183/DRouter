package d.drouter;

public class RouterResponse {
    private int mErrorCode;
    private String mErrorMassage;

    public RouterResponse(Builder builder) {
        this.mErrorCode = builder.mErrorCode;
        this.mErrorMassage = builder.mErrorMassage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMassage() {
        return mErrorMassage;
    }

    public static class Builder {

        private int mErrorCode;
        private String mErrorMassage;

        public Builder errorCode(int mErrorCode) {
            this.mErrorCode = mErrorCode;
            return this;
        }

        public Builder errorMassage(String mErrorMassage) {
            this.mErrorMassage = mErrorMassage;
            return this;
        }

        public RouterResponse build() {
            return new RouterResponse(this);
        }
    }
}
