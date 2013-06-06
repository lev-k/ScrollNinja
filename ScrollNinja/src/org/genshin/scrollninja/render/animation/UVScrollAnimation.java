package org.genshin.scrollninja.render.animation;

import org.genshin.scrollninja.render.texture.TextureFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * UVスクロールアニメーション
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class UVScrollAnimation implements AnimationInterface
{
	/**
	 * コンストラクタ
	 * @param def		UVスクロールアニメーションの初期化用定義
	 */
	public UVScrollAnimation(UVScrollAnimationDef def)
	{
		final Texture texture = TextureFactory.getInstance().get(def.textureFilePath);
		final float invWidth = 1.0f / texture.getWidth();
		final float invHeight = 1.0f / texture.getHeight();
		
		//---- テクスチャ設定
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		textureRegion.setTexture(texture);
		
		//---- UV設定
		startPosition.set(def.startPosition.x * invWidth, def.startPosition.y * invHeight);
		uvSize.set(def.uvSize.x * invWidth, def.uvSize.y * invHeight);
		
		//---- アニメーションの終了時間
		endTime = def.totalTime;
		
		//---- アニメーションの速度設定
		scrollSpeed
			.set((def.endPosition.x - def.startPosition.x) * invWidth, (def.endPosition.y - def.startPosition.y) * invHeight)
			.mul(1.0f / endTime);
		
		//---- ループフラグ
		looping = def.looping;
	}
	
	@Override
	public TextureRegion getKeyFrame(float stateTime)
	{
		//---- 時間の補正
		// ループする場合
		if(looping)
		{
			stateTime = stateTime % endTime;
		}
		// ループしない場合
		else
		{
			stateTime = Math.min(stateTime, endTime);
		}
		
		//---- UV座標を設定する。
		uvPosition
			.set(scrollSpeed)
			.mul(stateTime)
			.add(startPosition);
		textureRegion.setRegion(uvPosition.x, uvPosition.y, uvPosition.x + uvSize.x, uvPosition.y + uvSize.y);
		
		return textureRegion;
	}
	
	@Override
	public float getAnimationLength()
	{
		return endTime;
	}

	@Override
	public boolean isAnimationFinished(float stateTime)
	{
		return !looping && stateTime > endTime;
	}
	
	@Override
	public boolean isAnimationLooping()
	{
		return looping;
	}
	
	
	/** テクスチャ */
	private final TextureRegion textureRegion = new TextureRegion();
	
	/** アニメーション開始時のUV座標 */
	private final Vector2 startPosition = new Vector2();
	
	/** UVマップの座標 */
	private final Vector2 uvPosition = new Vector2();
	
	/** UVマップの大きさ */
	private final Vector2 uvSize = new Vector2();
	
	/** アニメーションの終了時間 */
	private final float endTime;
	
	/** UVスクロールの速度（毎秒） */
	private final Vector2 scrollSpeed = new Vector2();
	
	/** ループフラグ */
	private final boolean looping;
}
