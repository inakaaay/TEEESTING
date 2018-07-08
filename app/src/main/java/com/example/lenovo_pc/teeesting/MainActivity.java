package com.example.lenovo_pc.teeesting;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.things.contrib.driver.apa102.Apa102;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.graphics.Color;

import static android.content.ContentValues.TAG;

/**https://www.survivingwithandroid.com/2017/10/how-to-integrate-android-things-with-firebase-firebase-iot-tutorial.html
 *
 */

public class MainActivity extends Activity {
    private Gpio mLedGpio;
    private static final String Button_name = "BCM6";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Step 1. Create GPIO connection.
        PeripheralManager service = PeripheralManager.getInstance();

        try {
            mLedGpio = service.openGpio(Button_name);

            // Step 2. Configure as an input.
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);


        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              Switch color = dataSnapshot.getValue(Switch.class);
              updatePin(mLedGpio, color.getled());
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
    }



    private void updatePin(Gpio pin, boolean con) {
        try {
            pin.setValue(con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
