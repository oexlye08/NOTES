package com.example.noteapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class tambah_activity extends AppCompatActivity {
    public static final String FILENAME="namafile.txt";
    EditText editText_name, editText_catatan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editText_name=findViewById(R.id.editText_name);
        editText_catatan=findViewById(R.id.editText_catatan);

/**UNTUK MENERIMA DATA DARI NOTES SEBELUMNYA**/
        Bundle extras  = getIntent().getExtras();
        if (extras !=null){
            String fileName = extras.getString("filename");
            editText_name.setText(fileName);
            getSupportActionBar().setTitle("Ubah Catatan");
        }else {
            getSupportActionBar().setTitle("Tambah Catatan");
        } bacaFile();
    }

/**MEMBACA FILE DARI CATATAN SEBELUMNYA YG SUDAH TERSIMPAN**/
    void bacaFile(){
        File path=getDir("NOTES", MODE_PRIVATE);
        File file =new File(path,editText_name.getText().toString());
        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line !=null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }
            editText_catatan.setText(text.toString());
        }
    }

/**MENAMBAHKAN AKSI UNTUK TOMBOL BACK DI ACTION BAR**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }return  super.onOptionsItemSelected(item);
    }


    public void save_btn(View view) {
        SaveData();
    }

/**MENYIMPAN DATA CATATAN**/
    void SaveData(){
        File path=getDir("NOTES", MODE_PRIVATE);
        File file=new File(path.toString(), editText_name.getText().toString());
        FileOutputStream outputStream=null;
        try {
            file.createNewFile();
            outputStream=new FileOutputStream(file,false);
            outputStream.write(editText_catatan.getText().toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        onBackPressed();
    }


}
