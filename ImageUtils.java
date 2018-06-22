
public class ImageUtils {

    private final static String MEIZU = "meizu";
    private final static int MEIZU_IMG_KB = 200;//魅族图片压缩限制最大这尺寸

    private final static int DEFAULT_IMG_KB = 250;

    private final static int compress_granularity = 4;

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于200kb,大于继续压缩

        int max_kb = DEFAULT_IMG_KB;
        if (MEIZU.equalsIgnoreCase(android.os.Build.BRAND)) {
            max_kb = MEIZU_IMG_KB;
        }

        while (baos.toByteArray().length / 1024 > max_kb) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= compress_granularity;
            if (options < compress_granularity)
                break;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     *
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#E6E9ED"));
        }
        if (h == 0 || scrollView.getWidth() == 0) {
            return null;
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0xFF, 0xFF, 0xFF, 0xFF);
        scrollView.draw(canvas);
        return compressImage(bitmap);
    }

    public static Bitmap getBitmapByListView(ListView listView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
//            listView.getChildAt(i).setBackgroundColor(
//                    Color.parseColor("#ffffff"));
        }
        if (h == 0 || listView.getWidth() == 0) {
            return null;
        }

        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);

        return compressImage(bitmap);
    }

    public static Bitmap getBitmapByPullToRefreshListView(PullToRefreshListView listView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
//            listView.getChildAt(i).setBackgroundColor(
//                    Color.parseColor("#ffffff"));
        }
        if (h == 0 || listView.getWidth() == 0) {
            return null;
        }

        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);

        return compressImage(bitmap);
    }

    public static Bitmap getScrollBitmap(Activity activity, ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }

        // 创建对应大小的bitmap
        if (h == 0 || scrollView.getWidth() == 0) {
            return null;
        }
        h = Math.min(h, Utils.getScreenHeight(activity)); //防止OOM
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        scrollView.draw(canvas);
        return bitmap;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, b1.getHeight()
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static String saveBitmapPNG(Context context, Bitmap imageBitmap) {
        if (imageBitmap == null) {
            return null;
        }

        String fileName = String.valueOf(System.currentTimeMillis()) + StringPool.JPG;
        File savefile = new File(Utils.getImageFileDir(context), fileName);
        if (savefile.exists()) {
            boolean delete = savefile.delete();
            Utils.printFileDirState(delete);
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(savefile, false);
            if (null != out) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
        } catch (FileNotFoundException e) {
            com.dnurse.common.logger.Log.printThrowable(e);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return savefile.getPath();
    }

    public static String uploadPic(String path, AppContext appContext) {
        User user = appContext.getActiveUser();
        if (user != null && !TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }

            String sn = user.getSn();
            HashMap<String, String> h1 = new HashMap<String, String>();
            h1.put("sn", sn);
            HashMap<String, File> h2 = new HashMap<String, File>();
            h2.put("file", new File(path));
            try {
                String reply = BaseClient.post(appContext, DataURLs.UPLOAD_PIC, Utils.rebuildMap(h1, user), h2);
                JSONObject replyJson = new JSONObject(reply);
                if (NetRetKeys.SUCCESS_CODE == replyJson.optInt(NetRetKeys.STATE_CODE)) {
                    path = replyJson.optString(NetRetKeys.DATA);
                    return path;
                }
                /* 图片上传失败，先返回本地路径 */
                return path;
            } catch (Exception e) {
                
            /* 图片上传失败，先返回本地路径 */
                return path;
            }
        }
        return "";
    }

    public static void copyAssetToSD(Context context, String pic) throws IOException {
        OutputStream myOutput = new FileOutputStream(Utils.getImageFileDir(context) + pic);
        try {
            InputStream myInput = context.getAssets().open(pic);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();

        } finally {
            if (myOutput != null) {
                myOutput.close();
            }
        }

    }

    private static void streamToSD(InputStream myInput, String path) throws IOException {
        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();
            myInput.close();
        } finally {
            if (myOutput != null) {
                myOutput.close();
            }
        }
    }

    public static String assetSaveToSD(Context context, String resName) {
        String baseUrl = Utils.getImageFileDir(context).getAbsolutePath();
        String path = baseUrl + "/" + resName;
        if (!new File(path).exists()) {
            try {
                streamToSD(context.getAssets().open(resName), path);
            } catch (IOException e) {
                
            }
        }
        return path;
    }

}
