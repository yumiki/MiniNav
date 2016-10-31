package com.tlg.mininav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.BufferUnderflowException;

public class MainActivity extends AppCompatActivity {
    public URL urlPage= new URL("http://www.perdu.com");

    Thread web = new Thread(new Runnable() {
        @Override
        public void run() {

            try {
                String cacheMd5="test";
                cacheMd5=md5(urlPage.toString());
                File cache = new File(getFilesDir(),cacheMd5);
                if(!cache.exists()) {
                    HttpURLConnection pageWeb = (HttpURLConnection) urlPage.openConnection();
                    pageWeb.setReadTimeout(10000);
                    InputStreamReader isr = new InputStreamReader(pageWeb.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    while (br.readLine() != null) {

                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    public MainActivity() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText field1 = (EditText) findViewById(R.id.editText);
        Button go = (Button) findViewById(R.id.button);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texteURL = field1.getText().toString();
                TextView t1= (TextView) findViewById(R.id.TextView1);
                t1.setText(texteURL);

                Log.d("Texte", texteURL);
               // web.start();
            }
        });
    }
}
