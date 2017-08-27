package com.doggo.molly.breadboard.manager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Abhishek Vadnerkar
 */

public class StorageManager {

    private static final String TAG = StorageManager.class.getSimpleName();

    public void create(Context context, String key, Object object) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fis = context.openFileOutput(key, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fis);
            oos.writeObject(object);
        } catch (Exception e) {
            Log.e(TAG, "Unable to write to local storage");
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Object read(Context context, String key) {
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(context.openFileInput(key));
            return ois.readObject();
        } catch (Exception e) {
            Log.e(TAG, "Unable to read from local storage");
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean delete(Context context, String key) {
        File storageFile = context.getFileStreamPath(key);
        return storageFile == null || !storageFile.exists() || !storageFile.isFile() || storageFile.delete();
    }
}
