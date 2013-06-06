package org.genshin.scrollninja.render;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.GlobalDefine.RenderDepth;
import org.genshin.scrollninja.render.sprite.SpriteFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * 描画オブジェクト
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class RenderObject extends AbstractRenderObject
{
	/**
	 * コンストラクタ
	 * @param spriteFilePath	スプライトの定義ファイルのパス
	 * @param posture			位置情報
	 * @param depth				描画深度（値が大きいものを手前に描画する）
	 */
	public RenderObject(String spriteFilePath, PostureInterface posture, int depth)
	{
		super(depth);
		
		//---- フィールドを初期化する。
		sprite = SpriteFactory.getInstance().get(spriteFilePath);
		this.posture = posture;
	}
	
	/**
	 * コンストラクタ
	 * @param spriteFilePath	スプライトの定義ファイルのパス
	 * @param posture			位置情報
	 */
	public RenderObject(String spriteFilePath, PostureInterface posture)
	{
		this(spriteFilePath, posture, GlobalDefine.RenderDepth.DEFAULT);
	}
	
	/**
	 * コンストラクタ（duplicate専用）
	 */
	protected RenderObject()
	{
		super(RenderDepth.DEFAULT);
	}
	
	/**
	 * 描画オブジェクトを複製する。
	 * @return		複製した描画オブジェクト
	 */
	public RenderObject duplicate()
	{
		return duplicate(null);
	}

	@Override
	public void dispose()
	{
		//---- フィールドを破棄する。
		sprite = null;
		posture = null;
		
		//---- 基本クラスを破棄する。
		super.dispose();
	}
	
	/**
	 * 反転フラグを設定する。
	 * @param flipX			X方向の反転フラグ
	 * @param flipY			Y方向の反転フラグ
	 */
	public void flip(boolean flipX, boolean flipY)
	{
		this.flipX(flipX);
		this.flipY(flipY);
	}
	
	/**
	 * X方向の反転フラグを設定する。
	 * @param flip		X方向の反転フラグ
	 */
	public void flipX(boolean flip)
	{
		flipScale.x = flip ? -1.0f : 1.0f;
	}
	
	/**
	 * Y方向の反転フラグを設定する。
	 * @param flip		Y方向の反転フラグ
	 */
	public void flipY(boolean flip)
	{
		flipScale.y = flip ? -1.0f : 1.0f;
	}
	
	/**
	 * 位置情報を設定する。
	 * @param posture		位置情報
	 */
	public void setPosture(PostureInterface posture)
	{
		this.posture = posture;
	}
	
	/**
	 * 拡縮率を設定する。
	 * @param scaleXY	拡縮率
	 */
	public void setScale(float scaleXY)
	{
		setScale(scaleXY, scaleXY);
	}
	
	/**
	 * 拡縮率を設定する。
	 * @param scaleX	X方向の拡縮率
	 * @param scaleY	Y方向の拡縮率
	 */
	public void setScale(float scaleX, float scaleY)
	{
		scale.x = scaleX;
		scale.y = scaleY;
	}
	
	/**
	 * 色を設定する。（0.0 <= n <= 1.0）
	 * @param r		赤
	 * @param g		緑
	 * @param b		青
	 * @param a		透明度
	 */
	public void setColor(float r, float g, float b, float a)
	{
		color.set(r, g, b, a);
		color.clamp();
	}
	
	/**
	 * 色を設定する。（0 <= n <= 255）
	 * @param r		赤
	 * @param g		緑
	 * @param b		青
	 * @param a		透明度
	 */
	public void setColor(int r, int g, int b, int a)
	{
		final float inv = 1.0f / 255.0f;
		setColor(r*inv, g*inv, b*inv, a*inv);
	}
	
	/**
	 * 色を設定する。
	 * @param color		色
	 */
	public void setColor(Color color)
	{
		this.color.set(color);
	}
	
	@Override
	public float getPositionX()
	{
		return posture.getPositionX();
	}

	@Override
	public float getPositionY()
	{
		return posture.getPositionY();
	}

	@Override
	public float getRotation()
	{
		return posture.getRotation();
	}

	/**
	 * X軸方向の拡縮率を取得する。
	 * @return		X軸方向の拡縮率
	 */
	public float getScaleX()
	{
		return scale.x;
	}
	
	/**
	 * Y軸方向の拡縮率を取得する。
	 * @return		Y軸方向の拡縮率
	 */
	public float getScaleY()
	{
		return scale.y;
	}

	/**
	 * スプライトを取得する。
	 * @return			スプライト
	 * @deprecated		スプライトを直接いじるのは最終手段。基本的には使わない。
	 */
	@Deprecated
	public final Sprite getSprite()
	{
		return sprite;
	}
	
	/**
	 * X方向の反転フラグを取得する。
	 * @return		X方向の反転フラグ
	 */
	public boolean isFlipX()
	{
		return flipScale.x == -1.0f;
	}
	
	/**
	 * Y方向の反転フラグを取得する。
	 * @return		Y方向の反転フラグ
	 */
	public boolean isFlipY()
	{
		return flipScale.y == -1.0f;
	}
	
	/**
	 * 描画オブジェクトを複製する。
	 * @param dest		複製先となる描画オブジェクト（nullの場合は自己生成する）
	 * @return			複製した描画オブジェクト
	 */
	protected RenderObject duplicate(RenderObject dest)
	{
		//---- 引数がnullなら描画オブジェクトを生成する。
		if(dest == null)
		{
			dest = new RenderObject();
		}
		
		//---- 基本クラスの複製
		dest.setRenderEnabled(isRenderEnabled());
		dest.setDepth(getDepth());
		
		//---- フィールドをコピーする。
		dest.sprite = sprite;
		dest.posture = posture;
		dest.scale.set(scale);
		dest.flipScale.set(flipScale);
		dest.color.set(color);
		
		return dest;
	}

	@Override
	protected void localRender()
	{
		//---- 下準備
		final SpriteBatch sb = Global.spriteBatch;
		final float x = posture.getPositionX();
		final float y = posture.getPositionY();
		final float rotation = posture.getRotation();
		final float oldScaleX = sprite.getScaleX();
		final float oldScaleY = sprite.getScaleY();
		final Color spriteColor = sprite.getColor();
		final Color oldColor = spriteColor.tmp();
		spriteColor.mul(color);
		
		//---- 座標、回転、拡縮率、色を反映する。
		sprite.translate(x, y);
		sprite.rotate(rotation);
		sprite.setScale(oldScaleX * scale.x * flipScale.x, oldScaleY * scale.y * flipScale.y);
		sprite.setColor(spriteColor);
		
		//---- 描画する。
		sprite.draw(sb);
		
		//---- 座標、回転、拡縮率、色を元に戻す。
		sprite.translate(-x, -y);
		sprite.rotate(-rotation);
		sprite.setScale(oldScaleX, oldScaleY);
		sprite.setColor(oldColor);
	}
	
	
	/** スプライト */
	private Sprite	sprite;
	
	/** 位置情報 */
	private PostureInterface	posture;
	
	/** 拡縮率 */
	private final Vector2 scale = new Vector2(1.0f, 1.0f);
	
	/** 反転に使用する拡縮率 */
	private final Vector2 flipScale = new Vector2(1.0f, 1.0f);
	
	/** 色 */
	private final Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
}
