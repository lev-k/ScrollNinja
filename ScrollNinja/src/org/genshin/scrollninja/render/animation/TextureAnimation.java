package org.genshin.scrollninja.render.animation;

import org.genshin.scrollninja.render.texture.TextureFactory;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * テクスチャアニメーション
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class TextureAnimation implements AnimationInterface
{
	/**
	 * コンストラクタ
	 * @param def		テクスチャアニメーションの初期化用定義
	 */
	public TextureAnimation(TextureAnimationDef def)
	{
		//---- ループフラグ設定
		looping = def.looping;
		
		//---- アニメーション生成
		final int frameCount = def.frames.length;
		final TextureRegion[] textureRegions = new TextureRegion[frameCount];
		
		for(int i = 0;  i < frameCount;  ++i)
		{
			textureRegions[i] = new TextureRegion(
				TextureFactory.getInstance().get(def.textureFilePath),
				(def.startIndex.x + def.frames[i]) * def.uvSize.x, def.startIndex.y * def.uvSize.y,
				def.uvSize.x, def.uvSize.y
			);
		}
		
		animation = new Animation(def.oneFrameCount / 60.0f, textureRegions);
		animation.setPlayMode(looping ? Animation.LOOP : Animation.NORMAL);
	}
	
	@Override
	public TextureRegion getKeyFrame(float stateTime)
	{
		return animation.getKeyFrame(stateTime);
	}
	
	@Override
	public float getAnimationLength()
	{
		return animation.animationDuration;
	}

	@Override
	public boolean isAnimationFinished(float stateTime)
	{
		return animation.isAnimationFinished(stateTime);
	}
	
	@Override
	public boolean isAnimationLooping()
	{
		return looping;
	}
	
	
	/** アニメーションのループフラグ */
	final boolean looping;
	
	/** アニメーションオブジェクト */
	final Animation animation;
}
