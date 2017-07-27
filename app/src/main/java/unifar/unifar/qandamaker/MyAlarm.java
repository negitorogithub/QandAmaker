package unifar.unifar.qandamaker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

public class MyAlarm extends DialogFragment {

    public static MyAlarm newInstance()
    {
        return new MyAlarm();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String Qbook_delete = getString(R.string.Qbook_delete , MainActivity.mainValue_longclick);
        CharSequence charSequence_Qbook_delete;
        if (Build.VERSION.SDK_INT <= 23) {
        charSequence_Qbook_delete =Html.fromHtml(Qbook_delete);
        }else{
        charSequence_Qbook_delete =Html.fromHtml(Qbook_delete, Html.FROM_HTML_MODE_LEGACY);
        }
        final MainActivity mainActivity = new MainActivity();
        return new AlertDialog.Builder(getActivity())
                .setTitle("Warning!")
                .setMessage(charSequence_Qbook_delete)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
