package unifar.unifar.qandamaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class FlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        final String str_question_name = intent.getStringExtra("question_name");
        final String str_answer_name = intent.getStringExtra("answer_name");


    }
}
