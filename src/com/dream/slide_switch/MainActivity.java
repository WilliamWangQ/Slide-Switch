package com.dream.slide_switch;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dream.slide_switch.ToggleButton.OnToggleStateChangeListener;
import com.dream.slide_switch.ToggleButton.ToggleState;

public class MainActivity extends Activity {

	
		private ToggleButton toggle;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			toggle = (ToggleButton) findViewById(R.id.toggle);

			//设置背景图片
			toggle.setToggleBackgroundResource(R.drawable.switch_background);
			//设置滑动图片
			toggle.setSlideImage(R.drawable.slide_image);
			//设置默认状态是开
			toggle.setToggleState(ToggleState.Close);

			//设置状态改变的监听器
			toggle.setOnToggleStateChangeListener(new OnToggleStateChangeListener() {
				@Override
				public void onToggleStateChange(ToggleState mState) {
				
					Toast.makeText(MainActivity.this, mState==ToggleState.Close?"关闭":"开启", 0).show();
				}
			});
		}

    
}
