package com.hora.priyank.trackingapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.data.model.Event;
import com.hora.priyank.trackingapp.databinding.MessageItemBinding;
import com.hora.priyank.trackingapp.ui.activity.ChildrenActivity;

import java.util.List;



public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MessageViewHolder> {
    //region Variable Declaration
    private List<? extends Event> mEventList;
    private Context mContext;
    private Activity activity;
    public EventAdapter(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }
    //endregion

    //region Set Message List Data
    public void setMessageList(final List<? extends Event> mEventList){
        this.mEventList = mEventList;
        notifyDataSetChanged();
    }
    //endregion

    //region Create View Holder
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.message_item,
                        parent,
                        false);

        return new MessageViewHolder(binding);
    }
    //endregion

    //region Bind View Holder
    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Event event = mEventList.get(position);

        holder.binding.setMessage(mEventList.get(position));
        holder.binding.title.setText(event.getTitle());
        holder.binding.type.setText(event.getType());
        String url = "http://maps.google.com/maps/api/staticmap?center=" +
                event.getLat() +
                "," +
                event.getLon() +
                "&zoom=20&size=900x400&sensor=false" +
                "&markers=color:red|" +
                event.getLat() +
                "," +
                event.getLon() +
                "&key="+mContext.getResources().getString(R.string.google_maps_key);

        Glide.with(mContext)
                .load(url)
                .into(holder.binding.mapView);

        holder.binding.dateTime.setText(event.getCreateDate());
        holder.binding.eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildrenActivity) activity).updateEvent(event.getCreateDate());
            }
        });
        holder.binding.executePendingBindings();
    }
    @Override
    public int getItemCount() {
        return mEventList == null ? 0 : mEventList.size();
    }
    //endregion

    //region View Holder Class
    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final MessageItemBinding binding;

        public MessageViewHolder(MessageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    //endregion
}
