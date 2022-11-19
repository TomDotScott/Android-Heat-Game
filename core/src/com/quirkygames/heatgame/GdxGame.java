package com.quirkygames.heatgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GdxGame extends ApplicationAdapter {
	private UpdateThread m_gameLoop;

	SpriteBatch batch;
	private Player m_player;



	@Override
	public void create () {
		batch = new SpriteBatch();
		m_player = new Player(new Vector2(300, 300));

		m_gameLoop = new UpdateThread(this);

		m_gameLoop.startLoop();
	}

	public void update()
	{
		m_player.update();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();

		m_player.draw(batch);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		m_player.dispose();
	}
}
