package com.gregorjosh.engine;

import android.view.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.view.animation.*;
import java.util.*;

class GameView extends SurfaceView implements Runnable {
	boolean gravityIsOn, isPlaying;
	Box box;
	Canvas canvas;
	World world;
	int width, height;
	long fps, frameTime;
	Paint paint;
	Player player;
	SurfaceHolder surfaceHolder;
	Thread thread = null;
	
	final static short DIR_LEFT = 1, DIR_RIGHT = 2;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		surfaceHolder = getHolder();
		paint = new Paint();
	}

	public void pause() {
		isPlaying = false;
	   
		try {
			thread.join();
		} catch (InterruptedException e) { }
	}

	public void resume() {
		isPlaying = true;
		
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		world = new World(this);

		player = new Player(this, 200, 200);
		player.loadSpritesheet(R.drawable.human, 2, 1, 400, 200);
		player.setAnimation("moving", 1, 2);
	
		box = new Box(this, 50, 300);
	
		world.objects.add(player);
		world.objects.add(box);

		
		while (isPlaying) {
			long startTime = System.currentTimeMillis();
			
			update();
			draw();

			frameTime = System.currentTimeMillis() - startTime;

			if (frameTime > 0) fps = 1000 / frameTime;
		}
	}

	private void update() {
		world.update();
	}

	private void draw() {
		if (surfaceHolder.getSurface().isValid()) {	
			canvas = surfaceHolder.lockCanvas();
			canvas.drawColor(Color.GRAY);
			
			paint.setColor(Color.BLACK);
			paint.setTextSize(25);
			
			world.draw();
			
			canvas.drawText("FPS: " + fps, 20, 100, paint);
			
			canvas.drawText("Player position: x = " + player.position.x
				+ " y = " + player.position.y
				, 20, 125, paint);
			canvas.drawText("t1: left = " + world.t1.left
				+ " right = " + world.t1.right
				, 20, 150, paint);
			canvas.drawText("t2: left = " + world.t2.left
				+ " right = " + world.t2.right
				, 20, 175, paint);
			canvas.drawText("jump target x = " + player.jumpTarget.x
				+ " y = " + player.jumpTarget.y 
				, 20, 200, paint);
			canvas.drawText("Player velocity x = " + player.velocity.x
				+ " y = " + player.velocity.y
				, 20, 225, paint);
				
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}
}
