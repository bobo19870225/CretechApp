package cn.com.cretech.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import cn.com.cretech.BuildConfig;
import cn.com.cretech.base.BaseApplication;

import java.io.*;
import java.nio.channels.FileChannel;

public class UriUtil {

    private UriUtil() {
    }

    public static Uri getUriFromFileProvider(@NonNull File file) {
        return FileProvider.getUriForFile(BaseApplication.app,
                BuildConfig.APPLICATION_ID + ".file_provider",
                file);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Uri copy(@NonNull Context context, @NonNull Uri fromUri, @NonNull File toFile) {
        try (FileChannel source = ((FileInputStream) context.getContentResolver().openInputStream(fromUri)).getChannel();
             FileChannel destination = new FileOutputStream(toFile).getChannel()) {
            if (source != null && destination != null) {
                destination.transferFrom(source, 0, source.size());
                return UriUtil.getUriFromFileProvider(toFile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
