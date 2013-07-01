package org.genshin.scrollninja.collision;

import org.genshin.scrollninja.object.attack.AbstractAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.kaginawa.Kaginawa;
import org.genshin.scrollninja.object.terrain.Terrain;
import org.genshin.scrollninja.stage.Stage;

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
	 * ステージオブジェクトと衝突した。（世界の果て）
	 * @param obj		衝突したステージオブジェクト
	 * @param contact	衝突情報
	 */
	public void collision(Stage obj, Contact contact)
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
	
	/**
	 * キャラクターオブジェクトと衝突した。
	 * @param obj		衝突したキャラクターオブジェクト
	 * @param contact	衝突情報
	 */
	public void collision(AbstractCharacter obj, Contact contact)
	{
		/* 何もしない */
	}
	
	/**
	 * 鉤縄オブジェクトと衝突した。
	 * @param obj		衝突した鉤縄オブジェクト
	 * @param contact	衝突情報
	 */
	public void collision(Kaginawa obj, Contact contact)
	{
		/* 何もしない */
	}
	
	/**
	 * 攻撃オブジェクトと衝突した。
	 * @param obj		衝突した攻撃オブジェクト
	 * @param contact	衝突情報
	 */
	public void collision(AbstractAttack obj, Contact contact)
	{
		/* 何もしない */
	}
	
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
