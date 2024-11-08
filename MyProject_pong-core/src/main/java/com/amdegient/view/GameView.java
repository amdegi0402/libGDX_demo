/**
 * GameView.java
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package com.amdegient.view;

import com.amdegient.model.GameModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author FLM
 * @version 1.0.0
 */
public class GameView {
	// 定数の定義
	private static final float SCORE_POSITION_X_PLAYER = 100;// プレイヤーのスコアを表示するX座標を表す定数を定義
	private static final float SCORE_POSITION_X_AI = 500;// COM３のスコアを表示するX座標を表す定数を定義
	private static final float SCORE_POSITION_Y = 20;// スコアボード表示のY座標

	// 描画に使用するリソース
	private SpriteBatch batch; // 複数の画像（テクスチャ）を一度に描画し、描画処理を効率化するためのクラス
	private Texture paddleTexture; // ゲームに表示するための2D画像（テクスチャ）を読み込み、管理するクラス
	private Texture ballTexture;
	private Texture itemTexture;
	private BitmapFont font;// テキスト（文字列）を描画するためのクラス

	/*
	 * コンストラクタ： 描画に必要なリソースを初期化
	 */
	public GameView() {
		this.batch = new SpriteBatch();

		// テスクチャのエラーハンドリング
		try {
			paddleTexture = new Texture("paddle.png"); // 画像を読み込み
			ballTexture = new Texture("ball.png");
			itemTexture = new Texture("item.png");
		} catch (com.badlogic.gdx.utils.GdxRuntimeException e) {
			System.err.println("画像ファイルが見つからない。もしくは破損しています。: " + e.getMessage());
			// エラーが発生した場合はゲームを終了
			Gdx.app.exit();
		} catch (Exception e) {
			System.err.println("その他、予期せぬエラーが発生しました。: " + e.getMessage());
			// エラーが発生した場合はゲームを終了
			Gdx.app.exit();
		}

		// フォント初期化
		font = new BitmapFont();
	}

	/*
	 * ゲームの描画処理
	 */
	public void render(GameModel model) {
		// 画面クリア
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);// 画面上に描画された前のフレームの残りが消去され、真っさらな状態にする

		// 描画の開始
		batch.begin();

		//アイテム取得数の表示
		//font.draw(batch, "Item Count: " + model.getItemCount(), 20, Gdx.graphics.getHeight() - 30);

		// プレイヤーとAIのパドルを描画
		drawPaddles(model);

		// ボールを描画
		drawBall(model);
		

		//アイテムの描画
		if(model.isItemActive()) {
			Rectangle item = model.getItem(); //GameModel クラスから**アイテムの情報（位置とサイズ）**を取得
			batch.draw(itemTexture, item.x, item.y, item.width, item.height);
		}

		// スコアを描画
		drawScores(model);

		// 描画の終了
		batch.end();
	}

	/*
	 * プレイヤーとAIのパドルを描画
	 */
	private void drawPaddles(GameModel model) {
		// プレイヤーのパドルの描画
		Rectangle playerPaddle = model.getPlayerPaddle();
		batch.draw(paddleTexture, playerPaddle.x, playerPaddle.y,
				playerPaddle.width, playerPaddle.height);

		// AIのパドルを描画
		Rectangle aiPaddle = model.getAiPaddle();
		batch.draw(paddleTexture, aiPaddle.x, aiPaddle.y, aiPaddle.width,
				aiPaddle.height);
	}

	/*
	 * ボールを描画します。
	 */
	private void drawBall(GameModel model) {
		Rectangle ball = model.getBall();
		batch.draw(ballTexture, ball.x, ball.y, ball.width, ball.height);
	}

	/*
	 * スコア描画
	 */
	private void drawScores(GameModel model) {
		// プレイヤーのスコア描画
		font.draw(batch, "Player: " + model.getPlayerScore(),
				SCORE_POSITION_X_PLAYER,
				Gdx.graphics.getHeight() - SCORE_POSITION_Y);
		// AIのスコア描画
		font.draw(batch, "AI: " + model.getAiScore(),
				SCORE_POSITION_X_AI,
				Gdx.graphics.getHeight() - SCORE_POSITION_Y);
	}

	/*
	 * リソースを開放
	 */
	public void dispose() {
		batch.dispose();// 内部で使用しているリソース（バッファなど）を解放
		paddleTexture.dispose();// GPUメモリにロードされたテクスチャを解放
		ballTexture.dispose();// GPUメモリにロードされたテクスチャを解放
		font.dispose();// 使用しているフォントデータを解放
	}
}
