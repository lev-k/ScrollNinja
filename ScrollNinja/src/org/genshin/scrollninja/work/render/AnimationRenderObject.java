package org.genshin.scrollninja.work.render;

import org.genshin.engine.system.PostureInterface;
import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.render.animation.AnimationInterface;
import org.genshin.scrollninja.work.render.animation.AnimationSet;
import org.genshin.scrollninja.work.render.animation.AnimationSetFactory;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimationRenderObject extends RenderObject implements Updatable
{
	/**
	 * コンストラクタ
	 * @param spriteFilePath		スプライトの定義ファイルのパス
	 * @param animationSetFilePath	アニメーションセットの定義ファイルのパス
	 * @param posture				位置情報
	 * @param depth					深度（値が大きいものを手前に描画する）
	 */
	public AnimationRenderObject(String spriteFilePath, String animationSetFilePath, PostureInterface posture, int depth)
	{
		super(spriteFilePath, posture, depth);
		
		//---- アニメーションデータを生成する。
		animationSet = AnimationSetFactory.getInstance().get(animationSetFilePath);
		
		//---- 更新管理オブジェクトに自身を追加する。
		registUpdatableManager();
	}
	
	/**
	 * コンストラクタ
	 * @param spriteName		スプライト名
	 * @param animationSetName	アニメーションセット名
	 * @param posture			位置情報
	 */
	public AnimationRenderObject(String spriteName, String animationSetName, PostureInterface posture)
	{
		this(spriteName, animationSetName, posture, GlobalDefine.RenderDepth.DEFAULT);
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src		コピー元となるオブジェクト
	 * @param depth		深度（値が大きいものを手前に描画する）
	 */
	public AnimationRenderObject(AnimationRenderObject src, int depth)
	{
		super(src, depth);
		
		//---- フィールドをコピーする。
		copy(src);

		//---- 更新管理オブジェクトに自身を追加する。
		registUpdatableManager();
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src		コピー元となるオブジェクト
	 */
	public AnimationRenderObject(AnimationRenderObject src)
	{
		super(src);
		
		//---- フィールドをコピーする。
		copy(src);

		//---- 更新管理オブジェクトに自身を追加する。
		registUpdatableManager();
	}

	@Override
	public void dispose()
	{
		//---- フィールドを破棄する。
		animationSet = null;
		currentAnimation = null;
		
		//---- 更新管理オブジェクトから自身を削除する。
		Global.updatableManager.remove(this, GlobalDefine.UpdatePriority.ANIMATION);
		
		//---- 基本クラスの破棄処理を実行する。
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		if(!paused)
			timer += deltaTime * speedRatio;
	}

	@Override
	public void render()
	{
		//---- アニメーションを反映する。
		if(currentAnimation != null)
		{
			@SuppressWarnings("deprecation")
			final Sprite sprite = getSprite();
			
			sprite.setRegion(currentAnimation.getKeyFrame(timer));
		}
		
		//---- 描画処理
		super.render();
	}
	
	/**
	 * アニメーションを一時停止する。
	 */
	public void pauseAnimation()
	{
		paused = true;
	}
	
	/**
	 * アニメーションの一時停止を解除する。
	 */
	public void resumeAnimation()
	{
		paused = false;
	}
	
	/**
	 * アニメーションを設定する。
	 * @param animationName		アニメーション名
	 */
	public void setAnimation(String animationName)
	{
		final AnimationInterface newAnimation = animationSet.getAnimation(animationName);
		if(currentAnimation != newAnimation)
		{
			currentAnimation = animationSet.getAnimation(animationName);
			timer = 0.0f;
		}
	}
	
	/**
	 * アニメーションの時間を設定する。
	 * @param time		アニメーションの時間
	 */
	public void setAnimationTime(float time)
	{
		timer = time;
	}
	
	/**
	 * 再生速度の倍率を設定する。
	 * @param speedRatio	再生速度の倍率（初期値：1.0）
	 */
	public void setSpeedRatio(float speedRatio)
	{
		this.speedRatio = speedRatio;
	}
	
	/**
	 * アニメーションの時間を取得する。
	 * @return	アニメーションの時間
	 */
	public float getAnimationTime()
	{
		return timer;
	}

	/**
	 * 再生速度の倍率を取得する。
	 * @return	再生速度の倍率
	 */
	public float getSpeedRatio()
	{
		return speedRatio;
	}
	
	/**
	 * アニメーションが一時停止中か調べる。
	 * @return	アニメーションが一時停止中ならtrue
	 */
	public boolean isAnimationPaused()
	{
		return paused;
	}
	
	/**
	 * アニメーションが終了しているか調べる。
	 * @return	アニメーションが終了している場合はtrue
	 */
	public boolean isAnimationFinished()
	{
		return currentAnimation.isAnimationFinished(timer);
	}
	
	/**
	 * ループするアニメーションか調べる。
	 * @return	ループするアニメーションならtrue
	 */
	public boolean isAnimationLooping()
	{
		return currentAnimation.isAnimationLooping();
	}
	
	/**
	 * フィールドをコピーする。
	 * @param src		コピー元となるオブジェクト
	 */
	private void copy(AnimationRenderObject src)
	{
		animationSet = src.animationSet;
		currentAnimation = src.currentAnimation;
		timer = src.timer;
		speedRatio = src.speedRatio;
		paused = src.paused;
	}
	
	/**
	 * 更新管理オブジェクトに自身を追加する。
	 */
	private void registUpdatableManager()
	{
		Global.updatableManager.add(this, GlobalDefine.UpdatePriority.ANIMATION);
	}
	
	
	/** アニメーションセット */
	private AnimationSet animationSet;
	
	/** 現在のアニメーション */
	private AnimationInterface currentAnimation;
	
	/** アニメーションタイマー */
	private float timer = 0.0f;
	
	/** 再生速度の倍率 */
	private float speedRatio = 1.0f;
	
	/** ポーズフラグ */
	private boolean paused = false;
}
