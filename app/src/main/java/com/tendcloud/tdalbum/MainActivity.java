package com.tendcloud.tdalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.tendcloud.tdalbum.browse.SelectResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_UP_INTENT = 100;
    private static final String TAG="TD";
    ArrayList<String> dataList;
    SelectResultAdapter selectResultAdapter;
    GridView imgGridView;
    private static final int PERMISSION_REQUESTS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }


        Button button = (Button) findViewById(R.id.all_picture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PictureFolderActivity.MAX_SIZE, 3);
                intent.setClass(MainActivity.this, PictureFolderActivity.class);
                startActivityForResult(intent, PICK_UP_INTENT);
            }
        });

        dataList = new ArrayList<>();

        imgGridView = (GridView) findViewById(R.id.all_picture_gridView);
        imgGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrowse(position);
            }
        });

        selectResultAdapter = new SelectResultAdapter(this, dataList);
        imgGridView.setAdapter(selectResultAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_UP_INTENT) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                ArrayList<String> temp = bundle.getStringArrayList(PictureActivity.SELECT_PICTURE_PATH);
                if (temp != null) {
                    dataList.addAll(temp);
                }
                selectResultAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void imageBrowse(int position) {
        Intent intent = new Intent(this, BrowseImageActivity.class);
        // 图片url,一般从数据库中或网络中获取
        intent.putExtra(BrowseImageActivity.IMAGE_URLS, dataList);
        intent.putExtra(BrowseImageActivity.IMAGE_INDEX, position);
        startActivity(intent);
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }


}


