package unifar.unifar.qandamaker;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    OnReachedLastQuestionListener onReachedLastQuestionListener;
    TextView questionText;
    ListView alternativesListView;
    ArrayList<String> examQuestionsBuffer;
    ArrayList<String> examAnswersBuffer;
    ArrayList<String> examTagsBuffer;
    ArrayList<Boolean>examResultsBuffer;
    List<String> examQuestionsArray ;
    List<String> examAnswersArray ;
    List<String> examTagsArray;
    int rightAnswer;
    int questionIndex;
    int alpha;
    int repeatCnt ;
    int decrease ;
    Handler handler;
    Runnable r;

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
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {

            }
        };
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
        alpha = 255;
        final Fragment thisFragment = this;
        examQuestionsBuffer= new ArrayList<>();
        examAnswersBuffer= new ArrayList<>();
        examTagsBuffer= new ArrayList<>();
        examResultsBuffer = new ArrayList<>();
        for (int i = 0; i<MainActivity.qlistData.size(); i++){
            examQuestionsBuffer.add((MainActivity.qlistData.get(i)));
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
        alternativesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position ==rightAnswer) {
                    onAnswer(true);
                    examResultsBuffer.add(true);
                    if (questionIndex <examQuestionsArray.size()-1) {

                        showQuestion(questionIndex+1);
                    }else{
                             // 結果発表
                        for (int i = 0; i<  examQuestionsArray.size()-1; i++) {
                            MainActivity.makeAnswerHistory(MainActivity.mainValue, examAnswersArray.get(i) , examResultsBuffer.get(i));
                        }
                        getFragmentManager().beginTransaction().remove(thisFragment).commit();

                        if (onReachedLastQuestionListener != null) {
                            onReachedLastQuestionListener.OnReachedLastQuestion();
                        }

                    }
                }else{
                    onAnswer(false);
                    examResultsBuffer.add(false);
                    if (questionIndex <examQuestionsArray.size()-1) {

                        showQuestion(questionIndex+1);
                    }else{
                            // 結果発表
                        for (int i = 0; i<  examQuestionsArray.size()-1; i++) {
                            MainActivity.makeAnswerHistory(MainActivity.mainValue, examAnswersArray.get(i) , examResultsBuffer.get(i));
                        }
                        getFragmentManager().beginTransaction().remove(thisFragment).commit();

                    }
                }
            }
        });
        return view;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (!(context instanceof OnReachedLastQuestionListener)) {
            throw new ClassCastException("activity が OnOkBtnClickListener を実装していません.");
        }
        onReachedLastQuestionListener = ((OnReachedLastQuestionListener) context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(r);
    }


    void showQuestion(int questionIndexarg){
        questionIndex = questionIndexarg;
        questionText.setText(examQuestionsArray.get(questionIndex));
        List<String> alternatives = MainActivity.makeAlterrnatives(MainActivity.mainValue, examTagsArray.get(questionIndex),examAnswersArray.get(questionIndex) );
        ArrayAdapter<String> alternativesAdapter = new ArrayAdapter<>(MyApplication.getAppContext(),R.layout.exam_alternatives);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i =0; i<alternatives.size();i++ ){
            alternativesAdapter.add(alternatives.get(i));
        }
        if (alternatives.contains(examAnswersArray.get(questionIndex))) {
            rightAnswer = alternatives.indexOf(examAnswersArray.get(questionIndex));
        }else {
            rightAnswer = alternatives.size()-1;
        }
        alternativesListView.setAdapter(alternativesAdapter);

    }
    void onAnswer(final Boolean collect){
        handler.removeCallbacks(r);
        repeatCnt = 0;
        decrease = 0;
        alpha = 170;
        r = new Runnable() {
            @Override
            public void run() {

                    if (collect) {
                        questionText.setBackgroundColor(Color.argb(alpha, 255, 41, 65));
                    } else {
                        questionText.setBackgroundColor(Color.argb(alpha, 12, 91, 254));
                    }
                    repeatCnt++;
                    decrease = ((80-repeatCnt)/10);
                    alpha = alpha - decrease;
                    if (!(alpha < 0)) {
                        handler.postDelayed(this, 10);
                    }
                }
        };
            handler.post(r);

    }
}
