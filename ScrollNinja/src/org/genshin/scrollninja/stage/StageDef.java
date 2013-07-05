package org.genshin.scrollninja.stage;

import org.genshin.scrollninja.collision.box2d.AbstractFixtureGenerator;
import org.genshin.scrollninja.object.background.BackgroundLayerDef;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * ステージの初期化用定義クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class StageDef
{
	/** 夜フラグ */
	public boolean isNight;
	
	/** ステージ開始時のプレイヤーの初期座標 */
	public Vector2 startPosition;
	
	/** 背景レイヤーの初期化用定義の配列 */
	public BackgroundLayerDef[] backgroundLayers;
	
	/** 敵の初期化用定義の配列 */
	public EnemyDef[] enemies;
	
	/** 地形の初期化用定義の配列 */
	public TerrainDef[] terrains;
}

/**
 * 敵の初期化用定義クラス
 */
class EnemyDef
{
	public Vector2 position;
}

/**
 * 地形の初期化用定義
 */
class TerrainDef
{
	/** 地形の生成対象となる背景レイヤーの名前 */
	public String layerName;
	
	/** Bodyの初期化用定義 */
	public BodyDef bodyDef;
	
	/** Fixture製造ましん */
	public AbstractFixtureGenerator fixture;
}
