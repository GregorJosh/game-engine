package com.gregorjosh.engine;

import android.graphics.*;
import java.util.*;

class World {
	Bitmap bmp;
	boolean t1IsFirst;
	GameView gameView;
	int screenWidth, screenHeight, bmpWidth, bmpHeight;
	Rect drawFrom, t1, t2;
	Vector<Object> objects;
	
	World(GameView gv) {
		gameView = gv;
		screenWidth = gv.getWidth();
		screenHeight = gv.getHeight();
			
		bmp = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bgl1);
		bmpWidth = bmp.getWidth();
		bmpHeight = bmp.getHeight();
			
		drawFrom = new Rect(0, 0, bmpWidth, bmpHeight);
		t1 = new Rect(0, 0, bmpWidth, bmpHeight);
		t2 = new Rect(t1.right, 0, t1.right + bmpWidth, bmpHeight);
			
		t1IsFirst = true;
		objects = new Vector<>();
	}
		
	void draw() {
		gameView.canvas.drawBitmap(bmp, drawFrom, t1, gameView.paint);
		gameView.canvas.drawBitmap(bmp, drawFrom, t2, gameView.paint);
	
		Iterator it = objects.iterator();
	
		while (it.hasNext()) {
			Object object = (Object) it.next();
			object.draw();
		}
	}
	
	void scroll(short direction, int velocity) {
		Iterator it = objects.iterator();
		
		if (direction == GameView.DIR_RIGHT) {
			while (it.hasNext()) {
				Object object = (Object) it.next();
				object.velocity.x += velocity;
			}
			
			t1.left += velocity;
			t2.left += velocity;
		}
			
		if (direction == GameView.DIR_LEFT) {
			while (it.hasNext()) {
				Object object = (Object) it.next();
				object.velocity.x -= velocity;
			}
			
			t1.left -= velocity;
			t2.left -= velocity;
		}
	}
	
	void update() {
		Iterator it = objects.iterator();

		while (it.hasNext()) {
			Object object = (Object) it.next();
			object.update();
		}
		
		if (t1IsFirst) {
			if (t1.left > 0) {
				t2.left = t1.left - bmpWidth;
				t1IsFirst = false;
			}
			
			if (t2.right < gameView.getWidth()) {
				t1.left = t2.right - 5;
				t1IsFirst = false;
			}
		} else {
			if (t2.left > 0) {
				t1.left = t2.left - bmpWidth;
				t1IsFirst = true;
			}
				
			if (t1.right < gameView.getWidth()) {
				t2.left = t1.right - 5;
				t1IsFirst = true;
			}
		}
		
		t1.right = t1.left + bmpWidth;
		t2.right = t2.left + bmpWidth;
	}
}
