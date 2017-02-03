package facers.test.fingers;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

public class HomeActivity extends AppCompatActivity {

    Process su;
    EditText cmdInput;
    TextView cmdOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cmdInput = (EditText) findViewById(R.id.input_home_cmd_input);
        cmdOutput = (TextView) findViewById(R.id.label_home_cmd_output);

        cmdInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(v.getText().toString().trim().length() > 0){
                        processCmd(v.getText().toString().trim());
                    }
                }
                return false;
            }
        });
    }

    private void processCmd(String cmd){
        if (cmd.equalsIgnoreCase("clear output")) {
            cmdInput.setText("");
            cmdOutput.setText("");
            Toast.makeText(this, "Output cleared", Toast.LENGTH_SHORT).show();
        }else{
            try {
                su = Runtime.getRuntime().exec("su");
                InputStream in = su.getInputStream();
                OutputStream out = su.getOutputStream();
                out.write(cmd.getBytes());
                out.flush();
                out.close();
                byte[] buffer = new byte[1024 * 12];
                int length = in.read(buffer);
                if(length > -1){
                    String content = new String(buffer, 0, length);
                    cmdInput.setText("");
                    String currentText = cmdOutput.getText().toString();
                    String newText = currentText + "\n> " + cmd + "\n" + content.trim();
                    cmdOutput.setText(newText);
                    su.waitFor();
                }else{
                    Toast.makeText(this, "Invalid command", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error processing command.\nSU Access is Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
