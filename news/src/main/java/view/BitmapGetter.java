package view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bruse on 16/3/22.
 */
public class BitmapGetter {

    public static Bitmap getUnderMaxSizeImage(String pathName, int maxWidth,
                                              int maxHeight) {
//        BitmapFactory.Options ops = new BitmapFactory.Options();
//        ops.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(pathName, ops);
//
//        int actWidth = ops.outWidth;
//        int actHeight = ops.outHeight;
//
//        int scale = 1;
//        if (actWidth > actHeight) {
//            if (maxWidth < actWidth) {
//                scale = maxWidth / actWidth;
//            }
//        } else {
//            if (maxHeight < actHeight) {
//                scale = maxHeight / actHeight;
//            }
//        }
//
//        ops.inJustDecodeBounds = false;
//        ops.inScaled = true;
//        ops.inSampleSize = scale;
//        Bitmap bm = null;
//        try {
//            bm = BitmapFactory.decodeFile(pathName, ops);
//
//        } catch (OutOfMemoryError err) {
//
//        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 128*128);
        opts.inJustDecodeBounds = false;
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeFile(pathName, opts);
        } catch (OutOfMemoryError err) {
        }
        return bm;
    }

    public static Bitmap getImageFromNet(String url, String desPath,
                                         String desName, int maxWidth, int maxHeight) {
        downloadImageAndSave(url, desPath, desName);
        return getUnderMaxSizeImage(desPath + File.separator + desName,
                maxWidth, maxHeight);
    }

    private static void downloadImageAndSave(String url, String desPath,
                                             String desName) {
        File filePath = new File(desPath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File filePathName = new File(filePath.getAbsolutePath()
                + File.separator + desName);

        InputStream input = null;
        FileOutputStream fileOutput = null;
        try {
            HttpURLConnection hURLConn = (HttpURLConnection) new URL(url)
                    .openConnection();
            input = hURLConn.getInputStream();
            fileOutput = new FileOutputStream(filePathName);
            int temp = -1;
            while ((temp = input.read()) != -1) {
                fileOutput.write(temp);
            }
        } catch (Exception e) {
            Log.d("tgnet.com", e.getMessage());
        } finally {
            IOStreamManager.closeInputStreams(input);
            IOStreamManager.closeOutputStreams(fileOutput);
        }
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}

