package unifar.unifar.qandamaker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashSet;
import java.util.LinkedHashSet;

 // TODO:選択肢が一つの場合スキップ機能
 // TODO:スキップ機能
public class CustomizedDialog_questionbook extends DialogFragment {
     private static String questionName;
     private static String answerName;
     private static String tagName;
     private EditText et_question;
    private EditText answerInput;
    private EditText tagInput;
    private Spinner tagSpinner;
    private ArrayAdapter<String> tagSpinnerAdapter;
    private Dialog dialog;
    public LinkedHashSet<String> tagSet;
    public DialogListener dialogListener;
    public static final String IsRecreatedKeyStr = "isRecreated";
    String tagArray[];
    static View view;
    Boolean isOkPressedOn3;
    public static CustomizedDialog_questionbook newInstance() {
        questionName = "";
        answerName = "";
        tagName = "";
        return new CustomizedDialog_questionbook();
    }
     public static CustomizedDialog_questionbook newInstance( String questionNameArg, String answerNameArg, String tagNameArg) {
         questionName = questionNameArg;
         answerName = answerNameArg;
         tagName = tagNameArg;
         return new CustomizedDialog_questionbook();
     }


     @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("onqbook","Dialog onattach");
        if (context instanceof DialogListener) {
            dialogListener = (DialogListener) context;
        }
        tagSet = new LinkedHashSet<>(MainActivity.getTaglistData());
        tagArray = new String[tagSet.size()];
        tagSet.toArray(tagArray);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("onqbook","onDetach");
        if (MainActivity.viewFlag == 3) {
            MainActivity.viewFlag = 2;
            Log.d("onqbook", "3 -> 2");
            if (MyApplication.bundle.getBoolean(IsRecreatedKeyStr)) {
                CustomizedDialog_questionbook customizedDialog_questionbook = new CustomizedDialog_questionbook();
                customizedDialog_questionbook.show(getFragmentManager(), "recreated dialog");
                MainActivity.addTaglistData(MyApplication.bundle.getString("str_tag_name"));
                Log.d("onqbook", "タグリスト：" + MainActivity.getTaglistData());
                Log.d("onqbook", "新規タグの名前：" + MyApplication.bundle.getString("str_tag_name"));
            }
        }
        dialogListener = null;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (MainActivity.viewFlag != 1 && MainActivity.viewFlag != 2 && MainActivity.viewFlag != 3){
            Log.d("onqbook","viewFlag:"+MainActivity.viewFlag+"に対応するダイアログはありません。");
        } else {
            switch (MainActivity.viewFlag) {

                case 1:
                    view = inflater.inflate(R.layout.questionbookinput,null , false);
                    et_question = (EditText) view.findViewById(R.id.questionbox);
                break;
                case 2:
                    Log.d("onqbook","onCreateDialog @viewFlag = 2");
                    addTagSetTotagSpinnerAdapterAndInflateView();
                    et_question = (EditText) view.findViewById(R.id.questionbox);
                    answerInput = (EditText) view.findViewById(R.id.answerbox);
                    tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (!tagSpinner.isFocusable()) {
                                tagSpinner.setFocusable(true);
                                Log.d("onqbook","初回起動");
                            } else {
                                if (!MyApplication.bundle.getBoolean(IsRecreatedKeyStr)) {
                                    if (position == 0) {
                                        //タグ新規作成のダイアログ作成
                                        if (MainActivity.viewFlag == 2) {
                                            MyApplication.bundle.putString("answerStr", ifNullReplace(String.valueOf(answerInput.getText())));
                                            MyApplication.bundle.putString("questionStr", ifNullReplace(String.valueOf(et_question.getText())));
                                            MainActivity.viewFlag = 3;
                                            Log.d("onqbook", "2 -> 3");
                                        }
                                        final CustomizedDialog_questionbook dialog = new CustomizedDialog_questionbook();
                                        dialog.show(getFragmentManager(), "tagInputDialog");
                                        Log.d("onqbook", "ダイアログ作成");
                                    } else {
                                        MyApplication.bundle.putString("str_tag_name", tagArray[position - 1]);
                                    }
                                }else{
                                    MyApplication.bundle.putString("str_tag_name", tagArray[position]);
                                }
                                tagSpinnerAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // do nothing
                        }

                    });
                    tagSpinnerAdapter.notifyDataSetChanged();

                break;
                case 3:
                    view = inflater.inflate(R.layout.tag_edit_dialog, null, false);
                    tagInput =(EditText)view.findViewById(R.id.tagBox);

                    break;
            }
        }

        Button okButton = (Button) view.findViewById(R.id.ok_Button);
        Button closeButton = (Button) view.findViewById(R.id.close_Button);
        // viewflag = 2 のときのみ存在

        if (MyApplication.bundle.getBoolean(IsRecreatedKeyStr)){
            if (MainActivity.viewFlag == 2) {
                et_question.setText(MyApplication.bundle.getString("questionStr"));
                answerInput.setText(MyApplication.bundle.getString("answerStr"));
                int newTagPosition = MainActivity.getQlistData().size() + 1;
                if (newTagPosition == 0) {
                    newTagPosition = 1;
                }
                    tagSpinner.setSelection(newTagPosition, true);
            }
        } else if(MyApplication.bundle.getBoolean("isEditMode")){

            if (MainActivity.viewFlag ==2 ) {
                et_question.setText(CustomizedDialog_questionbook.questionName);
                answerInput.setText(CustomizedDialog_questionbook.answerName);
                tagSpinner.setSelection(tagSpinnerAdapter.getPosition(CustomizedDialog_questionbook.tagName), true);
            }
        }
        dialog = new Dialog(getActivity());
        dialog.setTitle("編集画面");
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        okButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if (MainActivity.viewFlag ==1 || MainActivity.viewFlag ==2 ) {
                    if (ifNullReplace(String.valueOf(et_question.getText())).equals("")){
                        Toast.makeText(MyApplication.getAppContext(), getString(R.string.questionInputEmpty),Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (MainActivity.makeListFromQuetionArray(MainActivity.examQuestionsDataBuffer, MainActivity.INT_QfileQuestionIndex).contains(ifNullReplace(String.valueOf(et_question.getText())))) {
                        if (!(MyApplication.bundle.getBoolean("isEditMode"))) {
                            Toast.makeText(MyApplication.getAppContext(), R.string.same_question_exsits,Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                        MyApplication.bundle.putString("questionStr",ifNullReplace(String.valueOf(et_question.getText())));


                    if (MainActivity.viewFlag == 2) {
                        if (String.valueOf(answerInput.getText()).equals("")){
                            Toast.makeText(MyApplication.getAppContext(), getString(R.string.answerInputEmpty),Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!MyApplication.bundle.getBoolean(IsRecreatedKeyStr)) {
                            if (tagSpinner.getSelectedItemPosition()==0){
                                Toast.makeText(MyApplication.getAppContext(), getString(R.string.tagSpinnerEmpty),Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        MyApplication.bundle.putString("answerStr",ifNullReplace(String.valueOf(answerInput.getText())));
                        MyApplication.bundle.putString("str_tag_name",ifNullReplace(String.valueOf(tagSpinner.getSelectedItem())));
                    }
                }
                if (MainActivity.viewFlag == 3) {
                    if (String.valueOf(tagInput.getText()).equals("")){
                        Toast.makeText(MyApplication.getAppContext(), getString(R.string.tagInputEmpty),Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (MainActivity.makeListFromQuetionArray(MainActivity.examQuestionsDataBuffer, MainActivity.INT_QfileTagIndex).contains(ifNullReplace(String.valueOf(tagInput.getText())))) {
                        if (!(MyApplication.bundle.getBoolean("isEditMode"))) {
                            Toast.makeText(MyApplication.getAppContext(), R.string.same_tag_exists, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    MyApplication.bundle.putString("str_tag_name",ifNullReplace(String.valueOf(tagInput.getText())));
                    MyApplication.bundle.putBoolean(IsRecreatedKeyStr, true);
                }
                    if (dialogListener != null) {

                        if (MyApplication.bundle.getBoolean("isEditMode")) {
                            Question question = new Question(
                                    MyApplication.bundle.getString("questionStr"),
                                    MyApplication.bundle.getString("answerStr"),
                                    MyApplication.bundle.getString("str_tag_name"),
                                    MainActivity.examQuestionsDataBuffer.get(MainActivity.int_onListViewPositionOn2).getResults(),
                                    MainActivity.examQuestionsDataBuffer.get(MainActivity.int_onListViewPositionOn2).getIndex()
                                    );
                            dialogListener.onClickOkOnEditMode(question);
                        } else {
                            dialogListener.onClickOk();
                        }
                    }
                dialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MyApplication.bundle.putBoolean("isEditMode",false);
                dialog.dismiss();
            }
        });
        return dialog;
    }
    @Override
    public  void onActivityCreated(Bundle savedInstansState){
        super.onActivityCreated(savedInstansState);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        lp.width = (int) (metrics.widthPixels*1.0);
        dialog.getWindow().setAttributes(lp);


    }
    public static String ifNullReplace(String string){
        if (string == null){
            return "";
        }
        return string;
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    void addTagSetTotagSpinnerAdapterAndInflateView(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.inputdialog, null, false);
        tagSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        tagSpinnerAdapter.clear();
        if (! MyApplication.bundle.getBoolean(IsRecreatedKeyStr)) {
            tagSpinnerAdapter.add("【新規作成】");
        }
        tagSet = new LinkedHashSet<>(MainActivity.getTaglistData());
        tagArray = new String[tagSet.size()];
        tagSet.toArray(tagArray);
        for(int i = 0; i < tagSet.toArray().length; i++) {
            tagSpinnerAdapter.add(tagArray[i]);
        }
        tagSpinnerAdapter.notifyDataSetChanged();
        tagSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner = (NDSpinner) view.findViewById(R.id.tagSpinner);
        tagSpinner.setFocusable(false);
        tagSpinner.setAdapter(tagSpinnerAdapter);
    }
    

    }