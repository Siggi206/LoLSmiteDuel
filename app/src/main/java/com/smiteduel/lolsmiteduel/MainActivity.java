package com.smiteduel.lolsmiteduel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView hp;
    TextView infoText;
    TextView scoreP1;
    TextView scoreP2;
    int smiteDamage;
    long timeUntilSmite;
    boolean alreadyBelowHp;
    Thread damageThread;
    boolean threadPause;

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
        scoreP1 = (TextView) findViewById(R.id.scoreP1);
        scoreP2 = (TextView) findViewById(R.id.scoreP2);
        smiteDamage = getResources().getInteger(R.integer.smiteDamage);
        timeUntilSmite = 0;
        alreadyBelowHp = false;
        threadPause = false;

        final Button smiteButton1 = (Button) findViewById(R.id.smiteButton1);
        final Button smiteButton2 = (Button) findViewById(R.id.smiteButton2);

        damageThread = new Thread() {
            @Override
            public void run()
            {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(99);
                        if(!threadPause)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int hpWhenSmite = Integer.parseInt(hp.getText().toString());
                                    int remainingHp = (int) (hpWhenSmite - Math.random() * 100);
                                    hp.setText(String.valueOf(remainingHp));
                                    if (remainingHp <= 0) {
                                        infoText.setText("You didn't smite in time'");
                                        smiteButton1.setEnabled(false);
                                        smiteButton2.setEnabled(false);
                                        toggleDamageThread();
                                        hp.setText("0");
                                    }
                                    if (!alreadyBelowHp) {
                                        timeUntilSmite = System.currentTimeMillis();
                                    }
                                    if (remainingHp <= smiteDamage) {
                                        alreadyBelowHp = true;
                                    }
                                    Log.d("HP", hp.getText().toString() + " HP");
                                    Log.d("HP", String.valueOf(System.currentTimeMillis() - timeUntilSmite) + " TIME");
                                }
                            });
                        }
                    }
                }
                catch (InterruptedException e) {
                }
            }
        };
        damageThread.start();


        smiteButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int hpWhenSmite = Integer.parseInt(hp.getText().toString());
                Log.d("HP",String.valueOf(hpWhenSmite) + " SMITE");
                if (hpWhenSmite <= smiteDamage)
                {
                    timeUntilSmite = System.currentTimeMillis() - timeUntilSmite;
                    infoText.setText("P1 Smited at " + hpWhenSmite + "\nReaction time: "+ timeUntilSmite + "ms");
                    threadPause = true;
                    smiteButton2.setEnabled(false);
                    scoreP1.setText(String.valueOf(Integer.parseInt(scoreP1.getText().toString()) + 1));
                }
                hp.setText(String.valueOf(hpWhenSmite - smiteDamage));
                smiteButton1.setEnabled(false);
            }
        });

        smiteButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int hpWhenSmite = Integer.parseInt(hp.getText().toString());
                if (hpWhenSmite <= smiteDamage)
                {
                    timeUntilSmite = System.currentTimeMillis() - timeUntilSmite;
                    infoText.setText("P2 Smited at " + hpWhenSmite + "\nReaction time: "+ timeUntilSmite + "ms");
                    threadPause = true;
                    smiteButton1.setEnabled(false);
                    scoreP2.setText(String.valueOf(Integer.parseInt(scoreP2.getText().toString()) + 1));
                }
                hp.setText(String.valueOf(hpWhenSmite - smiteDamage));
                smiteButton2.setEnabled(false);
            }
        });


        final Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hp.setText(getString(R.string.MonsterHp));
                infoText = (TextView) findViewById(R.id.infoText);
                smiteButton1.setEnabled(true);
                smiteButton2.setEnabled(true);
                alreadyBelowHp = false;
                threadPause = false;
            }
        });
    }

    void toggleDamageThread()
    {
        threadPause = !threadPause;
    }
}
