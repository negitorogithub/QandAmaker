package unifar.unifar.qandamaker;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// TODO:戻るボタンの実装
// TODO:編集機能の実装
// TODO:OnPause()時のBundleの実装
// TODO:qsimpのタイトル
public class MainActivity extends AppCompatActivity implements DialogListener, ExamDialogFragment.OnFragmentInteractionListener {

    public static final int INT_QfileLinesPerOneQuestion = 100;
    public static final int INT_QfileQuestionIndex = 0;
    public static final int INT_QfileAnswerIndex = 1;
    public static final int INT_QfileTagIndex = 2;
    public static final int INT_QfileAnswerHistoryIndex1 = 3;
    public static final int INT_QfileAnswerHistoryIndex2 = 4;
    public static final int INT_QfileAnswerHistoryIndex3 = 5;
    public static final int INT_QfileLastIndex = 5;
    public static int R_id_listviewHeight;
    public static int viewFlag;
    public static int int_listview_position;
    private static List<String> listData;
    private static List<String> qlistData;
    private static List<String> alistData;
    private static List<String> taglistData;
    private static List<Boolean[]> historyData;
    public static ArrayList<Question> examQuestionsDataBuffer;
    public static String mainValue;
    public static String mainValue_longclick;
    public static String[] arraystr_qbook_names;
    public static HashMap<String, String> hashTemp;
    static ArrayAdapter simp;
    static QBookListAdapter qsimp;
    public CustomizedDialog_questionbook customizedDialog_questionbook;
    public ImageView imageViewHeader;
    public static ListView R_id_listview;
    public static int int_onLonglistView_Position;
    public static LinearLayout.LayoutParams layoutParams;
    Toolbar  toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlag = 1;
        int_listview_position = -1;
        listData = new ArrayList<>();
        qlistData = new ArrayList<>();
        alistData = new ArrayList<>();
        taglistData = new ArrayList<>();
        historyData = new ArrayList<>();
        hashTemp = new HashMap<>();
        arraystr_qbook_names = this.fileList();
        examQuestionsDataBuffer = new ArrayList<>();

        toolbar = (Toolbar)findViewById(R.id.toolbar) ;
        toolbar.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.colorPrimary));
        toolbar.setTitleTextColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        R_id_listview = (ListView) findViewById(R.id.listView);
        imageViewHeader = (ImageView) getLayoutInflater().inflate(R.layout.qheader, null);
        R_id_listview.addHeaderView(imageViewHeader, null, true);

        simp = new QBooksListAdapter(MyApplication.getAppContext(),
                R.layout.qbooksitem,
                listData
        );

        qsimp = new QBookListAdapter(MyApplication.getAppContext(),
                R.layout.questionslistitem,
                qlistData
        );


        inputQbookFiles();
        (R_id_listview).setAdapter(simp);
        R_id_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    int_onLonglistView_Position = position - 1;
                    if (viewFlag == 1) {
                        mainValue_longclick = listData.get(int_onLonglistView_Position);
                    }
                    if (viewFlag == 2) {
                        mainValue_longclick = qlistData.get(int_onLonglistView_Position);
                    }

                    Log.d("OnQbook", String.valueOf(mainValue_longclick));

                    DialogFragment dialogFragment = MyAlarm.newInstance();
                    dialogFragment.show(getFragmentManager(), "Alart");
                }


                return true;
            }
        });
        R_id_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int_listview_position = position - 1;
                if (viewFlag == 1) {
                    if (position != 0) {

                        viewFlag = 2;
                        Log.d("OnQbook", "1 -> 2");
                        //クイズの画面に遷移
                        mainValue = listData.get(int_listview_position);
                        reloadLists();
                        Log.d("OnQbook", "to qsimp");
                        (R_id_listview).setAdapter(qsimp);
                    } else {
                        Log.d("OnQbook", "// diplay ad");
                    }

                    return;
                }
                if (viewFlag == 2) {
                    if (position != 0) {
                        reloadLists();
                        Intent intent = new Intent(MyApplication.getAppContext(), DetailQuizActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("OnQbook", "// do test");
                        if (qlistData.size() != 0) {
                            ExamDialogFragment examDialogFragment = new ExamDialogFragment();
                            examDialogFragment.show(getFragmentManager(), "ExamDialogFragment");
                        }

                    }
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        customizedDialog_questionbook = new CustomizedDialog_questionbook();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.bundle.putBoolean(CustomizedDialog_questionbook.IsRecreatedKeyStr, false);
                reloadLists();
                customizedDialog_questionbook.show(getFragmentManager(), "firstInputDialog");
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        R_id_listviewHeight = R_id_listview.getHeight();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) convertDp2Px(72, MyApplication.getAppContext()));
        reloadAdapter();

    }
    @Override
    public void onResume(){
        super.onResume();
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


    public void onClickOk() {
        //
        Log.d("onqbook", "onClickOk();");
        if (viewFlag == 1) {
            listData.clear();
            makefiles(MyApplication.bundle.getString("questionStr"));
            inputQbookFiles();
        }
        if (viewFlag == 2) {
            customizedDialog_questionbook.str_tag_name = MyApplication.bundle.getString("str_tag_name");
            Log.d("onqbook", "questionStr:" + customizedDialog_questionbook.questionStr);
            outputtoFile(mainValue, MyApplication.bundle.getString("questionStr"));
            outputtoFile(mainValue, MyApplication.bundle.getString("answerStr"));
            outputtoFile(mainValue, MyApplication.bundle.getString("str_tag_name"));
            outputtoFile(mainValue, "false");
            outputtoFile(mainValue, "false");
            outputtoFile(mainValue, "false");

            for (int i = 0; i < INT_QfileLinesPerOneQuestion - 1 - INT_QfileLastIndex; i++) {
                outputtoFile(mainValue, String.valueOf(i));
            }
            reloadLists();


        }
        if (viewFlag == 3) {
            if (MyApplication.bundle.getBoolean(CustomizedDialog_questionbook.IsRecreatedKeyStr)) {
                Fragment firstDialog = getFragmentManager().findFragmentByTag("firstInputDialog");
                getFragmentManager().beginTransaction().remove(firstDialog).commit();
            }
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
            delete100Line(mainValue, int_onLonglistView_Position);
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

    static void outputtoFile(String file, String str) {
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

    static void outputtoFileByList(String file, List<String> list) {
        removeExtension(file);
        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = MyApplication.getAppContext().openFileOutput(plusTxt(file), MODE_APPEND);
            for (int i = 0; i < list.size(); i++) {
                fileOutputStream.write(list.get(i).getBytes());
                fileOutputStream.write(13);
                fileOutputStream.write(10);
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static ArrayList<Question> makeQuestionsArrayFromFile(String file){
        ArrayList<Question> questionArrayList = new ArrayList<>();
        Question questionForExam = new Question();
        ArrayList<String> textBuffer = inputFromFileToArray(file);
        Boolean resultsBuffer[] = new Boolean[3];
        if (textBuffer.size() > 99) {
            for (int i = 0; i < textBuffer.size(); i++) {
                        switch (i % INT_QfileLinesPerOneQuestion) {
                            case INT_QfileQuestionIndex:
                                questionForExam.setQuestionName(textBuffer.get(i));
                                break;
                            case INT_QfileAnswerIndex:
                                questionForExam.setAnswerName(textBuffer.get(i));

                                break;
                            case INT_QfileTagIndex:
                                questionForExam.setTagName(textBuffer.get(i));
                                break;
                            case INT_QfileAnswerHistoryIndex1:
                                resultsBuffer[0] = returnBooleanByString(CustomizedDialog_questionbook.ifNullReplace(textBuffer.get(i)));
                                break;
                            case INT_QfileAnswerHistoryIndex2:
                                resultsBuffer[1] = returnBooleanByString(CustomizedDialog_questionbook.ifNullReplace(textBuffer.get(i)));
                                break;
                            case INT_QfileAnswerHistoryIndex3:
                                resultsBuffer[2] = returnBooleanByString(CustomizedDialog_questionbook.ifNullReplace(textBuffer.get(i)));
                                questionForExam.setResults(resultsBuffer.clone());
                                resultsBuffer[0] = null;
                                resultsBuffer[1] = null;
                                resultsBuffer[2] = null;
                                break;
                            // 6-99 lines are empty.
                            case INT_QfileLastIndex+1:
                                i=i+INT_QfileLinesPerOneQuestion-INT_QfileLastIndex-2;
                            case INT_QfileLinesPerOneQuestion - 1:
                                questionForExam.setIndex(i/INT_QfileLinesPerOneQuestion);
                                questionArrayList.add(questionForExam.clone());
                                questionForExam.resetAll();
                                break;
                        }
                }
            }
        return questionArrayList;

    }


    static void inputfromFile(String file) {
        Question questionForExam = new Question();
        ArrayList<String> textBuffer;
        textBuffer = inputFromFileToArray(file);
        Boolean resultsBuffer[] = new Boolean[3];
        if (textBuffer.size() > 99) {
            for (int i = 0; i < textBuffer.size(); i++) {
                switch (viewFlag) {
                    case 1:
                        Log.d("onqbook", "viewFlag = 1でinputfromFileを呼ばないでください");
                        break;
                    case 2:
                        switch (i % INT_QfileLinesPerOneQuestion) {
                            case INT_QfileQuestionIndex:
                                addQlistData(textBuffer.get(i));
                                questionForExam.setQuestionName(textBuffer.get(i));
                                break;
                            case INT_QfileAnswerIndex:
                                addAlistData(textBuffer.get(i));
                                questionForExam.setAnswerName(textBuffer.get(i));

                                break;
                            case INT_QfileTagIndex:
                                addTaglistData(textBuffer.get(i));
                                questionForExam.setTagName(textBuffer.get(i));
                                break;
                            case INT_QfileAnswerHistoryIndex1:
                                resultsBuffer[0] = returnBooleanByString(CustomizedDialog_questionbook.ifNullReplace(textBuffer.get(i)));
                                break;
                            case INT_QfileAnswerHistoryIndex2:
                                resultsBuffer[1] = returnBooleanByString(CustomizedDialog_questionbook.ifNullReplace(textBuffer.get(i)));
                                break;
                            case INT_QfileAnswerHistoryIndex3:
                                resultsBuffer[2] = returnBooleanByString(CustomizedDialog_questionbook.ifNullReplace(textBuffer.get(i)));
                                addHistoryData(resultsBuffer.clone());
                                questionForExam.setResults(resultsBuffer.clone());
                                resultsBuffer[0] = null;
                                resultsBuffer[1] = null;
                                resultsBuffer[2] = null;
                                break;
                            // 6-99 lines are empty.
                            case INT_QfileLinesPerOneQuestion - 1:
                                questionForExam.setIndex(i/INT_QfileLinesPerOneQuestion);
                                examQuestionsDataBuffer.add(questionForExam.clone());
                                questionForExam.resetAll();
                                break;
                        }
                }
            }
        }
    }

    private static Boolean returnBooleanByString(String string) {
        Boolean b = false;
        if (string.equals("true")) {
            b = true;
        }
        return b;

    }

    private static void resetfiles(String file) {
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
        for (String arraystr_qbook_name : arraystr_qbook_names) {
            addListData(removeExtension(arraystr_qbook_name));
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

    public static void delete100Line(String file, int index) {

        ArrayList<String> text_buffer = new ArrayList<>();
        ArrayList<String> textBufferBuffer = inputFromFileToArray(file);
        int delta;
        for (int i = 0; i < textBufferBuffer.size(); i++) {
            delta = i + 1 - index * INT_QfileLinesPerOneQuestion;
            if (!((delta > 0) & (delta < INT_QfileLinesPerOneQuestion + 1))) {
                text_buffer.add(textBufferBuffer.get(i));
                break;

            }

        }
        resetfiles(file);
        for (int i = 0; i < text_buffer.size(); i++) {
            outputtoFile(file, text_buffer.get(i));
        }
        reloadLists();

    }

    public static ArrayList<String> inputFromFileToArray(String file) {
        ArrayList<String> contentsArray = new ArrayList<>();
        removeExtension(file);
        FileInputStream fileInputStream;
        String text;
        try {
            fileInputStream = MyApplication.getAppContext().openFileInput(plusTxt(file));
            String lineBuffer;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"), 10000);
            contentsArray = new ArrayList<>();
            while ((lineBuffer = bufferedReader.readLine()) != null) {
                text = lineBuffer;
                contentsArray.add(text);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentsArray;
    }

    public static void reloadLists() {
        listData.clear();
        qlistData.clear();
        alistData.clear();
        taglistData.clear();
        historyData.clear();
        examQuestionsDataBuffer.clear();

        if (mainValue != null) {
            inputfromFile(mainValue);
        }
        reloadAdapter();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    public static List<String> makeAlterrnatives(String file, String tagName, String answer) {
        List<String> alternatives;
        ArrayList<String> textBuffer = inputFromFileToArray(file);
        List<String> allAnswer = new ArrayList<>();
        final int FULLALTERNATIVESAMOUNT = 5;
        int alternativesAmount;
        for (int i = 0; i < textBuffer.size(); i++) {
            if (i % 100 == INT_QfileTagIndex) {
                if (textBuffer.get(i).equals(tagName)) {
                    if (!textBuffer.get(i - 1).equals(answer)) {
                        allAnswer.add(textBuffer.get(i - 1));
                    }
                }
            }
        }
        Collections.shuffle(allAnswer);
        if (allAnswer.size() < FULLALTERNATIVESAMOUNT - 1) {
            alternativesAmount = allAnswer.size();
        } else {
            alternativesAmount = FULLALTERNATIVESAMOUNT - 1;
        }
        alternatives = allAnswer.subList(0, alternativesAmount);
        alternatives.add(answer);
        Collections.shuffle(alternatives);
        if (!(alternatives.size() < 3)) {
            alternatives.remove(alternatives.size() - 1);
            alternatives.add("この選択肢の中には無い");
        }
        return alternatives;
    }

    public static void makeAnswerHistoryByQuestionsList(String file,List<Question> questionList) {
        ArrayList<String> textBuffer = new ArrayList<>();
        ArrayList<String> textBufferBuffer = inputFromFileToArray(file);
        Collections.sort(questionList, new Comparator<Question>() {
            @Override
            public int compare(Question q1, Question q2) {
                return q1.getIndex()-q2.getIndex();
            }
        });
        int j=0;
        for (int i = 0; i < textBufferBuffer.size(); i++) {
            textBuffer.add(textBufferBuffer.get(i));
            if (j < questionList.size()) {
                if (i == questionList.get(j).getIndex() * INT_QfileLinesPerOneQuestion + INT_QfileAnswerHistoryIndex1) {
                    textBuffer.remove(textBuffer.size() - 1);
                    textBuffer.add(questionList.get(j).getResultBuffer().toString());
                    textBuffer.add(textBufferBuffer.get(i));
                }
                if (i == questionList.get(j).getIndex() * INT_QfileLinesPerOneQuestion + INT_QfileAnswerHistoryIndex3) {
                    textBuffer.remove(textBuffer.size() - 1);
                }
                if ((i % INT_QfileLinesPerOneQuestion)==INT_QfileLinesPerOneQuestion-1) {
                    j++;
                }
            }
        }
        resetfiles(file);
        outputtoFileByList(mainValue, textBuffer);
        reloadLists();
        reloadAdapter();
    }

    public static List makeListFromQuetionArray(ArrayList<Question> questionArrayList ,int index){
        List list = new ArrayList();
        if (index>INT_QfileLinesPerOneQuestion){
            Log.d("onqbook", "index size error" );
        }else{
            for (int i = 0;i < (questionArrayList.size())*INT_QfileLinesPerOneQuestion ;i++ ){
                if ((i %INT_QfileLinesPerOneQuestion)==index){
                    list.add(questionArrayList.get(i/INT_QfileLinesPerOneQuestion).getOBjectFromIndex(index));

                }
            }
        }
        return list;


    }
    public static void reloadAdapter(){
        ListAdapter wrapperListAdapter = ((WrapperListAdapter) R_id_listview.getAdapter()).getWrappedAdapter();
        if (wrapperListAdapter == simp) {
            R_id_listview.setAdapter(simp);
        }
        if (wrapperListAdapter == qsimp) {
            R_id_listview.setAdapter(qsimp);
        }


    }
    /**
     * dpからpixelへの変換
     *
     * @param dp
     * @param context
     * @return float pixel
     */
    public static float convertDp2Px(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }

    /**
     * pixelからdpへの変換
     *
     * @param px
     * @param context
     * @return float dp
     */
    public static float convertPx2Dp(int px, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / metrics.density;
    }

    public static List<String> getListData() {
        return listData;
    }

    public static void addListData(String s) {
        MainActivity.listData.add(s);
        simp.notifyDataSetChanged();
    }

    public static List<String> getQlistData() {
        return qlistData;
    }

    public static void addQlistData(String s) {
        MainActivity.qlistData.add(s);
        qsimp.notifyDataSetChanged();
    }

    public static List<String> getAlistData() {
        return alistData;
    }

    public static void addAlistData(String s) {
        MainActivity.alistData.add(s);
    }

    public static List<String> getTaglistData() {
        return taglistData;
    }

    public static void addTaglistData(String s) {
        MainActivity.taglistData.add(s);
    }

    public static List<Boolean[]> getHistoryData() {
        return historyData;
    }

    public static void addHistoryData(Boolean b[]) {
        MainActivity.historyData.add(b);
    }
}

