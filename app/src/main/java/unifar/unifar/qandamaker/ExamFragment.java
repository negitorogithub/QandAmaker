package unifar.unifar.qandamaker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


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
    ListView alternativesListView;
    ArrayList<String> examQuestionsBuffer;
    ArrayList<String> examAnswersBuffer;
    ArrayList<String> examTagsBuffer;
    List<String> examQuestionsArray ;
    List<String> examAnswersArray ;
    List<String> examTagsArray;

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
        alternativesListView = (ListView)view.findViewById(R.id.alternativeList);
        final int QUESTIONAMOUNT = getArguments().getInt("questionAmount");
        final int EXAMMODE = getArguments().getInt("examMode");
        examQuestionsBuffer= new ArrayList<>();
        examAnswersBuffer= new ArrayList<>();
        examTagsBuffer= new ArrayList<>();
        for (int i = 0; i<MainActivity.qlistData.size(); i++){
            examQuestionsBuffer.add((MainActivity.qlistData.get(i)).get("main"));
            examAnswersBuffer.add(MainActivity.alistData.get(i));
            examTagsBuffer.add(MainActivity.taglistData.get(i));
        }
        Random random = new Random();
        long seed = random.nextLong();
        Collections.shuffle(examQuestionsBuffer, new Random(seed));
        Collections.shuffle(examAnswersBuffer, new Random(seed));
        Collections.shuffle(examTagsBuffer, new Random(seed));
        examQuestionsArray =  examQuestionsBuffer.subList(0, QUESTIONAMOUNT);
        examAnswersArray =  examAnswersBuffer.subList(0, QUESTIONAMOUNT);
        examTagsArray =  examTagsBuffer.subList(0, QUESTIONAMOUNT);
        showQuestion(0);
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
    void showQuestion(int questionIndex){
        questionText.setText(examQuestionsArray.get(questionIndex));
        List<String> alterrnatives = MainActivity.makeAlterrnatives(MainActivity.mainValue, examTagsArray.get(questionIndex),examAnswersArray.get(questionIndex) );
        ArrayAdapter<String> alternativesAdapter = new ArrayAdapter<String>(MyApplication.getAppContext(),R.layout.exam_alternatives);
        for (int i =0; i<alterrnatives.size();i++ ){
            alternativesAdapter.add(alterrnatives.get(i));
        }
        alternativesListView.setAdapter(alternativesAdapter);
    }
}
