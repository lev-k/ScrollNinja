package org.genshin.scrollninja.render.animation;

import com.badlogic.gdx.math.Vector2;

/**
 * UVスクロールアニメーションの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class UVScrollAnimationDef extends AbstractAnimationDef
{
	@Override
	AnimationInterface generate()
	{
		return new UVScrollAnimation(this);
	}
	
	
	/** アニメーション開始時のUV座標 */
	public Vector2 startPosition;
	
	/** アニメーション終了時のUV座標 */
	public Vector2 endPosition;
	
	/** アニメーション開始から終了までの時間（単位：秒） */
	public float totalTime;
}
