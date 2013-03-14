package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.genshin.scrollninja.screen.GameScreen;
import org.genshin.scrollninja.utils.debug.DebugString;
import org.genshin.scrollninja.utils.input.InputHelperInterface;
import org.genshin.scrollninja.utils.input.KeyboardInputHelper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * ScrollNinja エントリポイント
 * @author インターン生
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class ScrollNinja extends Game
{
	@Override
	public void create()
	{
		//---- グローバル変数に登録しておく。
		Global.game = this;
		
		//---- デバッグ文字列の初期化
		DebugString.initialize(true);
		
		//---- アイコンを設定する。
		final Pixmap[] pixmaps = {
			new Pixmap(Gdx.files.internal("data/textures/icon/128.png")),
			new Pixmap(Gdx.files.internal("data/textures/icon/32.png")),
			new Pixmap(Gdx.files.internal("data/textures/icon/16.png")),
		};
		Gdx.graphics.setIcon(pixmaps);
		
		//---- 画面のクリアカラーを設定する。
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		//---- 初期スクリーンを設定する。
		setScreen(new GameScreen());
	}

	@Override
	public void render()
	{
		//---- 画面をクリアする
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//---- 基本クラスの処理を実行する。
		super.render();
		
		//---- デバッグ文字列を描画する。
		DebugString.render();
		DebugString.add("Frame Count : " + Global.frameCount);
		DebugString.add("FPS : " + Gdx.graphics.getFramesPerSecond());
		
		//---- 状態別の処理を実行する。
		state.invoke(this, Gdx.graphics.getDeltaTime());
		
		//---- ゲーム内時間をカウントする。
		Global.frameCount++;
		Global.gameTime += Gdx.graphics.getDeltaTime();
		
		//---- [Esc] 入力でプログラムを終了する。
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
			Gdx.app.exit();
			return;
		}
	}
	
	/**
	 * 状態を変更する。
	 * @param newState	新しい状態
	 */
	private void changeState(State newState)
	{
		if(state != newState)
		{
			state = newState;
			state.initialize();
		}
	}
	
	/** ゲームの状態 */
	private State state = State.MAIN;
	
	
	/**
	 * 状態管理
	 */
	private enum State
	{
		/** メイン状態 */
		MAIN
		{
			@Override
			void initialize()
			{
				/** 何もしない */
			}
			
			@Override
			void invoke(ScrollNinja me, float deltaTime)
			{
				//---- [Alt] + [Enter] 入力でフルスクリーン切り替え（仮）
				inputHelper.update();
				if( Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT) )
				{
					if(inputHelper.isTrigger())
					{
						int newWidth = GlobalDefine.INSTANCE.CLIENT_WIDTH;
						int newHeight = GlobalDefine.INSTANCE.CLIENT_HEIGHT;
						final boolean newFullscreen = !Gdx.graphics.isFullscreen();
						
						if( newFullscreen )
						{
							final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
							newWidth = d.width;
							newHeight = d.height;
						}
						
						Gdx.graphics.setDisplayMode(newWidth, newHeight, newFullscreen);
						
						final Screen screen = me.getScreen();
						screen.resize(newWidth, newHeight);
						screen.pause();
						
						me.changeState(SWITCH_FULLSCREEN);
					}
				}
			}
			
			/** 仮。 */
			private final InputHelperInterface inputHelper = new KeyboardInputHelper(Keys.ENTER);
		},
		
		/** フルスクリーンの切り替え状態 */
		SWITCH_FULLSCREEN
		{
			@Override
			void initialize()
			{
				timer = 0.3f;
			}
			
			@Override
			void invoke(ScrollNinja me, float deltaTime)
			{
				if(Gdx.input.isCursorCatched())
				{
					if( (timer -= deltaTime) > 0.0f )
						return;
					Gdx.input.setCursorCatched(false);
					Gdx.input.setCursorCatched(true);
				}
				me.getScreen().resume();
				me.changeState(State.MAIN);
			}
			
			/** タイマー */
			float timer;
		}
		;
		
		/**
		 * 初期化する。
		 */
		abstract void initialize();
		
		/**
		 * 処理を実行する。
		 * @param me			自身を示すオブジェクト
		 * @param deltaTime		経過時間
		 */
		abstract void invoke(ScrollNinja me, float deltaTime);
	}
}