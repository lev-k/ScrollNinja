package org.genshin.scrollninja.render;

import org.genshin.engine.system.PostureInterface;
import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.render.animation.AnimationInterface;
import org.genshin.scrollninja.render.animation.AnimationSet;
import org.genshin.scrollninja.render.animation.AnimationSetFactory;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimationRenderObject extends RenderObject implements Updatable
{
	/**
	 * コンストラクタ
	 * @param spriteFilePath		スプライトの定義ファイルのパス
	 * @param animationSetFilePath	アニメーションセットの定義ファイルのパス
	 * @param posture				位置情報
	 * @param depth					描画深度（値が大きいものを手前に描画する）
	 */
	public AnimationRenderObject(String spriteFilePath, String animationSetFilePath, PostureInterface posture, int depth)
	{
		super(spriteFilePath, posture, depth);
		
		//---- アニメーションデータを生成する。
		animationSet = AnimationSetFactory.getInstance().get(animationSetFilePath);
		
		//---- 更新管理オブジェクトに自身を追加する。
		Global.updatableManager.add(this, GlobalDefine.UpdatePriority.ANIMATION);
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
	 * コンストラクタ（duplicate専用）
	 */
	protected AnimationRenderObject()
	{
		Global.updatableManager.add(this, GlobalDefine.UpdatePriority.ANIMATION);
	}

	@Override
	public AnimationRenderObject duplicate()
	{
		return (AnimationRenderObject)super.duplicate();
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
		//---- 変更禁止フラグが立っている場合、アニメーションが終了していなければ何もしない
		if(lock && !currentAnimation.isAnimationFinished(timer))
			return;
		lock = false;
		
		//---- 現在のアニメーションと指定されたアニメーションが別のアニメーションであれば、アニメーションを変更する。
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
	 * 現在のアニメーションの再生が終了するまでアニメーションの変更を禁止するフラグを設定する。
	 * @param lock		変更を禁止する場合はtrue
	 */
	public void setAnimationLock(boolean lock)
	{
		this.lock = lock;
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
	 * アニメーションの長さを取得する。
	 * @return		アニメーションの長さ（秒）
	 */
	public float getAnimationLength()
	{
		return currentAnimation.getAnimationLength();
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

	@Override
	protected RenderObject duplicate(RenderObject dest)
	{
		//---- 引数がnullなら描画オブジェクトを生成する。
		if(dest == null)
		{
			dest = new AnimationRenderObject();
		}
		
		//---- 基本クラスのコピー
		super.duplicate(dest);
		
		//---- フィールドのコピー
		final AnimationRenderObject animationRenderObject = (AnimationRenderObject)dest;
		animationRenderObject.animationSet = animationSet;
		animationRenderObject.currentAnimation = currentAnimation;
		animationRenderObject.timer = timer;
		animationRenderObject.speedRatio = speedRatio;
		animationRenderObject.paused = paused;
		
		return dest;
	}

	@Override
	protected void localRender()
	{
		//---- アニメーションを反映する。
		if(currentAnimation != null)
		{
			@SuppressWarnings("deprecation")
			final Sprite sprite = getSprite();
			
			sprite.setRegion(currentAnimation.getKeyFrame(timer));
		}
		
		//---- 描画処理
		super.localRender();
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
	
	/** 変更禁止フラグ */
	private boolean lock = false;
}
