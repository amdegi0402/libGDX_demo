/**
 * GameModel.java
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package com.amdegient.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author FLM
 * @version 1.0.0
 */
public class GameModel {
	// 定数の定義
	private static final float PADDLE_WIDTH = 10; // パドル横幅
	private static final float PADDLE_HEIGHT = 100; // パドル縦幅
	private static final float BALL_SIZE = 30; // ボールサイズ
	private static final float INITIAL_BALL_SPEED_X = 200; // x軸ボールスピード
	private static final float INITIAL_BALL_SPEED_Y = 200;// y軸ボールスピード
	private float speedBoostTimer = 0; // 速度が2倍になる時間を管理
	private boolean isSpeedBoosted = false; // 速度が2倍になっているか？
	//private Vector2 ballSpeedNow; // 現在のボール速度

	// フィールド変数
	private Rectangle playerPaddle;// 長方形クラス プレイヤーパドル
	private Rectangle aiPaddle;// 長方形クラス comパドル
	private Rectangle ball;// 長方形クラス ボール
	private Rectangle item;// イベントアイテム
	private Vector2 ballSpeed;// X座標とY座標の2つの値を使って、位置や速度、方向などを表現
	private int playerScore;
	private int aiScore;
	private boolean isItemActive = false; // アイテムがアクティブかどうか
	//private int itemCount = 0; // 取得したアイテムの数
	private float itemSpawnTimer = 0;//アイテム生成タイマー

	/**
	 * コンストラクタ: ゲーム内のパドルとボールを初期化します。
	 */
	public GameModel() {
		float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();

		// パドルとボールを初期化
		this.playerPaddle
				= new Rectangle(50, screenHeight / 2 - PADDLE_HEIGHT / 2,
						PADDLE_WIDTH, PADDLE_HEIGHT);// プレイヤーパドルの初期位置とサイズを設定
		this.aiPaddle = new Rectangle(screenWidth - 60,
				screenHeight / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH,
				PADDLE_HEIGHT);// comパドルの初期位置とサイズを設定
		this.ball = new Rectangle(screenWidth / 2 - BALL_SIZE / 2,
				screenHeight / 2 - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE);// ボール初期位置とサイズ設定
		this.item = new Rectangle(0, 0, 30, 30); // アイテム初期値は画面外

		// ボールの速度を初期化
		ballSpeed = new Vector2(INITIAL_BALL_SPEED_X, INITIAL_BALL_SPEED_Y);

		playerScore = 0;
		aiScore = 0;
	}

	public void itemUpdate(float deltaTime) {
		// アイテム生成タイマーの更新
		itemSpawnTimer += deltaTime;

		// 一定時間ごとにアイテムを生成
		if (itemSpawnTimer > 15 && !isItemActive) {
			spawnNewItem(); // 新しいアイテムをランダムな場所に生成
			itemSpawnTimer = 0;
		}
	}

	/*
	 * ボール位置を更新する
	 */
	public void updateBallPosition(float deltaTime) {
		// ボールの位置を更新
		ball.x += ballSpeed.x * deltaTime;
		ball.y += ballSpeed.y * deltaTime;

		// 画面の上端もしくは下端にぶつかったときの処理
		if (ball.y < 0 || ball.y > Gdx.graphics.getHeight() - ball.height) {
			ballSpeed.y = -ballSpeed.y; // Y方向の速度を反転させる（ボールを跳ね返す）
		}

		// ボールが左端に出た場合（プレイヤー側のミス）
		if (ball.x < 0) {
			// AIスコアを増やす（スコア管理のメソッドを追加）
			setAiScore(1);// 得点追加
			resetPlayerPaddle();// プレイヤー位置リセット
			resetAiPaddle();// AI位置リセット
			resetBall(); // ボールを中央にリセット
		}

		// ボールが右端に出た場合（AI側のミス）
		if (ball.x > Gdx.graphics.getWidth()) {
			// プレイヤーのスコアを増やす（スコア管理のメソッドを追加）
			setPlayerScore(1);// 得点追加
			resetPlayerPaddle();// プレイヤー位置リセット
			resetAiPaddle();// AI位置リセット
			resetBall();// ボールを中央にリセット
		}

		// パドルとの衝突判定（プレイヤーのパドル）
		if (ball.overlaps(playerPaddle)) {
			ballSpeed.x = -ballSpeed.x; // X方向の速度を反転させる（ボールを跳ね返す）
		}

		// パドルとの衝突判定（AIパドル）
		if (ball.overlaps(aiPaddle)) {
			ballSpeed.x = -ballSpeed.x; // X方向の速度を反転させる（ボールを跳ね返す）
		}

		// 速度が2倍になっている場合（イベント実行中）はタイマーを減らす
		if (isSpeedBoosted) {
			speedBoostTimer -= deltaTime;
			if (speedBoostTimer <= 0) {
				// ボールの速度を初期化
//				ballSpeed = new Vector2(INITIAL_BALL_SPEED_X,
//						INITIAL_BALL_SPEED_Y);
				resetBallSpeed();
			}
		}
	}

	// ボール速度が2倍になる処理
	public void doubleBallSpeed(float duration) {
		ballSpeed.set(ballSpeed.x * 2, ballSpeed.y * 2);
		isSpeedBoosted = true;// 速度がブーストされている状態
		speedBoostTimer = duration; //ブースとの持続時間を設定
	}

	//元のボール速度に戻す
	private void resetBallSpeed() {
		ballSpeed.set(ballSpeed); //元の速度に戻す
		isSpeedBoosted = false; //ブースト解除
	}

	// ボールとアイテムの衝突判定
	public boolean checkBallItemCollision() {
		System.out.println("isItemActive: " + isItemActive);
		if (isItemActive && ball.overlaps(item)) {
			
			//itemCount++; // アイテム取得数を増やす
			isItemActive = false; // アイテムを非表示に
			doubleBallSpeed(5); //５秒間ボール速度を2倍にする
			//System.out.println("(GameModel)アイテムを取得しました。itemCount: " + itemCount);
			return true; // アイテムを取得
		}
		return false; // アイテムを取得していない
	}

	// アイテムをY軸のランダムな位置に生成
	private void spawnNewItem() {
		item.x = Gdx.graphics.getWidth() / 2;
		item.y = MathUtils.random(100, Gdx.graphics.getHeight() - 100);
		isItemActive = true;
	}

	/*
	 * プレイヤーパドルを動かすメソッド
	 */
	public void movePlayerPaddle(float dy) {
		// パドルの座標をdy分移動
		playerPaddle.y += dy;

		// パドルが画面外に出ないように制限する
		if (playerPaddle.y < 0) {
			playerPaddle.y = 0; // 画面の下端に固定
		} else if (playerPaddle.y
				+ playerPaddle.height > Gdx.graphics.getHeight()) {
			playerPaddle.y = Gdx.graphics.getHeight() - playerPaddle.height; // 画面の上端に固定
		}
	}

	/*
	 * AIパドルを動かすメソッド
	 */
	public void moveAiPaddle(float dy) {
		// AIパドルのY座標をdy分移動
		aiPaddle.y += dy;

		// AIパドルが画面外に出ないように制限
		if (aiPaddle.y < 0) {
			aiPaddle.y = 0; // 画面の下端に固定
		} else if (aiPaddle.y + aiPaddle.height > Gdx.graphics.getHeight()) {
			aiPaddle.y = Gdx.graphics.getHeight() - aiPaddle.height; // 画面の上端に固定
		}
	}

	/**
	 * ボールの位置と速度をリセットする
	 */
	public void resetBall() {
		ball.setPosition(Gdx.graphics.getWidth() / 2 - ball.width / 2,
				Gdx.graphics.getHeight() / 2 - ball.height / 2);
		ballSpeed.set(INITIAL_BALL_SPEED_X, INITIAL_BALL_SPEED_Y); // 初期速度にリセット
	}

	/**
	 * プレイヤーのパドルを初期位置にリセットします。
	 */
	public void resetPlayerPaddle() {
		playerPaddle.setPosition(50,
				Gdx.graphics.getHeight() / 2 - PADDLE_HEIGHT / 2);
	}

	/**
	 * AIのパドルを初期位置にリセットします。
	 */
	public void resetAiPaddle() {
		aiPaddle.setPosition(Gdx.graphics.getWidth() - 60,
				Gdx.graphics.getHeight() / 2 - PADDLE_HEIGHT / 2);
	}

	/**
	 * playerPaddleのGetter
	 * @return playerPaddle
	 */
	public Rectangle getPlayerPaddle() {
		return playerPaddle;
	}

	/**
	 * playerPaddleのSetter
	 * @param playerPaddle
	 */
	public void setPlayerPaddle(Rectangle playerPaddle) {
		this.playerPaddle = playerPaddle;
	}

	/**
	 * aiPaddleのGetter
	 * @return aiPaddle
	 */
	public Rectangle getAiPaddle() {
		return aiPaddle;
	}

	/**
	 * aiPaddleのSetter
	 * @param aiPaddle
	 */
	public void setAiPaddle(Rectangle aiPaddle) {
		this.aiPaddle = aiPaddle;
	}

	/**
	 * ballのGetter
	 * @return ball
	 */
	public Rectangle getBall() {
		return ball;
	}

	/**
	 * ballのSetter
	 * @param ball
	 */
	public void setBall(Rectangle ball) {
		this.ball = ball;
	}

	/**
	 * ballSpeedのGetter
	 * @return ballSpeed
	 */
	public Vector2 getBallSpeed() {
		return ballSpeed;
	}

	/**
	 * ballSpeedのSetter
	 * @param ballSpeed
	 */
	public void setBallSpeed(Vector2 ballSpeed) {
		this.ballSpeed = ballSpeed;
	}

	/**
	 * playerScoreのGetter
	 * @return playerScore
	 */
	public int getPlayerScore() {
		return playerScore;
	}

	/**
	 * playerScoreのSetter
	 * @param playerScore
	 */
	public void setPlayerScore(int playerScore) {
		this.playerScore += playerScore;
	}

	/**
	 * aiScoreのGetter
	 * @return aiScore
	 */
	public int getAiScore() {
		return aiScore;
	}

	/**
	 * aiScoreのSetter
	 * @param aiScore
	 */
	public void setAiScore(int aiScore) {
		this.aiScore += aiScore;
	}

	/**
	 * itemのGetter
	 * @return item
	 */
	public Rectangle getItem() {
		return item;
	}

	/**
	 * itemのSetter
	 * @param item
	 */
	public void setItem(Rectangle item) {
		this.item = item;
	}

	/**
	 * isItemActiveのGetter
	 * @return isItemActive
	 */
	public boolean isItemActive() {
		return isItemActive;
	}

	/**
	 * isItemActiveのSetter
	 * @param isItemActive
	 */
	public void setItemActive(boolean isItemActive) {
		this.isItemActive = isItemActive;
	}

	

	/**
	 * itemSpawnTimerのGetter
	 * @return itemSpawnTimer
	 */
	public float getItemSpawnTimer() {
		return itemSpawnTimer;
	}

	/**
	 * itemSpawnTimerのSetter
	 * @param itemSpawnTimer
	 */
	public void setItemSpawnTimer(float itemSpawnTimer) {
		this.itemSpawnTimer = itemSpawnTimer;
	}

}
