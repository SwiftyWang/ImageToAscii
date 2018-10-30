package com.swifty.asciimediaconverter.base;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public abstract class ConvertRequest {
    String mFilePath;

    public String getFilePath() {
        return mFilePath;
    }

    protected static class Builder<Request extends ConvertRequest, Builder extends ConvertRequest.Builder> {
        protected Request mConvertRequest;

        protected Builder(Request convertRequest) {
            mConvertRequest = convertRequest;
        }

        public Builder setFilePath(String filePath) {
            mConvertRequest.mFilePath = filePath;
            return (Builder) this;
        }

        public Request build() {
            return mConvertRequest;
        }
    }
}
