package com.tendcloud.tdalbum.pickup;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class PicturePickUp {

    private Context context;

    public PicturePickUp(Context context) {
        this.context = context;
    }

    /**
     * get all picture path
     */
    private ArrayList<String> listAllDir() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri uri = intent.getData();
        ArrayList<String> list = new ArrayList<>();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(0);
                    list.add(new File(path).getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }

    /**
     * get all picture folder
     */
    public List<FileTraversal> LocalImgFileList() {
        List<FileTraversal> data = new ArrayList<>();
        String filename;
        List<String> allImgList = listAllDir();
        List<String> resultList = new ArrayList<>();
        if (allImgList != null) {
            @SuppressWarnings("rawtypes")
            Set set = new TreeSet();
            String[] str;
            for (int i = 0; i < allImgList.size(); i++) {
                resultList.add(getFileInfo(allImgList.get(i)));
            }
            for (int i = 0; i < resultList.size(); i++) {
                set.add(resultList.get(i));
            }
            str = (String[]) set.toArray(new String[0]);
            for (int i = 0; i < str.length; i++) {
                filename = str[i];
                FileTraversal ftl = new FileTraversal();
                ftl.fileName = filename;
                data.add(ftl);// 设置文件名
            }

            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < allImgList.size(); j++) {
                    if (data.get(i).fileName.equals(getFileInfo(allImgList
                            .get(j)))) {
                        data.get(i).fileContent.add(allImgList.get(j));// 文件夹所有图片路径
                    }
                }
            }
        }
        return data;
    }

    /**
     * get folder info
     *
     * @param data
     * @return
     */
    private String getFileInfo(String data) {
        String filename[] = data.split("/");
        return filename[filename.length - 2];
    }

}
