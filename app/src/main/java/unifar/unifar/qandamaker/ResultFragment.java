package unifar.unifar.qandamaker;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class ResultFragment extends Fragment {
    Bundle bundle;
    int questionsAmount;
    int correctAmount;
    AdView adView;
    TextView tVCorrect;
    TextView tVResult;
    Button bTFinish;
    Fragment thisFragment;
    OnResultFragmentFinishListener onResultFragmentFinishListener;
    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
        if (getArguments() != null) {
            bundle=getArguments();
            questionsAmount = bundle.getInt("QUESTION_AMOUNT");
            correctAmount = bundle.getInt("CORRECT_AMOUNT");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results,container, false);
        tVCorrect = (TextView)view.findViewById(R.id.textViewCorrect) ;
        tVResult = (TextView)view.findViewById(R.id.textViewResult) ;
        String messageToShow =getString(R.string.resultMessage,String.valueOf(questionsAmount),String.valueOf(correctAmount));
        tVResult.setText(messageToShow);
        bTFinish = (Button)view.findViewById(R.id.buttonFinish);
        bTFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onResultFragmentFinishListener != null) {
                    onResultFragmentFinishListener.onResultFragmentFinish();
                }
            }
        });
        MobileAds.initialize(MyApplication.getAppContext());
        adView = (AdView)view.findViewById(R.id.adViewOnExamFragment);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return view;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof OnResultFragmentFinishListener) {
            onResultFragmentFinishListener =(OnResultFragmentFinishListener)context;
        }else{
            throw new ClassCastException("activity が OnResultFragmentFinishListener を実装していません.");
        }
    }
}
