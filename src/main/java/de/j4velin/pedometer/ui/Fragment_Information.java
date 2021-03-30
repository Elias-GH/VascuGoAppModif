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

public class Fragment_Information extends Fragment implements View.OnClickListener {


    private Fragment newFragment;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_main, container, false);
        Button button1 = (Button) v.findViewById(R.id.bouton1);
        Button button2 = (Button) v.findViewById(R.id.bouton2);
        Button button3 = (Button) v.findViewById(R.id.bouton3);
        ImageView button4 = (ImageView) v.findViewById(R.id.bouton4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button1.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bouton1:
                newFragment = new Fragment_Overview();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.bouton2:
                newFragment = new Fragment_Exercices();
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
