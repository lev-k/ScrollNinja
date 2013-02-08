package org.genshin.scrollninja.work.collision;

import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.work.object.terrain.Terrain;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * 衝突判定コールバックの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractCollisionCallback
{
	/**
	 * 衝突判定を振り分ける。
	 * @param collisionCallback		衝突相手の衝突判定コールバックオブジェクト
	 * @param contact				衝突情報
	 */
	public abstract void dispatch(AbstractCollisionCallback collisionCallback, Contact contact);
	
	/**
	 * プレイヤー忍者オブジェクトと衝突した。
	 * @param obj		衝突したプレイヤー忍者オブジェクト
	 * @param contact	衝突情報
	 */
	public void collision(PlayerNinja obj, Contact contact)
	{
		/* 何もしない */
	}
	
	/**
	 * 地形オブジェクトと衝突した。
	 * @param obj		衝突した地形オブジェクト
	 * @param contact	衝突情報
	 */
	public void collision(Terrain obj, Contact contact)
	{
		/* 何もしない */
	}
	
//	/**
//	 * 敵オブジェクトと衝突した。
//	 * @param obj		衝突した敵オブジェクト
//	 * @param contact	衝突情報
//	 */
//	public void collision(AbstractEnemy obj, Contact contact)
//	{
//		/* 何もしない */
//	}
	
//	/**
//	 * 攻撃オブジェクトと衝突した。
//	 * @param obj		衝突した攻撃オブジェクト
//	 * @param contact	衝突情報
//	 */
//	public void collision(AbstractAttack obj, Contact contact)
//	{
//		/* 何もしない */
//	}
	
//	/**
//	 * アイテムオブジェクトと衝突した。
//	 * @param obj		衝突したアイテムオブジェクト
//	 * @param contact	衝突情報
//	 */
//	public void collision(AbstractItem obj, Contact contact)
//	{
//		/* 何もしない */
//	}
}
