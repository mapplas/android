package com.synesth.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import app.synesth.com.R;

public class ProblemDialog extends Dialog {

    public interface ReadyListener {
        public void ready(String name);
    }

    private String name;
    private ReadyListener readyListener;
    private EditText mText;

    public ProblemDialog(Context context, String name, ReadyListener readyListener) {
        super(context);
        this.name = name;
        this.readyListener = readyListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.problem_dialog);
        
        Button buttonOK = (Button) findViewById(R.id.btnAccept);
        buttonOK.setOnClickListener(new OKListener());
        
        Button buttonKO = (Button) findViewById(R.id.btnCancel);
        buttonKO.setOnClickListener(new KOListener());
        
        mText = (EditText) findViewById(R.id.txtProblem);
    }

    private class OKListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            readyListener.ready(String.valueOf(mText.getText()));
            
            ProblemDialog.this.dismiss();
        }
    }
    
    private class KOListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            readyListener.ready("CANCEL");
            
            ProblemDialog.this.dismiss();
        }
    }

}