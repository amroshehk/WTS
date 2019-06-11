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
import com.firatnet.wts.database.SafetyDbHelper;
import com.firatnet.wts.entities.Phone;
import com.firatnet.wts.entities.Post;

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
import static com.firatnet.wts.classes.URLTAG.EDIT_POSt_URL;


public class RecyclerMyPostsCardAdapter extends RecyclerView.Adapter<RecyclerMyPostsCardAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private Context context;
    private TextView nonumbers;

    public RecyclerMyPostsCardAdapter(ArrayList<Post> posts, Context context, TextView nonumbers) {
        this.posts = posts;
        this.context=context;
        this.nonumbers=nonumbers;

    }

    @NonNull
    @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_posts_layout,parent,false);

        return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


                holder.number_tv.setText(posts.get(position).getTitle());
                holder.created_tv.setText(posts.get(position).getCreated_date());

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
            ImageView delete;
            ImageView edit;
            Dialog dialog2;
            Button cancel;
            Button ensure;
            TextView item_name;

            @SuppressLint("SetTextI18n")
            ViewHolder(final View itemView)
            {
                super(itemView);
                number_tv=itemView.findViewById(R.id.number_tv);
                created_tv=itemView.findViewById(R.id.created_date_tv);
                delete=itemView.findViewById(R.id.deleterow);
                edit=itemView.findViewById(R.id.editrow);

                dialog2 = new Dialog(context);
                Objects.requireNonNull(dialog2.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.delete_dialog_layout);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                cancel =  dialog2.findViewById(R.id.cancel);
                ensure =  dialog2.findViewById(R.id.ensure);
                item_name =  dialog2.findViewById(R.id.item_name);
                item_name.setText("Delete Number?");
                // if button is clicked, close the custom dialog
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });


                // if button is clicked, close the custom dialog
                ensure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        Toast.makeText(context,"Successfully deleted", Toast.LENGTH_SHORT).show();
                        removeAt(position);
                        dialog2.dismiss();
                    }
                });

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



                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog2.show();

                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position=getAdapterPosition();
                        Intent intent=new Intent(context, EditSellPostActivity.class);
                        intent.putExtra("EDIT",posts.get(position));
                        context.startActivity(intent);


                    }
                });


            }

            void removeAt(int position) {


                if (StaticMethod.ConnectChecked(context)) {
                    DeletePostServer(posts.get(position),position);

                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }


            private void DeletePostServer(final Post post, final int position) {

                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Waiting.....");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                StringRequest request = new StringRequest(Request.Method.POST, DELETE_POSt_URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            progressDialog.dismiss();

                            if (obj.getString(TAG_MESSAGE).equals("Post deleted successfully")) {
                                posts.remove(position);
                                notifyItemRemoved(position);

                                notifyItemRangeChanged(0, posts.size());

                                if(posts.size()==0)
                                {
                                    nonumbers.setVisibility(View.VISIBLE);
                                }

                                Toast.makeText(context, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "The student is not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                progressDialog.dismiss();

                                switch (volleyError.networkResponse.statusCode) {

                                    case 400:
                                        Toast.makeText(context,
                                                "here is validation errors",
                                                Toast.LENGTH_LONG).show();
                                        break;

                                    case 401:
                                        Toast.makeText(context,
                                                "here is validation errors",
                                                Toast.LENGTH_LONG).show();
                                        break;

                                    case 402:
                                        Toast.makeText(context,
                                                "There is validation errors",
                                                Toast.LENGTH_LONG).show();
                                        break;

                                    case 404:
                                        Toast.makeText(context,
                                                "Couldn't reach the server",
                                                Toast.LENGTH_LONG).show();
                                        break;

                                }

                            }
                        }) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        params.put("Content-Type", "application/json; charset=utf-8");
                        params.put(TAG_ID, String.valueOf(post.getId()));
                        params.put(TAG_TITLE, post.getTitle());
                        params.put(TAG_DESCRIPTION, post.getDescription());


                        return params;

                    }


                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                request.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(request);
            }



        }
    }