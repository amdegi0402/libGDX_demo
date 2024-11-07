/**
 * GameController.java
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package com.amdegient.controller;

import com.amdegient.model.GameModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author FLM
 * @version 1.0.0
 */
public class GameController {
	private static final float PLAYER_PADDLE_SPEED = 300f; // プレイヤーパドルの速度
	private static final float AI_PADDLE_SPEED = 250f; // AIパドルの追従速度

	private final GameModel model;

	/*
	 * コンストラクタ：ゲームモデルを受け取る
	 */
	public GameController(GameModel model) {
		this.model = model;
	}

	/*
	 * 入力処理およびゲーム状態の更新
	 */
	public void update(float deltaTime) {
		handlePlayerInput(deltaTime);
		handleAiMovement(deltaTime);
		model.updateBallPosition(deltaTime);// ボールの位置を更新するためのメソッド
	}

	/*
	 * プレイヤーの入力処理を行うメソッド
	 */
	private void handlePlayerInput(float deltaTime) {
		// キーボード入力によるパドル操作
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			model.movePlayerPaddle(-PLAYER_PADDLE_SPEED * deltaTime);
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			model.movePlayerPaddle(PLAYER_PADDLE_SPEED * deltaTime);
		}
	}

	/*
	 * AIのパドルをボールに追従させる処理
	 */
	private void handleAiMovement(float deltaTime) {
		Rectangle ball = model.getBall();
		Rectangle aiPaddle = model.getAiPaddle();

		// ボールに追従させるためのAIパドルの中央位置を設定
		float aiPaddleCenterY = aiPaddle.y + aiPaddle.height / 2;
		if (ball.y + ball.height / 2 > aiPaddleCenterY) {
			model.moveAiPaddle(AI_PADDLE_SPEED * deltaTime);// ボールの方向に移動
		} else if (ball.y + ball.height / 2 < aiPaddleCenterY) {
			model.moveAiPaddle(-AI_PADDLE_SPEED * deltaTime); // ボールの方向に移動
		}
	}
}
