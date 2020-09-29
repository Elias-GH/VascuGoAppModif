/*
 * Copyright 2014 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.j4velin.pedometer.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.j4velin.pedometer.BuildConfig;
import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.Database1;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.SensorListener;
import de.j4velin.pedometer.util.API26Wrapper;
import de.j4velin.pedometer.util.Logger;
import de.j4velin.pedometer.util.Util;

public class WeeklyExercice extends Fragment implements SensorEventListener {

    private TextView stepsView1, totalView1, averageView1;
    private PieModel sliceGoal1, sliceCurrent1;
    private PieChart pg1;

    private int todayOffset, total_start1, goal, since_boot1, total_days1;
    public final static NumberFormat formatter1 = NumberFormat.getInstance(Locale.getDefault());
    private boolean showSteps1 = true;


    private TextView counterdownText1;
    private Button counterdownButton1;
    private CountDownTimer countDownTimer1;
    private long timeLeftMillisecond1 =360000; //6 min
    private  boolean timerRunning1;

    public void displayPainMsg(){
        Toast.makeText(getActivity(), "Douleur enregistré sur la marche. " , Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= 26) {
            API26Wrapper.startForegroundService(getActivity(),
                    new Intent(getActivity(), SensorListener.class));
        } else {
            getActivity().startService(new Intent(getActivity(), SensorListener.class));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_weekly_exercice, null);
        stepsView1 = (TextView) v.findViewById(R.id.steps);
        totalView1 = (TextView) v.findViewById(R.id.total);
        averageView1 = (TextView) v.findViewById(R.id.average);
        counterdownText1 = v.findViewById(R.id.counterdown_text);
        counterdownButton1 = v.findViewById(R.id.counterdown_button);

        pg1 = (PieChart) v.findViewById(R.id.graph);

        // slice for the steps taken today
        sliceCurrent1 = new PieModel("", 0, Color.parseColor("#99CC00"));
        pg1.addPieSlice(sliceCurrent1);

        // slice for the "missing" steps until reaching the goal
        sliceGoal1 = new PieModel("", Fragment_Settings.DEFAULT_GOAL, Color.parseColor("#CC0000"));
        pg1.addPieSlice(sliceGoal1);

        pg1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                showSteps1 = !showSteps1;
                stepsDistanceChanged();
            }
        });

        counterdownButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        updateTimer();
        pg1.setDrawValueInPie(false);
        pg1.setUsePieRotation(true);
        pg1.startAnimation();
        return v;
    }

    public void start(){
        if (!timerRunning1){
            startTimer();
        }
    }

    public void stop(){
        if (timerRunning1){
            stopTimer();
            counterdownButton1.setText("REPRENDRE");
        }
    }

    public void startTimer(){
        countDownTimer1 =new CountDownTimer(timeLeftMillisecond1,1000) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillisecond1 =millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
       // counterdownText1.setText("PAUSE");
        timerRunning1 =true;
    }

    public void stopTimer(){
        countDownTimer1.cancel();
       //counterdownText1.setText("REPRENDRE");
        timerRunning1 =false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateTimer(){
        int minute =(int) timeLeftMillisecond1 /60000;
        int second =(int) timeLeftMillisecond1 %60000/1000;
        String timeLeftText;
        timeLeftText=""+minute;
        timeLeftText+=":";
        if (second<10)timeLeftText+="0";
        timeLeftText+=second;
        counterdownText1.setText(timeLeftText);
        if (minute==0 && second==0){
            try {
                Toast.makeText(getActivity(), "Marche terminé, résultat enregistré. " , Toast.LENGTH_SHORT).show();
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
                r.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);

        Database1 db = Database1.getInstance(getActivity());

        if (BuildConfig.DEBUG) db.logState();
        // read todays offset
        todayOffset = db.getSteps(Util.getToday());

        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);

        goal = prefs.getInt("goal", Fragment_Settings.DEFAULT_GOAL);
        since_boot1 = 0; //db.getCurrentSteps();
        int pauseDifference = since_boot1 - prefs.getInt("pauseCount", since_boot1);

        // register a sensorlistener to live update the UI if a step is taken
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.no_sensor)
                    .setMessage(R.string.no_sensor_explain)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(final DialogInterface dialogInterface) {
                            getActivity().finish();
                        }
                    }).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
        }

        since_boot1 -= pauseDifference;

        total_start1 = db.getTotalWithoutToday();
        total_days1 = db.getDays();

        db.close();

        stepsDistanceChanged();
    }

    /**
     * Call this method if the Fragment should update the "steps"/"km" text in
     * the pie graph as well as the pie and the bars graphs.
     */
    private void stepsDistanceChanged() {
        if (showSteps1) {
            ((TextView) getView().findViewById(R.id.unitfrag)).setText(getString(R.string.steps));
        } else {
            String unit = getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getString("stepsize_unit", Fragment_Settings.DEFAULT_STEP_UNIT);
            if (unit.equals("cm")) {
                unit = "km";
            } else {
                unit = "mi";
            }
            ((TextView) getView().findViewById(R.id.unitfrag)).setText(unit);
        }

        updatePie();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPause() {
        super.onPause();
        try {
            SensorManager sm =
                    (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) Logger.log(e);
        }
        Database1 db1 = Database1.getInstance(getActivity());
        db1.saveCurrentSteps(since_boot1);
        db1.close();
        //getContext().deleteDatabase("steps1");
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_home || item.getItemId() == R.id.action_about || item.getItemId() == R.id.action_achievements
                    || item.getItemId() == R.id.action_faq || item.getItemId() == R.id.action_settings || item.getItemId() == R.id.action_specific_exercice
                    || item.getItemId() == R.id.action_specific_exercice || item.getItemId() == R.id.action_split_count || item.getItemId() == R.id.action_weekly_exercice)
            {
                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
                int end = spanString.length();
                spanString.setSpan(new RelativeSizeSpan(1.3f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                item.setTitle(spanString);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_split_count:
                Dialog_Split.getDialog(getActivity(),
                        total_start1 + Math.max(todayOffset + since_boot1, 0)).show();
                return true;
            default:
                return ((Activity_Main) getActivity()).optionsItemSelected(item);
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, int accuracy) {
        // won't happen
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (BuildConfig.DEBUG) Logger.log(
                "UI - sensorChanged | todayOffset: " + todayOffset + " since boot: " +
                        event.values[0]);
        if (event.values[0] > Integer.MAX_VALUE || event.values[0] == 0) {
            return;
        }
        if (todayOffset == Integer.MIN_VALUE) {
            // no values for today
            // we dont know when the reboot was, so set todays steps to 0 by
            // initializing them with -STEPS_SINCE_BOOT
            todayOffset = -(int) event.values[0];
            Database1 db = Database1.getInstance(getActivity());
            db.insertNewDay(Util.getToday(), (int) event.values[0]);
            db.close();
        }
        since_boot1 = (int) event.values[0];
        updatePie();
    }

    /**
     * Updates the pie graph to show todays steps/distance as well as the
     * yesterday and total values. Should be called when switching from step
     * count to distance.
     */
    private void updatePie() {
        if (BuildConfig.DEBUG) Logger.log("UI - update steps: " + since_boot1);
        // todayOffset might still be Integer.MIN_VALUE on first start
        int steps_today = Math.max(todayOffset + since_boot1,0);
        sliceCurrent1.setValue(steps_today);
        if (goal - steps_today > 0) {
            // goal not reached yet
            if (pg1.getData().size() == 1) {
                // can happen if the goal value was changed: old goal value was
                // reached but now there are some steps missing for the new goal
                pg1.addPieSlice(sliceGoal1);
            }
            sliceGoal1.setValue(goal - steps_today);
        } else {
            // goal reached
            pg1.clearChart();
            pg1.addPieSlice(sliceCurrent1);
        }
        pg1.update();
        if (showSteps1) {
            stepsView1.setText(formatter1.format(steps_today));
            totalView1.setText(formatter1.format(total_start1 + steps_today));
            averageView1.setText(formatter1.format((total_start1 + steps_today) / total_days1));
        } else {
            // update only every 10 steps when displaying distance
            SharedPreferences prefs =
                    getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
            float stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
            float distance_today = steps_today * stepsize;
            float distance_total = (total_start1 + steps_today) * stepsize;
            if (prefs.getString("stepsize_unit", Fragment_Settings.DEFAULT_STEP_UNIT)
                    .equals("cm")) {
                distance_today /= 100000;
                distance_total /= 100000;
            } else {
                distance_today /= 5280;
                distance_total /= 5280;
            }
            stepsView1.setText(formatter1.format(distance_today));
            totalView1.setText(formatter1.format(distance_total));
            averageView1.setText(formatter1.format(distance_total / total_days1));
        }
    }

}
