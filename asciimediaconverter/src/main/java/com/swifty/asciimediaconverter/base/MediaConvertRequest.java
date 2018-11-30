package com.swifty.asciimediaconverter.base;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public abstract class MediaConvertRequest {
    String mFilePath;
    boolean enableColor;

    public String getFilePath() {
        return mFilePath;
    }

    public boolean isEnableColor() {
        return enableColor;
    }

    protected static class Builder<Request extends MediaConvertRequest, Builder extends MediaConvertRequest.Builder> {
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

        public Builder setEnableColor(boolean enableColor) {
            mConvertRequest.enableColor = enableColor;
            return (Builder) this;
        }
    }
}
