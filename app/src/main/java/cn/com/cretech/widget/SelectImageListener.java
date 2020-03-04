package cn.com.cretech.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import cn.com.cretech.base.BaseApplication;
import cn.com.cretech.setting.BaseLink;
import cn.com.cretech.util.ImageUtil;
import cn.com.cretech.util.UtilPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.List;

/**
 * 类说明：点击图片选择本地照片
 *
 * @author wz
 * @version 1.0
 * @date 2014-9-9
 */
public class SelectImageListener implements DialogInterface.OnClickListener {
    private String imagePath = "";
    private FragmentActivity activity;
    private CircleImageView image;

    public SelectImageListener(FragmentActivity activity, CircleImageView img) {
        this.activity = activity;
        this.image = img;
    }

    public SelectImageListener(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                cameraImage();
                break;
            case 1:
                locationImage();
                break;
            default:
                dialog.dismiss();
        }

    }

    // 本地图片
   /* public void locationImage() {
        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
        getImage.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(getImage, "选择照片"),
                BaseLink.SWITCH_PHONE_PICTURE);
    }*/
    public void locationImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, BaseLink.SWITCH_PHONE_PICTURE);
    }
    // 获取相机拍摄图片
    public File cameraImage() {
        // 启动相机
        Intent myIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        String picName = System.currentTimeMillis() + ".jpg";
        File file = null;
        try {
            String path = ImageUtil.saveFilePaht(picName);
            file = new File(path);
            Uri uri = Uri.fromFile(file);
            setImagePath(path);
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            myIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            myIntent.putExtra("return-data", true);
        } catch (FileNotFoundException e) {
            Log.d("headImageChangeListener", "wztest err" + e.toString());
        }
        activity.startActivityForResult(myIntent, BaseLink.PHONE_PICTURE);
        return file;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public String startPhotoZoom(Uri uri, int width, int height) {
        imagePath = "file://" + BaseApplication.cachePath + File.separator + "temp.jpg";
        // 裁剪图片
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // outputX outputY 是裁剪图片宽高
//		widht / height = 500 / x;
        if (width != 0 && height != 0) {
            intent.putExtra("outputX", 180);
            intent.putExtra("outputY", 180 * height / width);
        } else {
            intent.putExtra("outputX", 180);
            intent.putExtra("outputY", 180);
        }

        /*return-data 修改为 false，增加输出路径，适配不同手机（PS：为true 返回图片>1M,可能会崩溃）updated 5/2/2017 by leiyan*/
        intent.putExtra("return-data", false);
        Uri parse = Uri.parse(imagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, parse);
        activity.startActivityForResult(intent, BaseLink.ZOOM_IMAGE);
        return imagePath;
    }

    public Intent getIntentOfCroppingImage(@NonNull Context context, @NonNull Uri imageUri) {
        Intent croppingImageIntent = new Intent("com.android.camera.action.CROP");
        croppingImageIntent.setDataAndType(imageUri, "image/*");
        croppingImageIntent.putExtra("crop", "true");
        //crop into circle image
        //        croppingImageIntent.putExtra("circleCrop", "true");
        //The proportion of the crop box is 1:1
        croppingImageIntent.putExtra("aspectX", 1);
        croppingImageIntent.putExtra("aspectY", 1);
        //Crop the output image size
        croppingImageIntent.putExtra("outputX", 256);//输出的最终图片文件的尺寸, 单位是pixel
        croppingImageIntent.putExtra("outputY", 256);
        //scale selected content
        croppingImageIntent.putExtra("scale", true);
        //image type
        croppingImageIntent.putExtra("outputFormat", "JPEG");
        croppingImageIntent.putExtra("noFaceDetection", true);
        //false - don't return uri |  true - return uri
        croppingImageIntent.putExtra("return-data", true);//
        croppingImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        grantIntentAccessUriPermission(context, croppingImageIntent, imageUri);
        return croppingImageIntent;
    }

    public void cropImage(Uri uri) {
        Intent croppingImageIntent = getIntentOfCroppingImage(activity, uri);
        if (croppingImageIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(croppingImageIntent, BaseLink.ZOOM_IMAGE);
        }
    }

    public Intent getIntentOfTakingPhoto(@NonNull Context context, @NonNull Uri photoUri) {
        Intent takingPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takingPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        grantIntentAccessUriPermission(context, takingPhotoIntent, photoUri);
        return takingPhotoIntent;
    }

    public Intent getIntentOfPickingPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

    private static void grantIntentAccessUriPermission(@NonNull Context context, @NonNull Intent intent, @NonNull Uri uri) {
        if (!UtilPermission.requireSDKInt(Build.VERSION_CODES.N)) {//in pre-N devices, manually grant uri permission.
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    /**
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
