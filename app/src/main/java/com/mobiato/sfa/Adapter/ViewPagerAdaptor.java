package com.mobiato.sfa.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mobiato.sfa.R;
import com.mobiato.sfa.utils.PhotoFullPopupWindow;

import java.util.ArrayList;

public class ViewPagerAdaptor extends PagerAdapter {
    float cornerRadius = 50.0f;
    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner};
    private Bitmap mBitmap;
    private Resources mResources;
    ArrayList<String> arrData;

    public ViewPagerAdaptor(Context context, ArrayList<String> arrData) {
        this.context = context;
        this.arrData = arrData;
    }

    @Override
    public int getCount() {
        return arrData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_recommendedImage);
        Glide.with(context).load(arrData.get(position)).into(imageView);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, arrData.get(position), null);
            }
        });
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}