package split.splitbills.groupexpense.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import split.splitbills.groupexpense.DATA.MODEL;
import split.splitbills.groupexpense.DATA.Member;
import split.splitbills.groupexpense.R;


public class Expence_Splitter extends AppCompatActivity {
    private List<MODEL> MODELS = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private ImageButton StarAgain;
    private ImageButton shareBTN;
    private PDFView pdfView;
    private String currency;
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private ImageButton imgbtn;
    private String GRPNAME;


    @Override
    public void onBackPressed() {

//        TextView text;
//        final Dialog dialog = new Dialog(Expence_Splitter.this);
//
//        dialog.setContentView(R.layout.confirmation_pop);
//
//        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        text = dialog.findViewById(R.id.title);
//        text.setText("Are You Sure to Exit?");
//
//        ((TextView) dialog.findViewById(R.id.conf_no_button)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        ((TextView) dialog.findViewById(R.id.conf_yes_button)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                dialog.dismiss();
//                Intent a = new Intent(Intent.ACTION_MAIN);
//                a.addCategory(Intent.CATEGORY_HOME);
//                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(a);
//            }
//        });
//
//        dialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"DefaultLocale", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_expence__splitter);


        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        currency = sp.getString("currency", null);
        GRPNAME = sp.getString("grpname", null);
        imgbtn = findViewById(R.id.back);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toastview,
                (ViewGroup) findViewById(R.id.tosteview_1));

        TextView text = (TextView) layout.findViewById(R.id.textontoast);
        TextView text_1 = (TextView) layout.findViewById(R.id.downloadtitle);
        text.setText(" \n Loacation :download/" + GRPNAME + ".pdf");
        text_1.setText("File downloaded");
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        StarAgain = findViewById(R.id.startAgain);
        shareBTN = findViewById(R.id.shareBTN);
        pdfView = findViewById(R.id.pdfView);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<MODEL>>() {
        }.getType();
        MODELS = gson.fromJson(json, type);


        SharedPreferences shared_Preferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson g_son = new Gson();
        String j_son = shared_Preferences.getString("task list", null);
        Type _type = new TypeToken<ArrayList<Member>>() {
        }.getType();
        members = g_son.fromJson(j_son, _type);
        shareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(Expence_Splitter.this, getPackageName() + ".provider", new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS), GRPNAME + ".pdf"));

                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(share, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                } else {
                    uri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), GRPNAME + ".pdf"));
                }


                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(share);

            }
        });
        StarAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
                settings.edit().clear().apply();
                startActivity(new Intent(Expence_Splitter.this, currencyselector.class));
            }
        });

        int temp = members.size();
        float[][] Spliteer = new float[temp][temp];
        float[][] FINAL = new float[temp][temp];


        for (int i = 0; i < temp; i++) {
            for (int j = 0; j < temp; j++) {
                Spliteer[i][j] = 0;
                FINAL[i][j] = 0;
            }
        }

        for (int k = 0; k < MODELS.size(); k++) {

            for (int i = 0; i < temp; i++) {

                String T = (String) MODELS.get(k).getName().trim();
                String Y = (String) members.get(i).getName().trim();

                if (Y.equals(T)) {
                    for (int j = 0; j < temp; j++) {

                        for (int l = 0; l < MODELS.get(k).GetPERSONS().size(); l++) {
                            String L = MODELS.get(k).GetPERSONS().get(l).trim();
                            String g = (String) members.get(j).getName().trim();
                            if (g.equals(L)) {
                                Spliteer[i][j] = Spliteer[i][j] + ((MODELS.get(k).getMoney()) / (MODELS.get(k).GetPERSONS().size()));
                            }
                        }

                    }

                }

            }
        }


        for (int i = 0; i < temp; i++) {
            for (int j = 0; j < temp; j++) {
                FINAL[i][j] = Spliteer[i][j] - Spliteer[j][i];

            }
        }
        float[] total = new float[temp];
        float[] debt = new float[temp];
        float[] dues = new float[temp];
        for (int i = 0; i < temp; i++) {
            total[i] = 0;
            debt[i] = 0;
            for (int j = 0; j < temp; j++) {
                total[i] = (float) total[i] + Spliteer[i][j];

                if (FINAL[i][j] < 0) {
                    debt[i] = debt[i] + FINAL[i][j];
                } else {
                    dues[i] = dues[i] + FINAL[i][j];
                }
            }
            debt[i] = debt[i] * (-1);

        }


        try {
            float total_1 = 0;
            float dues_1 = 0;
            float debt_1 = 0;


            String PDFPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(PDFPATH, GRPNAME + ".pdf");
            OutputStream outputStream = new FileOutputStream(file);

            PdfWriter writer = new PdfWriter(file);
            PdfDocument Pdfdocument = new PdfDocument(writer);
            Document document = new Document(Pdfdocument);

            Paragraph conclusion = new Paragraph("INVOICE").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(20);
            Paragraph space = new Paragraph("\n");
            float COLUMNINDEX[] = {140, 140, 140, 140};
            Table table = new Table(COLUMNINDEX);

            table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("NAME")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("USED")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("DUES")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("DEBT")).setBorder(Border.NO_BORDER));

            for (int i = 0; i < temp; i++) {

                table.addCell(new Cell().add(new Paragraph(members.get(i).getName().toUpperCase())).setBorder(Border.NO_BORDER));
                @SuppressLint("DefaultLocale") String strDouble = String.format("%.2f", total[i]);
                table.addCell(new Cell().add(new Paragraph(strDouble)).setBorder(Border.NO_BORDER));
                total_1 = total_1 + total[i];
                strDouble = String.format("%.2f", dues[i]);
                table.addCell(new Cell().add(new Paragraph(strDouble)).setBorder(Border.NO_BORDER));
                dues_1 = dues_1 + dues[i];
                if (debt[i] <= 0) {
                    debt[i] = debt[i] * (-1);
                }
                strDouble = String.format("%.2f", debt[i]);
                table.addCell(new Cell().add(new Paragraph(strDouble)).setBorder(Border.NO_BORDER));
                debt_1 = debt_1 + debt[i];

            }
            table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph(String.valueOf(total_1))).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph(String.valueOf(dues_1))).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph(String.valueOf(debt_1))).setBorder(Border.NO_BORDER));

            float column_index[] = {110, 110, 110, 110, 110};
            Table table_1 = new Table(column_index);

            table_1.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("EXPENSE")).setBorder(Border.NO_BORDER));
            table_1.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("PAYER")).setBorder(Border.NO_BORDER));
            table_1.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("MONEY")).setBorder(Border.NO_BORDER));
            table_1.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("DATE/TIME")).setBorder(Border.NO_BORDER));
            table_1.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN, 4).add(new Paragraph("PAID FOR")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));


            for (int i = 0; i < MODELS.size(); i++) {

                table_1.addCell(new Cell().add(new Paragraph(MODELS.get(i).getTitle().toUpperCase())).setBorder(Border.NO_BORDER));
                table_1.addCell(new Cell().add(new Paragraph(MODELS.get(i).getName().toUpperCase())).setBorder(Border.NO_BORDER));
                table_1.addCell(new Cell().add(new Paragraph(String.valueOf(MODELS.get(i).getMoney()).toUpperCase())).setBorder(Border.NO_BORDER));
                table_1.addCell(new Cell().add(new Paragraph(String.valueOf(MODELS.get(i).getDate().toUpperCase()))).setBorder(Border.NO_BORDER));
                table_1.addCell(new Cell().add(new Paragraph(String.valueOf(MODELS.get(i).GetPERSONS()).toUpperCase())).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

            }
            @SuppressLint("UseCompatLoadingForDrawables") Drawable d = getDrawable(R.drawable.spalsh_logo);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] Bitmapdata = stream.toByteArray();

            ImageData imagedata = ImageDataFactory.create(Bitmapdata);
            Image image = new Image(imagedata).setWidth(120).setHeight(100);


            document.add(image);
            document.add(new Paragraph("created by eSplitt App").setFontSize(15).setMarginLeft(5).setTextAlignment(TextAlignment.LEFT).setFontColor(ColorConstants.BLACK));
            document.add(conclusion);
            document.add(table_1);
            document.add(space);
            document.add(space);
            document.add(table);
            document.add(space);
            document.add(space);
            for (int i = 0; i < temp; i++) {
                for (int j = 0; j < temp; j++) {
                    if (FINAL[i][j] < 0) {
                        Paragraph pr = new Paragraph("  " + members.get(i).getName().toUpperCase() + " WILL HAVE TO PAY " + currency + "  " + FINAL[i][j] * (-1) + " TO " + members.get(j).getName().toUpperCase() + " ").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(13);
                        document.add(pr);
                    }
                }
            }
            document.close();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    String PDFPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    File file = new File(PDFPATH, GRPNAME + ".pdf");
                    if (file == null) {
                        Snackbar.make(pdfView, "PERMISSION REQUIRED", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                                .show();
                    } else {
                        pdfView.fromFile(file).load();

                    }
                }
            }, 1000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}

