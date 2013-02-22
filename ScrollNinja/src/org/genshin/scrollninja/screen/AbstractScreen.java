package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.object.UpdateManager;
import org.genshin.scrollninja.work.object.gui.Cursor;
import org.genshin.scrollninja.work.render.RenderManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * スクリーンの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractScreen implements Screen
{
	/**
	 * コンストラクタ
	 */
	AbstractScreen()
	{
		//---- カレントスクリーンに設定する。
		setCurrentScreen();
		
		//---- カーソルオブジェクトを生成する。
		cursor = createCursor();
	}
	
	@Override
	public void render(float delta)
	{
		//---- 更新処理を実行する。
		if( !isPaused() )
		{
			// 世界の更新処理
			world.step(delta, 20, 20);
			
			// オブジェクトの更新処理
			objectManager.update(delta);
		}
		
		//---- カメラを更新する。
		camera.update();
		
		//---- 描画処理を実行する。
		final SpriteBatch spriteBatch = Global.spriteBatch;
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		renderManager.render();
		spriteBatch.end();
	}

	@Override
	public final void resize(int width, int height)
	{
		//---- アスペクト比を計算する
		final float windowAspectRatio = (float)width / height;
		final float viewportAspectRatio = camera.viewportWidth / camera.viewportHeight;
		
		//---- アスペクト比が等しくない場合、ビューポートを調整する
		if( Math.abs(windowAspectRatio - viewportAspectRatio) > 1.0e-6 )
		{
			int newWidth = width;
			int newHeight = height;
			
			// ウィンドウの横幅が広い
			if(windowAspectRatio > viewportAspectRatio)
			{
				newWidth = (int)(height * viewportAspectRatio);
			}
			// ウィンドウの縦幅が広い
			else
			{
				newHeight = (int)(width / viewportAspectRatio);
			}
			
			// ビューポートを設定する
			Gdx.gl.glViewport((width-newWidth)/2, (height-newHeight)/2, newWidth, newHeight);
		}
	}

	@Override
	public final void show()
	{
		//---- カレントスクリーンに設定する。
		setCurrentScreen();
	}

	@Override
	public final void hide()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public final void pause()
	{
		paused = true;
	}

	@Override
	public final void resume()
	{
		paused = false;
	}

	@Override
	public void dispose()
	{
		//---- 更新管理オブジェクトを空にする。
		objectManager.clear();
		
		//---- 描画管理オブジェクトを空にする。
		renderManager.clear();
		
		//---- 世界オブジェクトを破棄する。
		world.dispose();
	}
	
	/**
	 * 一時停止中か調べる。
	 * @return		一時停止中ならtrue
	 */
	public final boolean isPaused()
	{
		return paused;
	}
	
	/**
	 * カーソルを生成する。
	 * @return		カーソルオブジェクト
	 */
	protected abstract Cursor createCursor();
	
	/**
	 * 世界オブジェクトを取得する。
	 * @return		世界オブジェクト
	 */
	protected final World getWorld()
	{
		return world;
	}
	
	/**
	 * カメラオブジェクトを取得する。
	 * @return		カメラオブジェクト
	 */
	protected final Camera getCamera()
	{
		return camera;
	}
	
	/**
	 * カーソルを取得する。
	 * @return		カーソルオブジェクト
	 */
	protected final Cursor getCursor()
	{
		return cursor;
	}
	
	/**
	 * このスクリーンをカレントスクリーンに設定する。
	 */
	private final void setCurrentScreen()
	{
		Global.updateManager = objectManager;
		Global.renderManager = renderManager;
		Global.camera = camera;
	}
	
	
	/** 世界オブジェクト */
	private final World world = new World(new Vector2(0, GlobalDefine.INSTANCE.GRAVITY), true);
	
	/** カメラオブジェクト */
	private final Camera camera = new OrthographicCamera(GlobalDefine.INSTANCE.CLIENT_WIDTH * GlobalDefine.INSTANCE.WORLD_SCALE, GlobalDefine.INSTANCE.CLIENT_HEIGHT * GlobalDefine.INSTANCE.WORLD_SCALE);
	
	/** 更新管理オブジェクト */
	private final UpdateManager objectManager = new UpdateManager();
	
	/** 描画管理オブジェクト */
	private final RenderManager renderManager = new RenderManager();
	
	/** カーソルオブジェクト */
	private final Cursor cursor;
	
	/** 一時停止フラグ */
	private boolean paused = false;
}
