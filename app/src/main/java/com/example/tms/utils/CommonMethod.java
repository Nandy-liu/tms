package com.example.tms.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;

import com.example.tms.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CommonMethod {
    public static void setToolbarColor(Activity activity) {
        try {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getColor(R.color.colorPrimary));
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap compressBoundsBitmap(Context context, Uri uri, int targetWidth, int targetHeight) {
        InputStream input = null;
        Bitmap bitmap = null;
        try {
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, (Rect) null, options);
            input.close();
            int originalWidth = options.outWidth;
            int originalHeight = options.outHeight;
            if (originalWidth != -1 && originalHeight != -1) {
                boolean be1 = true;
                int widthBe = 1;
                if (originalWidth > targetWidth) {
                    widthBe = originalWidth / targetWidth;
                }
                int heightBe = 1;
                if (originalHeight > targetHeight) {
                    heightBe = originalHeight / targetHeight;
                }
                int be2 = widthBe > heightBe ? heightBe : widthBe;
                if (be2 <= 0) {
                    be2 = 1;
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = be2;
                input = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(input, (Rect) null, options);
                input.close();
                input = null;
            } else {
                Object be = null;
            }
        } catch (FileNotFoundException var23) {
            var23.printStackTrace();
        } catch (IOException var24) {
            var24.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }
            return bitmap;
        }
    }
}
