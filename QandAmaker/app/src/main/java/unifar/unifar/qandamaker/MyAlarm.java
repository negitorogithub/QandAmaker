package unifar.unifar.qandamaker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class MyAlarm extends DialogFragment {

    public static MyAlarm newInstance()
    {
        return new MyAlarm();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity mainActivity = new MainActivity();
        return new AlertDialog.Builder(getActivity())
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("OnQBookBoxOkClick", String.valueOf("リスト:"+mainActivity.listData));
                        mainActivity.onClickOk_myalarm();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
