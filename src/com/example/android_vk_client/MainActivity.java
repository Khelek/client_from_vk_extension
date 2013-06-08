package com.example.android_vk_client;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.example.VkClient.R;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
        public void authorization_Click(View v){ 
            EditText email = (EditText)findViewById(R.id.email);
            EditText pass = (EditText)findViewById(R.id.password);
            // function autho
            if(true){
            Intent intent = new Intent(MainActivity.this, Freinds.class);
            intent.putExtra("id", 1234);
	    startActivity(intent);
            }
            else{
                TextView errorText = (TextView)findViewById(R.id.errorText);
                errorText.setText("Неправильный e-mail или пароль!");
            }
        }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
        public boolean onOptionsItemSelected(MenuItem item) {
	
            switch (item.getItemId()) {
            case R.id.freinds:
                    Intent intent = new Intent(MainActivity.this, Freinds.class);
                    startActivity(intent);
                    return true;
            case R.id.dialogs: Intent intent2 = new Intent(MainActivity.this, Dialogs.class);
                    startActivity(intent2);
                    return true;
            default:
                    return super.onOptionsItemSelected(item);
            }
        }

}
