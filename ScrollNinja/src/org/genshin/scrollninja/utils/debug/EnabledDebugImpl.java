package org.genshin.scrollninja.utils.debug;

import java.lang.reflect.Field;

import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.screen.GameScreen;
import org.genshin.scrollninja.screen.MapScreen;
import org.genshin.scrollninja.screen.TitleScreen;
import org.genshin.scrollninja.utils.input.InputHelperInterface;
import org.genshin.scrollninja.utils.input.KeyboardInputHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * デバッグモード時の実装オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
final class EnabledDebugImpl implements DebugImplInterface
{
	@Override
	public void updateDebugCommand()
	{
		//---- 入力オブジェクトを更新する。
		for(InputType inputType : InputType.values())
			inputType.input.update();
		
		//---- 描画フラグを切り替える。
		if(InputType.SwitchRender.input.isRelease())
			renderMask = (renderMask + 1) % RenderType.MAX.bit;
		
		//---- スクリーンを強制的に遷移する。
		// タイトル
		if(InputType.ToTitle.input.isRelease())
		{
			Global.game.getScreen().dispose();
			Global.game.setScreen(new TitleScreen());
		}
		// マップ
		else if(InputType.ToMap.input.isRelease())
		{
			Global.game.getScreen().dispose();
			Global.game.setScreen(new MapScreen());
		}
		// ゲーム
		else if(InputType.ToGame.input.isRelease())
		{
			Global.game.getScreen().dispose();
			Global.game.setScreen(new GameScreen());
		}
	}

	@Override
	public void renderCollision(World world, Camera camera)
	{
		//---- 衝突判定を描画する。
		if( isRenderEnabled(RenderType.COLLISION) )
		{
			box2dDebugRenderer.render(world, camera.combined);
		}
	}
	
	@Override
	public void renderLog()
	{
		//---- ログを出力する。
		if( isRenderEnabled(RenderType.LOG) )
		{
			final SpriteBatch spriteBatch = Global.spriteBatch;
			spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			spriteBatch.begin();
			font.drawMultiLine(spriteBatch, screenLogBuf, 0.0f, Gdx.graphics.getHeight());
			spriteBatch.end();
		}
		
		// ログのバッファをクリアする。
		screenLogBuf.setLength(0);
	}

	@Override
	public void logToScreen(String message)
	{
		screenLogBuf.append(message);
		screenLogBuf.append("\n");
	}
	
	@Override
	public void logToConsole(String message)
	{
		//---- ヘッダを出力する。
		createHeader(consoleLogBuf);
		
		//---- メッセージを出力する。
		consoleLogBuf.append(message);
		
		//---- コンソールに出力する。
		System.out.println(consoleLogBuf);
		
		//---- ログのバッファをクリアする。
		consoleLogBuf.setLength(0);
	}
	
	@Override
	public void logToConsole(Object object)
	{
		//---- ヘッダを出力する。
		createHeader(consoleLogBuf);
		
		//---- メッセージを出力する。
		if(object != null)
		{
			// クラス
			consoleLogBuf.append("\n  class = ");
			consoleLogBuf.append(object.getClass().getName());
			
			// フィールド
			for(Field field : object.getClass().getDeclaredFields())
			{
				consoleLogBuf.append("\n  ");
				consoleLogBuf.append(field.getName());
				consoleLogBuf.append(" = ");
				
				try
				{
					field.setAccessible(true);
					consoleLogBuf.append(field.get(object).toString());
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			consoleLogBuf.append("null");
		}
		
		//---- コンソールに出力する。
		System.out.println(consoleLogBuf);
		
		//---- ログのバッファをクリアする。
		consoleLogBuf.setLength(0);
	}

	/**
	 * 指定した種類の描画フラグを取得する。
	 * @param type		取得する描画フラグの種類
	 * @return			描画が有効な場合はtrue
	 */
	private boolean isRenderEnabled(RenderType type)
	{
		return (renderMask & type.bit) != 0;
	}
	
	/**
	 * ログのヘッダ部分を生成する。
	 * @param buf		格納先バッファ
	 */
	private void createHeader(StringBuffer buf)
	{
		//---- 現在のフレーム数を出力する。
		buf.append(Global.frameCount);
		buf.append(": ");
		
		//---- 呼び出し元のメソッド情報を出力する。
		final StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
		buf.append("[");
		buf.append(ste.getClassName());
		buf.append("#");
		buf.append(ste.getMethodName());
		buf.append("] ");
	}
	
	
	/** 描画フラグを表すマスクビット */
	private int renderMask = 0;
	
	/** Box2Dの衝突判定オブジェクトを描画するためのレンダラ  */
	private final Box2DDebugRenderer box2dDebugRenderer = new Box2DDebugRenderer();
	
	/** フォント */
	private final BitmapFont font = new BitmapFont();

	/** 画面出力用のログバッファ */
	private final StringBuffer screenLogBuf = new StringBuffer(1000);
	
	/** コンソール出力用のログバッファ */
	private final StringBuffer consoleLogBuf = new StringBuffer(100);
	
	
	/**
	 * 描画の種類
	 */
	private enum RenderType
	{
		LOG,
		COLLISION,
		MAX,
		;
		
		RenderType() { bit = (short)(1 << ordinal()); }
		
		public final short bit;
	}
	
	
	/**
	 * 入力の種類
	 */
	private enum InputType
	{
		SwitchRender	( new KeyboardInputHelper(Keys.NUM_0) ),
		ToTitle			( new KeyboardInputHelper(Keys.F1) ),
		ToMap			( new KeyboardInputHelper(Keys.F2) ),
		ToGame			( new KeyboardInputHelper(Keys.F3) ),
		;
		
		InputType(InputHelperInterface input) { this.input = input; }
		
		public final InputHelperInterface input;
	}
}
