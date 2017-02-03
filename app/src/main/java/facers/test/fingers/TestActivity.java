package facers.test.fingers;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TestActivity extends AppCompatActivity {

    TextView labelAnimate;
    Button btnTest1;
    Button btnTest2;
    Button btnTest3;
    Button btnTest4;
    Button btnTest5;
    Button btnTest6;
    Button btnTest7;
    Button btnTest8;
    Button btnTest9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        labelAnimate = (TextView) findViewById(R.id.label_animate_test);
        btnTest1 = (Button) findViewById(R.id.btn_animate_test1);
        btnTest2 = (Button) findViewById(R.id.btn_animate_test2);
        btnTest3 = (Button) findViewById(R.id.btn_animate_test3);
        btnTest4 = (Button) findViewById(R.id.btn_animate_test4);
        btnTest5 = (Button) findViewById(R.id.btn_animate_test5);
        btnTest6 = (Button) findViewById(R.id.btn_animate_test6);
        btnTest7 = (Button) findViewById(R.id.btn_animate_test7);
        btnTest8 = (Button) findViewById(R.id.btn_animate_test8);
        btnTest9 = (Button) findViewById(R.id.btn_animate_test9);

        ViewAnimation.resetAnimateDuration();



        btnTest1.setText("center");
        btnTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.slideToCenter(labelAnimate, new SimpleCallback() {
                    @Override
                    public void done() {}
                });
            }
        });

        btnTest2.setText("out right");
        btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.slideOutRight(labelAnimate, new SimpleCallback() {
                    @Override
                    public void done() {}
                });
            }
        });

        btnTest3.setText("offset bot");
        btnTest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.setOffsetBot(labelAnimate);
            }
        });

        btnTest4.setText("out top");
        btnTest4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.slideOutTop(labelAnimate, new SimpleCallback() {
                    @Override
                    public void done() {}
                });
            }
        });

        btnTest5.setText("out left");
        btnTest5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.slideOutLeft(labelAnimate, new SimpleCallback() {
                    @Override
                    public void done() {}
                });
            }
        });

        btnTest6.setText("out bot");
        btnTest6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.slideOutBot(labelAnimate, new SimpleCallback() {
                    @Override
                    public void done() {
                        ViewAnimation.slideToCenter(labelAnimate, new SimpleCallback() {
                            @Override
                            public void done() {}
                        });
                    }
                });
            }
        });

        btnTest7.setText("offset top");
        btnTest7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.setOffsetTop(labelAnimate);
            }
        });

        btnTest8.setText("offset left");
        btnTest8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.setOffsetLeft(labelAnimate);
            }
        });

        btnTest9.setText("offset right");
        btnTest9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.setOffsetRight(labelAnimate);
            }
        });

    }

}
