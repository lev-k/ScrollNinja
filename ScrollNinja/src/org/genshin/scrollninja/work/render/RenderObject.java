package org.genshin.scrollninja.work.render;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.render.sprite.SpriteFactory;

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
public class RenderObject implements RenderObjectInterface
{
	/**
	 * コンストラクタ
	 * @param spriteFilePath	スプライトの定義ファイルのパス
	 * @param posture			位置情報
	 * @param depth				深度（値が大きいものを手前に描画する）
	 */
	public RenderObject(String spriteFilePath, PostureInterface posture, int depth)
	{
		//---- フィールドを初期化する。
		sprite = SpriteFactory.getInstance().get(spriteFilePath);
		this.posture = posture;
		this.depth = depth;
		
		//---- 描画管理オブジェクトに自身を追加する。
		Global.renderManager.add(this, this.depth);
	}
	
	/**
	 * コンストラクタ
	 * @param spriteName		スプライト名
	 * @param posture			位置情報
	 */
	public RenderObject(String spriteName, PostureInterface posture)
	{
		this(spriteName, posture, GlobalDefine.RenderDepth.DEFAULT);
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src		コピー元となるオブジェクト
	 */
	public RenderObject(RenderObject src)
	{
		if(getClass() != src.getClass())
		{
			System.out.println("なんかちがうかも？");
		}
		
		//---- フィールドをコピーする。
		sprite = src.sprite;
		posture = src.posture;
		depth = src.depth;
		
		//---- 描画管理オブジェクトに自身を追加する。
		Global.renderManager.add(this, depth);
	}

	@Override
	public void dispose()
	{
		//---- フィールドを破棄する。
		sprite = null;
		posture = null;
		
		//---- 描画管理オブジェクトから自身を削除する。
		Global.renderManager.remove(this, depth);
	}

	@Override
	public void render()
	{
		//---- 描画フラグがfalseなら描画しない。
		if(!renderEnabled)
			return;
		
		//---- 下準備
		final SpriteBatch sb = Global.spriteBatch;
		final float x = posture.getPositionX();
		final float y = posture.getPositionY();
		final float rotation = posture.getRotation();
		final float oldScaleX = sprite.getScaleX();
		final float oldScaleY = sprite.getScaleY();
		final Color spriteColor = sprite.getColor();
		tmpColor.set(spriteColor);
		spriteColor.mul(color);
		
		//---- 座標、回転、拡縮率、色を反映する。
		sprite.translate(x, y);
		sprite.rotate(rotation);
		sprite.setScale(oldScaleX * scale.x, oldScaleY * scale.y);
		sprite.setColor(spriteColor);
		
		//---- 描画する。
		sprite.draw(sb);
		
		//---- 座標、回転、拡縮率、色を元に戻す。
		sprite.translate(-x, -y);
		sprite.rotate(-rotation);
		sprite.setScale(oldScaleX, oldScaleY);
		sprite.setColor(tmpColor);
	}
	
	/**
	 * 描画フラグを設定する。
	 * @param enabled	描画フラグ
	 */
	public void setRenderEnabled(boolean enabled)
	{
		renderEnabled = enabled;
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

	@Override
	public boolean isDisposed()
	{
		return sprite == null;
	}

	/** スプライト */
	private Sprite	sprite;
	
	/** 位置情報 */
	private PostureInterface	posture;
	
	/** 深度（値が大きいものを手前に描画する） */
	private int	depth;
	
	/** 描画フラグ */
	private boolean renderEnabled = true;
	
	/** 拡縮率 */
	private final Vector2 scale = new Vector2(1.0f, 1.0f);
	
	/** 色 */
	private final Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	
	/** 作業用の色オブジェクト */
	private static final Color tmpColor = new Color();
}
