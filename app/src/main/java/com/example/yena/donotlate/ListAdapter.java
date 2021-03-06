package com.example.yena.donotlate;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yena on 2015-11-10.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ListData> items;
    int itemLayout;
    private static final int COMPLETED = 1, NOT_COMPLETED = 0;

    public ListAdapter(Context context, List<ListData> items, int itemLayout){
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
    }


    public class NotCompleteViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        Button btStart, btMore;


        public NotCompleteViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView)itemView.findViewById(R.id.list_title);
            this.btMore = (Button)itemView.findViewById(R.id.bt_more);
            this.btStart = (Button)itemView.findViewById(R.id.bt_start);
        }

    }
    public class CompleteViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        Button  btMore;
        ImageView successImage;


        public CompleteViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView)itemView.findViewById(R.id.list_title);
            this.btMore = (Button)itemView.findViewById(R.id.bt_more);
            this.successImage = (ImageView)itemView.findViewById(R.id.check_icon);
        }

    }
    @TargetApi(21)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ListData item = items.get(position);
        /////////////////////// 여기는 안끝난거 뷰홀더

        if(holder instanceof NotCompleteViewHolder){
            final NotCompleteViewHolder tempHolder = (NotCompleteViewHolder) holder;
            tempHolder.title.setText(item.title);

            if(position == 0 && !(item.isStarted)){
                tempHolder.btStart.setText("시작!");
                tempHolder.btStart.setEnabled(true);
                tempHolder.btStart.setBackgroundResource(R.drawable.bt_basic);

                tempHolder.btStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YenaDAO.startDataUpdate(context,item);
                        context.startService(new Intent(context, GpsService.class));
                        tempHolder.btStart.setText("시작했음!");
                        tempHolder.btStart.setEnabled(false);
                        tempHolder.btStart.setBackgroundResource(R.drawable.bt_enabled);
                        for(int i = 1; i< items.size(); i++){
                            if(items.get(i).isStarted){
                                YenaDAO.notStartDataUpdate(context, items.get(i));
                            }
                        }

                    }
                });
            }
            else if(position == 0 && item.isStarted){
                tempHolder.btStart.setText("시작했음!");
                tempHolder.btStart.setEnabled(false);
                tempHolder.btStart.setBackgroundResource(R.drawable.bt_enabled);
            }
            else{
                tempHolder.btStart.setBackgroundResource(R.drawable.bt_enabled);
            }

            tempHolder.btMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),MoreActivity.class);
                    intent.putExtra("data", item);
                    v.getContext().startActivity(intent);
                }
            });
        }
        ///////////////////// 여기는 끝난거 뷰홀더

        else if(holder instanceof CompleteViewHolder){
            CompleteViewHolder tempHolder = (CompleteViewHolder) holder;
            tempHolder.title.setText(item.title);
            if(!item.isSuccess){
                tempHolder.successImage.setImageDrawable(context.getDrawable(R.drawable.ic_x));
            } else{
                tempHolder.successImage.setImageDrawable(context.getDrawable(R.drawable.ic_check));
            }

            tempHolder.btMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),MoreActivity.class);
                    intent.putExtra("data", item);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == COMPLETED){
            return new CompleteViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_appointment_list,parent,false));
        }
        else if(viewType == NOT_COMPLETED){
            return new NotCompleteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.current_appointment_list,parent,false));
        }
        return new NotCompleteViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on positionNotCompleteViewHolder
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (items.get(position).isComplete) {
            return COMPLETED;
        }
        return NOT_COMPLETED;
    }
}
