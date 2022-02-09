package split.splitbills.groupexpense.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import split.splitbills.groupexpense.DATA.MODEL;
import split.splitbills.groupexpense.DATA.Member;
import split.splitbills.groupexpense.R;

public class splashactivity extends AppCompatActivity {
    private Button Button_start;
    private ArrayList<Member> members;
    private List<MODEL> MODELS = new ArrayList<>();

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        loadData();
        Button_start = findViewById(R.id.LETSSTART);

//        if (SDK_INT >= Build.VERSION_CODES.R) {
//
//            if (Environment.isExternalStorageManager()) {
//
//            } else {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//            }
//        }

        if (!members.isEmpty()) {
            startActivity(new Intent(splashactivity.this,
                    MainActivity.class));
        }
        if (!MODELS.isEmpty()) {
            startActivity(new Intent(splashactivity.this,
                    CreatExpence.class));
        }
        Button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(splashactivity.this,
                        currencyselector.class));

            }
        });

    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Member>>() {
        }.getType();
        members = gson.fromJson(json, type);
        if (members == null) {
            members = new ArrayList<>();
        }
        SharedPreferences shared_preferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson g_son = new Gson();
        String j_son = shared_preferences.getString("list", null);
        Type t_ype = new TypeToken<ArrayList<MODEL>>() {
        }.getType();
        MODELS = g_son.fromJson(j_son, t_ype);
        if (MODELS == null) {
            MODELS = new ArrayList<>();
        }
    }
}