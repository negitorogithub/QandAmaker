package unifar.unifar.qandamaker;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static unifar.unifar.qandamaker.CustomizedDialog_questionbook.newInstance;

public class MainActivity extends AppCompatActivity implements DialogListener {

    public static int viewFlag;
    public static int int_listview_position;
    public static ArrayList<HashMap<String,String>> listData;
    public static ArrayList<HashMap<String,String>> qlistData;
    public static ArrayList<HashMap<String,String>> alistData;
    public static String mainValue;
    public HashMap<String,String> hashTemp;
    static SimpleAdapter simp;
    static SimpleAdapter qsimp;
    CustomizedDialog_questionbook customizedDialog_questionbook;
    ListView R_id_listview ;
    public static int int_onLonglistView_Position;
    public final String FiletoSave1="Qbooks_1.text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlag = 1;
        int_listview_position=-1;
        listData = new ArrayList<>();
        qlistData = new ArrayList<>();
        alistData = new ArrayList<>();
        hashTemp = new HashMap<>();
        R_id_listview= (ListView)findViewById(R.id.listView);
        simp = new SimpleAdapter(MyApplication.getAppContext(),
                listData, R.layout.twolineitems,
                new String[]{"main", "right"}, new int[]{R.id.item_main, R.id.item_right});

        qsimp = new SimpleAdapter(MyApplication.getAppContext(),
                qlistData, R.layout.twolineitems,
                new String[]{"main", "right"}, new int[]{R.id.item_main, R.id.item_right});
        inputfromFile(FiletoSave1);
        (R_id_listview).setAdapter(simp);

        R_id_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("OnQbook",String.valueOf((listData.get(position)).get("main")));
                int_onLonglistView_Position=position;
                DialogFragment dialogFragment = MyAlarm.newInstance();
                dialogFragment.show(getFragmentManager(),"Alart");


                return false;
            }
        });
        R_id_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int_listview_position=position;
                mainValue = listData.get(position).get("main");
                        if (viewFlag == 1) {
                            viewFlag = 2;
                            //クイズの画面に遷移
                            qlistData.clear();
                            alistData.clear();
                            inputfromFile(plusTxt(String.valueOf(mainValue)));
                            (R_id_listview).setAdapter(qsimp);
                            return;
                        }
                        if (viewFlag == 2) {

                            Intent intent = new Intent(MyApplication.getAppContext(),DetailQuizActivity.class);
                            startActivity(intent);
                        }
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        customizedDialog_questionbook =new CustomizedDialog_questionbook();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                final CustomizedDialog_questionbook dialog =newInstance();
                dialog.show(getFragmentManager(),"dialog_fragment");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void listadd(String main, String eva , String adapter){
        Log.d("OnQBookBoxOkClick","listadd呼ばれたよん");
        hashTemp.clear();
        hashTemp.put("main",main);
        hashTemp.put("right",eva);
        if ("simp" == adapter){
            listData.add(new HashMap<>(hashTemp));
        }
        if ("qsimp" == adapter){
            qlistData.add(new HashMap<>(hashTemp));
        }
        if ("alist" == adapter){
            alistData.add(new HashMap<>(hashTemp));
        }



    }
    public void listadd(String main, String eva){
        this.listadd(main,eva,"simp");
    }

    public void onClickOk(){
        String file = plusTxt(CustomizedDialog_questionbook.questionStr);
        if (viewFlag ==1) {
            listData.clear();
            outputtoFile(FiletoSave1,CustomizedDialog_questionbook.questionStr);
            makefiles(plusTxt(CustomizedDialog_questionbook.questionStr));
            inputfromFile(FiletoSave1);
        }
        if (viewFlag ==2) {
            qlistData.clear();
            alistData.clear();
            outputtoFile(plusTxt(mainValue),CustomizedDialog_questionbook.questionStr);
            outputtoFile(plusTxt(mainValue),CustomizedDialog_questionbook.answerStr);
            for(int i =0;i<4;i++){
                outputtoFile(plusTxt(mainValue),String.valueOf(i));
            }
            inputfromFile(plusTxt(mainValue));
        }
        if (CustomizedDialog_questionbook.questionStr.equals("delete")){
            deletefiles(FiletoSave1);
        }
    }
    public void onClickOk_myalarm(){
        Log.d("OnQBookBoxOkClick","ポジション:"+ String.valueOf(int_onLonglistView_Position));
        Log.d("OnQBookBoxOkClick","リスト:"+ String.valueOf(listData));
        listData.remove(int_onLonglistView_Position);
        simp.notifyDataSetChanged();
        /* Snackbar.make(findViewById(R.id.activityMain_relativeLayout), "Snackbar test", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();

        */
    }

    public void outputtoFile(String file ,String str) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream= openFileOutput(file, MODE_APPEND);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.write(13);
            fileOutputStream.write(10);
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    public void inputfromFile(String file){
        FileInputStream fileInputStream;
        String text = null;
        int line =0;
        try {
            fileInputStream=openFileInput(file);
            String lineBuffer = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream,"UTF-8"),1024);
            while( (lineBuffer = bufferedReader.readLine()) != null ) {
                text = lineBuffer ;
                switch (viewFlag) {
                    case 1:
                        listadd(text, "B", "simp");
                        break;
                    case 2:
                        switch (line%6){
                            case 0:
                                listadd(text,"Q","qsimp");
                                break;
                            case 1:
                                listadd(text,"A","alist");
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;


                        }

                }
                line++;
            }
            bufferedReader.close();
        } catch (IOException e ) {
            e.printStackTrace();
        }
        }
    private void deletefiles(String file){
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openFileOutput(file,MODE_PRIVATE);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void makefiles(String file){
        deletefiles(file);
    }
    public String plusTxt(String str){
        String resultStr = str+".txt";
        return resultStr;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (viewFlag ==2){
                viewFlag = 1;
                (R_id_listview).setAdapter(simp);
                return false;
            }else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    }

