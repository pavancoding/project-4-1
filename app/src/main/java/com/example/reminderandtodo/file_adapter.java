package com.example.reminderandtodo;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;

import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class file_adapter extends RecyclerView.Adapter<file_adapter.ViewHolder>{
    ArrayList<set_file_data> data;
    RecyclerView r;
    TextView nofiles;
    Activity main;
    file_adapter(RecyclerView r,TextView nofiles,ArrayList<set_file_data> data,Activity main){
        this.data=data;
        this.main=main;
        this.r=r;
        this.nofiles=nofiles;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.repeat_component, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            position=holder.getAdapterPosition();
            holder.filename.setText(data.get(position).getFilename());
            holder.type.setText(data.get(position).getType());
            holder.size.setText(data.get(position).getSize());
            holder.extension.setText(data.get(position).getExtension());
            holder.logo_backgound.setCardBackgroundColor(data.get(position).getColor());
            holder.background.setCardElevation(5.0f);
        String[] bits = data.get(position).getFilename().split("[.]");
        String extension = bits[bits.length-1];
        int finalPosition = position;
        holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(main)
                            .setTitle("Delete?")
                            .setMessage("Are you sure want to delete this file?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", R.drawable.ic_baseline_delete_24, new BottomSheetMaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Toast.makeText(v.getContext(), "Deleted "+data.get(finalPosition).getFilename(), Toast.LENGTH_SHORT).show();
                                    delete(finalPosition);
                                      dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", R.drawable.ic_baseline_close_24, new BottomSheetMaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Toast.makeText(v.getContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            })
                            .build();

                    // Show Dialog
                    mBottomSheetDialog.show();
                }
            });
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
                sharingIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(v.getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(data.get(finalPosition).getPath())));
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                sharingIntent.setDataAndType( FileProvider.getUriForFile(v.getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(data.get(finalPosition).getPath())),MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
                sharingIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "View using"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return  data.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView filename,type,size,extension;
        CardView logo_backgound;
        MaterialCardView background;
        ImageView delete,send;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filename=itemView.findViewById(R.id.File_name);
            type=itemView.findViewById(R.id.File_type);
            size=itemView.findViewById(R.id.File_size);
            extension=itemView.findViewById(R.id.Logo);
            logo_backgound=itemView.findViewById(R.id.Logo_background);
            background=itemView.findViewById(R.id.card_background);
            delete=itemView.findViewById(R.id.delete);
            send=itemView.findViewById(R.id.send);
        }
    }
    public void delete(int position){
        new File(data.get(position).getPath()).delete();
            data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
        if(data.size()==0){
            nofiles.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);
        }
    }
}
