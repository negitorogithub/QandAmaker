package unifar.unifar.qandamaker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class CustomizedDialog_questionbook extends DialogFragment {
    private EditText et_question;
    private EditText answerInput;
    private Dialog dialog;
    MainActivity mainActivity = new MainActivity();
    public static String questionStr;
    public static String answerStr;
    public DialogListener dialogListener;
    public static int listaddflag;

    public static CustomizedDialog_questionbook newInstance() {
        CustomizedDialog_questionbook fragment = new CustomizedDialog_questionbook();
        return fragment;
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view ;
        if (MainActivity.viewFlag == 1) {
            view = inflater.inflate(R.layout.questionbookinput, null, false);
        }else{
            view = inflater.inflate(R.layout.inputdialog, null, false);
            answerInput = (EditText) view.findViewById(R.id.answerbox);
        }
        Button okButton = (Button) view.findViewById(R.id.ok_Button);
        Button closeButton = (Button) view.findViewById(R.id.close_Button);
        et_question = (EditText) view.findViewById(R.id.questionbox);
        dialog = new Dialog(getActivity());
        dialog.setTitle("編集画面");
        dialog.setContentView(view);

        okButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SpannableStringBuilder sb1 = (SpannableStringBuilder)et_question.getText();
                questionStr =sb1.toString();
                if (MainActivity.viewFlag == 2) {
                    SpannableStringBuilder sb2 = (SpannableStringBuilder) answerInput.getText();
                    answerStr = sb2.toString();
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
        lp.width = (int) (metrics.widthPixels*1.4);
        lp.height =(int)(metrics.widthPixels*0.9);
        dialog.getWindow().setAttributes(lp);

    }

}