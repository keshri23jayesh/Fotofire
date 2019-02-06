package com.example.jayesh123.fotofire.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jayesh123.fotofire.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class All_Post_Adapter extends RecyclerView.Adapter<All_Post_Adapter.ViewHolder> {

    private ArrayList<String> detail;
    private ArrayList<String> image;
    private ArrayList<String> title;
    private ArrayList<String> profile_pic;
    private ArrayList<String> user_name;
    private Context context;

    private boolean flag = false;

    public All_Post_Adapter(Context context, ArrayList<String> user_name, ArrayList<String> profile_pic, ArrayList<String> detail, ArrayList<String> image, ArrayList<String> title) {
        this.user_name = user_name;
        this.profile_pic = profile_pic;
        this.detail = detail;
        this.image = image;
        this.title = title;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post, parent, false);
        return new ViewHolder(view);
    }

    public void getContextMain(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(position<title.size() && position<detail.size() && position<image.size() && position<profile_pic.size() && position<user_name.size()){

            String username = user_name.get(position);
            holder.username.setText(username);
            String post_title = title.get(position);
            holder.title.setText(post_title);
            String post_im = image.get(position);
            getContextMain(context);
            holder.setImage(context, post_im);
            String profile_image = profile_pic.get(position);
            getContextMain(context);
            holder.setImage1(context, profile_image);

            final String post_detail = detail.get(position);
            if(post_detail != null && !post_detail.isEmpty()){
                char[] array = post_detail.toCharArray();
                if(array.length<=100){
                    holder.post_detail.setText(post_detail);
                }else{
                    String reviewStr = post_detail.substring(0,100)+" ...";
                    holder.post_detail.setText(reviewStr);
                }
            }

            holder.post_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    char[] array = post_detail.toCharArray();
                    if(array.length>100){
                        if(flag){
                            String reviewStr = post_detail.substring(0,100)+" ...";
                            holder.post_detail.setText(reviewStr);
                            flag = false;
                        }else {
                            holder.post_detail.setText(post_detail);
                            flag = true;
                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView title;
        private TextView post_detail;
        private ImageView post_im;
        private CircleImageView circleImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.blog_user_name);
            title = (TextView) itemView.findViewById(R.id.post_title);
            post_detail = (TextView) itemView.findViewById(R.id.post_details);

        }

        public void setImage(Context ctx, String img) {
            post_im = (ImageView) itemView.findViewById(R.id.blog_image);
            Picasso.get().load(img).into(post_im);
        }

        public void setImage1(Context ctx, String img) {
            circleImageView = (CircleImageView) itemView.findViewById(R.id.blog_user_image);
            Picasso.get().load(img).into(circleImageView);
        }
    }
}
