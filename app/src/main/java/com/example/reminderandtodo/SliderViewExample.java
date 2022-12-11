package com.example.reminderandtodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;

import pl.droidsonroids.gif.GifImageView;

public class SliderViewExample extends SliderViewAdapter<SliderViewExample.SliderAdapterVH>{
    int images[];
    String data[];
    SliderViewExample(int images[],String data[]){
        this.images=images;
        this.data=data;
    }
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slideritem,parent, false);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
                viewHolder.im.setImageResource(images[position]);
                viewHolder.tv.setText(data[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder{
        GifImageView im;
        TextView tv;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            im=itemView.findViewById(R.id.Image_view);
            tv=itemView.findViewById(R.id.textView2);
        }
    }
}
