package com.example.configuracion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ResultReceiver resultReceiver;
    private TextView uno, dos;
    Button btn1,btn2;
    EditText textubicacion,textentrega,verfecha,verhora,verfecha2,verhora2;
    Spinner carro;
    String ubicacion1,getUbicacion2, puto,seguro,joto;
    Intent i;
    String relleno[] = {"Todos","Pickup","compacto","Sedan","Sub"};
    int dia,mes,año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uno = (TextView) findViewById(R.id.txtentregar);
        textubicacion =(EditText)findViewById(R.id.textubicacion);
        textentrega =(EditText)findViewById(R.id.textentregan);
        verfecha =(EditText)findViewById(R.id.mostrar_fecha);
        verhora = (EditText)findViewById(R.id.mostrar_hora);
        verfecha2 =(EditText)findViewById(R.id.mostrar_fecha2);
        verhora2 = (EditText)findViewById(R.id.mostrar_hora2);
        carro = (Spinner)findViewById(R.id.carro);
        dos = (TextView) findViewById(R.id.txtrecoger);


        textubicacion.setOnClickListener(this);
        textentrega.setOnClickListener(this);
        verfecha.setOnClickListener(this);
        verhora.setOnClickListener(this);
        verfecha2.setOnClickListener(this);
        verhora2.setOnClickListener(this);
        carro.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line,relleno);
        carro.setAdapter(aa);


        puto = getIntent().getStringExtra("puto");
            joto = getIntent().getStringExtra("joto");
            if(puto == null && joto == null){
                //Toast.makeText(MainActivity.this,"aun no hay valores ",Toast.LENGTH_LONG).show();
            }  else if(puto !=null){
                SharedPreferences datos =this.getSharedPreferences("yo",Context.MODE_PRIVATE);
                SharedPreferences.Editor yolo = datos.edit();
                yolo.putString("id_yolo",puto);
                yolo.commit();
                textubicacion.setText(puto);

                String hy = datos.getString("id_yolo2","");
                textentrega.setText(hy);

              //  SharedPreferences kiko = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
               // String hy = datos.getString("id_yolo","");
                //Toast.makeText(MainActivity.this,hy,Toast.LENGTH_LONG).show();
            }else if (joto != null){
                SharedPreferences datos =this.getSharedPreferences("yo",Context.MODE_PRIVATE);
                SharedPreferences.Editor yolo = datos.edit();
                yolo.putString("id_yolo2",joto);
                yolo.commit();
                String hy = datos.getString("id_yolo","");
                textentrega.setText(joto);
                textubicacion.setText(hy);
            }







    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textubicacion:
                seguro = "ub";
                i = new Intent(MainActivity.this,BotonesUbicacion.class);
                Intent iii =new Intent(MainActivity.this,BotonesUbicacion.class);
                iii.putExtra("puto",seguro);
                startActivity(i);
                startActivity(iii);
                finish();
                finish();
                       break;
            case R.id.textentregan:
                seguro = "recoger";
                i = new Intent(MainActivity.this,BotonesUbicacion.class);
                Intent ii =new Intent(MainActivity.this,BotonesUbicacion.class);
                ii.putExtra("puto",seguro);
                startActivity(i);
                startActivity(ii);
                finish();
                finish();
                break;
            case R.id.mostrar_fecha:
                final Calendar nac = Calendar.getInstance();
                dia = nac.get(Calendar.DAY_OF_MONTH);
                mes = nac.get(Calendar.MONTH);
                año = nac.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        verfecha.setText(dayOfMonth+"/"+ (month + 1) + "/"+ year);

                    }
                },dia,mes,año);
                datePickerDialog.show();
                break;
                case  R.id.mostrar_hora:
                    final Calendar c = Calendar.getInstance();
                    int hour, minute;
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    verhora.setText(hour +":" +minute);
                                }
                            },hour,minute,false);
                    timePickerDialog.show();
                    break;
            case R.id.mostrar_fecha2:
                fecha();
                break;
            case R.id.mostrar_hora2:
                hora();
                break;


                }
        }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quieres Salir?")
                .setMessage("Estas seguro de salir de la Aplicacion?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences datos =MainActivity.this.getSharedPreferences("yo",Context.MODE_PRIVATE);
                        SharedPreferences.Editor yolo = datos.edit();
                        String gyt = null;
                        yolo.putString("id_yolo",gyt);
                        yolo.putString("id_yolo2",gyt);
                        yolo.commit();

                        finish();//cerrar la aplicacion xd
                    }
                }).create().show();
    }

    private  void fecha(){
        final Calendar nac = Calendar.getInstance();
        dia = nac.get(Calendar.DAY_OF_MONTH);
        mes = nac.get(Calendar.MONTH);
        año = nac.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                verfecha2.setText(dayOfMonth+"/"+ (month +1 ) + "/"+ year);

            }
        },dia,mes,año);
        datePickerDialog.show();

    }

    private void hora(){
        final Calendar c = Calendar.getInstance();
        int hour, minute;
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        verhora2.setText(hour +":" +minute);
                    }
                },hour,minute,false);
        timePickerDialog.show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}