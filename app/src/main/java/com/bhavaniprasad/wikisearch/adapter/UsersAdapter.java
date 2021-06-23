package com.bhavaniprasad.wikisearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bhavaniprasad.wikisearch.MainActivity;
import com.bhavaniprasad.wikisearch.R;
import com.bhavaniprasad.wikisearch.SecondActivity;
import com.bhavaniprasad.wikisearch.model.wikiUsersList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Customview> implements Filterable {
    private ArrayList<wikiUsersList> arrList;
    private Context cnt;
    private LayoutInflater layoutInflater;
    OnUsersclicklistener onclickuser;
    private ArrayList<wikiUsersList> arrListall;

    public UsersAdapter(Context context, ArrayList<wikiUsersList> userViewModels) {
        this.arrList = userViewModels;
        this.cnt = context;
        this.arrListall=new ArrayList<>(userViewModels);
    }

    @NonNull
    @Override
    public UsersAdapter.Customview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.from(parent.getContext())
                .inflate(R.layout.users_row_layout, parent, false);
        return new Customview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Customview holder, final int position) {
        final Customview customview = (Customview) holder;
        String title = arrList.get(position).getTitle();
        try {
            customview.description.setText("Description: "+arrList.get(position).getTerms().getDescription().get(0));
        } catch (Exception e) {
            customview.description.setText("----");
        }
        if (title != null)
            customview.title.setText("Title: "+arrList.get(position).getTitle());
        else
            customview.title.setText("----");
        if (arrList.get(position).getThumbnail() != null)
            Picasso.with(cnt).load(arrList.get(position).getThumbnail().getSource()).into(customview.imageView);

        customview.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onclickuser != null) {
                    onclickuser.onclickuser(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int size=0;
            size=arrList.size();
        return size;
    }

    public class Customview extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView imageView;
        CardView cardView;

        public Customview(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.usertitle);
            description = itemView.findViewById(R.id.description);
            cardView = itemView.findViewById(R.id.mobilecardview);
            imageView = itemView.findViewById(R.id.thumbnail);
//            itemView.setOnClickListener(this);
        }

    }

    public interface OnUsersclicklistener {
        public void onclickuser(int adapterposition);
    }

    public void setOnUsersClick(OnUsersclicklistener onUsersClick) {
        this.onclickuser = onUsersClick;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<wikiUsersList> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(arrListall);
            } else {
                try {
                    Log.e("sdf", "sd");
                    for (int i = 0; i < arrList.size(); i++) {
                        if (arrList.get(i).getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            List list=new ArrayList();
                            list.add(arrList.get(i));
                            filteredList.addAll(list);
                        }
                    }
                } catch (Exception e) {
                    Log.e("sdf", "except" + e);
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrList.clear();
            arrList.addAll((Collection<? extends wikiUsersList>) results.values);
            notifyDataSetChanged();

        }
    };

}
