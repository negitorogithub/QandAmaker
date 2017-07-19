package unifar.unifar.qandamaker;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FlashActivity extends AppCompatActivity implements Fragment_flash.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        Intent intent = getIntent();
        String question_name = intent.getStringExtra("question_name");
        String answer_name = intent.getStringExtra("answer_name");

        Bundle bundle = new Bundle();
        bundle.putString("question_name",question_name);
        bundle.putString("answer_name",answer_name);


        Fragment_flash fragment_flash = Fragment_flash.newInstance();
        fragment_flash.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_detail_flash, fragment_flash);
        fragmentTransaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
