package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.collision.CollisionDispatcher;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.Ninja;
import org.genshin.scrollninja.object.gui.Cursor;
import org.genshin.scrollninja.object.utils.CameraTranslater;
import org.genshin.scrollninja.object.utils.RespawnManager;
import org.genshin.scrollninja.stage.Stage;
import org.genshin.scrollninja.stage.StageInterface;

import com.badlogic.gdx.physics.box2d.World;

/**
 * ゲームスクリーン
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class GameScreen extends AbstractScreen
{
	/**
	 * コンストラクタ
	 */
	public GameScreen()
	{
		final World world = getWorld();
		
		//---- 衝突判定の監視
		collisionDispatcher = new CollisionDispatcher(world);
		
		//---- ステージを生成する。
		stage = new Stage(world, "data/stages/stage_test");
		
		//---- 忍者を生成する。
		final AbstractCharacter ninja = new Ninja(world, stage.getStartPosition(), getCursor());
		new RespawnManager(ninja, stage);
		
		//---- カメラの追従設定
		final CameraTranslater cameraTranslater = new CameraTranslater();
		cameraTranslater.addTargetObject(getCursor());
		cameraTranslater.addTargetObject(ninja);
		cameraTranslater.setTranslateArea(stage.getArea());
	}

	@Override
	public void dispose()
	{
		//---- ステージを破棄する。
		stage.dispose();
		
		//---- 衝突判定の振り分けを管理するオブジェクトを破棄する。
		collisionDispatcher.dispose();
		
		//---- 基本クラスを破棄する。
		super.dispose();
	}

	@Override
	protected Cursor createCursor()
	{
		return new Cursor(2.0f);
	}
	
	
	/** ステージオブジェクト */
	private final StageInterface stage;
	
	/** 衝突判定の振り分けを管理するオブジェクト */
	private final CollisionDispatcher collisionDispatcher;
}
