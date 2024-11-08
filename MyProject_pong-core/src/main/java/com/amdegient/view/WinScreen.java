/**
 * WinScreen.java
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package com.amdegient.view;

import com.amdegient.MyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 *
 * @author FLM
 * @version 1.0.0
 */
public class WinScreen implements Screen {
	private MyGame game;
	private Stage stage;
	private SpriteBatch batch;
	private ImageButton continuteButton;
	private Texture continuteBackTexture;
	private Texture buttonUpTexture;
	private Texture buttonDownTexture;
	private BitmapFont font; //文字列を表示するフォント
	private boolean result;//ゲームの勝敗結果

	public WinScreen(MyGame game, boolean result) {
		this.game = game;
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();//フォントオブジェクト生成
		font.getData().setScale(4.0f);
		this.result = result;

		// ステージ作成
		stage = new Stage(new ScreenViewport()); // 画面の解像度に対応するためのビューポートで、ウィンドウのサイズに応じて表示領域を調整
		Gdx.input.setInputProcessor(stage); // ステージを入力の処理対象に設定

		// ボタンの画像ロード
		buttonUpTexture = new Texture(Gdx.files.internal("conButton_up.png")); // 通常時のボタン背景
		buttonDownTexture = new Texture(Gdx.files.internal("conButton_down.png")); // 通常時のボタン背景

		// 画像を描画
		TextureRegionDrawable buttonUp
				= new TextureRegionDrawable(buttonUpTexture);
		TextureRegionDrawable buttonDown
				= new TextureRegionDrawable(buttonDownTexture);

		//文字列を表示

		// ImageButtonを作成
		continuteButton = new ImageButton(buttonUp, buttonDown); // 通常時と押されたときの画像を設定

		// ボタン位置を設定（画面中央）
		continuteButton.setPosition(
				Gdx.graphics.getWidth() / 2 - continuteButton.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - continuteButton.getHeight() / 2);

		// ボタンがクリックされたときの処理を設定
		continuteButton.addListener(event -> {
			if (continuteButton.isPressed()) {
				game.setScreen(new GameScreen(game)); // メインゲームへ遷移
			}
			return true;
		});

		// ステージにボタンを追加
		stage.addActor(continuteButton);

		// 背景画像を読み込む
		if(this.result) {
			continuteBackTexture
			= new Texture(Gdx.files.internal("winBack.png"));

		}else {
			continuteBackTexture
			= new Texture(Gdx.files.internal("loseBack.png"));
		}

	}


	@Override
	public void render(float delta) {
		// 画面をクリア
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// 画面を描画
		batch.begin();
		batch.draw(continuteBackTexture, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch.end();

		// ステージを描画
		stage.act(delta);
		stage.draw();

		//表示する文字列を選択
		if(this.result) {
			drawResult("YOU WIN!");
		}else {
			drawResult("YOU LOSE");
		}

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

	//文字列を描画するためのメソッド
	private void drawResult(Object text) {
		batch.begin();
		final String resultText = text.toString();//テキストを文字列に変換

		//文字列のレイアウトを計算する
		final GlyphLayout layout =new GlyphLayout();
		layout.setText(font,  resultText); //テキストに基づいてレイアウトを計算
		final float textWidth = layout.width; //テキストのピクセル幅を取得

		final float xPosition = (Gdx.graphics.getWidth() - textWidth) / 2; //画面中央に表示
		font.draw(batch, resultText, xPosition, Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 4);
		batch.end();
	}
}
