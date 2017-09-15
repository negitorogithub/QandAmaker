package unifar.unifar.qandamaker;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ExamActivity extends AppCompatActivity implements ExamFragment.OnFragmentInteractionListener, OnReachedLastQuestionListener{

    int questionAmount ;
    int examMode;
    Toolbar toolbar ;

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
        toolbar = (Toolbar) findViewById(R.id.toolbarOnExam);
        toolbar.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.colorPrimaryDark));
        toolbar.setTitleTextColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.white));
        toolbar.setTitle(MainActivity.mainValue);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().add(R.id.examActivityContainer, examfragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void OnReachedLastQuestion() {
        finish();
    }
}
