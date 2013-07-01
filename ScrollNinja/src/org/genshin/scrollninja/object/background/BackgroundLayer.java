package org.genshin.scrollninja.object.background;

import java.util.ArrayList;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.render.AnimationRenderObject;
import org.genshin.scrollninja.render.RenderObject;
import org.genshin.scrollninja.render.animation.AnimationSet;
import org.genshin.scrollninja.render.sprite.SpriteUtils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
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
	 * @param def			背景レイヤーの初期化用定義
	 * @param renderDepth	描画処理の優先順位
	 */
	public BackgroundLayer(BackgroundLayerDef def, int renderDepth)
	{
		this.scale = def.scale;
		this.renderDepth = renderDepth;
		
		if(def.backgrounds != null)
		{
			for(BackgroundGeneratorInterface generator : def.backgrounds)
			{
				generator.generate(this);
			}
		}
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
		//---- 計算式メモ
		// 可動領域を[0, 1]の範囲とし、カメラ座標をその範囲に変換する
		//   range = (cameraX - stageX - viewportWidth / 2) / (stageWidth - viewportWidth)
		// 
		// カメラ座標をscaleに合わせて変換する
		//   x = range * (stageWidth - stageWidth * scale) + (stageX - stageX * scale)
		//   x = range * stageWidth * (1 - scale)          + stageX * (1 - scale)
		// 
		// 即ち…
		//   x = (cameraX - stageX - viewportWidth / 2) / (stageWidth - viewportWidth) * stageWidth * (1 - scale) + stageX * (1 - scale)
		// 
		
		final Camera camera = Global.camera;
		position.set(
			(camera.position.x - scrollArea.x - camera.viewportWidth  * 0.5f) / (scrollArea.width  - camera.viewportWidth ) * scrollArea.width  * (1.0f - scale) + scrollArea.x * (1.0f - scale),
			(camera.position.y - scrollArea.y - camera.viewportHeight * 0.5f) / (scrollArea.height - camera.viewportHeight) * scrollArea.height * (1.0f - scale) + scrollArea.y * (1.0f - scale)
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
		
		if(def.position == null)
			def.position = Vector2.Zero;
		
		//---- 位置情報
		final PostureInterface posture = new BackgroundPosture(def.position.tmp().mul(scale), 0.0f);
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
	
	public void setScrollArea(Rectangle area)
	{
		setScrollArea(area.x, area.y, area.width, area.height);
	}
	
	public void setScrollArea(float x, float y, float width, float height)
	{
		scrollArea.set(x, y, width, height);
	}
	
	public Rectangle getScrollArea()
	{
		return scrollArea;
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
	
	/** スクロールする範囲 */
	private final Rectangle scrollArea = new Rectangle();
	
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
