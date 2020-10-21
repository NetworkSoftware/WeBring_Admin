package smart.network.patasuadmin.cmobile;

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


/**
 * Created by ravi on 16/11/17.
 */

public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Cmobile> contactList;
    private List<Cmobile> contactListFiltered;
    private StaffAdapterListener listener;

    public MobileAdapter(Context context, List<Cmobile> contactList, StaffAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mobile_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Cmobile contact = contactListFiltered.get(position);
        holder.name.setText(contact.getCustomerName());
        holder.phoneName.setText(contact.getName());
        holder.staffName.setText(contact.getStaffName());
        if (contact.image.size() > 1) {
            GlideApp.with(context)
                    .load(contact.image.get(1))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.profile)
                    .into(holder.thumbnail);
        }
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
                    List<Cmobile> filteredList = new ArrayList<>();
                    for (Cmobile row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String val = row.getCustomerContact() + " " + row.getImei();
                        if (row.getCustomerContact().toLowerCase().contains(charString.toLowerCase())
                                || row.getImei().toLowerCase().contains(charString.toLowerCase())
                                || row.getStaffName().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<Cmobile>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface StaffAdapterListener {
        void onStaffSelected(Cmobile contact);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phoneName, staffName;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phoneName = view.findViewById(R.id.phoneName);
            staffName = view.findViewById(R.id.staffName);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onStaffSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}
