package org.genshin.scrollninja.object.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * 背景オブジェクトを生成するオブジェクトの基本クラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public abstract class AbstractBackgroundGenerator implements BackgroundGeneratorInterface
{
	@Override
	public void setWorldScale(float worldScale)
	{
		if(position != null)
			position.mul(worldScale);
	}
	

	/** 背景オブジェクトの座標 */
	public Vector2 position;
	
	/** 背景オブジェクトの色 */
	public Color color;
}
