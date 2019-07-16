package com.example.noteapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity {
    ListView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list_item=findViewById(R.id.list_item);

/**MEMBERIKAN AKSI DARI LIST CATATAN KE EDIT**/
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int i, long l) {
                Intent intent=new Intent(home.this, tambah_activity.class);
                Map<String, Object> data = (Map<String, Object>)adapterView.getAdapter().getItem(i);
                intent.putExtra("filename", data.get("name").toString());
                Toast.makeText(home.this, "You Clicked " + data.get("name"),
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

/**TEKAN DAN TAHAN UNTUK MENGHAPUS**/
        list_item.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView,
                                           View view, int i, long l) {
                Map<String, Object> data = (Map<String, Object>)adapterView.getAdapter().getItem(i);
                tampilkanDialogKonfirmasiHapusCatatan(data.get("name").toString());
                return true;
            }
        });
    }
/**MENAMPILKAN DIALOG KONFIRMASI**/
    void tampilkanDialogKonfirmasiHapusCatatan(final String filename){
        new AlertDialog.Builder(this)
                .setTitle("Hapus Catatan Ini?")
                .setMessage("Apakah anda yakin ingin menghapus catatan"+ filename+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        hapusFile(filename);
                    }
                }) .setNegativeButton(android.R.string.no , null).
                show();
    }

/**AKSI UNTUK MENGHAPUS CATATAN**/
    void hapusFile(final String filename){
        File path= getDir("NOTES", MODE_PRIVATE);
        File file = new File(path.toString(), filename);
        if (file.exists()){
            file.delete();
        }
        mengambilListFilePadaFolder();
    }

/**MENAMPILKAN ICON PLUS DARI MENU**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_, menu);
        return true;
    }

/**MENAMBHAKNA AKSI TOMBOL PLUS**/
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_tambah:
        Intent tambah =new Intent(this, tambah_activity.class);
        startActivity(tambah);
        break;
        }
        return super.onOptionsItemSelected(item);
    }

/** MENAMPILKAN LIST VIEW DARI STORAGE**/
    @Override
    protected void onResume(){
        super.onResume();

        mengambilListFilePadaFolder();
    }
    void mengambilListFilePadaFolder(){
        File path = getDir("NOTES",MODE_PRIVATE);
        File directory=new File(path.toString());

        if (directory.exists()){
            File[] files = directory.listFiles();
            String[] filenames = new  String[files.length];
            String[] dateCreated = new  String[files.length];
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd MMM YYYY HH:mm:ss");
            ArrayList<Map <String, Object>> itemDataList=
                    new ArrayList<Map <String, Object>>();

            for (int i=0; i< files.length; i++) {
                filenames[i] = files[i].getName();
                Date lastModDate = new Date(files[i].lastModified());
                dateCreated[i]= simpleDateFormat.format(lastModDate);
                Map<String, Object>listItemMap = new HashMap<>();
                listItemMap.put("name", filenames[i]);
                listItemMap.put("date",dateCreated[i]);
                itemDataList.add(listItemMap);
            }
            SimpleAdapter simpleAdapter=
                    new SimpleAdapter(this, itemDataList, android.R.layout.simple_list_item_2,
                            new String[]{"name","date"},
                            new  int[]{android.R.id.text1, android.R.id.text2});
            list_item.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }
}
