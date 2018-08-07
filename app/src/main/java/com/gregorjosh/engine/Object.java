package com.gregorjosh.engine;

import android.graphics.*;
import android.util.*;
import java.util.*;

abstract class Object {
	boolean onGround;
	GameView gameView;
	int x, y, width, height, bx, by;
	Point position, velocity;
	RectF drawTo;

	Object(GameView v, int w, int h) {
		position = new Point(0, 0);
		bx = position.x + w;
		by = position.y + h;
		width = w;
		height = h;
		drawTo = new RectF(position.x, position.y, bx, by);
		velocity = new Point(0, 0);
		gameView = v;
	}

	abstract void draw();

	void update() {
		Iterator it = gameView.world.objects.iterator();
	
		while (it.hasNext()) {
			Object object = (Object) it.next();
			
			if (object != this && drawTo.intersect(object.drawTo)) {
				velocity.x *= -1;
				velocity.y *= -1;
			}
		}
		
		position.x += velocity.x;
		position.y += velocity.y;
		
		bx = position.x + width;
		by = position.y + height;

		drawTo.set(position.x, position.y, bx, by);
	
		if (gameView.gravityIsOn && !onGround) {
			if (by > gameView.getHeight()) {
				onGround = true;
				velocity.y = 0;
			} else velocity.y = 10; 
		}
	}
}

class Box extends Object {
	Box(GameView v, int w, int h) {
		super(v, w ,h);
	}

	void draw() {
		gameView.canvas.drawRect(drawTo, gameView.paint);
	}
}

class Sprite extends Object { 
	Animation animation;
	Map<String, Animation> animations;
	Rect drawFrom;
	Spritesheet spritesheet;

	Sprite(GameView v, int w, int h) {
		super(v, w, h);
		animations = new HashMap<>();
	}

	void draw() {
		gameView.canvas.drawBitmap(getSpritesheet(), drawFrom, drawTo, gameView.paint);
	}

	Bitmap getSpritesheet() {
		return spritesheet.bitmap;
	}

	void loadSpritesheet(int ss, int c, int r,int w, int h) {
		spritesheet = new Spritesheet(ss, c, r, w, h, gameView);
	}

	void setAnimation(String name, int startFrame, int lastFrame) {
		animations.put(name, new Animation(this, startFrame, lastFrame));
		animation = animations.get(name);
	}
}

class Player extends Sprite {
	boolean isMoving, isJumping;
	int walkSpeed, jumpSpeed, direction, viewDistance, jumpHeight;
	Point jumpTarget;

	Player(GameView v, int w, int h) {
		super(v, w, h);

		position.x = 600;
		walkSpeed = 200;
		viewDistance = 400;
		direction = GameView.DIR_RIGHT;
		jumpHeight = 200;
		jumpTarget = new Point(position.x, position.y - jumpHeight);
	}

	void jump() {
		if (onGround) {
			jumpTarget.set(position.x, position.y - jumpHeight);
			jumpSpeed = 1000;
			isJumping = true;
		}
	}

	void update() {
		if (isMoving) {
			animation = animations.get("moving");
			animation.animate();

			if (direction == GameView.DIR_LEFT) {
				if (position.x - viewDistance <= 0) {
					gameView.world.scroll(GameView.DIR_RIGHT, (int) (walkSpeed / gameView.fps));
					velocity.x = 0;
				} else velocity.x = (int) -(walkSpeed / gameView.fps);
			} else if (direction == GameView.DIR_RIGHT) {
				if (position.x + viewDistance >= gameView.getWidth()) { 
					gameView.world.scroll(GameView.DIR_LEFT, (int) (walkSpeed / gameView.fps));
					velocity.x = 0;
				} else velocity.x = (int) (walkSpeed / gameView.fps);
			}
		} else {
			animation.rewind();
			velocity.x = 0;
		}

		if (isJumping) { 
			if (position.y > jumpTarget.y) {
				velocity.y = (int) -(jumpSpeed / gameView.fps);

				if (jumpSpeed > 100) {
					jumpSpeed -= 40;
				}

				onGround = false;

				Log.i("Player", "Jumping: position Y = " + position.y);
			} else {
				isJumping = false;
				velocity.y = 0;
			}
		}

		super.update();
	}
}
