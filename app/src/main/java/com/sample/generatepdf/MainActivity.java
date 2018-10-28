package com.sample.generatepdf;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_generate;
    TextView tv_link;
    LinearLayout ll_pdflayout;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String pdfName;
    public  static  final String FILE_NAME = "file name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        fn_permission();
        listener();
    }


    private void init(){

        btn_generate = (Button)findViewById(R.id.btn_generate);
        ll_pdflayout = (LinearLayout)findViewById(R.id.ll_pdflayout);
        tv_link = (TextView)findViewById(R.id.tv_link);
        if(getIntent() != null){
            String text =
                    "Name: "+getIntent().getStringExtra("NAME")+"\n"+
                            "Mobile: "+getIntent().getStringExtra("MOBILE")+"\n"+
                            "Address: "+getIntent().getStringExtra("ADDRESS")+"\n"+
                            "Blood: "+getIntent().getStringExtra("BLOOD");
            tv_link.setText(text);
        }

    }



    private void listener(){
        btn_generate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_generate:

                if (boolean_save) {
                    Intent intent = new Intent(getApplicationContext(), PDFViewActivity.class);
                    intent.putExtra(FILE_NAME, pdfName);
                    startActivity(intent);

                } else {
                    if (boolean_permission) {
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Please wait");
                        ll_pdflayout.post(new Runnable() {
                            @Override
                            public void run() {
                                ll_pdflayout.buildDrawingCache(true);
                                Bitmap bitmap = ll_pdflayout.getDrawingCache();
                                ll_pdflayout.setDrawingCacheEnabled(false);
                                ll_pdflayout.destroyDrawingCache();
                                createImage(bitmap);
                            }
                        });
                    }
                    break;
                }
        }
    }

    private void createImage(Bitmap m_listview) {
        storeImage(m_listview, "test.jpg");

    }

    private void storeImage(Bitmap imageData, String filename) {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
            pdfName = "pdfdemo" + sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            storePdf(imageData, pdfName);
    }

    private void storePdf(Bitmap imageData, String filename){

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(ll_pdflayout.getWidth(), ll_pdflayout.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paintObj = new Paint();
        paintObj.setColor(Color.WHITE);
        canvas.drawPaint(paintObj);
        canvas.drawBitmap(imageData, 0, 0, paintObj);
        document.finishPage(page);

        File filePath = new File("/sdcard", filename);

        try{
            document.writeTo(new FileOutputStream(filePath));
            Log.d("TAG", "uspesno sacuvan pdf");
            btn_generate.setText("Check PDF");
            boolean_save=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

    }





    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
