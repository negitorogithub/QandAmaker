package unifar.unifar.qandamaker;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExamDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExamDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamDialogFragment extends DialogFragment{
    private OnFragmentInteractionListener mListener;

    public ExamDialogFragment() {
        // Required empty public constructor
    }
    public static ExamDialogFragment newInstance() {
        ExamDialogFragment fragment = new ExamDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exam_dialog, container, false);
        int intExamMode = 0 ;
        final NumberPicker numberPicker = (NumberPicker)view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(MainActivity.getQlistData().size());
        numberPicker.setMinValue(1);
        final RadioGroup examMode = (RadioGroup)view.findViewById(R.id.examMode);
        examMode.check(R.id.recommended);
        Button okButton = (Button) view.findViewById(R.id.ok_Button);
        Button closeButton = (Button) view.findViewById(R.id.close_Button);
        final Fragment thisInstance =this;
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyApplication.getAppContext(), ExamActivity.class);
                        intent.putExtra("questionAmount", numberPicker.getValue());
                        if (examMode.getCheckedRadioButtonId() == R.id.random) {
                            intent.putExtra("examMode", 1);
                        }else{
                            intent.putExtra("examMode", 2);
                        }
                        startActivity(intent);
                        getFragmentManager().beginTransaction().remove(thisInstance).commit();
                    }
                });
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getFragmentManager().beginTransaction().remove(thisInstance).commit();
                    }
                });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public  void onActivityCreated(Bundle savedInstansState){
        super.onActivityCreated(savedInstansState);
        Dialog dialog = getDialog();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.8);
        int dialogHeight = (int) (metrics.heightPixels * 0.5);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
