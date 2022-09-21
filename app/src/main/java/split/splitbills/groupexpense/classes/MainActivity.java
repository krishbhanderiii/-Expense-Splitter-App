package split.splitbills.groupexpense.classes;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import split.splitbills.groupexpense.DATA.MODEL;
import split.splitbills.groupexpense.DATA.Member;
import split.splitbills.groupexpense.Expences.Adapter2;
import split.splitbills.groupexpense.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CONTACT_PICKER_REQUEST = 1;
    //    private static int FLAG = 0;
    private String[] Names = new String[200];
    private ArrayList<Member> members = new ArrayList<>();
    private Member member;
    private EditText editText;
    private RecyclerView mRecyclerView;
    private Adapter2 Adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button NEXTSTEP;
    private ImageButton ADD;
    private List<MODEL> MODELS = new ArrayList<>();


    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                List<String> name = Arrays.asList(Names);
                List<ContactResult> results = MultiContactPicker.obtainResult(data);

                saveData();
                loadData();
                if (!results.isEmpty()) {
                    for (int i = 0; i < results.size(); i++) {
                        if (name.contains(results.get(i).getDisplayName().toUpperCase().trim()))
                         {
                            Snackbar snackbar = Snackbar
                                    .make(this.getWindow().getDecorView().getRootView(), "SOME USERS ARE ALREADY EXITS", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
//                        Toast.makeText(MainActivity.this, results.get(i).getDisplayName().trim() + " IS AlREADY EXITS", Toast.LENGTH_LONG).show();
                         }
                         else
                         {
                            if (results.get(i).getPhoto() == null ) {
                                insertItem(results.get(i).getDisplayName().toUpperCase().trim(), null, results.get(i).getPhoneNumbers().get(0).getNumber());
                            }
//                            else if (results.get(i).getPhoto() == null && results.get(i).getPhoneNumbers().get(0).getNumber() != null) {
//                                insertItem(results.get(i).getDisplayName().toUpperCase().trim(), null, results.get(i).getPhoneNumbers().get(0).getNumber());
//                            }
//                            else if (results.get(i).getPhoto() != null && results.get(i).getPhoneNumbers() == null) {
//                                insertItem(results.get(i).getDisplayName().toUpperCase().trim(), results.get(i).getPhoto().toString(), null);
//                            }
                            else {
                                insertItem(results.get(i).getDisplayName().toUpperCase().trim(), results.get(i).getPhoto().toString(), results.get(i).getPhoneNumbers().get(0).getNumber());
                            }
                         }
                    }
                }

                saveData();
                loadData();
                buildRecyclerView();
                if (members.size() == 1) {
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
                    loadData();
                    buildRecyclerView();
                }

            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Permisionformedia();

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
        NEXTSTEP = findViewById(R.id.NEXT_STEP);
        NEXTSTEP.setOnClickListener(this);

        ADD = findViewById(R.id.ADD_MEMBER);
        ADD.setOnClickListener(this);
        loadData();
        buildRecyclerView();


    }

    private void Permisionforcontacts() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
//                            FLAG = 1;
                            new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
                                    .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                    .hideScrollbar(false) //Optional - default: false
                                    .showTrack(true) //Optional - default: true
                                    .searchIconColor(Color.BLACK) //Option - default: White
                                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
//                        .handleColor(ContextCompat.getColor(MainActivity.this, R.color.black)) //Optional - default: Azure Blue
//                        .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.white)) //Optional - default: Azure Blue
                                    .bubbleTextColor(Color.WHITE) //Optional - default: White
                                    .setTitleText("Select Contacts") //Optional - default: Select Contacts
                                    .setSelectedContacts("10", "5") //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                                    .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                                    .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                                    .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                            android.R.anim.fade_in,
                                            android.R.anim.fade_out) //Optional - default: No animation overrides
                                    .showPickerForResult(CONTACT_PICKER_REQUEST);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Snackbar snackbar = Snackbar
                                    .make(getWindow().getDecorView(), "Permission Required", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void Permisionformedia() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Snackbar snackbar = Snackbar
                                    .make(getWindow().getDecorView(), "Permission Required", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.RECYCLERVIEW);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        Adapter = new Adapter2(members, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(Adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                member = members.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                members.remove(position);
                Adapter.notifyItemRemoved(position);
                saveData();

                Snackbar.make(mRecyclerView, member.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        members.add(position, member);
                        Adapter.notifyItemInserted(position);
                        saveData();
                    }
                }).show();
            }
        }).attachToRecyclerView(mRecyclerView);


    }


    private void insertItem(String toString, String uri, String phonenumber) {
        members.add(new Member(toString, uri, phonenumber));
        Adapter.notifyItemInserted(members.size() - 1);

    }

    private void saveData() {
        Arrays.fill(Names, null);
        for (int i = 0; i < members.size(); i++) {
            Names[i] = members.get(i).getName().trim();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(members);
        editor.putString("task list", json);
        editor.apply();
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
        SharedPreferences shared_Preferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson g_son = new Gson();
        String j_son = shared_Preferences.getString("list", null);
        Type t_ype = new TypeToken<ArrayList<MODEL>>() {
        }.getType();
        MODELS = g_son.fromJson(j_son, t_ype);
        if (MODELS == null) {
            MODELS = new ArrayList<>();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ADD_MEMBER:
                final Dialog selectdialog = new Dialog(this);
                selectdialog.setContentView(R.layout.select_option);
                selectdialog.setCancelable(true);
                selectdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                (selectdialog.findViewById(R.id.SELECTMANUALLY)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectdialog.dismiss();
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.add_member);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        List<String> name = Arrays.asList(Names);
                        editText = dialog.findViewById(R.id.name);
                        (dialog.findViewById(R.id.saveButton)).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {

                                TextView Exits;
                                saveData();
                                if (!editText.getText().toString().trim().isEmpty()) {

                                    if (name.contains(editText.getText().toString().toUpperCase().trim())) {
                                        Exits = dialog.findViewById(R.id.exits);
                                        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                                        editText.setAnimation(shake);
                                        editText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                                        Exits.setText(editText.getText().toString().toUpperCase().trim() + " ALREADY EXITS");
                                    } else {
                                        editText.getBackground().clearColorFilter();
                                        insertItem(editText.getText().toString().toUpperCase().trim(), null, null);
                                        saveData();

                                        dialog.dismiss();
                                        if (members.size() == 1) {
//                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                                            loadData();
                                            buildRecyclerView();

                                        }

                                    }


                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(view, "Empty Field Not Allowed", Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(Color.WHITE);
                                    snackbar.setTextColor(Color.BLACK);
                                    snackbar.show();

                                }
                            }
                        });
                        dialog.show();
                    }
                });


                (selectdialog.findViewById(R.id.SELECFROMCONTACT)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectdialog.dismiss();
                        Permisionforcontacts();
//                        if (FLAG == 1) {
//                            new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
//                                    .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
//                                    .hideScrollbar(false) //Optional - default: false
//                                    .showTrack(true) //Optional - default: true
//                                    .searchIconColor(Color.BLACK) //Option - default: White
//                                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
////                        .handleColor(ContextCompat.getColor(MainActivity.this, R.color.black)) //Optional - default: Azure Blue
////                        .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.white)) //Optional - default: Azure Blue
//                                    .bubbleTextColor(Color.WHITE) //Optional - default: White
//                                    .setTitleText("Select Contacts") //Optional - default: Select Contacts
//                                    .setSelectedContacts("10", "5") //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
//                                    .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
//                                    .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
//                                    .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
//                                            android.R.anim.fade_in,
//                                            android.R.anim.fade_out) //Optional - default: No animation overrides
//                                    .showPickerForResult(CONTACT_PICKER_REQUEST);
//                        }

                    }
                });


                selectdialog.show();


                break;
            case R.id.NEXT_STEP:
                if (!members.isEmpty() && members.size() > 1) {
                    for (int i = 0; i < members.size(); i++) {
                        members.get(i).setID(i);
                        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(members);
                        editor.putString("task list", json);
                        editor.apply();
                    }
                    startActivity(new Intent(this, CreatExpence.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "You must add at least two persons", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                break;
        }
    }
}