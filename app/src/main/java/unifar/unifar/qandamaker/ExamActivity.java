package unifar.unifar.qandamaker;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExamActivity extends AppCompatActivity implements ExamFragment.OnFragmentInteractionListener{

    int questionAmount ;
    int examMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Intent intent = getIntent();
        questionAmount = intent.getIntExtra("questionAmount",1);
        examMode = intent.getIntExtra("examMode", 0);
        ExamFragment examfragment = new ExamFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("questionAmount", questionAmount);
        bundle.putInt("examMode", examMode);
        examfragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.examActivityContainer, examfragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
