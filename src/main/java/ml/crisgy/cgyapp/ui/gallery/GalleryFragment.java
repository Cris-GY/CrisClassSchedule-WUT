package ml.crisgy.cgyapp.ui.gallery;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.crisgy.cgyapp.R;
import ml.crisgy.cgyapp.coursecardadapter;
import ml.crisgy.cgyapp.coursecarditem;
import ml.crisgy.cgyapp.databasehelper;
import ml.crisgy.cgyapp.examcalendarremind;
import ml.crisgy.cgyapp.examcardadapter;
import ml.crisgy.cgyapp.examcarditem;
import ml.crisgy.cgyapp.timemanager;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private databasehelper coursedbhelper;
    ArrayList<coursecarditem> allcourselist = new ArrayList<>();
    ArrayList<coursecarditem> weekcourselist = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        coursedbhelper = new databasehelper(getActivity(),"database.db",null,1);
        final SQLiteDatabase coursedatabase = coursedbhelper.getWritableDatabase();
        initcourseitemcard();//???????????????LISTVIEW
        coursecardadapter allcoursecardadapter = new coursecardadapter(getActivity(), R.layout.courseitem, allcourselist);
        final ListView allcourselistview = (ListView) root.findViewById(R.id.allcourse_listview);
        allcourselistview.setAdapter(allcoursecardadapter);//??????listview
        allcourselistview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                coursecarditem coursecarditem = allcourselist.get(position);//??????????????????
                final String coursename = coursecarditem.getCoursename();
                int weekstart = coursecarditem.getWeekstart();
                String weekstarts = "" + weekstart;
                int weekend = coursecarditem.getWeekend();
                String weekends = "" + weekend;
                int ofweek = coursecarditem.getOfweek();
                String ofweeks = "" + ofweek;
                int timestart = coursecarditem.getTimestart();
                String timestarts = "" + timestart;
                int timeend = coursecarditem.getTimeend();
                String timeends = "" + timeend;
                String courseroom = coursecarditem.getCourseroom();
                String courseteacher = coursecarditem.getCourseteacher();
                final String coursecontent = "?????????" + coursename + "\n?????????" + "???" + weekstarts + "?????????" + weekends + "??????" + ofweeks + "???" + timestarts + "?????????" + timeends + "???\n" +"?????????" + courseroom + "\n?????????" + courseteacher ;
                AlertDialog.Builder coursedialog = new AlertDialog.Builder(getActivity()).setTitle("????????????").setMessage(coursecontent).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//??????????????????
                    }
                }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//???????????????????????????
                        coursedatabase.delete("Course","coursename=?", new String[]{String.valueOf(coursename)});
                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
                    }
                });
                coursedialog.create().show();
            }
        });

        initweekcourseitemcard();
        final ListView weekcourselistview = (ListView) root.findViewById(R.id.weekcourse_listview);
        coursecardadapter weekcoursecardadapter = new coursecardadapter(getActivity(), R.layout.courseitem, weekcourselist);
        weekcourselistview.setAdapter(weekcoursecardadapter);//??????listview
        weekcourselistview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                coursecarditem coursecarditem = weekcourselist.get(position);//??????????????????
                final String coursename = coursecarditem.getCoursename();
                int weekstart = coursecarditem.getWeekstart();
                String weekstarts = "" + weekstart;
                int weekend = coursecarditem.getWeekend();
                String weekends = "" + weekend;
                int ofweek = coursecarditem.getOfweek();
                String ofweeks = "" + ofweek;
                int timestart = coursecarditem.getTimestart();
                String timestarts = "" + timestart;
                int timeend = coursecarditem.getTimeend();
                String timeends = "" + timeend;
                String courseroom = coursecarditem.getCourseroom();
                String courseteacher = coursecarditem.getCourseteacher();
                final String coursecontent = "?????????" + coursename + "\n?????????" + "???" + weekstarts + "?????????" + weekends + "??????" + ofweeks + "???" + timestarts + "?????????" + timeends + "???\n" +"?????????" + courseroom + "\n?????????" + courseteacher ;
                AlertDialog.Builder coursedialog = new AlertDialog.Builder(getActivity()).setTitle("????????????").setMessage(coursecontent).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//??????????????????
                    }
                }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//???????????????????????????
                        coursedatabase.delete("Course","coursename=?", new String[]{String.valueOf(coursename)});
                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
                    }
                });
                coursedialog.create().show();
            }
        });

        Button examclear = root.findViewById(R.id.examclear_button);//??????????????????
        examclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder examcleardialog = new AlertDialog.Builder(getActivity()).setTitle("??????????????????????????????").setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        coursedatabase.delete("Course", null, null);
                        Toast.makeText(getContext(),"???????????????",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("????????????", null );
                examcleardialog.create().show();
            }
        });
        return root;
    }

    private void initweekcourseitemcard(){
        final SQLiteDatabase coursedatabase = coursedbhelper.getWritableDatabase();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datasettings", MODE_PRIVATE);//??????????????????
        int firstweek = sharedPreferences.getInt("firstweek",0);
        int weekOfYear = timemanager.getWeekOfYear();
        int week = weekOfYear - firstweek;
        Cursor coursecursor = coursedatabase.query("Course",null,"weekstart<=?",new String[]{String.valueOf(week)},null,null,"weekend desc");
        if (coursecursor.moveToFirst()){//?????????????????????????????????
            do {
                int weekend = coursecursor.getInt(coursecursor.getColumnIndex("weekend"));
                if(weekend >= week){//??????????????????
                String coursename = coursecursor.getString(coursecursor.getColumnIndex("coursename"));
                int weekstart = coursecursor.getInt(coursecursor.getColumnIndex("weekstart"));
                int ofweek = coursecursor.getInt(coursecursor.getColumnIndex("ofweek"));
                int timestart = coursecursor.getInt(coursecursor.getColumnIndex("timestart"));
                int timeend = coursecursor.getInt(coursecursor.getColumnIndex("timeend"));
                String courseroom = coursecursor.getString(coursecursor.getColumnIndex("courseroom"));
                String courseteacher = coursecursor.getString(coursecursor.getColumnIndex("courseteacher"));
                coursecarditem coursecard = new coursecarditem(coursename, weekstart, weekend, ofweek, timestart, timeend, courseroom, courseteacher);
                weekcourselist.add(coursecard);}//???????????????????????????????????????
                else
                    break;
            }while (coursecursor.moveToNext());
        }
        coursecursor.close();
    }

    private void initcourseitemcard(){
        final SQLiteDatabase coursedatabase = coursedbhelper.getWritableDatabase();
        Cursor coursecursor = coursedatabase.query("Course",null,null,null,null,null,null);
        if (coursecursor.moveToFirst()){//?????????????????????????????????
            do {
                String coursename = coursecursor.getString(coursecursor.getColumnIndex("coursename"));
                int weekstart = coursecursor.getInt(coursecursor.getColumnIndex("weekstart"));
                int weekend = coursecursor.getInt(coursecursor.getColumnIndex("weekend"));
                int ofweek = coursecursor.getInt(coursecursor.getColumnIndex("ofweek"));
                int timestart = coursecursor.getInt(coursecursor.getColumnIndex("timestart"));
                int timeend = coursecursor.getInt(coursecursor.getColumnIndex("timeend"));
                String courseroom = coursecursor.getString(coursecursor.getColumnIndex("courseroom"));
                String courseteacher = coursecursor.getString(coursecursor.getColumnIndex("courseteacher"));
                coursecarditem coursecard = new coursecarditem(coursename, weekstart, weekend, ofweek, timestart, timeend, courseroom, courseteacher);
                allcourselist.add(coursecard);//???????????????????????????????????????
            }while (coursecursor.moveToNext());
        }
        coursecursor.close();
    }


}
