package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class StageEditor implements Screen {
	
	// 定数
	private static final int MAX_STAGE			= 1;
	private static final int MAX_PLAYER			= 1;
	private static final int MAX_ENEMY			= 10;
	private static final int MAX_STAGEOBJECT	= 10;
	
	// 変数
	private static int 			stageNum;
	private static Vector2		position = new Vector2( 0.0f, 0.0f);
	private Box2DDebugRenderer	renderer = new Box2DDebugRenderer();
	public static Mouse mouse = new Mouse();
	
	/**
	 * 初期化
	 * とりあえず全部生成
	 */
	public static void Init() {
		for( int i = 0; i < MAX_STAGE; i ++) {
//			BackgroundManager.CreateBackground(i + 1, false);
		}
		for( int i = 0; i < MAX_PLAYER; i ++ ) {
//			PlayerManager.CreatePlayer(new Vector2(0.0f, 0.0f));
		}
		for( int i = 0; i < MAX_ENEMY; i ++ ) {
			EnemyManager.CreateEnemy(Enemy.NORMAL, new Vector2(0.0f, 0.0f));
		}
		for( int i = 0; i < MAX_STAGEOBJECT; i ++ ) {
			StageObjectManager.CreateStageObject(StageObject.ROCK, new Vector2(0.0f, 0.0f));
		}
	}
	
	/**
	 * 更新
	 */
	private void Update() {
//		ChangeStage();
		Move();
	}
	
	/**
	 * 描画
	 */
	private void Draw() {		
		// 全部クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		GameMain.spriteBatch.setProjectionMatrix(GameMain.camera.combined);		// プロジェクション行列のセット
		GameMain.spriteBatch.begin();									// 描画開始
		{
			BackgroundManager.GetBackground(0).Draw(Background.FAR);
			BackgroundManager.GetBackground(0).Draw(Background.MAIN);

			PlayerManager.Draw();
			EnemyManager.Draw();
			StageObjectManager.Draw();
			
		}
		GameMain.spriteBatch.end();										// 描画終了
		renderer.render(GameMain.world, GameMain.camera.combined);
		GameMain.world.step(Gdx.graphics.getDeltaTime(), 20, 20);
	}
	
	/**
	 * マウス処理
	 */
	private void Mouse() {
		if (Gdx.input.isTouched()) {
			
		}
	}
	
	/**
	 * カメラ移動
	 */
	private void Move() {
		GameMain.camera.position.set(position.x, position.y, 0);
		
		if( Gdx.input.isKeyPressed(Keys.UP) ) {
			position.y ++;
		}
		if( Gdx.input.isKeyPressed(Keys.DOWN) ) {
			position.y --;
		}
		if( Gdx.input.isKeyPressed(Keys.RIGHT) ) {
			position.x ++;
		}
		if( Gdx.input.isKeyPressed(Keys.LEFT) ) {
			position.x --;
		}
		GameMain.camera.update();
	}
	
	
	/**
	 * ステージ遷移
	 */
	private void ChangeStage() {
		if( Gdx.input.isKeyPressed(1)) {
			stageNum = 1;
		}
		
		if( Gdx.input.isKeyPressed(2)) {
			stageNum = 2;
		}
		
		if( Gdx.input.isKeyPressed(3)) {
			stageNum = 3;
		}
	}
	
	

	@Override
	public void render(float delta) {
		Update();
		Draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
