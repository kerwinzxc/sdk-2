package com.game.sdk.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtils {

//    /**
//
//     * 进行截取屏幕
//
//     * @param pActivity
//
//     * @return bitmap
//
//     */
//
//    public static Bitmap takeScreenShot(Activity pActivity)
//
//    {
//
//        Bitmap bitmap=null;
//
//        View view=pActivity.getWindow().getDecorView();
//
//        // 设置是否可以进行绘图缓存
//
//        view.setDrawingCacheEnabled(true);
//
//        // 如果绘图缓存无法，强制构建绘图缓存
//
//        view.buildDrawingCache();
//
//        // 返回这个缓存视图
//
//        bitmap=view.getDrawingCache();
//
//
//
//        // 获取状态栏高度
//
//        Rect frame=new Rect();
//
//        // 测量屏幕宽和高
//
//        view.getWindowVisibleDisplayFrame(frame);
//
//        int stautsHeight=frame.top;
//
//        Log.d("jiangqq", "状态栏的高度为:"+stautsHeight);
//
//
//
//        int width=pActivity.getWindowManager().getDefaultDisplay().getWidth();
//
//        int height=pActivity.getWindowManager().getDefaultDisplay().getHeight();
//
//        // 根据坐标点和需要的宽和高创建bitmap
//
//        bitmap= Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height-stautsHeight);
//
//        return bitmap;
//
//    }
//
//
//
//
//
//    /**
//
//     * 保存图片到sdcard中
//
//     * @param pBitmap
//
//     */
//
//    private static boolean savePic(Bitmap pBitmap, String strName)
//
//    {
//
//        FileOutputStream fos=null;
//
//        try {
//
//            fos=new FileOutputStream(strName);
//
//            if(null!=fos)
//
//            {
//
//                pBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
//
//                fos.flush();
//
//                fos.close();
//
//                return true;
//
//            }
//
//
//
//        } catch (FileNotFoundException e) {
//
//            e.printStackTrace();
//
//        }catch (IOException e) {
//
//            e.printStackTrace();
//
//        }
//
//        return false;
//
//    }
//
//    /**
//
//     * 截图
//
//     * @param pActivity
//
//     * @return 截图并且保存sdcard成功返回true，否则返回false
//
//     */
//
//    public static boolean shotBitmap(Activity pActivity)
//
//    {
//
//
//
//        return  ScreenShotUtils.savePic(takeScreenShot(pActivity), "sdcard/"+ System.currentTimeMillis()+".png");
//
//    }

    // 获取指定Activity的截屏，保存到png文件
    private static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
         Bitmap b = Bitmap.createBitmap(b1, 0, 25, b1.getWidth(), 600);
//        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
//                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    // 保存到sdcard
    private static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 程序入口
    public static void shootview(Activity a) {
        ScreenShotUtils.savePic(ScreenShotUtils.takeScreenShot(a), "sdcard/jietu.png");
    }
}
