package smart.network.patasuadmin.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import smart.network.patasuadmin.R;
import smart.network.patasuadmin.app.GlideApp;
import de.hdodenhof.circleimageview.CircleImageView;


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private List<Shop> moviesList;
    private Context context;
    private OnShopClick onShopClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shopName, contact,area;
        private ImageView editImg, deleteImg;

        public MyViewHolder(View view) {
            super(view);
            contact = (TextView) view.findViewById(R.id.contact);
            area=view.findViewById(R.id.area);
            shopName = (TextView) view.findViewById(R.id.shopName);
            editImg = (ImageView) view.findViewById(R.id.editImg);
            deleteImg = (ImageView) view.findViewById(R.id.deleteImg);


        }
    }


    public ShopAdapter(List<Shop> moviesList, Context context, OnShopClick onShopClick) {


        this.moviesList = moviesList;
        this.context = context;
        this.onShopClick = onShopClick;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Shop shop = moviesList.get(position);
        holder.contact.setText(shop.getContact());
        holder.shopName.setText(shop.getShopname());
        holder.area.setText(shop.getAddress());



        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShopClick.onEditClick(position);
            }
        });

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShopClick.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void notifyData(List<Shop> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }


}
