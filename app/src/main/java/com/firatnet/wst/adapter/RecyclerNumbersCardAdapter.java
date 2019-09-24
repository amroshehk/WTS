package com.firatnet.wst.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.firatnet.wst.R;
import com.firatnet.wst.classes.PreferenceHelper;
import com.firatnet.wst.database.SafetyDbHelper;
import com.firatnet.wst.entities.Phone;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.firatnet.wst.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wst.classes.JsonTAG.TAG_NUMBER;
import static com.firatnet.wst.classes.JsonTAG.TAG_STUDENT_ID;
import static com.firatnet.wst.classes.URLTAG.DELETE_PHONE_URL;


public class RecyclerNumbersCardAdapter extends RecyclerView.Adapter<RecyclerNumbersCardAdapter.ViewHolder> {

    private ArrayList<Phone> phones;
    private Context context;
    private TextView nonumbers;

    public RecyclerNumbersCardAdapter(ArrayList<Phone> phones, Context context,TextView nonumbers) {
        this.phones = phones;
        this.context=context;
        this.nonumbers=nonumbers;

    }

    @NonNull
    @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_phone_number_layout,parent,false);

        return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


                holder.number_tv.setText(phones.get(position).getNumber());

               }


        @Override
        public int getItemCount() {
            return phones.size();
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

            TextView number_tv;
            ImageView delete;
            Dialog dialog2;
            Button cancel;
            Button ensure;
            TextView item_name;
            ProgressDialog progressDialog;
            @SuppressLint("SetTextI18n")
            ViewHolder(final View itemView)
            {
                super(itemView);
                number_tv=itemView.findViewById(R.id.number_tv);
                delete=itemView.findViewById(R.id.deleterow);

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


            }

            void removeAt(int position) {

                SafetyDbHelper dh = new SafetyDbHelper(context);
                dh.deletePhone(phones.get(position));

                deletePhoneNumber(phones.get(position).getNumber());
                phones.remove(position);


                notifyItemRemoved(position);

                notifyItemRangeChanged(0, phones.size());

                if(phones.size()==0)
                {
                    nonumbers.setVisibility(View.VISIBLE);
                }
            }


            private void deletePhoneNumber(final String num) {

                final PreferenceHelper helper = new PreferenceHelper(context);
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("delete number");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                StringRequest request = new StringRequest(Request.Method.POST, DELETE_PHONE_URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            progressDialog.dismiss();
                            //Toast.makeText(getBaseContext(), "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, obj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                        params.put(TAG_STUDENT_ID, helper.getSettingValueId());
                        params.put(TAG_NUMBER, num);


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
