/**
 * 截屏工具
 */
package com.qican.ifarmmanager.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtil {

    /**
     * 整个Activity截屏
     *
     * @param activity
     * @return
     */
    public static Bitmap captureScreen(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    /**
     * 截取某个View
     *
     * @param v
     * @return
     */
    public static Bitmap getBitmapFromViewForPrint(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    //保存文件到指定路径
    public static boolean saveImageAndShare(Context context, Bitmap bmp) {

        CommonTools myTool = new CommonTools(context);

        // 首先保存图片
        String storePath = CommonTools.USER_FILE_PATH + File.separator + myTool.getManagerId();

        myTool.log("Save img to " + storePath);

        File appDir = new File(storePath);

        myTool.log("Check File is exist ...");
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                myTool.log("Make file failed ...");
                return false;
            }
        }

        String fileName = "farm_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            if (isSuccess) {

                myTool.showInfo("图片已保存至[ " + storePath + " ]");
                myTool.log("Share Img ...");

                sharePhoto(context, storePath, fileName);
                return true;
            } else {

                myTool.log("Save Img Failed ...");

                return false;
            }
        } catch (IOException e) {

            myTool.log("IOException : " + e.getMessage());
        }
        return false;
    }

    // 分享照片
    public static void sharePhoto(Context context, String photoUri, String fileName) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(photoUri, fileName);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, "分享商标至"));
    }
}
