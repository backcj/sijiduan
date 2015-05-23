package com.rd.callcarDriver;


import com.rd.callcarDriver.R;
import com.rd.callcar.Util.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StepThree extends Activity {
	
	Button ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.threestep_layout);
		ExitApplication.getInstance().addActivity(this);
		ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(StepThree.this, StepOne.class));
			}
		});
	}


}
