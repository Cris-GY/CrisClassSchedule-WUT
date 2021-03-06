package ml.crisgy.cgyapp.ui.home;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Calendar;

import ml.crisgy.cgyapp.MainActivity;
import ml.crisgy.cgyapp.R;
import ml.crisgy.cgyapp.coursecarditem;
import ml.crisgy.cgyapp.databasehelper;
import ml.crisgy.cgyapp.timemanager;
import ml.crisgy.cgyapp.zhlgd;

import static android.content.Context.MODE_PRIVATE;
import static ml.crisgy.cgyapp.MainActivity.courseflag;
import static ml.crisgy.cgyapp.MainActivity.examflag;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ArrayList<String> todaycourselist = new ArrayList<>();
    private ArrayList<String> todayexamlist = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datasettings", MODE_PRIVATE);//????????????????????????
        String call = sharedPreferences.getString("call", "USER");
        TextView greettextview = root.findViewById(R.id.greet_greet);
        greettextview.setText("Hello, "+call);
        String term = sharedPreferences.getString("term","?????????");
        int firstweek = sharedPreferences.getInt("firstweek",0);
        int weekOfYear = timemanager.getWeekOfYear();
        int week = weekOfYear - firstweek;
        TextView greettime = root.findViewById(R.id.greet_time);//??????????????????
        String weeks = Integer.toString(week);
        greettime.setText("?????????"+term+"???"+weeks+"???");
        CardView greetcard = root.findViewById(R.id.greetcard);
        greetcard.setOnClickListener(new View.OnClickListener() {//????????????
            @Override
            public void onClick(View view) {
                final EditText calleditText = new EditText(getContext());
                AlertDialog.Builder editcalldialog = new AlertDialog.Builder(getActivity()).setTitle("???????????????").setView(calleditText).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String call = calleditText.getText().toString();
                        SharedPreferences.Editor editsharedPreferences = getActivity().getSharedPreferences("datasettings",MODE_PRIVATE).edit();
                        editsharedPreferences.putString("call", call);
                        editsharedPreferences.apply();
                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
                    }
                });
                editcalldialog.create().show();
            }
        });

        databasehelper coursedbhelper = new databasehelper(getActivity(),"database.db",null,1);//??????????????????
        final SQLiteDatabase coursedatabase = coursedbhelper.getWritableDatabase();
        Cursor coursecursor = coursedatabase.query("Course",null,"weekstart<=?",new String[]{String.valueOf(week)},null,null,"weekend desc");
        if (coursecursor.moveToFirst()){//?????????????????????????????????
            do {
                int weekend = coursecursor.getInt(coursecursor.getColumnIndex("weekend"));
                if(weekend >= week){//??????????????????
                    int ofweek = coursecursor.getInt(coursecursor.getColumnIndex("ofweek"));
                    int weekday = timemanager.getWeekDay();
                    if(ofweek == weekday){//?????????????????????
                        String coursename = coursecursor.getString(coursecursor.getColumnIndex("coursename"));
                        int timestart = coursecursor.getInt(coursecursor.getColumnIndex("timestart"));
                        int timeend = coursecursor.getInt(coursecursor.getColumnIndex("timeend"));
                        String courseroom = coursecursor.getString(coursecursor.getColumnIndex("courseroom"));
                        final String coursecontent = coursename + "  ???" + timestart + "?????????" + timeend + "???  " + courseroom;
                        courseflag = 1;
                        todaycourselist.add(coursecontent);}//???????????????????????????????????????
                    }
                else
                    break;
            }while (coursecursor.moveToNext());
        }
        if(courseflag == 0){
            todaycourselist.add("???????????????");
        }
        coursecursor.close();
        ArrayAdapter<String> courseadapter = new ArrayAdapter<String>(getContext(), R.layout.simple_list_item_1, todaycourselist);//??????????????????listview
        ListView todaycourse = root.findViewById(R.id.todaycourse_listview);
        todaycourse.setAdapter(courseadapter);

        int month = timemanager.getMonth();
        Cursor examcursor = coursedatabase.query("Exam",null,"month=?",new String[]{String.valueOf(month)},null,null,"day");
            if (examcursor.moveToFirst()){//?????????????????????????????????
                do {
                    int day = examcursor.getInt(examcursor.getColumnIndex("day"));
                    int today = timemanager.getDay();
                    if(day == today){//??????????????????
                        String examname = examcursor.getString(examcursor.getColumnIndex("examname"));
                        String examroom = examcursor.getString(examcursor.getColumnIndex("examroom"));
                        final String examcontent = examname + "  " + examroom;
                        examflag = 1;
                        todayexamlist.add(examcontent);}//???????????????????????????????????????
                }while (examcursor.moveToNext());
            }
        examcursor.close();
        if(examflag == 0){
            todayexamlist.add("??????????????????");
        }
        ArrayAdapter<String> examadapter = new ArrayAdapter<String>(getContext(), R.layout.simple_list_item_1, todayexamlist);//??????????????????listview
        ListView todayexam = root.findViewById(R.id.todayexam_listview);
        todayexam.setAdapter(examadapter);

        final CardView zhlgdcard = root.findViewById(R.id.zhlgdcard);//???????????????CARD
        zhlgdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent zhlgdintent = new Intent(getActivity(), zhlgd.class);
                startActivity(zhlgdintent);
            }
        });

        return root;
    }

}
