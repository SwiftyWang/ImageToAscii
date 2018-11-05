package com.swifty.asciimediaconverter.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public class ImageConverterImpl implements ImageConverter {
    @Override
    public Single<ImageConvertResponse> convertRx(final ImageMediaConvertRequest convertRequest) {

        Single<ImageConvertResponse> imageConvertResponseObservable = Single.create(new SingleOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(SingleEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap = convert2Bitmap(convertRequest);
                emitter.onSuccess(bitmap);
            }
        }).map(new Function<Bitmap, ImageConvertResponse>() {
            @Override
            public ImageConvertResponse apply(Bitmap bitmap) throws Exception {
                return new ImageConvertResponse(bitmap);
            }
        }).subscribeOn(Schedulers.computation());
        return imageConvertResponseObservable;
    }

    @Override
    public ImageConvertResponse convertSync(ImageMediaConvertRequest convertRequest) {
        Bitmap bitmap = convert2Bitmap(convertRequest);
        return new ImageConvertResponse(bitmap);
    }

    private Bitmap convert2Bitmap(ImageMediaConvertRequest convertRequest) {
        final String base = "#8XOHLTI)i=+;:,.";// 字符串由复杂到简单
        Bitmap bitmap = convertRequest.getBitmap();
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile(convertRequest.getFilePath());  //读取图片
        }
        if (bitmap == null) {
            throw new IllegalArgumentException("Cannot get image path or bitmap from ImageConvertRequest");
        }

        WindowManager wm = (WindowManager) convertRequest.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int width0 = bitmap.getWidth();
        int height0 = bitmap.getHeight();
        int width1, height1;
        int scale = convertRequest.getSampleSize();
        if (width0 <= width / scale) {
            width1 = width0;
            height1 = height0;
        } else {
            width1 = width / scale;
            height1 = width1 * height0 / width0;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, width1, height1, true);

        //输出到指定文件中
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (int y = 0; y < bitmap.getHeight(); y += 2) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                final int pixel = bitmap.getPixel(x, y);
                final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                final int index = Math.round(gray * (base.length() + 1) / 255);
                String s = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                if (convertRequest.isEnableColor()) {
                    SpannableString redSpannable = new SpannableString(s);
                    redSpannable.setSpan(new ForegroundColorSpan(pixel), 0, s.length(), 0);
                    spannableStringBuilder.append(redSpannable);
                } else {
                    spannableStringBuilder.append(s);
                }
            }
            spannableStringBuilder.append("\n");
        }
        return textAsBitmap(spannableStringBuilder, convertRequest.getContext());
    }

    public static Bitmap textAsBitmap(CharSequence text, Context context) {

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.MONOSPACE);

        textPaint.setTextSize(12);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         //

        StaticLayout layout = new StaticLayout(text, textPaint, width,

                Layout.Alignment.ALIGN_CENTER, 1f, 0.0f, true);

        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,

                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        canvas.translate(10, 10);

        canvas.drawColor(Color.WHITE);

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色

        layout.draw(canvas);

        Log.d("textAsBitmap",

                String.format("1:%d %d", layout.getWidth(), layout.getHeight()));

        return bitmap;

    }
}
