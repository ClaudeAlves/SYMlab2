package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int RETURN_CLOSE = 6;

    private Button asynchronousButton = null;
    private Button delayedButton = null;
    private Button serializationButton = null;
    private Button graphQLButton = null;
    private Button compressedButton = null;
    Intent intent = null;

    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity.setTitle("Application Lab02 SYM");

        this.asynchronousButton = findViewById(R.id.asynchronous);
        this.compressedButton = findViewById(R.id.compressed);
        this.delayedButton = findViewById(R.id.delayed);
        this.graphQLButton = findViewById(R.id.graphQL);
        this.serializationButton = findViewById(R.id.serialization);

        asynchronousButton.setOnClickListener((v) -> {
            intent = new Intent(this, Asynchronous.class);
            startActivityForResult(intent, RETURN_CLOSE);
        });
        compressedButton.setOnClickListener((v) -> {
            intent = new Intent(this, Compressed.class);
            startActivityForResult(intent, RETURN_CLOSE);
        });
        delayedButton.setOnClickListener((v) -> {
            intent = new Intent(this, Delayed.class);
            startActivityForResult(intent, RETURN_CLOSE);
        });
        graphQLButton.setOnClickListener((v) -> {
            intent = new Intent(this, GraphQL.class);
            startActivityForResult(intent, RETURN_CLOSE);
        });
        serializationButton.setOnClickListener((v) -> {
            intent = new Intent(this, Serialization_json_XML.class);
            startActivityForResult(intent, RETURN_CLOSE);
        });


    }


    protected void launchActivity(Intent intent) {

    }
}
