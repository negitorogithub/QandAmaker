package unifar.unifar.qandamaker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    TextView questionText;

    public ExamFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ExamFragment newInstance() {
        return new ExamFragment();
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
        View view = inflater.inflate(R.layout.fragment_exam, container, false);
        questionText = (TextView)view.findViewById(R.id.questionText);
        final int QUESTIONAMOUNT = getArguments().getInt("questionAmount");
        final int EXAMMODE = getArguments().getInt("examMode");
        ArrayList<String> examQuestionsBuffer= new ArrayList<>();
        for (int i = 0; i<MainActivity.qlistData.size(); i++){
            examQuestionsBuffer.add((MainActivity.qlistData.get(i)).get("main"));
        }
        Collections.shuffle(examQuestionsBuffer);
        Object[] examQuestionsArray = (examQuestionsBuffer.subList(0, QUESTIONAMOUNT)).toArray();
        showQuestion(examQuestionsArray, 0);
        return view;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    void showQuestion(Object[] questionList , int index){
        questionText.setText(questionList[index].toString());

    }
}
