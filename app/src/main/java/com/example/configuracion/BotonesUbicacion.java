package com.example.configuracion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class BotonesUbicacion extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;
    private TextView uno, dos;
    Button btn1,btn2;
    EditText textubicacion;
    String ubicacion,mano,puto,joto;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones_ubicacion);
        resultReceiver = new BotonesUbicacion.AddressResultReceiver(new Handler());

        uno = (TextView) findViewById(R.id.txtentregar);
        btn2 = (Button)findViewById(R.id.aseptar);
        textubicacion =(EditText)findViewById(R.id.textubicacion);
        btn1 = (Button)findViewById(R.id.ubicacion);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcurrentlocation();
            } else {
                Toast.makeText(this, "PERMIS NEGADO", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void getcurrentlocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(BotonesUbicacion.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        LocationServices.getFusedLocationProviderClient(BotonesUbicacion.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size() -1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitud =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            //variables de longitud y latitud


                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitud);
                            fetchAddressFromLatLong(location);


                        }
                    }
                }, Looper.getMainLooper());

    }

    private void fetchAddressFromLatLong (Location location){
        Intent intent = new Intent(this,direccion.class);
        intent.putExtra(constans.RECEIVER,resultReceiver);
        intent.putExtra(constans.LOCATION_DATA_EXTRA,location);
        startService(intent);

    }

    private void ubicaciones(){
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    BotonesUbicacion.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getcurrentlocation();
        }

    }

    @Override
    public void onClick(View v) {
        puto = getIntent().getStringExtra("puto");
        switch (puto){
            case "ub":
                switch (v.getId()){
                    case  R.id.ubicacion:
                        ubicaciones();
                        break;
                    case R.id.aseptar:
                        mano = textubicacion.getText().toString();
                        Intent iii =new Intent(BotonesUbicacion.this,MainActivity.class);
                        iii.putExtra("puto",mano);
                        startActivity(iii);
                        finish();
                        finish();
                        break;
                }
                break;
            case "recoger":
                switch (v.getId()) {
                    case R.id.ubicacion:
                        ubicaciones();
                        break;
                    case R.id.aseptar:
                        mano = textubicacion.getText().toString();
                        Intent iii = new Intent(BotonesUbicacion.this, MainActivity.class);
                        iii.putExtra("joto", mano);
                        startActivity(iii);
                        finish();
                        finish();
                        break;
                }

                break;
        }



    }


    private class AddressResultReceiver extends ResultReceiver{

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == constans.SUCCESS_RESULT){
                textubicacion.setText(resultData.getString(constans.RESULT_DATA_KEY));
                ubicacion = resultData.getString(constans.RESULT_DATA_KEY);

                    i = new Intent(BotonesUbicacion.this, MainActivity.class);
                    i.putExtra("puto", ubicacion);
                    startActivity(i);
                    finish();
                finish();

                puto = getIntent().getStringExtra("puto");
                switch (puto){
                    case "recoger":
                        i = new Intent(BotonesUbicacion.this, MainActivity.class);
                        i.putExtra("joto", ubicacion);
                        startActivity(i);
                        finish();
                        finish();

                }



            }else {
                Toast.makeText(BotonesUbicacion.this, resultData.getString(constans.RESULT_DATA_KEY),Toast.LENGTH_LONG).show();
            }
        }
    }
}