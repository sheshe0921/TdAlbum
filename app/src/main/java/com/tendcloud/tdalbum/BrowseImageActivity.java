package com.tendcloud.tdalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.tendcloud.tdalbum.browse.BasePagerAdapter;
import com.tendcloud.tdalbum.browse.GalleryViewPager;

import java.util.ArrayList;

public class BrowseImageActivity extends Activity {

    public static final String IMAGE_INDEX = "image_index";
    public static final String IMAGE_URLS = "image_urls";

    public static final String CURRENT_POSITION = "current_position";

    GalleryViewPager mViewPager;
    TextView imageCount;

    private int imageLength;

    private int currentPosition;

    ArrayList<String> picturePaths;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_image);
        int pagerPosition = getIntent().getIntExtra(IMAGE_INDEX, 0);
         picturePaths = getIntent().getStringArrayListExtra(IMAGE_URLS);

        imageLength = picturePaths.size();

        imageCount = (TextView) findViewById(R.id.image_count);

        BasePagerAdapter pagerAdapter = new BasePagerAdapter(this, picturePaths);// picture path

        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int index) {
                // update progress
                currentPosition = index;
                imageCount.setText((currentPosition + 1) + "/" + imageLength);
           //     Toast.makeText(BrowseImageActivity.this,picturePaths.get(currentPosition),Toast.LENGTH_SHORT).show();
                Toast.makeText(BrowseImageActivity.this,getlocation(picturePaths.get(currentPosition))+"", Toast.LENGTH_LONG).show();
            }
        });

        mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(pagerPosition);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            setResult();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_POSITION, currentPosition);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    public  String getlocation(String path){
        float output1 = 0;
        float output2 = 0;
        @SuppressWarnings("unused")
        String context ;
        Location location;
        String photoTime = null;
        try {
            ExifInterface exifInterface=new ExifInterface(path);
            String latValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latRef   = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lngValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String lngRef   = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
             photoTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
                try {
                    output1 = convertRationalLatLonToFloat(latValue, latRef);
                    output2= convertRationalLatLonToFloat(lngValue, lngRef);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = Context.LOCATION_SERVICE;
        location=new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(output1);
        location.setLongitude(output2);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        //    double[] f={lat,lng};
        return "拍摄经纬度："+lat+"，"+lng+";拍摄时间："+photoTime;
    }

    private static float convertRationalLatLonToFloat(
            String rationalString, String ref) {
        try {
            String [] parts = rationalString.split(",");

            String [] pair;
            pair = parts[0].split("/");
            double degrees = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            pair = parts[1].split("/");
            double minutes = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            pair = parts[2].split("/");
            double seconds = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
            if ((ref.equals("S") || ref.equals("W"))) {
                return (float) -result;
            }
            return (float) result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

}
