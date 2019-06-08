package com.firatnet.wts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firatnet.wts.R;

public class BuyOrSellActivity extends AppCompatActivity {

    public Button buy_btn,sell_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_or_sell);
        buy_btn=findViewById(R.id.buy_btn);
        sell_btn=findViewById(R.id.sell_btn);

        
        sell_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyOrSellActivity.this, SellActivity.class);
                startActivity(intent);
            }
        });
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyOrSellActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });
    }
}
