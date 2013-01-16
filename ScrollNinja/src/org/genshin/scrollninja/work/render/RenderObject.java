package org.genshin.scrollninja.work.render;

import org.genshin.engine.system.Disposable;
import org.genshin.engine.system.PostureInterface;
import org.genshin.engine.system.Renderable;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.work.render.sprite.SpriteFactory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


/**
 * 描画オブジェクト
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class RenderObject implements Renderable, Disposable
{
	/**
	 * コンストラクタ
	 * @param spriteName		スプライト名
	 * @param posture			位置情報
	 */
	public RenderObject(String spriteName, PostureInterface posture)
	{
		sprite = SpriteFactory.getInstance().get(spriteName); 
		this.posture = posture;
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
		
		sprite = src.sprite;
		posture = src.posture;
	}

	@Override
	public void dispose()
	{
		sprite = null;
		posture = null;
	}

	@Override
	public void render()
	{
		final SpriteBatch sb = Global.currentSpriteBatch;
		
		//---- 位置情報を反映する。
		sb.setTransformMatrix(
			transform
				.setToRotation(Vector3.Z, posture.getRotation())
				.translate(posture.getPositionX(), posture.getPositionY(), 0.0f)
		);
		
		//---- 描画する。
		sprite.draw(sb);
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
	
	/** 作業用マトリクス */
	private static final Matrix4	transform	= new Matrix4();
}
