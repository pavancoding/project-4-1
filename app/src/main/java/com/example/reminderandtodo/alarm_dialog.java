package com.example.reminderandtodo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import taimoor.sultani.sweetalert2.Sweetalert;

public class alarm_dialog extends Reminder {
    Context c ;
    Context application;
    ChipGroup tags_display;
    ContentResolver resolver;
    TextInputLayout Tags_editText;
    TextInputLayout time;
    String playtone;
    TextInputLayout datepicker;
    TextInputLayout Name;
    RecyclerView files;
    ImageView done,close;
    Switch vibration;
    Chip ring_once,custom;
    TextInputLayout description;
    ChipGroup days;
    Button addfiles;
    String directoryname;
    ArrayList<set_file_data> data;
    TextView nofiles;
    Chip date;
    ChipGroup types;
    file_adapter adapter;
    MaterialRadioButton low,high;
    Uri uri;
    TextView ringtone;
    FragmentManager manager;
    ArrayList<String> tags;
    CompoundButton previousCheckedCompoundButton = null;
    DatabaseReference ref;

        alarm_dialog(Context c,FragmentManager manager,ContentResolver resolver,Context application){
        this.c=c;
        this.manager=manager;
        this.resolver=resolver;
        this.application=application;
    }
    Chip createchip(String data){
        Chip chip=new Chip(c);
        chip.setText(data);
        tags.add(data);
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tags.remove(data);
                Log.d("key",tags.toString());
                tags_display.removeView((Chip)v);
            }
        });
        return chip;
    }
    void getRingtone(String data,String song){
        ringtone.setText(data);
        this.playtone=song;
    }
    void showbottomsheet(){
        BottomSheetDialog dialog=new BottomSheetDialog(c);
        BottomSheetBehavior<View> behavior;
        RelativeLayout ringtone_selection;
        ref= FirebaseDatabase.getInstance().getReference();
        tags=new ArrayList<String>();
        View bottomesheetview= LayoutInflater.from(c).inflate(R.layout.reminder_alarm_data, null);
        dialog.setContentView(bottomesheetview);

        ringtone_selection=dialog.findViewById(R.id.ringtone_selection);
        Name=dialog.findViewById(R.id.Name);
        vibration=dialog.findViewById(R.id.switch1);
        description=dialog.findViewById(R.id.Description);
        ringtone=dialog.findViewById(R.id.ringtone_name);
        low=dialog.findViewById(R.id.low);
        high=dialog.findViewById(R.id.high);
        tags_display=dialog.findViewById(R.id.tags);
        Tags_editText=dialog.findViewById(R.id.tags_editText);
        types=dialog.findViewById(R.id.type);
        ring_once=dialog.findViewById(R.id.ringonce);
        custom=dialog.findViewById(R.id.custom);
        date=dialog.findViewById(R.id.date);
       days=dialog.findViewById(R.id.days);
        datepicker=dialog.findViewById(R.id.Date_picker);
        files=dialog.findViewById(R.id.files);
        addfiles=dialog.findViewById(R.id.add_files);
        directoryname="reminder1";
        nofiles=dialog.findViewById(R.id.no_files);
        done=dialog.findViewById(R.id.done);
        close=dialog.findViewById(R.id.close);
        time=dialog.findViewById(R.id.time);

        ringtone_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, audio.class);
                ((Activity)c).startActivityForResult(i, 1);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sweetalert pDialog = new Sweetalert(c, Sweetalert.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Please Wait");
                pDialog.setCancelable(false);
                if( validate()==true){

                    pDialog.show();
                    sendDataToFireBase();
                    pDialog.cancel();
                }

            }
        });
        CompoundButton.OnCheckedChangeListener onRadioButtonCheckedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) return;
                if (previousCheckedCompoundButton != null) {
                    previousCheckedCompoundButton.setChecked(false);
                    previousCheckedCompoundButton = buttonView;
                } else {
                    previousCheckedCompoundButton = buttonView;
                }
            }
        };
        low.setOnCheckedChangeListener(onRadioButtonCheckedListener);
        high.setOnCheckedChangeListener(onRadioButtonCheckedListener);

        Tags_editText.getEditText().setCursorVisible(false);
        Tags_editText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence,int start, int before, int count) {
                //Toast.makeText(application, String.valueOf(charSequence.length())+" "+count, Toast.LENGTH_SHORT).show();
                if(charSequence.length()>0 && String.valueOf(charSequence.charAt(charSequence.length()-1)).equals("#")){
                    int i=charSequence.length()-2;
                    while(i>=0){
                        if(String.valueOf(charSequence.charAt(i)).equals("#")){
                            break;
                        }
                        i--;
                    }
                    if(i!=-1){
                        String data=charSequence.subSequence(i, charSequence.length()-1).toString();
                        tags_display.addView(createchip(data));
                        String value=charSequence.toString();
                        //   value.replace(data,"");
                        if(i==0){
                            Tags_editText.getEditText().setText("#");
                            Tags_editText.getEditText().setSelection(1);
                        }

                        else{
                            Tags_editText.getEditText().setText(charSequence.subSequence(0, i)+"#");
                            Tags_editText.getEditText().setSelection(i+1);
                        }

                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        days.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

            }
        });


        types.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if(ring_once.isChecked()){
                    days.setVisibility(View.GONE);
                    datepicker.setVisibility(View.GONE);
                }
                if(custom.isChecked()){
                    days.setVisibility(View.VISIBLE);
                    datepicker.setVisibility(View.GONE);
                }
                if(date.isChecked()){
                    days.setVisibility(View.GONE);
                    datepicker.setVisibility(View.VISIBLE);
                }
            }
        });

        datepicker.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    displaycalender();
                }
            }
        });
        datepicker.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaycalender();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        behavior=BottomSheetBehavior.from((View)bottomesheetview.getParent());
        behavior.setState(behavior.STATE_EXPANDED);
        behavior.setDraggable(false);
        ConstraintLayout layout=dialog.findViewById(R.id.bottomsheet_data);
        assert layout!=null;
        time.getEditText().setInputType(0);
        datepicker.getEditText().setInputType(0);
        time.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaytime();
            }
        });
        time.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true) {
                    displaytime();
                }
            }
        });
        /*File*/




        createDirectory(directoryname);


        data=new ArrayList<set_file_data>();
        adapter= new file_adapter(files,nofiles,data,((Activity) c));

        files.setHasFixedSize(true);
        files.setLayoutManager(new LinearLayoutManager(c));
        files.setAdapter(adapter);
        addfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                ((Activity)c).startActivityForResult(chooseFile, 100);
            }
        });
        /*file end*/

        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        dialog.show();
    }
    boolean validate(){
            if(Name.getEditText().getText().length()==0)
            {
                showErrorToast("Fill Name");
                return false;
            }
            if(time.getEditText().getText().length()==0)
            {
                showErrorToast("Pick Time");
                return false;
            }
            if(custom.isChecked()){
                if(days.getCheckedChipIds().size()==0){
                    showErrorToast("Pick Day");
                    return false;
                }
            }
            if(date.isChecked()){
                if(datepicker.getEditText().getText().length()==0){
                    showErrorToast("Pick Date");
                    return false;
                }
            }
            return true;
    }
    void showErrorToast(String data){
        FancyToast.makeText(c,data,FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
    }
    void showSuccessToast(String data){
        FancyToast.makeText(c,data,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
    }
    //firebasecode

    void readDatafromFirebase(){

    }
    boolean sendDataToFireBase(){
        Map<String,String> data=new HashMap<>();
        data.put("Name",Name.getEditText().getText().toString());
        data.put("Description",description.getEditText().getText().toString());
        data.put("Time",time.getEditText().getText().toString());
        data.put("Ring_Once",String.valueOf(ring_once.isChecked()));
        data.put("Custom",String.valueOf(custom.isChecked()));
        data.put("Date",String.valueOf(date.isChecked()));
        List<Integer> id=this.days.getCheckedChipIds();
        String days="";
        for(int i=0;i<id.size();i++){
            if(id.get(i)==R.id.Monday)
                days+="M,";
            if(id.get(i)==R.id.Tuesday)
                days+="T,";
            if(id.get(i)==R.id.Wednessday)
                days+="W,";
            if(id.get(i)==R.id.Thursday)
                days+="Tu,";
            if(id.get(i)==R.id.Friday)
                days+="F,";
            if(id.get(i)==R.id.Saturday)
                days+="S,";
            if(id.get(i)==R.id.Sunday)
                days+="Su,";
        }
        data.put("days",days);
        data.put("date_field",datepicker.getEditText().getText().toString());
        String tags_data = "";
        for(int i=0;i<tags.size();i++)
            tags_data+=tags.get(i)+",";
        data.put("tags",tags_data);
        data.put("Priority",String.valueOf(low.isChecked()));
        data.put("Ringtone",ringtone.getText().toString());
        data.put("playtone",playtone);
        data.put("Vibration",String.valueOf(vibration.isChecked()));
        String filenames="",extensions="",size="",path="";
        for(int i=0;i<this.data.size();i++){
            filenames+=this.data.get(i).getFilename()+",";
            extensions+=this.data.get(i).getExtension()+",";
            size+=this.data.get(i).getSize()+",";
            path+=this.data.get(i).getPath()+",";

        }
        data.put("filenames",filenames);
        data.put("extensions",extensions);
        data.put("size",size);
        data.put("path",path);
        Log.d("data",data.toString());

            return true;
    }


    void displaycalender(){
        MaterialDatePicker.Builder builder= MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(calendarConstraintBuilder.build());
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        builder.setTitleText("SELECT DATE");
        builder.setSelection(today);
        MaterialDatePicker picker=builder.build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                datepicker.getEditText().setText(picker.getHeaderText());
            }
        });
        picker.show(manager, "tag2");
    }
    void displaytime(){
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Pick Time For Alarm")
                .build();
        picker.show(manager, "tag");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time_display=picker.getHour()+":"+picker.getMinute();
                DateFormat df = new SimpleDateFormat("HH:mm");
                DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
                Date date = null;
                String output = null;
                try{
                    date= df.parse(time_display);
                    output = outputformat.format(date);
                    System.out.println(output);
                }catch(ParseException pe){
                    pe.printStackTrace();
                }
                time.getEditText().setText(output);
            }
        });

    }



    /*files code*/
    public int generateRandom(int start, int end, ArrayList<Integer> excludeRows) {
        Random rand = new Random();
        int range = end - start + 1;

        int random = rand.nextInt(range) + 1;
        while(excludeRows.contains(random)) {
            random = rand.nextInt(range) + 1;
        }

        return random;
    }
    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            getActivity().finishAffinity();
        }

        else
        {
            getActivity().finish();
        }
    }
    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }
    public boolean createDirectory(String name){
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"ReminderAndTodo/Reminders");
        if(!file.exists()){
            file.mkdirs();
        }
        file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/ReminderAndTodo/Reminders",name);
        if(!file.exists())
        {
            file.mkdirs();
        }

        return true;
    }
    void ActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("the","data");
        if(requestCode==100 && data!=null) {
            uri = data.getData();
            String type = uri.getPath();
            String filename=getFileName(uri);
            String size="0kb";
            String scheme = uri.getScheme();
            File f = null;
            if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                int dataSize = 0;
                try {
                    InputStream fileInputStream = c.getContentResolver().openInputStream(uri);
                    dataSize = fileInputStream.available();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                size=humanReadableByteCountSI(dataSize);

            } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
                String path = uri.getPath();
                try {
                    f = new File(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                size=humanReadableByteCountSI(f.length());
            }
            Log.d("data","file name "+filename+" size :"+size);
            File source=new File(data.getData().getPath());
            ParcelFileDescriptor descriptor=null;
            try {
                descriptor =c.getContentResolver().openFileDescriptor(uri,"r", null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            File Destination=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/ReminderAndTodo/Reminders/"+directoryname+"/"+filename);
            Log.d("data",Destination.getAbsolutePath());
            String[] bits = filename.split("[.]");
            String extension = bits[bits.length-1];
            copy(descriptor,Destination);
            nofiles.setVisibility(View.GONE);

            files.setVisibility(View.VISIBLE);
            this.data.add(new set_file_data(filename, "."+extension, size,extension , getColor("."+extension), Destination.getAbsolutePath()));
            adapter.notifyDataSetChanged();

        }

    }
    private int getColor(String ext){
        String docs[]=application.getResources().getString(R.string.word).split(",");
        String ppt[]=application.getResources().getString(R.string.ppt).split(",");
        String excel[]=application.getResources().getString(R.string.excel).split(",");
        String audio[]=application.getResources().getString(R.string.audio).split(",");
        String video[]=application.getResources().getString(R.string.video).split(",");
        String images[]=application.getResources().getString(R.string.image).split(",");
        if(Arrays.asList(docs).contains(ext)){
            return application.getColor(R.color.docs);
        }
        if(Arrays.asList(ppt).contains(ext)){
            return application.getColor(R.color.ppts);
        }
        if(Arrays.asList(excel).contains(ext)){
            return application.getColor(R.color.excels);
        }
        if(Arrays.asList(audio).contains(ext)){
            return application.getColor(R.color.audio);
        }
        if(Arrays.asList(video).contains(ext)){
            return application.getColor(R.color.videos);
        }
        if(ext.equals(".pdf")){
            return application.getColor(R.color.pdf);
        }
        if(ext.equals(".txt")){
            return application.getColor(R.color.text);
        }
        if(Arrays.asList(images).contains(ext)){
            return application.getColor(R.color.images);
        }
        return application.getColor(R.color.others);
    }
    private void copy(ParcelFileDescriptor source, File destination) {

        FileChannel in = null;
        in = new FileInputStream(source.getFileDescriptor()).getChannel();
        FileChannel out = null;
        try {
            out = new FileOutputStream(destination).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            in.transferTo(0, in.size(), out);
        } catch(Exception e){
            e.printStackTrace();
            // post to log
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case 1:
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                }
                else
                {
                    closeNow();
                }
                break;
        }
    }
    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = resolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
