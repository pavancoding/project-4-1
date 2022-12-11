package com.example.reminderandtodo;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ReminderAdapter  extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{
    ArrayList<ReminderData> data;
     ReminderAdapter(   ArrayList<ReminderData> data){
        this.data=data;
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reminders_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageResource(data.get(position).getImage());
        holder.Reminder_name.setText(data.get(position).getName());
        String days = "";
        if (data.get(position).getDays().length == 7)
            days = "Daily";
        else {
            for (int i = 0; i < data.get(position).getDays().length; i++)
                if (i != data.get(position).getDays().length - 1)
                    days += data.get(position).getDays()[i] + ",    ";
                else
                    days += data.get(position).getDays()[i];
        }
        String timeleft = data.get(position).getTimeLeft();
        holder.Ring_data.setText(days + " " + timeleft);
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView img;
        Switch mswitch;
        LinearLayout long_click;
        TextView Reminder_name,Ring_data;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.logo);
            mswitch=itemView.findViewById(R.id.switch1);
            Reminder_name=itemView.findViewById(R.id.Name);
            Ring_data=itemView.findViewById(R.id.Ring_data);
            long_click=itemView.findViewById(
                    R.id.long_click
            );

            long_click.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),121,0,"Delete");
            menu.add(this.getAdapterPosition(),122,1,"Edit");
            menu.add(this.getAdapterPosition(),123,2,"Read");
            MenuItem i=menu.findItem(121);
            MenuItem i2=menu.findItem(122);
            MenuItem i3=menu.findItem(123);
            SpannableString s = new SpannableString(i.getTitle());
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(v.getContext(), R.color.error)), 0, s.length(), 0);
            i.setTitle(s);
            i.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    return true;
                }
            });
            i2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
            i3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
        }

    }
}
