package pro.network.villagemartadmin;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;

import pro.network.villagemartadmin.banner.MainActivityBanner;
import pro.network.villagemartadmin.order.MainActivityOrder;
import pro.network.villagemartadmin.product.MainActivityProduct;

public class NaviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);




        CardView stock = (CardView) findViewById(R.id.stock);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityProduct.class);
                startActivity(io);

            }
        });
        CardView banner = (CardView) findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityBanner.class);
                startActivity(io);

            }
        });

        CardView order = (CardView) findViewById(R.id.orders);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityOrder.class);
                startActivity(io);

            }
        });

    }
}
