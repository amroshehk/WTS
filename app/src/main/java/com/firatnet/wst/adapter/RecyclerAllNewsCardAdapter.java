package com.firatnet.wst.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firatnet.wst.R;
import com.firatnet.wst.activities.PostDetailsActivity;
import com.firatnet.wst.entities.News;
import com.firatnet.wst.entities.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class RecyclerAllNewsCardAdapter extends RecyclerView.Adapter<RecyclerAllNewsCardAdapter.ViewHolder> {

    private ArrayList<News> news;
    private Context context;
    private TextView nonumbers;

    ImageLoader imageLoader = ImageLoader.getInstance();
    ImageLoaderConfiguration config;
    DisplayImageOptions options;
    public RecyclerAllNewsCardAdapter(ArrayList<News> news, Context context, TextView nonumbers) {
        this.news = news;
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

            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_news_layout,parent,false);

        return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


                holder.number_tv.setText(news.get(position).getContent());
                holder.created_tv.setText(news.get(position).getCreated_at());
            if (!news.get(position).getPic_url().isEmpty()&&!news.get(position).getPic_url().equals("null"))
                {
                    imageLoader.displayImage(news.get(position).getPic_url(), holder.image, options);
                    holder.image.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.image.setVisibility(View.GONE);
                }

               }


        @Override
        public int getItemCount() {
            return news.size();
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
                        intent.putExtra("DETAILS",news.get(position));
                        context.startActivity(intent);

//                        Snackbar.make(v,"Cliecked detedcted item on "+position,Snackbar.LENGTH_SHORT).
//                                setAction("Action",null).show();
                    }
                });
            }


        }
    }
