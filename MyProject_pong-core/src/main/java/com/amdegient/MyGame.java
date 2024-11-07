/**
 * MyGame.java
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package com.amdegient;

import com.amdegient.view.TitleScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author FLM
 * @version 1.0.0
 */


public class MyGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new TitleScreen(this));  // 最初にタイトル画面を表示
    }

    @Override
    public void render() {
        super.render();  // 現在のスクリーンを描画
    }

    @Override
    public void dispose() {
        batch.dispose();  // リソースの解放
    }
}
