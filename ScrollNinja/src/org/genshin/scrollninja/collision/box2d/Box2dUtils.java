package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Box2D関連の便利関数群
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class Box2dUtils
{
	/**
	 * Fixtureを生成する
	 * @param owner		Fixtureの所属先Bodyオブジェクト
	 * @param def		Fixtureの定義
	 * @return			生成したFixture
	 */
	public static Fixture createFixture(Body owner, FixtureGenerator def)
	{
		//---- 形状を生成する
		if(def.shape != null)
		{
			def.fixtureDef.shape = def.shape.generate();
			def.shape = null;
		}
		
		//---- フィルタを生成する
		if(def.filter != null)
		{
			def.filter.generate(def.fixtureDef.filter);
			def.filter = null;
		}
		
		//---- Fixtureを生成する
		return owner.createFixture(def.fixtureDef);
	}
	
	/**
	 * 衝突対象を追加する
	 * @param outFixture		追加先Fixture
	 * @param category			追加する対象のカテゴリ名
	 */
	public static void addCollisionCategory(Fixture outFixture, String category)
	{
		final Filter filter = outFixture.getFilterData();
		filter.maskBits |= CategoryBitsFactory.getInstance().get(category);
		outFixture.setFilterData(filter);
	}
	
	/**
	 * 衝突対象を削除する
	 * @param outFixture		削除先Fixture
	 * @param category			削除する対象のカテゴリ名
	 */
	public static void removeCollisionCategory(Fixture outFixture, String category)
	{
		final Filter filter = outFixture.getFilterData();
		filter.maskBits &= ~CategoryBitsFactory.getInstance().get(category);
		outFixture.setFilterData(filter);
	}
}
