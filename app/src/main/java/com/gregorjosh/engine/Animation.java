package com.gregorjosh.engine;

import android.graphics.*;
import android.util.*;
import java.util.*;

class Animation {
	int currentFrame, firstFrame, lastFrame, timer, speed;
	Frame frame;
	Map<Integer, Frame> frames;
	Sprite sprite;
	Spritesheet spritesheet;

	Animation(Sprite s, int ff, int lf) {
		sprite = s;
		spritesheet = s.spritesheet;
		currentFrame = firstFrame = ff;
		lastFrame = lf;
		frames = new TreeMap<Integer, Frame>();
		timer = 0;
		speed = 5;

		for (; currentFrame <= lastFrame; ++currentFrame) {
			Frame frame = new Frame(currentFrame);
			frames.put(currentFrame, frame);
		}

		currentFrame = firstFrame;
		update(); 

		Log.i("GameView", "Animation initilized");
	}

	void animate() {
		if (timer > 10 - speed) {
			if (spritesheet.isInverted) {
				if (currentFrame >= firstFrame + 1) {
					currentFrame--;
					update();
				} else
					rewind();
			} else {
				if (currentFrame <= lastFrame - 1) { 
					currentFrame++;
					update();
				} else
					rewind();
			}

			timer = 0;
		} else
			timer++;
	}

	void goBack() {
		if (spritesheet.isInverted) {
			nextFrame();
			return;
		}

		if (currentFrame >= firstFrame + 1) {
			currentFrame--;
			update();
		}
	}

	void nextFrame() {
		if (spritesheet.isInverted) {
			goBack();
			return;
		}

		if (currentFrame <= lastFrame - 1) {
			currentFrame++;
			update();
		}
	}

	void setFrame(int index) {
		if (index >= firstFrame && index <= lastFrame) {
			currentFrame = index;
			update();
		}
	}

	void rewind() {
		if (spritesheet.isInverted) {
			currentFrame = lastFrame;
		} else {
			currentFrame = firstFrame;
		}

		update();
	}

	void update() {
		frame = frames.get(currentFrame);
		sprite.drawFrom = frame.drawFrom;
	}

	class Frame {
		int index, x, y, width, height;
		Rect drawFrom;

		Frame(int i) {
			index = i;
			width = spritesheet.width / spritesheet.columns;
			height = spritesheet.height / spritesheet.rows;

			x = ((i - 1) % spritesheet.columns) * width;
			y = ((i - 1) / spritesheet.columns) * height;

			drawFrom = new Rect(x, y, x + width, y + height);
		}
	}
}

class Spritesheet { 
	Bitmap bitmap, origBitmap, invBitmap;
	boolean isInverted;
	GameView gameView;
	int width, height, columns, rows;

	Spritesheet(int ss, int c, int r,int w, int h, GameView gv) {
		gameView = gv;
		bitmap = BitmapFactory.decodeResource(gameView.getResources(), ss);
		origBitmap = Bitmap.createScaledBitmap(bitmap, w, h, false);
		invBitmap = Bitmap.createScaledBitmap(bitmap, -w, h, false);

		width = w;
		height = h;
		columns = c;
		rows = r;

		useOriginal();
	}

	void useOriginal() {
		bitmap = origBitmap;
		isInverted = false;
	}

	void useInverted() {
		bitmap = invBitmap;
		isInverted = true;
	}
}
