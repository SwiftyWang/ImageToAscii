# This lib is converting an image or video to ascii image/video/gif

## The demo result

| Original                              | descriptionOutput                    |
|---------------------------------------|--------------------------------------|
| ![original image](./demo/pic-i.jpg)   | ![converted image](./demo/pic-o.jpg) |
| ![original video](./demo/video-i.gif) | ![converted gif](./demo/video-o.gif) |

## How to use

```java  
ImageConverter imageConverter = new ImageConverterImpl();
    ImageConvertRequest.Builder builder = new ImageConvertRequest.Builder(this);
    builder
           .setFilePath(path)
           .setEnableColor(true);
    imageConverter
            .convertRx(builder.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ImageConvertResponse>() {
                @Override
                public void accept(ImageConvertResponse imageConvertResponse) throws Exception {
                    Bitmap bitmap = imageConvertResponse.getResponse();
                }
            })
```

```java 
VideoConverter videoConverter = new VideoConverterImpl();
VideoConvertRequest.Builder builder = new VideoConvertRequest.Builder(MainActivity.this);
builder
        .setConvertedFileType(VideoConvertRequest.ConvertedFileType.GIF)
        .setFilePath(mediaPath)
        .setDesFolder(AppConfig.BASE_PATH)
        .setSpeed(1.5f)
        .setEnableColor(true)
        .setFps(fps);
videoConverter
        .convertRx(builder.build())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<VideoConvertResponse>() {
            @Override
            public void accept(VideoConvertResponse videoConvertResponse) throws Exception {
                if (videoConvertResponse.isComplete()) {
                    progressBar.setProgress(100);
                } else {
                    progressBar.setProgress((int) (videoConvertResponse.getProgress() * 100));
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, throwable.toString(), throwable);
            }
        })
```

## More APIs are developing...

# TODO LIST
- [x] compiled with FFmpeg lib success
- [x] finish coloring option
- [ ] optimize the mechanism of coloring option
- [ ] make the video convert to multiThread to reduce the process time.

# Ref repo
- https://github.com/meiniepan/Pic2Ascii
- https://github.com/GodFengShen/PicOrVideoToAscii
