package com.tukuri.ics.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tukuri.ics.R;

public class ImageAdapter extends PagerAdapter {
    Context mContext;

    public ImageAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return sliderImageId.length;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(sliderImageId[position]);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;     }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((ImageView) object);

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((ImageView) o);

    }
    private int[] sliderImageId = new int[]{
            R.drawable.app_icon,
            R.drawable.app_icon,

    };

}

