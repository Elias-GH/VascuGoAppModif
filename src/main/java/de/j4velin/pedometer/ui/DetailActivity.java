package de.j4velin.pedometer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import de.j4velin.pedometer.R;

public class DetailActivity extends AppCompatActivity {
    private final static Integer[] etat_exo_array = {0, 0, 0, 0, 0};

    public void setEtat_exo_array(int par) {
        etat_exo_array[par] = 1;
    }

    public Integer[] getEtat_exo_array(){

        return etat_exo_array;
    }

    public void displayMsg(String str){
        Toast.makeText(this, "Enregistrer : " + str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


    }
}
