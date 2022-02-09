package split.splitbills.groupexpense.classes;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import split.splitbills.groupexpense.DATA.MODEL;
import split.splitbills.groupexpense.DATA.Member;
import split.splitbills.groupexpense.Expences.ADAPTER;
import split.splitbills.groupexpense.R;

public class CreatExpence extends AppCompatActivity {
    private Spinner spinner;
    private String[] Members;
    private String[] Member;
    private EditText RUPEE;
    private EditText TITLE;
    private Button Next;
    private List<MODEL> Lists;
    private AlertDialog.Builder Dialog;
    private List<String> STRINGS = new ArrayList<String>();
    private List<MODEL> MODELS = new ArrayList<>();
    private RecyclerView recyclerView;
    private ADAPTER ADAPTER;
    private ArrayList<Member> members = new ArrayList<>();
    private String inputString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_creat_expence);

        ImageButton ADDExpence = (ImageButton) findViewById(R.id.ADDEXPENCE);

        Button NExt = findViewById(R.id.NEXTSHOWDATA);
        recyclerView = findViewById(R.id.EXPENCE_DATA);
        loadData();
        buildRecyclerView();


        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Member>>() {
        }.getType();
        members = gson.fromJson(json, type);

        int i = members.size();

        Members = new String[i];
        while (i != 0) {
            Members[i - 1] = members.get(i - 1).getName();
            i--;

        }

        int k = members.size();
        int j = members.size() + 1;

        Member = new String[j];
        while (j != 1) {
            Member[j - 1] = members.get(k - 1).getName();

            j--;
            k--;

        }
        Member[0] = "SELECT PAYER";
        if (Lists == null) {
            Lists = new ArrayList<>();
        }

        NExt.setOnClickListener(v -> {

            if (!MODELS.isEmpty()) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.animation, null);
                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        startActivity(new Intent(CreatExpence.this, Expence_Splitter.class));
                    }
                }, 3000);
            } else {
                Snackbar.make(v, "You must add at least one Expense", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                        .show();
            }

        });
        ADDExpence.setOnClickListener(view ->
        {
//            if (SDK_INT >= Build.VERSION_CODES.R) {
//
//                if (Environment.isExternalStorageManager()) {
//
//                } else {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                }
//            }
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.createxpence);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            spinner = dialog.findViewById(R.id.Spinner);
            RUPEE = dialog.findViewById(R.id.COSTS);
            TITLE = dialog.findViewById(R.id.TITLE_DISCRIPSION);
            Next = dialog.findViewById(R.id.NEXT_STEP);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatExpence.this, android.R.layout.simple_list_item_1, Member);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            spinner.setAdapter(adapter);

            dialog.show();
            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!RUPEE.getText().toString().trim().isEmpty()
                            && !TITLE.getText().toString().trim().isEmpty() && !spinner.getSelectedItem().equals("SELECT PAYER")) {
                        String rupee = RUPEE.getText().toString().trim();
                        String name = spinner.getSelectedItem().toString();
                        String title = TITLE.getText().toString().trim();

                        new Handler().postDelayed(() -> {
                            ChoosePerson(rupee, name, title);
                            dialog.dismiss();

                        }, 300);

                    } else if (TITLE.getText().toString().trim().isEmpty()) {
                        Snackbar.make(v, "Please, Enter Title ", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                                .show();

                    } else if (spinner.getSelectedItem().equals("SELECT PAYER")) {
                        Snackbar.make(v, "Please, Enter Payer ", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                                .show();
                    } else if (RUPEE.getText().toString().trim().isEmpty()) {
                        Snackbar.make(v, "Please, Enter Money ", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                                .show();

                    }
                }
            });
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CreatExpence.this));
        ADAPTER = new ADAPTER(MODELS, CreatExpence.this);
        recyclerView.setAdapter(ADAPTER);
        ADAPTER.notifyDataSetChanged();
    }

    private void saveData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MODELS);
        editor.putString("list", json);
        editor.apply();
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<MODEL>>() {
        }.getType();
        MODELS = gson.fromJson(json, type);
        if (MODELS == null) {
            MODELS = new ArrayList<>();
        }
    }

    @SuppressLint("ResourceType")
    private void ChoosePerson(String rupee, String name, String title) {

        MODEL ML = new MODEL();
        ML.setName(name);
        ML.setTitle(title);
        ML.setMoney(Integer.parseInt(rupee));
        Calendar c = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM   HH : mm");

        ML.setDate(format.format(c.getTime()));
        Dialog = new AlertDialog.Builder(CreatExpence.this);
        Dialog.setTitle(name.toUpperCase() + " HAS PAID FOR");
        Dialog.setMultiChoiceItems(Members, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (isChecked) {
                    STRINGS.add(Members[which]);
                } else if (STRINGS.contains(Members[which])) {
                    STRINGS.remove(Members[which]);
                }
            }
        });

        Dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Gson gson = new Gson();
                inputString = gson.toJson(STRINGS);
                ML.setGSONSTRING(inputString);
                Log.d("TAG", ML.GetPERSONS().toString());
                MODELS.add(ML);
                ADAPTER.notifyItemInserted(MODELS.size() - 1);
                saveData();
                Dialog.create().dismiss();
                if (MODELS.size() == 1) {
                    loadData();
                    buildRecyclerView();
//                    startActivity(new Intent(CreatExpence.this, CreatExpence.class));

                }
            }
        });
        Dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog.create().dismiss();
            }
        });

        Dialog.setNeutralButton("Select All",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListView list = ((AlertDialog) dialog).getListView();
                        for (int i = 0; i < list.getCount(); i++) {
                            list.setItemChecked(i, true);
                        }
                        STRINGS.addAll(Arrays.asList(Members));
                        Gson gson = new Gson();
                        inputString = gson.toJson(STRINGS);
                        ML.setGSONSTRING(inputString);
                        Log.d("TAG", ML.GetPERSONS().toString());
                        MODELS.add(ML);
                        ADAPTER.notifyItemInserted(MODELS.size() - 1);
                        saveData();
                        Dialog.create().dismiss();
                        if (MODELS.size() == 1) {
                            loadData();
                            buildRecyclerView();
//                        startActivity(new Intent(CreatExpence.this, CreatExpence.class));

                        }
                    }
                });


        Dialog.create().show();

        STRINGS.clear();

    }
}