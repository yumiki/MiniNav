package com.tlg.mininav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    URL urlPage= null;
    String buffer=null;
    StringBuffer sb;
    public boolean shouldContinue=true;


    Thread web = new Thread(new Runnable() {

        @Override
        public void run() {
            while (shouldContinue) {
                try {
                    String cacheMd5 = "test";
                    cacheMd5 = md5(urlPage.toString());
                    final File cache = new File(getFilesDir(), cacheMd5);
                    if (!cache.exists()) {
                        HttpURLConnection pageWeb = (HttpURLConnection) urlPage.openConnection();
                        pageWeb.setReadTimeout(10000);
                        InputStreamReader isr = new InputStreamReader(pageWeb.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        buffer = br.readLine();
                        FileOutputStream fos = new FileOutputStream(cache);
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        BufferedWriter bw = new BufferedWriter(osw);
                        while ((buffer != null)&&(shouldContinue==true)) {
                            bw.write(buffer);
                            buffer = br.readLine();
                            if(buffer==null)shouldContinue=false;
                        }
                        bw.close();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView t1 = (TextView) findViewById(R.id.TextView1);
                            try {
                                FileInputStream fis = new FileInputStream(cache);
                                InputStreamReader isr = new InputStreamReader(fis);
                                BufferedReader br = new BufferedReader(isr);
                                buffer = br.readLine();
                                while ((buffer != null)&&(shouldContinue==true)) {
                                    t1.setText(buffer);
                                    buffer = br.readLine();
                                    if(buffer==null)shouldContinue=false;
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        public void setShouldContinue(boolean bool){
            shouldContinue=bool;
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
                t1.setText("Chargement...");
                t1.setMovementMethod(new ScrollingMovementMethod());
                try {
                    urlPage=new URL(texteURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Log.d("Texte", texteURL);
                shouldContinue=false;
                try {
                    web.join();
                    shouldContinue=true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                web.start();

            }
        });
    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
