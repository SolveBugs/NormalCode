package demo.zq.com.normalcode;

/**
 * Created by zhen on 2016/8/19.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用代码片段
 */
public class Utils {

    //1.给当前界面添加一个透明度

    /**
     * 给界面添加透明度
     *
     * @param activity
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    //2.一键添加qq群
    //http://qun.qq.com/join.html 选择需要添加的群，然后选择手机平台，即可生成相应的代码。

    //3.得到当前版本号
    public static int getVersionCode(Context context) {
        int code = 0;
        if (context == null) {
            return code;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return code;
    }

    //4.判断当前网络是否可用

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable()
                        && mNetworkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    //5.当前是否是移动网络

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable()
                        && mMobileNetworkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    //6.当前是否是wifi

    public static boolean isWifi(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }

    //7.ListView根据item计算出实际的高度

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //8.拨打电话(记得在清单文件添加权限)

    /**
     * 给界面添加透明度
     *
     * @param phoneNum
     */
    public void call(String phoneNum, Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    //9. 字符串是否包含汉字
    public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();//
        return result;
    }

    //10.从assets 文件夹中读取图片
    public static Drawable loadImageFromAsserts(final Context ctx, String fileName) {
        try {
            InputStream is = ctx.getResources().getAssets().open(fileName);
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //11.判断字符串是否为空
    public static boolean isNull(String string) {
        if (string != null) {
            string = string.trim();
            if (string.length() != 0) {
                return false;
            }
        }
        return true;
    }

    //12.递归删除文件夹及里边的文件
    public void deletFiles(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deletFiles(f);
            }
            file.delete();
        }
    }
    //13.非常全面的一个图片库，个人开发者必看
    //http://www.iconfont.cn/collections

    //14.各种单位转换

    /**
     * 获得屏幕宽度（像素）
     *
     * @param activity
     * @return
     */
    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }

    /**
     * 获得屏幕高度（像素）
     *
     * @param activity
     * @return
     */
    public static int getDisplayHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;     // 屏幕高度（像素）
        return height;
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context 通过上下文获得显示参数中的屏幕密度
     * @param pxValue 需要转换的px值
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context  通过上下文获得显示参数中的屏幕密度
     * @param dipValue 需要转换的dp值
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context 通过上下文获得显示参数中的屏幕密度
     * @param pxValue 需要转换的px值
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context 通过上下文获得显示参数中的屏幕密度
     * @param spValue 需要转换的sp值
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //15. 压缩图片，防止Bitmap过大导致OOM

    /**
     * 谷歌推荐使用方法，从资源中加载图像，并高效压缩，有效降低OOM的概率
     *
     * @param res       资源
     * @param resId     图像资源的资源id
     * @param reqWidth  要求图像压缩后的宽度
     * @param reqHeight 要求图像压缩后的高度
     * @return
     */
    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 设置inJustDecodeBounds = true ,表示获取图像信息，但是不将图像的像素加入内存
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // 调用方法计算合适的 inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // inJustDecodeBounds 置为 false 真正开始加载图片
        options.inJustDecodeBounds = false;
        //将options.inPreferredConfig改成Bitmap.Config.RGB_565，
        // 是默认情况Bitmap.Config.ARGB_8888占用内存的一般
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // 计算 BitmapFactpry 的 inSimpleSize的值的方法
    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        // 获取图片原生的宽和高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // 如果原生的宽高大于请求的宽高,那么将原生的宽和高都置为原来的一半
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 主要计算逻辑
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //16.请求打开蓝牙
    //REQUEST_ENABLE_BT自定义requestCode
    int REQUEST_ENABLE_BT = 0;
    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT) {
    }
    //17.读取Excel文件
     使用jxl jar包。
    //18.应用程序启动短暂白屏或者黑屏解决办法（启动页设置背景图片）
    //1.自定义全屏主题
    <style name="FlashStyle" parent="android:style/Theme.NoTitleBar.Fullscreen">
    <item name="android:windowBackground">@drawable/start_app_flash</item>
    </style>
    //2.设置给闪屏页
    android:theme="@style/FlashStyle"
    //19.判断是否是短时间内重复点击按钮之类的动作（防止重复打开页面等）
    private static long lastClickTime;
    public synchronized static boolean isDoubleClick(long time) {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime > time) {
            lastClickTime = currentTime;
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        return isClick2;
    }
    //20.得到sd卡路径
    public static String getSDCardPath() {
        File sdcardDir = null;
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
            return sdcardDir.toString();
        } else {
            return null;
        }
    }
    //21测试本地连接github提交代码
    //22 从网络路径下载图片保存到本地，并返回本地保存路径
     public static String getImageURI(String networkPath, String dirPath, String s) {
        if (networkPath == null) {
            return null;
        }
        String name = networkPath.substring(networkPath.lastIndexOf("/"));
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirFile, name + s);
        try {
            // 从网络上获取图片
            URL url = new URL(networkPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
                return dirPath + File.separator + name + s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //23
    /**
     * 改变系统语言为英文环境
     */
    private void initLanguage() {
        try {
            Log.d("Appcontext", "初始化的时候设置系统语言为英文环境");
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Log.i("amnType", activityManagerNative.toString());

            Object am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            Log.i("amType", am.getClass().toString());

            Object config = am.getClass().getMethod("getConfiguration").invoke(am);
            Log.i("configType", config.getClass().toString());
            config.getClass().getDeclaredField("locale").set(config, Locale.ENGLISH);
            config.getClass().getDeclaredField("userSetLocale").setBoolean(config, true);

            am.getClass().getMethod("updateConfiguration", android.content.res.Configuration.class).invoke(am, config);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
