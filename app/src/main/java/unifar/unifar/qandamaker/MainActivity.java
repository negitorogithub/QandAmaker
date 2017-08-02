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
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import static unifar.unifar.qandamaker.CustomizedDialog_questionbook.newInstance;

public class MainActivity extends AppCompatActivity implements DialogListener {

    public static final int INT_QfileLinesPerOneQuestion = 100;
    public static final int INT_QfileQuestioniIndex = 0;
    public static final int INT_QfileAnswerIndex = 1;
    public static final int INT_QfileTagIndex = 2;


    public static int viewFlag;
    public static int int_listview_position;
    public static ArrayList<HashMap<String, String>> listData;
    public static ArrayList<HashMap<String, String>> qlistData;
    public static ArrayList<HashMap<String, String>> alistData;
    public static String mainValue;
    public static String mainValue_longclick;
    public static String[] arraystr_qbook_names;
    public static HashMap<String, String> hashTemp;
    static SimpleAdapter simp;
    static SimpleAdapter qsimp;
    public CustomizedDialog_questionbook customizedDialog_questionbook;
    ListView R_id_listview;
    public static int int_onLonglistView_Position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("onqbook",removeExtension("aaaa.txt"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlag = 1;
        int_listview_position = -1;
        listData = new ArrayList<>();
        qlistData = new ArrayList<>();
        alistData = new ArrayList<>();
        hashTemp = new HashMap<>();
        arraystr_qbook_names = this.fileList();
        R_id_listview = (ListView) findViewById(R.id.listView);
        simp = new SimpleAdapter(MyApplication.getAppContext(),
                listData, R.layout.twolineitems,
                new String[]{"main", "right"}, new int[]{R.id.item_main, R.id.item_right});

        qsimp = new SimpleAdapter(MyApplication.getAppContext(),
                qlistData, R.layout.twolineitems,
                new String[]{"main", "right"}, new int[]{R.id.item_main, R.id.item_right});
        inputQbookFiles();
        (R_id_listview).setAdapter(simp);

        R_id_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (viewFlag == 1) {
                    mainValue_longclick = (listData.get(position)).get("main");
                }
                if (viewFlag == 2) {
                    mainValue_longclick = (qlistData.get(position)).get("main");
                }

                Log.d("OnQbook", String.valueOf(mainValue_longclick));
                int_onLonglistView_Position = position;
                DialogFragment dialogFragment = MyAlarm.newInstance();
                dialogFragment.show(getFragmentManager(), "Alart");


                return true;
            }
        });
        R_id_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int_listview_position = position;
                if (viewFlag == 1) {
                    viewFlag = 2;
                    Log.d("OnQbook", "1 -> 2");
                    //クイズの画面に遷移
                    mainValue = listData.get(position).get("main");
                    qlistData.clear();
                    alistData.clear();
                    inputfromFile(String.valueOf(mainValue));
                    (R_id_listview).setAdapter(qsimp);
                    return;
                }
                if (viewFlag == 2) {

                    Intent intent = new Intent(MyApplication.getAppContext(), DetailQuizActivity.class);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        customizedDialog_questionbook = new CustomizedDialog_questionbook();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customizedDialog_questionbook.show(getFragmentManager(), "dialog_fragment");
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

    public void listadd(String main, String eva, String adapter) {
        Log.d("OnQBookBoxOkClick", "listadd呼ばれたよん");
        hashTemp.clear();
        hashTemp.put("main", main);
        hashTemp.put("right", eva);
        if ("simp" == adapter) {

            listData.add(new HashMap<>(hashTemp));
            simp.notifyDataSetChanged();
            return;
        }
        if ("qsimp" == adapter) {
            qlistData.add(new HashMap<>(hashTemp));
            qsimp.notifyDataSetChanged();
            return;
        }
        if ("alist" == adapter) {
            alistData.add(new HashMap<>(hashTemp));
            return;
        }
        Log.d("onqbook", "listaddの無効なアダプター名が指定されています。" + adapter + "は無効です。simp,qsimp,alistのいずれかを指定してください。");


    }

    public void listadd(String main, String eva) {
        this.listadd(main, eva, "simp");
    }

    public void onClickOk() {
        if (viewFlag == 1) {
            listData.clear();
            makefiles(customizedDialog_questionbook.questionStr);
            inputQbookFiles();
        }
        if (viewFlag == 2) {
            qlistData.clear();
            alistData.clear();
            outputtoFile(mainValue, customizedDialog_questionbook.questionStr);
            outputtoFile(mainValue, customizedDialog_questionbook.answerStr);
            outputtoFile(mainValue, customizedDialog_questionbook.str_tag_name);
            for (int i = 0; i < 97; i++) {
                outputtoFile(mainValue, String.valueOf(i));
            }
            inputfromFile(mainValue);
        }
        if (viewFlag == 3) {
            //do nothing
        }

    }

    public void onClickOk_myalarm() {
        Log.d("OnQBookBoxOkClick", "ポジション:" + String.valueOf(int_onLonglistView_Position));
        Log.d("OnQBookBoxOkClick", "データ:" + String.valueOf(plusTxt(mainValue_longclick)));
        if (viewFlag == 1) {
            MyApplication.getAppContext().deleteFile(plusTxt(mainValue_longclick));
            listData.clear();
            inputQbookFiles();
            simp.notifyDataSetChanged();
        }
        if (viewFlag == 2) {
            delete100Line(mainValue, int_onLonglistView_Position, "qsimp");
            alistData.clear();
            qlistData.clear();
            inputfromFile(mainValue);
            qsimp.notifyDataSetChanged();
        }

        /* Snackbar.make(findViewById(R.id.activityMain_relativeLayout), "Snackbar test", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();

        */
    }

    void outputtoFile(String file, String str) {
        removeExtension(file);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = MyApplication.getAppContext().openFileOutput(plusTxt(file), MODE_APPEND);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.write(13);
            fileOutputStream.write(10);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void inputfromFile(String file) {
        ArrayList<String> textBuffer;
        textBuffer = inputFromFileToArray(file);
        for (int i = 0; i < textBuffer.size();i++) {
            switch (viewFlag) {
                case 1:
                    Log.d("onqbook", "viewFlag = 1でinputfromFileを呼ばないでください");
                    break;
                case 2:
                    switch (i % INT_QfileLinesPerOneQuestion) {
                        case INT_QfileQuestioniIndex:
                            listadd(textBuffer.get(i), "Q", "qsimp");
                            break;
                        case INT_QfileAnswerIndex:
                            listadd(textBuffer.get(i), "A", "alist");
                            break;
                        case INT_QfileTagIndex:
                            break;
                        // 3-99 lines are empty.
                        case INT_QfileLinesPerOneQuestion-1:
                            break;

                    }

            }
        }
    }

    private void resetfiles(String file) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = MyApplication.getAppContext().openFileOutput(plusTxt(file), MODE_PRIVATE);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void makefiles(String file) {
        resetfiles(file);
    }

    public static String plusTxt(String str) {
        return str + ".txt";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewFlag == 2) {
                Log.d("OnQbook", "2 -> 1");
                viewFlag = 1;
                listData.clear();
                inputQbookFiles();
                R_id_listview.setAdapter(simp);
                simp.notifyDataSetChanged();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void inputQbookFiles() {
        arraystr_qbook_names = MyApplication.getAppContext().fileList();
        for (int i = 0; i < arraystr_qbook_names.length; i++) {
            listadd(removeExtension(arraystr_qbook_names[i]), "B", "simp");
        }
    }

    public static String removeExtension(String fileName) {
        String newName;
        int lastPosition = fileName.lastIndexOf('.');
        if (lastPosition > 0) {
            newName = fileName.substring(0, lastPosition);
        } else {
            newName = fileName;
        }
        return newName;
    }

    public void delete100Line(String file , int index , String adapter){

        ArrayList<String> text_buffer =new ArrayList<>();
        ArrayList<String> textBufferBuffer = inputFromFileToArray(file);
        int delta ;
        for (int i = 0;i < textBufferBuffer.size(); i++){
            delta = i+1 - index*INT_QfileLinesPerOneQuestion;
            if ((delta>0)&(delta<INT_QfileLinesPerOneQuestion+1)){
                // do nothing
            }else {
                switch (adapter){
                    case "qsimp":
                        text_buffer.add(textBufferBuffer.get(i) );
                        break;
                }
            }

        }
        resetfiles(file);
            for(int i = 0; i < text_buffer.size(); i++){
                outputtoFile(file, text_buffer.get(i));
        }


    }
    public Set<String> makeTagSetfromFile(String file){
        Set<String> tagSet =new HashSet<>();
        ArrayList<String> text_buffer ;
        text_buffer = inputFromFileToArray(file);
        for (int i = INT_QfileTagIndex; i < text_buffer.size(); i = i+INT_QfileLinesPerOneQuestion){
            tagSet.add(text_buffer.get(i));
        }
        return tagSet;
    }
    public static ArrayList<String> inputFromFileToArray(String file){
        ArrayList<String> contentsArray = null;
        removeExtension(file);
        FileInputStream fileInputStream;
        String text = null;
        int line = 0;
        try {
            fileInputStream = MyApplication.getAppContext().openFileInput(plusTxt(file));
            String lineBuffer = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"), 10000);
            contentsArray = new ArrayList<>();
            while ((lineBuffer = bufferedReader.readLine()) != null) {
                text = lineBuffer;
                contentsArray.add(text);
                line++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentsArray;
    }
}

