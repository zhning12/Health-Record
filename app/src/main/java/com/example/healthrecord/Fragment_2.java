package com.example.healthrecord;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Fragment_2 extends Fragment {

    private View mView;
    private MaterialCalendarView calendarView;
    SqliteDbManager bmiDb;
    String datestamp;
    TextView results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment_2,container,false);
        }

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bmiDb = SqliteDbManager.getInstance();
        bmiDb.setSqliteDbOpen(getContext());

        calendarView = (MaterialCalendarView) getActivity().findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(dateSelectedListener);
        Collection eventsToDisplayInTheCalendar= new ArrayList<>();
        eventsToDisplayInTheCalendar=bmiDb.getAll() ;
        calendarView.addDecorator(new EventDecorator(R.color.colorAccent, eventsToDisplayInTheCalendar));

        results = (TextView) getActivity().findViewById(R.id.tv_results);
    }

    public OnDateSelectedListener dateSelectedListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
            datestamp = calendarDay.getDate().toString();
            String resultMes = getResources().getString(R.string.resultMes);
            List<String> result = bmiDb.queryTb(datestamp,resultMes);
            if(result.isEmpty()){
                results.setText("");
            }else{
                results.setText(result.get(result.size() - 1));
            }

        }
    };
}