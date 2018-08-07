package com.gregorjosh.engine;

import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.gregorjosh.engine.*;

public class MainActivity extends Activity {
	Button btnLeft, btnJump, btnRight, btnGravity;
	GameView gameView;

	private OnTouchListener moveRight = new OnTouchListener() {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
				case motionEvent.ACTION_DOWN:
					gameView.player.isMoving = true;
					gameView.player.direction = GameView.DIR_RIGHT;
					gameView.player.spritesheet.useOriginal();
					break;
				case motionEvent.ACTION_UP:
					gameView.player.isMoving = false;
					break;
			}
			
			return false;
		}
	};

	private OnTouchListener moveLeft = new OnTouchListener() {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
				case motionEvent.ACTION_DOWN:
					gameView.player.isMoving = true;
					gameView.player.direction = GameView.DIR_LEFT;
					gameView.player.spritesheet.useInverted();
					break;
				case motionEvent.ACTION_UP:
					gameView.player.isMoving = false;
					break;
			}

			return false;
		}
	};

	private OnClickListener gravity = new OnClickListener() {
		public void onClick(View view) {
			if (gameView.gravityIsOn)
				gameView.gravityIsOn = false;
			else
				gameView.gravityIsOn = true;
		}
	};

	private OnClickListener jump = new OnClickListener() {
		public void onClick(View view) {
			gameView.player.jump();
		}
	};
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.main);
		
		btnLeft = findViewById(R.id.btn_left);
		btnLeft.setOnTouchListener(moveLeft);
		
		btnJump = findViewById(R.id.btn_jump);
		btnJump.setOnClickListener(jump);
		
		btnRight = findViewById(R.id.btn_right);
		btnRight.setOnTouchListener(moveRight);
	
		btnGravity = findViewById(R.id.btn_gravity);
		btnGravity.setOnClickListener(gravity);
		
		gameView = findViewById(R.id.game);
    }

	protected void onPause() {
		super.onPause();
		gameView.pause();
	}

	protected void onResume() {
		super.onResume();
		gameView.resume();
	}
}
