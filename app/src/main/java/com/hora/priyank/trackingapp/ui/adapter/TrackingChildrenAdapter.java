package com.hora.priyank.trackingapp.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.data.model.User;
import com.hora.priyank.trackingapp.databinding.TrackingChildrenBinding;
import com.hora.priyank.trackingapp.ui.fragment.ChildTrackingFragment;
import com.hora.priyank.trackingapp.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyank Jain on 28-09-2018.
 */
public class TrackingChildrenAdapter extends RecyclerView.Adapter<TrackingChildrenAdapter.MessageViewHolder> {
    //region Vairable Declaration
    private List<User> mUserList;
    private String parentEmail;
    private ChildTrackingFragment childTrackingFragment;
    public TrackingChildrenAdapter(ChildTrackingFragment childTrackingFragment) {
        this.childTrackingFragment = childTrackingFragment;
    }
    //endregion

    //region Set User List Data
    public void setUserList(final List<User> mUserList, String parentEmail){
        this.mUserList = mUserList;
        this.parentEmail = parentEmail;
        notifyDataSetChanged();
    }
    //endregion

    //region Create View Holder
    @Override
    public TrackingChildrenAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrackingChildrenBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.tracking_children,
                        parent,
                        false);

        return new MessageViewHolder(binding);
    }
    //endregion

    //region Bind View Holder
    @Override
    public void onBindViewHolder(TrackingChildrenAdapter.MessageViewHolder holder, int position) {
        holder.binding.setUser(mUserList.get(position));
        holder.binding.childrenName.setText(mUserList.get(position).getFirstName());
        holder.binding.lastUpdate.setText(Utility.dateTimeCoversion(mUserList.get(position).getLastUpdate())+"");
        holder.binding.deleteChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childTrackingFragment.deleteRecord(parentEmail,mUserList.get(position).getEmail(),position);
            }
        });
        holder.binding.childrenNameRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> uList = new ArrayList<User>();
                uList.add(mUserList.get(position));
                childTrackingFragment.markChildrenOnMapView(uList);
            }
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }
    //endregion

    //region Message View Holder Class
    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final TrackingChildrenBinding binding;

        public MessageViewHolder(TrackingChildrenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    //endregion
}
