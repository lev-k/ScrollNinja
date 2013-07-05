package org.genshin.scrollninja.object.utils;

import org.genshin.scrollninja.object.AbstractUpdatable;
import org.genshin.scrollninja.object.character.AbstractCharacter;

import com.badlogic.gdx.math.Vector2;

/**
 * キャラクターの復活管理
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class RespawnManager extends AbstractUpdatable
{
	/**
	 * コンストラクタ
	 * @param ninja				復活を管理するキャラクタオブジェクト
	 * @param positionGetter	復活する座標を取得するインタフェース
	 */
	public RespawnManager(AbstractCharacter character, RespawnPositionGetterInterface positionGetter)
	{
		this.character = character;
		this.positionGetter = positionGetter;
	}
	
	@Override
	public void update(float deltaTime)
	{
		//---- 死んでたら復活しよう。
		if(character.isDead())
		{
			character.spawn(positionGetter.getPosition());
		}
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	
	/** 復活を管理するキャラクタオブジェクト */
	private final AbstractCharacter character;
	
	/** 復活する座標を取得するインタフェース */
	private final RespawnPositionGetterInterface positionGetter;
	
	
	/** 復活する座標を取得するインタフェース */
	public interface RespawnPositionGetterInterface
	{
		public Vector2 getPosition();
	}
}
