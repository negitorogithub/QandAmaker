package unifar.unifar.qandamaker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
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

import java.util.HashSet;

public class CustomizedDialog_questionbook extends DialogFragment {
    private EditText et_question;
    private EditText answerInput;
    private EditText tagInput;
    private Spinner tagSpinner;
    private ArrayAdapter<String> tagSpinnerAdapter;
    private Dialog dialog;
    public HashSet<String> tagSet;
    public  String questionStr;
    public  String answerStr;
    public  String str_tag_name;
    public DialogListener dialogListener;
    public static CustomizedDialog_questionbook newInstance() {
        return new CustomizedDialog_questionbook();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ok_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogListener != null) {
                    dialogListener.onClickOk();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener) {
            dialogListener = (DialogListener) context;
        }
        tagSet = new HashSet<>(MainActivity.taglistData);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveInstanceState(Bundle.EMPTY);
        if (MainActivity.viewFlag == 3) {
            MainActivity.viewFlag =2;
            Log.d("onqbook","3 -> 2");
        }
        dialogListener = null;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = null;
        if (MainActivity.viewFlag != 1 && MainActivity.viewFlag != 2 && MainActivity.viewFlag != 3){
            Log.d("onqbook","viewFlag:"+MainActivity.viewFlag+"に対応するダイアログはありません。");
        } else {
            switch (MainActivity.viewFlag) {
                case 1:
                    view = inflater.inflate(R.layout.questionbookinput,null , false);

                break;
                case 2:
                    view = inflater.inflate(R.layout.inputdialog, null, false);
                    answerInput = (EditText) view.findViewById(R.id.answerbox);

                    tagSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
                    tagSpinnerAdapter.add("【新規作成】");
                    addTagSetTotagSpinnerAdapter();
                    tagSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tagSpinner = (NDSpinner) view.findViewById(R.id.tagSpinner);
                    tagSpinner.setFocusable(false);
                    tagSpinner.setAdapter(tagSpinnerAdapter);

                    tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (tagSpinner.isFocusable() == false) {
                                tagSpinner.setFocusable(true);
                                Log.d("onqbook","初回起動");
                            } else {
                                if (position == 0) {
                                    //タグ新規作成のダイアログ作成
                                    if (MainActivity.viewFlag ==2){
                                        MainActivity.viewFlag = 3;
                                        Log.d("onqbook","2 -> 3");
                                    }
                                    final CustomizedDialog_questionbook dialog = newInstance();
                                    dialog.show(getFragmentManager(), "dialog_fragment");
                                    Log.d("onqbook","ダイアログ作成");
                                } else {
                                    //タグ書き込み
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
        et_question = (EditText) view.findViewById(R.id.questionbox);
        dialog = new Dialog(getActivity());
        dialog.setTitle("編集画面");
        dialog.setContentView(view);

        okButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                if (MainActivity.viewFlag ==1 || MainActivity.viewFlag ==2 ) {
                    questionStr =  ifNullReplace(String.valueOf(et_question.getText()));
                    if (MainActivity.viewFlag == 2) {
                        answerStr = ifNullReplace(String.valueOf(answerInput.getText()));
                        str_tag_name =MyApplication.bundle.getString("str_tag_name");

                    }
                }
                if (MainActivity.viewFlag == 3) {
                    str_tag_name = ifNullReplace(String.valueOf(tagInput.getText()));
                }

                Log.d("OnQBookBoxOkClick","QuestionStrは  "+questionStr);
                    if (dialogListener != null) {
                        dialogListener.onClickOk();
                    }
                dialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
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
    void addTagSetTotagSpinnerAdapter(){
        String tagArray[] = new String[tagSet.size()];
        tagSet.toArray(tagArray);
        for(int i = 0; i < tagSet.toArray().length; i++) {
            tagSpinnerAdapter.add(tagArray[i]);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (MainActivity.viewFlag ==3 ) {
            MyApplication.bundle.putString("str_tag_name", str_tag_name);
        }
    }
    

    }