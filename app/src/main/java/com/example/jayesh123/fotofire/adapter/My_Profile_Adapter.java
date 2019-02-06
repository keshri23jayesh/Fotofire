package com.example.jayesh123.fotofire.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jayesh123.fotofire.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class My_Profile_Adapter extends RecyclerView.Adapter<My_Profile_Adapter.ViewHolder> {

    private ArrayList<String> image;
    private String profile_pic;
    private String user_name;
    private int i = 0;

    Context context;


    public My_Profile_Adapter(Context context, String user_name, String profile_pic, ArrayList<String> image) {
        this.user_name = user_name;
        this.profile_pic = profile_pic;
        this.image = image;
    }

    @NonNull
    @Override
    public My_Profile_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_profile_single_image, parent, false);
        return new My_Profile_Adapter.ViewHolder(view);
    }

    public void getContextMain(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull My_Profile_Adapter.ViewHolder holder, int position) {
        String post_im = image.get(position);
        getContextMain(context);
        holder.setImage(context, post_im);
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView post_im;

        private ImageView profile_pi;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setImage(Context ctx, String img) {
            post_im = (ImageView) itemView.findViewById(R.id.single_post_icon);
            Picasso.get().load(img).into(post_im);
//            if(i == 0)
//            {
//                profile_pi = (ImageView) itemView.findViewById(R.id.blog_user_image);
//                Picasso.get().load(profile_pic).into(profile_pi);
//                i++;
//            }
        }

    }
}
