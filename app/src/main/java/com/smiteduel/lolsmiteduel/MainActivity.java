package com.smiteduel.lolsmiteduel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    TextView hp;
    TextView infoText;
    int smiteDamage;
    long timeUntilSmite;
    boolean alreadyBelowHp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        hp = (TextView) findViewById(R.id.hp);
        infoText = (TextView) findViewById(R.id.infoText);
        smiteDamage = getResources().getInteger(R.integer.smiteDamage);
        timeUntilSmite = 0;
        alreadyBelowHp = false;

        final Thread damageThread = new Thread() {
            @Override
            public void run()
            {
                try {
                    while (!isInterrupted())
                    {
                        Thread.sleep(99);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                int hpWhenSmite = Integer.parseInt(hp.getText().toString());
                                hp.setText(String.valueOf((int) (Double.parseDouble(hp.getText().toString()) -  100)));
                                if(hpWhenSmite <= smiteDamage && !alreadyBelowHp)
                                {
                                    alreadyBelowHp = true;
                                    timeUntilSmite = System.currentTimeMillis();
                                }
                            }
                        });
                    }
                }
                catch (InterruptedException e) {
                }
            }
        };
        damageThread.start();

        final Button smiteButton = (Button) findViewById(R.id.smiteButton);
        smiteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int hpWhenSmite = Integer.parseInt(hp.getText().toString());
                if (hpWhenSmite <= smiteDamage)
                {
                    timeUntilSmite = System.currentTimeMillis() - timeUntilSmite;
                    infoText.setText("You Smited at " + hpWhenSmite + "\nReaction time: "+ timeUntilSmite + "ms");
                    damageThread.interrupt();
                }
                hp.setText(String.valueOf(hpWhenSmite - smiteDamage));
                smiteButton.setEnabled(false);
            }
        });


        final Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hp.setText("2000");
                infoText = (TextView) findViewById(R.id.infoText);
                smiteButton.setEnabled(true);
                damageThread.run();
            }
        });
    }
}
