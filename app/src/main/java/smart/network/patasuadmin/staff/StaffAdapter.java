package smart.network.patasuadmin.staff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import smart.network.patasuadmin.R;
import smart.network.patasuadmin.app.GlideApp;
import smart.network.patasuadmin.shop.OnShopClick;
import smart.network.patasuadmin.shop.Shop;

/**
 * Created by ravi on 16/11/17.
 */

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Staff> contactList;
    private List<Staff> contactListFiltered;
    private OnStaffClick onStaffClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, storeid, password;
        public ImageView editImg,deleteImg;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            storeid = view.findViewById(R.id.storeid);
            password = view.findViewById(R.id.password);
            editImg = (ImageView) view.findViewById(R.id.editImg);
            deleteImg = (ImageView) view.findViewById(R.id.deleteImg);


        }
    }


    public StaffAdapter(Context context, List<Staff> contactList, OnStaffClick onStaffClick) {
        this.context = context;
        this.onStaffClick=onStaffClick;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staff_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Staff contact = contactListFiltered.get(position);
        holder.name.setText("Mobile No: "+contact.getName());
        holder.password.setText("Password: "+contact.getPassword());
        holder.storeid.setText("Store:"+contact.getStoreid());

        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStaffClick.onEditClick(position);
            }
        });

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStaffClick.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Staff> filteredList = new ArrayList<>();
                    for (Staff row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String val = row.getName();
                        if (val.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Staff>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    public void notifyData(List<Staff> contactList) {
        this.contactListFiltered = contactList;
        notifyDataSetChanged();
    }
}
