package net.f4grx.astrocalc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AstroCalc";
    private final HorizonCoords horiz;
    private final Sun sun;
    private double lat;
    private double lon;

    Timer timer;
    private TimerTask tsk;
    private TextView txtDate, txtUTC, txtJD, txtSiderealTime, txtSiderealAngle;
    private Button btnLocator;
    private TextView txtLocator, txtLat, txtLon;
    private TextView txtSunRA, txtSunDec, txtSunAlt, txtSunAz;

    public MainActivity() {
        horiz = new HorizonCoords();
        sun = new Sun();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtDate = findViewById(R.id.txtCurDate);
        txtUTC = findViewById(R.id.txtUTC);
        txtJD = findViewById(R.id.txtJD);

        btnLocator = findViewById(R.id.btnFromLocator);
        btnLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocator();
            }
        });

        txtLat = findViewById(R.id.txtLat);
        txtLon = findViewById(R.id.txtLon);
        txtLocator = findViewById(R.id.txtLocator);
        txtSiderealAngle = findViewById(R.id.txtSiderealangle);
        txtSiderealTime = findViewById(R.id.txtSiderealtime);

        txtSunRA = findViewById(R.id.txtSunRA);
        txtSunDec = findViewById(R.id.txtSunDec);
        txtSunAlt = findViewById(R.id.txtSunAlt);
        txtSunAz = findViewById(R.id.txtSunAz);
    }

    private TimeZone utcz = TimeZone.getTimeZone("UTC");
    private DateFormat dateformat = new SimpleDateFormat("EE MMM dd HH:mm:ss.SSS zzz yyyy", Locale.getDefault());

    private void onTimerCallback() {
        Calendar utc = Calendar.getInstance(utcz);

        horiz.setDate(utc);
        double jd = horiz.getJulianDate();

        horiz.setPos(lat, lon);

        dateformat.setTimeZone(TimeZone.getDefault());
        txtDate.setText(dateformat.format(utc.getTime()));
        dateformat.setTimeZone(utcz);
        txtUTC.setText(dateformat.format(utc.getTime()));
        txtJD.setText(Double.toString(jd));

        txtLat.setText(Double.toString(lat));
        txtLon.setText(Double.toString(lon));
        String locator = Locator.fromCoords(lat, lon);
        txtLocator.setText(locator==null?"N/A":locator);

        double sa = TimeUtil.sidereal(jd, lon);
        txtSiderealAngle.setText(Double.toString(sa*15));

        int[] st = TimeUtil.dms(sa);
        txtSiderealTime.setText(st[0]+":"+st[1]+":"+st[2]+"."+st[3]);

        double[] sunradec = Sun.pos(jd);
        txtSunRA.setText(Double.toString(sunradec[0]));
        txtSunDec.setText(Double.toString(sunradec[1]));

        double[] sunaltaz = horiz.toHorizontal(sunradec);
        txtSunAlt.setText(Double.toString(sunaltaz[0]));
        txtSunAz.setText(Double.toString(sunaltaz[1]));
    }

    @Override protected void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume");
        timer = new Timer();
        tsk = new TimerTask() {
            public void run() {
                onTimerCallbackInternal();
            }
        };
        timer.schedule(tsk, 100,100);
    }

    private void onTimerCallbackInternal() {
        runOnUiThread(new Runnable() {
            public void run() {
                onTimerCallback();
            }
        });
    }

    @Override protected void onPause() {
        super.onPause();
        Log.i(TAG, "OnPause");
        tsk.cancel();
        timer.cancel();
    }

    private void updateLocator() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter locator:");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);// | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String locator = input.getText().toString();
                double[] res = Locator.decode(locator);
                if(res != null && res.length == 2) {
                    lat = res[0];
                    lon = res[1];
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
