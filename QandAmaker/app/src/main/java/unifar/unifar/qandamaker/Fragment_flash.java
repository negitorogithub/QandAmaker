package unifar.unifar.qandamaker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_flash.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_flash#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_flash extends Fragment  {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters
    public String str_question_name;
    public String str_answer_name;
    public static ViewPager mViewPager;

    private OnFragmentInteractionListener mListener;

    public Fragment_flash() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment_flash newInstance() {
        return new Fragment_flash();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        str_question_name= args.getString("question_name");
        str_answer_name = args.getString("answer_name");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.activity_detail_quiz, container,false);
        final TextView textView_switch = (TextView)rootView.findViewById(R.id.textview_switch);
        textView_switch.setText(str_question_name);
        mViewPager = (ViewPager) rootView.findViewById(R.id.container_detail_quiz);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            int int_flag_switch = 1;
            @Override
            public void run() {
                // UIスレッド
                switch (int_flag_switch){
                    case 1:
                        textView_switch.setText(str_answer_name);
                        int_flag_switch = 2;
                        break;
                    case 2:
                        textView_switch.setText(str_question_name);
                        int_flag_switch = 1;
                        break;


                }
                handler.postDelayed(this, 100);
            }
        };
        handler.post(r);


        return rootView;
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
