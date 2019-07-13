package com.firatnet.wts.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firatnet.wts.R;
import com.firatnet.wts.activities.EditSellPostActivity;
import com.firatnet.wts.activities.PostDetailsActivity;
import com.firatnet.wts.classes.StaticMethod;
import com.firatnet.wts.entities.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.firatnet.wts.classes.JsonTAG.TAG_DESCRIPTION;
import static com.firatnet.wts.classes.JsonTAG.TAG_ID;
import static com.firatnet.wts.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wts.classes.JsonTAG.TAG_TITLE;
import static com.firatnet.wts.classes.URLTAG.DELETE_POSt_URL;


public class RecyclerAllPostsCardAdapter extends RecyclerView.Adapter<RecyclerAllPostsCardAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private Context context;
    private TextView nonumbers;

    ImageLoader imageLoader = ImageLoader.getInstance();
    ImageLoaderConfiguration config;
    DisplayImageOptions options;
    public RecyclerAllPostsCardAdapter(ArrayList<Post> posts, Context context, TextView nonumbers) {
        this.posts = posts;
        this.context=context;
        this.nonumbers=nonumbers;
        config = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

    @NonNull
    @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_buy_posts_layout,parent,false);

        return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


                holder.number_tv.setText(posts.get(position).getTitle());
                holder.created_tv.setText(posts.get(position).getCreated_date());
            if (!posts.get(position).getPost_image_url().isEmpty()&&!posts.get(position).getPost_image_url().equals("null"))
                {
                    imageLoader.displayImage(posts.get(position).getPost_image_url(), holder.image, options);
                    holder.image.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.image.setVisibility(View.GONE);
                }

               }


        @Override
        public int getItemCount() {
            return posts.size();
        }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

        class ViewHolder extends  RecyclerView.ViewHolder{
            private ProgressDialog progressDialog;
            TextView number_tv;
            TextView created_tv;
            ImageView image;

            @SuppressLint("SetTextI18n")
            ViewHolder(final View itemView) {
                super(itemView);
                number_tv = itemView.findViewById(R.id.number_tv);
                created_tv = itemView.findViewById(R.id.created_date_tv);
                image = itemView.findViewById(R.id.image);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        Intent intent=new Intent(context, PostDetailsActivity.class);
                        intent.putExtra("DETAILS",posts.get(position));
                        context.startActivity(intent);

//                        Snackbar.make(v,"Cliecked detedcted item on "+position,Snackbar.LENGTH_SHORT).
//                                setAction("Action",null).show();
                    }
                });
            }


        }
    }
