package vanlandingham.friendimals;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Owner on 11/18/2017.
 */

public class Messages extends AppCompatActivity{

    private Button back_button;
    private Button new_chat_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
        setContentView(R.layout.messages_activity);



        back_button = findViewById(R.id.back_button);
        new_chat_button = findViewById(R.id.new_chat_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getFragmentManager();
        if(fm.getBackStackEntryCount()> 0) {

            fm.popBackStack();

        }

        else
            super.onBackPressed();

    }

    private void setUpWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

    }
}
