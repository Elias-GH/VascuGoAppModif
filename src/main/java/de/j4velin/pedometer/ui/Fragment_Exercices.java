package de.j4velin.pedometer.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import de.j4velin.pedometer.R;

public class Fragment_Exercices extends Fragment implements View.OnClickListener {


    private Fragment newFragment;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.exercices_choice, container, false);

        Button button2 = (Button) v.findViewById(R.id.button);
        Button button3 = (Button) v.findViewById(R.id.bouton1);
        Button button4 = (Button) v.findViewById(R.id.button2);
        Button button5 = (Button) v.findViewById(R.id.bouton3);
        ImageView button6 = (ImageView) v.findViewById(R.id.bouton4);

        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                newFragment = new WeeklyExercice();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.button:
                newFragment = new SpecificExercice();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.bouton1:
                newFragment = new Fragment_Overview();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.bouton3:
                newFragment = new Fragment_Information();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.bouton4:
                newFragment = new Fragment_Settings();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
        }
    }


}
