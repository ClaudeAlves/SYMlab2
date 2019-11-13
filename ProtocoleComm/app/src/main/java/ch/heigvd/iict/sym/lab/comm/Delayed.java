/**
 * @author Claude-André Alves, Catel Torres Arzur Gabriel, Thomas Benjamin
 *
 * @about Cette classe permet l'envoi asynchrone d'un message textuel au serveur qui réside à
 *         l'adresse http://sym.iict.ch/rest/txt. Comparé à la version asynchrone simple celle-ci
 *         permet l'envoi même lorsque l'on est hors connexion. On lance des threads qui attendent
 *         la connexion avant d'envoyer les requêtes.
 */
package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Delayed extends AppCompatActivity {

    private final int THREAD_WAIT_TIME = 1000;
    private final String URL = "http://sym.iict.ch/rest/txt";
    private final String TYPE = "text/plain";

    final Activity delayedActivity = this;
    private Button delayedSend = null;
    private EditText delayedToSend = null, textResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed);
        this.delayedActivity.setTitle("Delayed Asynchronus");
        this.delayedSend = findViewById(R.id.delayedSend);
        this.delayedToSend = findViewById(R.id.delayedToSend);
        this.textResponse = findViewById(R.id.delayedResponse);

        delayedSend.setOnClickListener((v) -> {
            new HandleThread(delayedToSend.getText().toString(), URL, TYPE, new SysCommManager()).start();
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private class HandleThread extends Thread {
        private String toSend;
        private String url;
        private String type;
        private SysCommManager sysCommManager;

        HandleThread(String toSend, String url, String type, SysCommManager sysCommManager) {
            this.toSend = toSend;
            this.url = url;
            this.type = type;
            this.sysCommManager = sysCommManager;
        }
        @Override
        public void run() {
            try {
                while (!isNetworkAvailable()) {
                    this.sleep(THREAD_WAIT_TIME);
                }
                sysCommManager.setCommunicationEventListener(response -> {
                    textResponse.setText(response);
                    return true;
                });
                sysCommManager.sendRequest(toSend, url, type);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
