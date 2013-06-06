package org.genshin.scrollninja.object.background;

import java.util.ArrayList;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.render.AnimationRenderObject;
import org.genshin.scrollninja.render.RenderObject;
import org.genshin.scrollninja.render.animation.AnimationSet;
import org.genshin.scrollninja.render.sprite.SpriteUtils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

/**
 * 背景レイヤー
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class BackgroundLayer extends AbstractBackground
{
	/**
	 * コンストラクタ
	 * @param scale			背景レイヤーの倍率
	 * @param renderDepth	描画処理の優先順位
	 */
	public BackgroundLayer(float scale, int renderDepth)
	{
//		this.stageSize.set(stageSize);
		this.scale = scale;
		this.renderDepth = renderDepth;
	}
	
	@Override
	public void dispose()
	{
		for(RenderObject ro : backgrounds)
		{
			ro.dispose();
		}
		backgrounds.clear();
		
		super.dispose();
	}
	
	@Override
	public void update(float deltaTime)
	{
		//---- 計算式の変形メモ
		// X = cameraX / ((stageW - viewportW) / 2) * ((stageW - stageW * scale) / 2)
		//   = cameraX /  (stageW - viewportW) * 2  *  (stageW - stageW * scale) / 2
		//   = cameraX /  (stageW - viewportW)      *  (stageW - stageW * scale)
		//   = cameraX /  (stageW - viewportW)      *  stageW * (1 - scale)
		
		//---- カメラに合わせて座標を調整する。
		final Camera camera = Global.camera;
		position.set(
			camera.position.x / (stageSize.x - camera.viewportWidth)  * stageSize.x * (1.0f - scale),
			camera.position.y / (stageSize.y - camera.viewportHeight) * stageSize.y * (1.0f - scale)
		);
	}

	/**
	 * 背景オブジェクトを生成する。
	 * @param def		背景オブジェクトの初期化用定義
	 */
	public void createBackground(BackgroundDef def)
	{
		//---- エラーチェック
		if(def.spriteDef == null)
			return;
		
//		if(def.position == null)
//			def.position = Vector2.Zero;
		
		//---- 位置情報
//		final PostureInterface posture = new BackgroundPosture(def.position.mul(scale), 0.0f);
		final PostureInterface posture = new BackgroundPosture(Vector2.Zero, 0.0f);
		RenderObject renderObject = null;
		
		//---- 描画オブジェクト生成
		// アニメーションあり
		if(def.animationSetDef != null)
		{
			final AnimationRenderObject ro = new AnimationRenderObject(SpriteUtils.createSprite(def.spriteDef), new AnimationSet(def.animationSetDef), posture, renderDepth);
			ro.setAnimation(def.animationName);
			renderObject = ro;
		}
		// アニメーションなし
		else
		{
			renderObject = new RenderObject(SpriteUtils.createSprite(def.spriteDef), posture, renderDepth);
		}
		
		// 色適用
		if(def.color != null)
		{
			renderObject.setColor(def.color);
		}
		
		// スケール適用
		renderObject.setScale(scale);
		
		// 管理オブジェクトに追加
		backgrounds.add(renderObject);
	}
	
	@Override
	public float getPositionX()
	{
		return position.x;
	}
	
	@Override
	public float getPositionY()
	{
		return position.y;
	}
	
	@Override
	public float getRotation()
	{
		return 0.0f;
	}
	
	/** 座標 */
	private final Vector2 position = new Vector2();
	
	/** ステージの大きさ */
	private final Vector2 stageSize = new Vector2();
	
	/** 背景レイヤーの倍率 */
	private final float scale;
	
	/** 描画処理の優先順位 */
	private final int renderDepth;
	
	/** 背景オブジェクトの配列 */
	private final ArrayList<RenderObject> backgrounds = new ArrayList<RenderObject>(1);
	
	
	/**
	 * 背景の位置情報
	 */
	private class BackgroundPosture implements PostureInterface
	{
		/**
		 * コンストラクタ
		 * @param localPosition		ローカル座標
		 * @param localRotation		ローカル角度（単位：度）
		 */
		public BackgroundPosture(Vector2 localPosition, float localRotation)
		{
			this.localPosition.set(localPosition);
			this.localRotation = localRotation;
		}
		
		@Override
		public float getPositionX()
		{
			return BackgroundLayer.this.getPositionX() + localPosition.x;
		}

		@Override
		public float getPositionY()
		{
			return BackgroundLayer.this.getPositionY() + localPosition.y;
		}

		@Override
		public float getRotation()
		{
			return BackgroundLayer.this.getRotation() + localRotation;
		}
		
		/** ローカル座標 */
		private final Vector2 localPosition = new Vector2();
		
		/** ローカル角度（単位：度） */
		private final float localRotation;
	}
}
