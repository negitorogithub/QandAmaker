package unifar.unifar.qandamaker;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MyCustomizedDialog extends DialogFragment{
    Button okButton;
    Button closeButton;
    EditText questionInput;
    EditText answerInput;
    Dialog dialog;
    MainActivity mainActivity=new MainActivity();
    String questionStr;
    public static MyCustomizedDialog newInstance() {
        MyCustomizedDialog fragment = new MyCustomizedDialog();
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.inputdialog, null, false);
        okButton = (Button) view.findViewById(R.id.ok_Button);
        closeButton = (Button) view.findViewById(R.id.close_Button);
        questionInput = (EditText) view.findViewById(R.id.questionbox);
        answerInput = (EditText) view.findViewById(R.id.answerbox);

        dialog = new Dialog(getActivity());
        dialog.setTitle("編集画面");
        dialog.setContentView(view);

        SpannableStringBuilder sb1 = (SpannableStringBuilder)questionInput.getText();
        final String questionStr =sb1.toString();
        SpannableStringBuilder sb2 = (SpannableStringBuilder)answerInput.getText();
        final String answerStr = sb2.toString();


        okButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            mainActivity.listadd(questionStr,"A");

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
    lp.height =(int)(metrics.widthPixels*0.6);
    dialog.getWindow().setAttributes(lp);
}
    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }

}

