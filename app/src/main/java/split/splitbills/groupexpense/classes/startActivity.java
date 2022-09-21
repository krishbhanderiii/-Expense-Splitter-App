package split.splitbills.groupexpense.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import split.splitbills.groupexpense.R;

public class startActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("shared prefrence", MODE_PRIVATE);

                if (prefs.getInt("idName", 0) == 100) {
                    startActivity(new Intent(startActivity.this, splashactivity.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    startActivity(new Intent(startActivity.this, guide.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        }, 500);
    }
}