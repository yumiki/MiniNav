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
                HttpURLConnection pageWeb = (HttpURLConnection) urlPage.openConnection() ;
                pageWeb.connect();
                pageWeb.setReadTimeout(10000);
                OutputStreamWriter wosr= new OutputStreamWriter(pageWeb.getOutputStream());
                BufferedWriter wbw=new BufferedWriter(wosr);
                wbw.write("GET / \n");
                wbw.flush();

                InputStreamReader isr = new InputStreamReader(pageWeb.getInputStream());
                BufferedWriter
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

        Button go = (Button) findViewById(R.id.button);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.start();
            }
        });
    }
}
