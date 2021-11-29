package com.transport.mall.utils.common.fullimageview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.transport.mall.R;
import com.transport.mall.model.PhotosModel;

import java.util.ArrayList;

import static com.transport.mall.utils.AppBindingKt.xloadImages;

public class ImageAdapter extends PagerAdapter {
    Context context;
    ArrayList<PhotosModel> data;
    LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context, ArrayList<PhotosModel> data) {
        this.context = context;
        this.data = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.row_image_pager, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        xloadImages(imageView, data.get(position).getPath(), R.drawable.placeholder);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}