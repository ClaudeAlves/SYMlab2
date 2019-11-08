package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

public class Asynchronous extends AppCompatActivity {

    final Activity asynch_activity = this;
    private Button asyncSend = null;
    private SysCommManager sysCommMan = new SysCommManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous);
        this.asynch_activity.setTitle("Asynchronous");
        this.asyncSend = findViewById(R.id.button_send_text);

        asyncSend.setOnClickListener((v) -> {
            try {
                sysCommMan.sendRequest("test edualc", "http://sym.iict.ch/rest/txt");
            } catch (Exception e) {
                System.out.println(e);
            }
        });

    }
    public boolean onOptionsIteémSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Asynchronous.class);
        startActivityForResult(myIntent, 0);

        return true;
    }
}