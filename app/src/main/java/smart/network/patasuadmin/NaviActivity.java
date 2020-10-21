package smart.network.patasuadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import smart.network.patasuadmin.ad.MainActivityAd;
import smart.network.patasuadmin.cmobile.MainActivityMobile;
import smart.network.patasuadmin.contact.MainActivityContact;
import smart.network.patasuadmin.offer.OfferActivity;
import smart.network.patasuadmin.shop.MainActivity;
import smart.network.patasuadmin.staff.MainActivityStaff;
import smart.network.patasuadmin.stock.MainActivityStock;

public class NaviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);


        CardView shop = (CardView) findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivity.class);
                startActivity(io);

            }
        });

        CardView stock = (CardView) findViewById(R.id.stock);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityStock.class);
                startActivity(io);

            }
        });
        CardView staff = (CardView) findViewById(R.id.staff);
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityStaff.class);
                startActivity(io);

            }
        });


    }
}
