package mx.com.cdp.consumirws;

import static mx.com.cdp.consumirws.ShowFields.HIDDEN_MESSAGE;
import static mx.com.cdp.consumirws.ShowFields.HIDDEN_RESULTS;
import static mx.com.cdp.consumirws.ShowFields.HIDDEN_START;
import static mx.com.cdp.consumirws.ShowFields.SHOW_BUTTON_CONS;
import static mx.com.cdp.consumirws.ShowFields.SHOW_BUTTON_SEND;
import static mx.com.cdp.consumirws.ShowFields.SHOW_IN;
import static mx.com.cdp.consumirws.ShowFields.SHOW_RESULTS;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://api.geonames.org/timezoneJSON?formatted=true&lat=%s&lng=%s&username=atuwaku&style=full";

    private String reqUrl;
    private EditText txtSunrise;
    private EditText txtLng;
    private EditText txtCountrycode;
    private EditText txtGmtoffset;
    private EditText txtRawoffset;
    private EditText txtSunset;
    private EditText txtTimezoneid;
    private EditText txtDstoffset;
    private EditText txtCountryname;
    private EditText txtTime;
    private EditText txtLat;
    private EditText txtMsg;
    private TextView lblLatIn;
    private EditText txtLatIn;
    private TextView lblLngIn;
    private EditText txtLngIn;
    private Button btnEnviar;
    private Button btnConsGeo;
    private Button btnStart;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createControls();

        createEvents();

        showFields(HIDDEN_RESULTS);
        showFields(HIDDEN_MESSAGE);
        showFields(HIDDEN_START);

    }


    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).setMargin(50,50);

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void createEvents() {

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readWs();
            }
        });

        btnConsGeo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                clean();
                showFields(HIDDEN_MESSAGE);
                if(!verificarPermi()) {
                    grantPermiLocation();
                }
                if(verificarPermi()) {
                    miUbicacion();
                }
                else{
                    showMsg("Debe conceder permisos en el GPS para utilizar la aplicación");
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFields(HIDDEN_RESULTS);
                showFields(HIDDEN_MESSAGE);
                showFields(SHOW_BUTTON_CONS);
                showFields(SHOW_IN);
                clean();
            }
        });
    }

    private void createControls() {
        lblLatIn = findViewById(R.id.lblLatIn);
        txtLatIn = findViewById(R.id.txtLatIn);
        lblLngIn = findViewById(R.id.lblLngIn);
        txtLngIn = findViewById(R.id.txtLngIn);
        txtSunrise = findViewById(R.id.txtSunrise);
        txtLng = findViewById(R.id.txtLng);
        txtCountrycode = findViewById(R.id.txtCountryCode);
        txtGmtoffset = findViewById(R.id.txtGmtOffset);
        txtRawoffset = findViewById(R.id.txtRawOffset);
        txtSunset = findViewById(R.id.txtSunset);
        txtTimezoneid = findViewById(R.id.txtTimezoneId);
        txtDstoffset = findViewById(R.id.txtDstOffset);
        txtCountryname = findViewById(R.id.txtCountryName);
        txtTime = findViewById(R.id.txtTime);
        txtLat = findViewById(R.id.txtLat);
        txtMsg = findViewById(R.id.txtMsg);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnConsGeo = findViewById(R.id.btnConsGeo);
        btnStart = findViewById(R.id.btnStart);
    }

    private void clean() {
        txtSunrise.setText("");
        txtLng.setText("");
        txtCountrycode.setText("");
        txtGmtoffset.setText("");
        txtRawoffset.setText("");
        txtSunset.setText("");
        txtTimezoneid.setText("");
        txtDstoffset.setText("");
        txtCountryname.setText("");
        txtTime.setText("");
        txtLat.setText("");
        txtLatIn.setText("");
        txtLngIn.setText("");
        txtMsg.setText("");
    }

    public boolean verificarPermi() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean grantPermiLocation() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);

        return verificarPermi();
    }


    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void miUbicacion() {

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            txtLatIn.setText(Double.toString(location.getLatitude()));
                            txtLngIn.setText(Double.toString(location.getLongitude()));
                            showFields(SHOW_BUTTON_SEND);

                        }
                    }
                });
    }

    private void showFields(ShowFields action){

        switch (action){

            case SHOW_RESULTS:
                txtSunrise.setVisibility(View.VISIBLE);
                txtLng.setVisibility(View.VISIBLE);
                txtCountrycode.setVisibility(View.VISIBLE);
                txtGmtoffset.setVisibility(View.VISIBLE);
                txtRawoffset.setVisibility(View.VISIBLE);
                txtSunset.setVisibility(View.VISIBLE);
                txtTimezoneid.setVisibility(View.VISIBLE);
                txtDstoffset.setVisibility(View.VISIBLE);
                txtCountryname.setVisibility(View.VISIBLE);
                txtTime.setVisibility(View.VISIBLE);
                txtLat.setVisibility(View.VISIBLE);
                lblLatIn.setVisibility(View.GONE);
                txtLatIn.setVisibility(View.GONE);
                lblLngIn.setVisibility(View.GONE);
                txtLngIn.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                btnEnviar.setVisibility(View.GONE);
            break;

            case HIDDEN_RESULTS:
                txtSunrise.setVisibility(View.GONE);
                txtLng.setVisibility(View.GONE);
                txtCountrycode.setVisibility(View.GONE);
                txtGmtoffset.setVisibility(View.GONE);
                txtRawoffset.setVisibility(View.GONE);
                txtSunset.setVisibility(View.GONE);
                txtTimezoneid.setVisibility(View.GONE);
                txtDstoffset.setVisibility(View.GONE);
                txtCountryname.setVisibility(View.GONE);
                txtTime.setVisibility(View.GONE);
                txtLat.setVisibility(View.GONE);
                lblLatIn.setVisibility(View.VISIBLE);
                txtLatIn.setVisibility(View.VISIBLE);
                lblLngIn.setVisibility(View.VISIBLE);
                txtLngIn.setVisibility(View.VISIBLE);
                btnEnviar.setVisibility(View.GONE);
                break;

            case SHOW_IN:
                lblLatIn.setVisibility(View.VISIBLE);
                txtLatIn.setVisibility(View.VISIBLE);
                lblLngIn.setVisibility(View.VISIBLE);
                txtLngIn.setVisibility(View.VISIBLE);
                txtMsg.setVisibility(View.GONE);
                break;

            case HIDDEN_IN:
                lblLatIn.setVisibility(View.GONE);
                txtLatIn.setVisibility(View.GONE);
                lblLngIn.setVisibility(View.GONE);
                txtLngIn.setVisibility(View.GONE);
                txtMsg.setVisibility(View.GONE);
                break;

            case SHOW_BTNENVIAR:
                btnEnviar.setVisibility(View.VISIBLE);
                break;

            case SHOW_MESSAGE:
                txtMsg.setVisibility(View.VISIBLE);
                break;

                case HIDDEN_MESSAGE:
                txtMsg.setVisibility(View.GONE);
                break;

            case SHOW_BUTTON_SEND:
                btnEnviar.setVisibility(View.VISIBLE);
                btnConsGeo.setVisibility(View.GONE);
                btnStart.setVisibility(View.GONE);
                break;

            case SHOW_BUTTON_CONS:
                btnEnviar.setVisibility(View.GONE);
                btnConsGeo.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.GONE);
                break;

            case HIDDEN_START:
                btnStart.setVisibility(View.GONE);
                break;

        }

    }

    private boolean isvalidResponse(String value){

        try {

            if(value.isEmpty()){
                txtMsg.setText("La respuesta llegó vacía");
                return false;
            }
            if(!value.contains("sunrise")){
                JSONObject jsonObject = new JSONObject(value);
                txtMsg.setText("ERROR: " + jsonObject.getJSONObject("status").getString("message"));
                return false;
            }
            return true;

        }
        catch (JSONException e){
            e.printStackTrace();
            return  false;
        }
    }
    
    private void showResponse(String response){
        try {
            if (!isvalidResponse(response)) {
                showFields(HIDDEN_RESULTS);
                return;
            }
            JSONObject jsonObject = new JSONObject(response);
            txtSunrise.setText("Sunrise: " + jsonObject.getString("sunrise"));
            txtLng.setText("Longitud: " + jsonObject.getString("lng"));
            txtCountrycode.setText("CountryCode: " + jsonObject.getString("countryCode"));
            txtGmtoffset.setText("GmtOffset: " + jsonObject.getString("gmtOffset"));
            txtRawoffset.setText("RawOffset: " + jsonObject.getString("rawOffset"));
            txtSunset.setText("Sunset: " + jsonObject.getString("sunset"));
            txtTimezoneid.setText("TimezoneId: " + jsonObject.getString("timezoneId"));
            txtDstoffset.setText("DstOffset: " + jsonObject.getString("dstOffset"));
            txtCountryname.setText("CountryName: " + jsonObject.getString("countryName"));
            txtTime.setText("Time: " + jsonObject.getString("time"));
            txtLat.setText("Latitud: " + jsonObject.getString("lat"));

            showFields(SHOW_RESULTS);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean canBuilUrl(){

        if(this.txtLatIn.getText().equals("") || this.txtLngIn.getText().equals("")){
            return false;
        }
        return true;
    }

    private boolean buildURL(){

        if(!canBuilUrl()){
            this.txtMsg.setText("Sin latitud o sin longitud no se puede hacer la consulta");
            showFields(HIDDEN_RESULTS);
            showFields(SHOW_IN);
            showFields(SHOW_BUTTON_CONS);
            return false;
        }

        reqUrl = String.format(URL,txtLatIn.getText().toString().trim().replace("Latitud ",""),txtLngIn.getText().toString().trim().replace("Longitud ",""));
        return true;
    }

    private void readWs() {

        if(!buildURL()){
            return;
        }

        StringRequest postResquest = new StringRequest(Request.Method.GET, reqUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    showResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showFields(HIDDEN_RESULTS);
                txtMsg.setText(error.getMessage());
                Log.e("Error", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(postResquest);
    }

    private void enviarWs(final String title, final String body, final String userId) {

        String url = "https://jsonplaceholder.typicode.com/posts";

        StringRequest postResquest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "RESULTADO POST = " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("body", body);
                params.put("userId", userId);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postResquest);
    }

    private void actualizarWs(final String title, final String body, final String userId) {

        String url = "https://jsonplaceholder.typicode.com/posts/1";

        StringRequest postResquest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "RESULTADO = " + response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", "1");
                params.put("title", title);
                params.put("body", body);
                params.put("userId", userId);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postResquest);
    }

    private void eliminarWs() {

        String url = "https://jsonplaceholder.typicode.com/posts/1";

        StringRequest postResquest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, "RESULTADO = " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(postResquest);
    }
}