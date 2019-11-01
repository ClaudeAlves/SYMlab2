package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class Asynchronous extends AppCompatActivity {

    final Activity asynch_activity = this;
    final ActionBar actionBar = getActionBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous);
        asynch_activity.setTitle("Asynchronous");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
