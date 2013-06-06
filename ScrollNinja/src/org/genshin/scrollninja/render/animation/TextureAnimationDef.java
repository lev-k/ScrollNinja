package org.genshin.scrollninja.render.animation;

import org.genshin.engine.utils.Point;

/**
 * テクスチャアニメーションの初期化用定義
 * @author kou
 * @since		1.0
 * @version		1.0
 */
class TextureAnimationDef extends AbstractAnimationDef
{
	@Override
	AnimationInterface generate()
	{
		return new TextureAnimation(this);
	}
	
	
	/** アニメーションの初期コマ */
	public Point startIndex;
	
	/** アニメーションのフレーム番号を再生順に格納した配列 */
	public int[] frames;
	
	/** アニメーションの再生速度（1コマあたりの経過フレーム数） */
	public int oneFrameCount;
}
