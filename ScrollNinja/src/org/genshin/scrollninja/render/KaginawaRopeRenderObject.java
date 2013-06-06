package org.genshin.scrollninja.render;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.GlobalDefine;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * 鉤縄の縄専用の描画オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class KaginawaRopeRenderObject extends RenderObject
{
	/**
	 * コンストラクタ
	 * @param posture		位置情報
	 */
	public KaginawaRopeRenderObject(PostureInterface posture)
	{
		super("data/jsons/render/kaginawa_rope_sprite.json", posture, GlobalDefine.RenderDepth.KAGINAWA);

		@SuppressWarnings("deprecation")
		final Sprite sprite = getSprite();
		sprite.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
	}

	/**
	 * コンストラクタ（duplicate専用）
	 */
	protected KaginawaRopeRenderObject()
	{
		/** 何もしない */
	}

	@Override
	public KaginawaRopeRenderObject duplicate()
	{
		return (KaginawaRopeRenderObject)super.duplicate();
	}
	
	/**
	 * 縄の長さを設定する。
	 * @param length		縄の長さ
	 */
	public void setLength(float length)
	{
		this.length = length;
	}
	
	/**
	 * 縄の角度を設定する。
	 * @param degrees		縄の角度（度）
	 */
	public void setAngle(float degrees)
	{
		angle = degrees;
	}

	@Override
	protected RenderObject duplicate(RenderObject dest)
	{
		//---- 引数がnullなら描画オブジェクトを生成する。
		if(dest == null)
		{
			dest = new KaginawaRopeRenderObject();
		}
		
		//---- 基本クラスのコピー
		super.duplicate(dest);
		
		//---- フィールドのコピー
		final KaginawaRopeRenderObject kaginawaRopeRenderObject = (KaginawaRopeRenderObject)dest;
		kaginawaRopeRenderObject.length = length;
		kaginawaRopeRenderObject.angle = angle;
		
		return dest;
	}

	@Override
	protected void localRender()
	{
		//---- 縄の長さと角度を設定する。
		@SuppressWarnings("deprecation")
		final Sprite sprite = getSprite();
		
		// スプライトの大きさ
		sprite.setSize(length, sprite.getHeight());
		
		// スプライトの角度
		sprite.setRotation(angle);
		
		// スプライトのUVマップ
		sprite.setRegion(0, 0, (int)(length * GlobalDefine.INSTANCE.INV_WORLD_SCALE), sprite.getRegionHeight());
		
		//---- 基本クラスの描画処理を実行する。
		super.localRender();
	}
	
	
	/** 縄の長さ */
	private float length;
	
	/** 縄の角度（度） */
	private float angle;
}
