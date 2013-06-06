package org.genshin.scrollninja.collision;

import org.genshin.scrollninja.collision.box2d.AbstractFixtureGenerator;

import com.badlogic.gdx.physics.box2d.BodyDef;


/**
 * 衝突判定の初期化用定義クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CollisionDef
{
	/** Bodyの初期化用定義 */
	public BodyDef bodyDef;
	
	/** Fixture生成装置の配列 */
	public AbstractFixtureGenerator[] fixtures;
}
