package org.genshin.scrollninja.object.weapon;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.object.AbstractObject;

/**
 * 武器の基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractWeapon extends AbstractObject
{
	/**
	 * コンストラクタ
	 * @param owner		所有者の位置情報
	 */
	public AbstractWeapon(PostureInterface owner)
	{
		this.owner = owner;
	}
	
	/**
	 * 攻撃する。
	 * @return		攻撃の結果
	 */
	public abstract AttackResult attack();
	
	@Override
	public void dispose()
	{
		/* 何もしない */
	}

	@Override
	public void update(float deltaTime)
	{
		// TODO チャクラゲージの回復など？
	}
	
	@Override
	public float getPositionX()
	{
		return owner.getPositionX();
	}

	@Override
	public float getPositionY()
	{
		return owner.getPositionY();
	}

	@Override
	public float getRotation()
	{
		return owner.getRotation();
	}
	
	
	/** 所有者の位置情報 */
	private final PostureInterface owner;
	
	
	/**
	 * 攻撃の結果
	 */
	public enum AttackResult
	{
		/** 攻撃が発動した */
		Success,
		
		/** 攻撃が発動しなかった */
		Failed
	}
}
