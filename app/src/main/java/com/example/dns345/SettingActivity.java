package com.example.dns345;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SettingActivity extends AppCompatActivity {
    private Socket socket;

    EditText server, nbrLance;
    RadioGroup direction;
    RadioButton Butdirection;
    Button newButton;
    String urlM = "";
    String nbrLanc, valDirection;
    boolean ok = false;
    final String[] orderSend = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        server = findViewById(R.id.server);
        nbrLance = findViewById(R.id.nbrLance);

        //find  selected  value
        direction = findViewById(R.id.direction);
        int selectedRadioButton = direction.getCheckedRadioButtonId();
        Butdirection = findViewById(selectedRadioButton);
        valDirection = (String) Butdirection.getText();
        // valDirection=direction.toString();
        //************************-*-****

        // builder = new AlertDialog.Builder(MainActivity.this);

        newButton = findViewById(R.id.newButton);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connectSocket();
                urlM = "http://"+ server.getText().toString() +":3000/getFromAndroid";

                Log.i("url =============>",urlM);

                // String url = "http://" + server.getText().toString() + ":3000/android";
                // String urlConnexion ="http://" + server.getText().toString() + ":3000/connexion";
                sendData(urlM);
                // String order="";
                   /* do{
                       order= sendNbrCars("http://" + server.getText().toString() + ":3000/","3");

                   }while(order!="send!");*/
                //connected("http://192.168.1.19:3000/androidb");

            }
        });
    }
    public void sendData (String url){
        nbrLanc = nbrLance.getText().toString();
        String brand = Build.BRAND;
        String model = Build.MODEL;
        url = url + "/" + nbrLanc + "/" + valDirection+"/"+brand+"/"+model;

        Log.i("URL ===>", urlM);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                toast("in if"+response);
                if(response.equals("connection successful")){
                    toast("in if");
                    Intent i = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(i);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

              toast(error.toString());

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void toast (String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    public void connectSocket(){
         try {
             socket = IO.socket("http://"+server.getText().toString()+":4000");

         } catch (URISyntaxException e) {
             e.printStackTrace();
         }                                                                                
         socket.connect();

    }
}
