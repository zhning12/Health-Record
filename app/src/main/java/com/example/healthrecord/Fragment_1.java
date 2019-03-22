package com.example.healthrecord;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_1 extends Fragment {

    private View mView;
    EditText vHeight, vWeight;
    Button submitButton;
    SqliteDbManager bmiDb;
    String datestamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_1, container, false);
        }
        //-- get views 
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bmiDb = SqliteDbManager.getInstance();
        bmiDb.setSqliteDbOpen(getContext());

        vHeight = (EditText) getActivity().findViewById(R.id.heightET);
        vWeight = (EditText) getActivity().findViewById(R.id.weightET);
        submitButton = (Button) getActivity().findViewById(R.id.reportBtn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heightS = vHeight.getText().toString();
                String weightS = vWeight.getText().toString();
                savePreferences(heightS, weightS);

                if (weightS.equals("") || heightS.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.errorHw, Toast.LENGTH_SHORT).show();
                } else {
                    double height = Double.parseDouble(heightS);
                    double weight = Double.parseDouble(weightS);
                    height = height / 100;
                    double bmi = weight / (height * height);
                    DecimalFormat nf = new DecimalFormat("0.00");
                    TextView result = (TextView) getActivity().findViewById(R.id.resultTV);
                    String resultMes = getResources().getString(R.string.resultMes);
                    result.setText(resultMes + nf.format(bmi));
//               获取当前时间，写入数据库
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date d1 = new Date();
                    String datestamp = sdf1.format(d1);
                    bmiDb.insertTb(Double.toString(bmi), datestamp);
// Give health advice
                    TextView advice = (TextView) getActivity().findViewById(R.id.adviceTV);
//cal the bmi
                    if (bmi > 25) {
                        advice.setTextColor(Color.parseColor("#B71C1C"));
                        advice.setText(R.string.advice_heavy);
                    } else if (bmi < 20) {
                        advice.setTextColor(Color.parseColor("#F57C00"));
                        advice.setText(R.string.advice_light);
                    } else {
                        advice.setTextColor(Color.parseColor("#03A9F4"));
                        advice.setText(R.string.advice_average);
                    }
                }
            }
        });
    }

    public void savePreferences(String h, String w) {
        SharedPreferences pref = getActivity().getSharedPreferences("BMI", MODE_PRIVATE);
        pref.edit().putString("height", h).commit();
        pref.edit().putString("weight", w).commit();
    }

    public void loadPreferences() {
        SharedPreferences pref = getActivity().getSharedPreferences("BMI", MODE_PRIVATE);
        vHeight.setText(pref.getString("height", "0"));
        vWeight.setText(pref.getString("weight", "0"));
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPreferences();
    }
//    public void addRecord(View view) {
//
//        boolean isInserted = bmiDb.insertData(firstName.getText().toString(), lastName.getText().toString(), marks.getText().toString());
//        if (isInserted)
//            results.setText("A new record is created.");
//        else
//            results.setText("Data cannot be inserted.");
//    }
//
//
//        results.setText(buffer.toString());
//}
//
//    public void updateRecord(View view) {
//
//        boolean isUpdate = bmiDb.updateData(id.getText().toString(),
//                firstName.getText().toString(), lastName.getText().toString(),
//                marks.getText().toString());
//        if (isUpdate)
//            results.setText("The record is updated.");
//        else
//            results.setText("The record cannot be updated.");
//    }


}