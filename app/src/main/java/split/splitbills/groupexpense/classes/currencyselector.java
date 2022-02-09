package split.splitbills.groupexpense.classes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import split.splitbills.groupexpense.R;

public class currencyselector extends AppCompatActivity {
    private Spinner spinner;
    private EditText GRPNAME;
    private Drawer drawer;

    @Override
    public void onBackPressed() {

    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.currencyselector);
        spinner = findViewById(R.id.currency);
        GRPNAME = findViewById(R.id.GRPNAME);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("eSplitt");

        new DrawerBuilder().withActivity(this).build();
//        SecondaryDrawerItem secondaryDrawerItem = (SecondaryDrawerItem) (new SecondaryDrawerItem().withIdentifier(3));

        this.drawer = new DrawerBuilder().withActivity(this).withHeader(R.layout.drawer_header).withToolbar(toolbar).addDrawerItems(((SecondaryDrawerItem) (new SecondaryDrawerItem().withIdentifier(1)).withName("HOME")).withIcon(R.drawable.ic_home), ((new SecondaryDrawerItem().withIdentifier(2)).withName("RATE US")).withIcon(R.drawable.rate_star), ((SecondaryDrawerItem) (new SecondaryDrawerItem().withIdentifier(3)).withName("PRIVACY POLICY")).withIcon(R.drawable.ic_lock)).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                Intent intent = null;
                switch ((int) iDrawerItem.getIdentifier()) {

                    case 1:
                        intent = new Intent(currencyselector.this, currencyselector.class);
                        intent.setFlags(67108864);
                        currencyselector.this.drawer.closeDrawer();
                        break;
                    case 2:
                        currencyselector.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=split.splitbills.groupexpense")));
                        currencyselector.this.drawer.closeDrawer();
                        break;
                    case 3:
                        currencyselector.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.termsfeed.com/live/cc6ebedc-b3de-4e63-8056-48ff7288a62f")));
                        currencyselector.this.drawer.closeDrawer();
                    default:
                        intent = null;
                        break;
                }
                if (null != intent) {
                    startActivity(intent);
                }
                return true;
            }
        }).build();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        Button saveCRN = findViewById(R.id.savecurrency);
        saveCRN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GRPNAME.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Please, Enter Group Name", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                            .show();
                } else if (spinner.getSelectedItem().toString().trim().equals("Currency")) {

                    Snackbar.make(v, "Please, Select Currency ", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                            .show();

                } else {

                    SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currency", spinner.getSelectedItem().toString());
                    editor.putString("grpname", GRPNAME.getText().toString().toUpperCase());
                    editor.apply();
                    startActivity(new Intent(currencyselector.this, MainActivity.class));

                }
            }

        });
    }
}