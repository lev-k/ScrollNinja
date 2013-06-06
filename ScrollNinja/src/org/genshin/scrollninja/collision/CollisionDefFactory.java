package org.genshin.scrollninja.collision;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.collision.box2d.AbstractFixtureGenerator;
import org.genshin.scrollninja.utils.JsonUtils;

/**
 * 衝突判定の初期化用定義オブジェクトの生成を管理するクラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class CollisionDefFactory extends AbstractFlyweightFactory<String, CollisionDef>
{
	/**
	 * コンストラクタ
	 */
	private CollisionDefFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static CollisionDefFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected CollisionDef create(String key)
	{
		final CollisionDef def = JsonUtils.read(key, CollisionDef.class);
		
		// test
		final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
		def.bodyDef.position.mul(worldScale);
		for(AbstractFixtureGenerator fixtureGenerator : def.fixtures)
		{
			fixtureGenerator.setScale(worldScale);
		}
		
		return def;
	}
	
	/** シングルトンインスタンス */
	private static CollisionDefFactory instance = new CollisionDefFactory();
}
