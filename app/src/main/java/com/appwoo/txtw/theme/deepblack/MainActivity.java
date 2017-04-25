package com.appwoo.txtw.theme.deepblack;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import android.os.SystemProperties;

public class MainActivity extends AppCompatActivity {

    private EditText m_editText_Input;
    private Button m_button_Run;
    private final static String TAG = "my_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_editText_Input = (EditText)findViewById(R.id.editText_Input);
        m_button_Run = (Button)findViewById(R.id.button_Run);

        m_button_Run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                OnClick_run();
            }
        });
    }

    private void OnClick_run()
    {
        if(null == m_editText_Input) return;

        String strCmd = m_editText_Input.getText().toString();
        m_editText_Input.setText("");

        if(null == strCmd) return;

        Log.e(TAG, " strCmd : " + strCmd);

        String[] Vars = strCmd.split(" ");

        List lVars = new ArrayList();

        for(int i =0; i < Vars.length ; i++)
        {
            String strTmp = Vars[i].trim();
            if(!strTmp.isEmpty() )
            {
                lVars.add(strTmp);
                Log.e(TAG, "Vars : [" +  Vars[i] + "]");
            }
        }

        if(lVars.size() < 1) return;
        strCmd = (String)lVars.get(0);

        Log.e(TAG, " strCmd : " + strCmd);
        if(strCmd.equals("1") )
        {
            SystemProperties.set("persist.sys.usbdebugdisablelw", "1");
            Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 1);
        }
        else if(strCmd.equals("2") )
        {
            Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0);
            SystemProperties.set("persist.sys.usbdebugdisablelw", "0");
        }
    }
}
