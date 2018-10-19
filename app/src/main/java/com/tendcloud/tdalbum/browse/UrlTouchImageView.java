package com.tendcloud.tdalbum.browse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tendcloud.tdalbum.R;
import com.tendcloud.tdalbum.util.ImageLoader;

public class UrlTouchImageView extends RelativeLayout {
    // protected ProgressBar mProgressBar;//进度条
    protected TouchImageView mImageView;

    protected Context mContext;

    public UrlTouchImageView(Context ctx) {
        super(ctx);
        mContext = ctx;
        init();

    }

    public UrlTouchImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }

    public TouchImageView getImageView() {
        return mImageView;
    }

    protected void init() {
        mImageView = new TouchImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        mImageView.setVisibility(GONE);

    }

    public void setUrl(String path) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.load);
        mImageView.setImageBitmap(bitmap);
        if (path != null) {
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(path, mImageView);
        }
        mImageView.setVisibility(VISIBLE);

    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mImageView.setScaleType(scaleType);

    }

}
