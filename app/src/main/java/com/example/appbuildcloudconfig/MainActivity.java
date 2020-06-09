package com.example.appbuildcloudconfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    String latitude;
    String longitude;
    Map<String, Object> dataToSave;
    private DocumentReference mi = FirebaseFirestore.getInstance().document("Sam/98074");
    int i;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button) findViewById(R.id.button);

    }
    public void onClick(View view)
    {
        pusher();
        fetchUpdates();
        LocationProvider();
    }
    public void LocationProvider()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            fusedLocationProviderClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(4000);


            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    Log.e("TAG", "latitude = " + locationResult.getLastLocation().getLatitude() + "      longitude = " + locationResult.getLastLocation().getLongitude());


                    latitude = "" + (locationResult.getLastLocation().getLatitude());
                    longitude = "" + (locationResult.getLastLocation().getLongitude());


                    String Random = Math.random() + "";

                    dataToSave = new HashMap<>();
                    dataToSave.put(Random, latitude + "****" + longitude);






            }
        }, getMainLooper());
    } else {
        callPermissions();
    }

}

    private void callPermissions() {
        Permissions.check(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, "Location permission required", new Permissions.Options().setSettingsDialogTitle("Warning!").setRationaleDialogTitle("Info"), new PermissionHandler() {
            @Override
            public void onGranted() {
                LocationProvider();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                callPermissions();
            }
        });

    }




    public void pusher()
    {
        mi.update(dataToSave);
        fetchUpdates();
    }
  public void fetchUpdates()
  {
     mi.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {
         Map<String,Object> x = documentSnapshot.getData();
          Log.e("TAG","here is the data"+x);
         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Log.d("TAG","FAIL");
         }
     });
  }



}