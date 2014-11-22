package com.tagdroid.android.ImageLoader;

import android.content.Context;

import java.io.File;

public class FileCache {
    private File cacheDir;

    public FileCache(Context context, String dirName) {
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(context.getExternalCacheDir(), dirName);
        else
            cacheDir = new File(context.getCacheDir(),         dirName);
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = context.getExternalCacheDir();
        else
            cacheDir = context.getCacheDir();

        assert cacheDir != null;
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        return new File(cacheDir, filename);
    }
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        if (files == null)
            return;
        for(File f:files)
            f.delete();
    }
}