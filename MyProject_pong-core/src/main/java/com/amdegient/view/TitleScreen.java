/**
 * Title.java
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package com.amdegient.view;

import com.amdegient.MyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * タイトル画面を表示するためのクラスを作成します。
 * タイトル画面には「Play」ボタンを表示し、そのボタンを押すとメインゲームに遷移するようにします。
 * Texture
 * クラスは、画像そのものをメモリに読み込んで描画できる状態にするためのクラスです。基本的に、**「画像データを管理・保持する」**役割を果たします。
 *
 * ImageButton
 * クラスは、libGDXのUI要素としてボタンを扱うためのクラスです。これは、TextureやTextureRegionDrawableなどを使ってボタンの見た目を定義し、
 * さらにクリックやタッチの処理を提供します。つまり、**「画像を使ったボタンを作成し、ユーザー入力を処理する」**役割を持っています。
 * @author FLM
 * @version 1.0.0
 */
public class TitleScreen implements Screen {
	private final MyGame game; // 画面を切り替えるためのゲームインスタンス（gameという変数に渡されたゲームオブジェクトを保存しています。これにより、タイトル画面からメインゲーム画面に遷移できる）
	private Stage stage; // ゲームの中でUI（ユーザーインターフェース、つまりボタンやテキストなど）を管理するものです。ここでは、「Play」ボタンを表示するためにステージを作成
	private Texture buttonUpTexture; // ボタンの通常状態のテクスチャ
	private Texture buttonDownTexture;// ボタンが押されたときのテクスチャ
	private ImageButton playButton; // playボタン（画像を使ったボタンを作成するためのオブジェクト）
	private Texture backgroundTexture;// 背景画像
	private SpriteBatch batch;// テクスチャを効率的に描画する
	private Texture titleTexture; //タイトル画像のテクスチャ

	public TitleScreen(MyGame game) {
		this.game = game;
		batch = new SpriteBatch();

		// ステージ作成
		stage = new Stage(new ScreenViewport()); // 画面の解像度に対応するためのビューポートで、ウィンドウのサイズに応じて表示領域を調整
		Gdx.input.setInputProcessor(stage); // ステージを入力の処理対象に設定

		// ボタンの画像ロード
		buttonUpTexture = new Texture(Gdx.files.internal("button_up.png")); // 通常時のボタン背景
		buttonDownTexture = new Texture(Gdx.files.internal("button_down.png")); // 通常時のボタン背景

		// 画像を描画
		TextureRegionDrawable buttonUp
				= new TextureRegionDrawable(buttonUpTexture);
		TextureRegionDrawable buttonDown
				= new TextureRegionDrawable(buttonDownTexture);

		// ImageButtonを作成
		playButton = new ImageButton(buttonUp, buttonDown); // 通常時と押されたときの画像を設定

		// ボタン位置を設定（画面中央）
		playButton.setPosition(
				Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - playButton.getHeight() / 2);

		// ボタンがクリックされたときの処理を設定
		playButton.addListener(event -> {
			if (playButton.isPressed()) {
				game.setScreen(new GameScreen(game)); // メインゲームへ遷移
			}
			return true;
		});

		// ステージにボタンを追加
		stage.addActor(playButton);

		// 背景画像を読み込む
		backgroundTexture = new Texture(Gdx.files.internal("background.png"));
		//タイトル文字読み込み
		titleTexture = new Texture(Gdx.files.internal("title.png"));
	}

	@Override
	public void render(float delta) {
		// 画面をクリア
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// 画面を描画
		batch.begin();

		//タイトル文字を幅400px,高さ150pxに縮小
		float desireWidth = 400;
		float desireHeight = 150;

		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(titleTexture, Gdx.graphics.getWidth() / 2 - desireWidth / 2, Gdx.graphics.getHeight() / 2, desireWidth, desireHeight);//タイトル文字の表示位置

		batch.end();

		// ステージを描画
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	/**
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// 画面が表示されるときに呼ばれる処理

	}

	/**
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// ゲームが一時停止されたときの処理（特に何もしない）

	}

	/**
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// ゲームが再開されたときの処理（特に何もしない）

	}

	/**
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// 画面が非表示になるときの処理（特に何もしない）

	}

	/**
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		stage.dispose();
		buttonUpTexture.dispose();
		buttonDownTexture.dispose();
	}
}
