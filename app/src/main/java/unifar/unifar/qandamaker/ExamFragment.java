package unifar.unifar.qandamaker;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.style.QuoteSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static unifar.unifar.qandamaker.MainActivity.examQuestionsDataBuffer;
import static unifar.unifar.qandamaker.MainActivity.inputFromFileToArray;

// TODO: 試験終了時をどうにかする

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
    List<Question> examQuestionsDataArray ;
    LayoutInflater thisInflter;
    int rightAnswer;
    int questionIndex;
    int alpha;
    int repeatCnt ;
    int decrease ;
    Handler handler;
    Runnable r;
    AdView adView;
    public ExamFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        thisInflter =inflater;
        View view = inflater.inflate(R.layout.fragment_exam, container, false);
        questionText = (TextView)view.findViewById(R.id.questionText);
        alternativesListView = (ListView)view.findViewById(R.id.alternativeList);
        final int QUESTIONAMOUNT = getArguments().getInt("questionAmount");
        final int EXAMMODE = getArguments().getInt("examMode");
        alpha = 255;
        final Fragment thisFragment = this;

        if (EXAMMODE == 1) {
            Random random = new Random();
            long seed = random.nextLong();
            Collections.shuffle(examQuestionsDataBuffer, new Random(seed));
            examQuestionsDataArray = examQuestionsDataBuffer.subList(0, QUESTIONAMOUNT);
        } else if (EXAMMODE ==2){
            Collections.sort(examQuestionsDataBuffer, new CustomComparatorByResult());
            examQuestionsDataArray = examQuestionsDataBuffer.subList(0, QUESTIONAMOUNT);

        }

        showQuestion(0);
        alternativesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int IntExamQuestionDataArraySize = examQuestionsDataArray.size();

                if (position ==rightAnswer) {
                    onAnswer(true);
                    examQuestionsDataArray.get(questionIndex).setResultBuffer(true);
                }else{
                    onAnswer(false);
                    examQuestionsDataArray.get(questionIndex).setResultBuffer(false);
                }

                if (questionIndex <IntExamQuestionDataArraySize-1) {
                        showQuestion(questionIndex+1);
                    }else{
                    // 結果発表
                    //処理待ち
                    List<Boolean> examResults = new ArrayList<>();
                    for(int i=0;i<examQuestionsDataArray.size();i++ ) {
                        examResults.add(examQuestionsDataArray.get(i).getResultBuffer());
                    }
                    MainActivity.makeAnswerHistoryByQuestionsList(MainActivity.mainValue, examQuestionsDataArray);
                    int correct =0;
                    for (int i =0;i<IntExamQuestionDataArraySize;i++) {
                        if (examResults.get(i)){
                            correct++;
                        }
                    }
                    if (onReachedLastQuestionListener != null) {
                        onReachedLastQuestionListener.OnReachedLastQuestion(IntExamQuestionDataArraySize,correct);
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
        if (context instanceof OnReachedLastQuestionListener) {
            onReachedLastQuestionListener = (OnReachedLastQuestionListener) context;
        }else{
            throw new ClassCastException("activity が OnOkBtnClickListener を実装していません.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(r);
    }


    void showQuestion(int questionIndexarg){
        questionIndex = questionIndexarg;
        questionText.setText(examQuestionsDataArray.get(questionIndex).getQuestionName());
        List<String> alternatives = MainActivity.makeAlterrnatives(MainActivity.mainValue, examQuestionsDataArray.get(questionIndex).getTagName(),examQuestionsDataArray.get(questionIndex).getAnswerName() );
        ArrayAdapter<String> alternativesAdapter = new ArrayAdapter<>(getActivity(),R.layout.exam_alternatives);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i =0; i<alternatives.size();i++ ){
            alternativesAdapter.add(alternatives.get(i));
        }
        if (alternatives.contains(examQuestionsDataArray.get(questionIndex).getAnswerName())) {
            rightAnswer = alternatives.indexOf(examQuestionsDataArray.get(questionIndex).getAnswerName());
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
