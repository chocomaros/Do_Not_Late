package com.example.yena.donotlate;

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
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    Context context;
    List<ListData> items;
    int itemLayout;

    public ListAdapter(Context context, List<ListData> items, int itemLayout){
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
    }


public class ViewHolder extends RecyclerView.ViewHolder{

    TextView title;
    Button btStart, btMore;


    public ViewHolder(View itemView) {
        super(itemView);
        this.title = (TextView)itemView.findViewById(R.id.list_title);
        this.btMore = (Button)itemView.findViewById(R.id.bt_more);
        this.btStart = (Button)itemView.findViewById(R.id.bt_start);
    }

}
    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        final ListData item = items.get(position);
        holder.title.setText(item.getTitle());

        holder.btMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MoreActivity.class);
                intent.putExtra("data", item);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ViewHolder(view);
    }
}
