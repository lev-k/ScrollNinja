package org.genshin.scrollninja.object.utils;

import java.util.ArrayList;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.AbstractObject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * カメラを動かすオブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CameraTranslater extends AbstractObject
{
	/**
	 * コンストラクタ
	 */
	public CameraTranslater()
	{
		super();
	}

	@Override
	public void dispose()
	{
		targetObjects.clear();
		
		super.dispose();
	}

	@Override
	public float getPositionX()
	{
		return targetPosition.x;
	}

	@Override
	public float getPositionY()
	{
		return targetPosition.y;
	}

	@Override
	public float getRotation()
	{
		return 0;
	}

	@Override
	protected int getUpdatePriority()
	{
		return GlobalDefine.UpdatePriority.INPUT;
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 目標座標を計算する。
		targetPosition.set(Vector2.Zero);
		for(PostureInterface object : targetObjects)
		{
			targetPosition.add(object.getPositionX(), object.getPositionY());
		}
		targetPosition.mul(1.0f/targetObjects.size());
		
		//---- 座標を適用する。
		final Camera camera = Global.camera;
		
		camera.translate(
			(targetPosition.x - camera.position.x) * interporation,
			(targetPosition.y - camera.position.y) * interporation,
			0.0f
		);
		
		//---- 移動範囲を制限する。
		final float left		= translateArea.x;
		final float right		= translateArea.x + translateArea.width;
		final float top		= translateArea.y;
		final float bottom		= translateArea.y + translateArea.height;
		
		// X
		if( camera.position.x > right )
		{
			camera.position.x = right;
		}
		else if( camera.position.x < left )
		{
			camera.position.x = left;
		}
		// Y
		if( camera.position.y > bottom )
		{
			camera.position.y = bottom;
		}
		else if( camera.position.y < top )
		{
			camera.position.y = top;
		}
	}
	
	/**
	 * 追従するオブジェクトを追加する。
	 * @param object	追従するオブジェクト
	 */
	public void addTargetObject(PostureInterface object)
	{
		targetObjects.add(object);
	}
	
	/**
	 * 追従するオブジェクトを削除する。
	 * @param object	追従するオブジェクト
	 */
	public void removeTargetObject(PostureInterface object)
	{
		targetObjects.remove(object);
	}
	
	/**
	 * 移動可能な範囲を設定する。
	 * @param rect		移動可能な範囲
	 */
	public void setTranslateArea(Rectangle rect)
	{
		setTranslateArea(rect.x, rect.y, rect.width, rect.height);
	}
	
	/**
	 * 移動可能な範囲を設定する。
	 * @param x			X座標
	 * @param y			Y座標
	 * @param width		X方向の大きさ
	 * @param height	Y方向の大きさ
	 */
	public void setTranslateArea(float x, float y, float width, float height)
	{
		final Camera camera = Global.camera;
		translateArea.set(x + camera.viewportWidth * 0.5f, y + camera.viewportHeight * 0.5f, width - camera.viewportWidth, height - camera.viewportHeight);
	}
	
	
	/** 移動可能な範囲 */
	private final Rectangle translateArea = new Rectangle(0, 0, 0, 0);
	
	/** 追従するオブジェクトの配列 */
	private final ArrayList<PostureInterface> targetObjects = new ArrayList<PostureInterface>(1);
	
	/** 補間係数（デフォルト値：0.1） */
	private float interporation = 0.1f;
	
	/** 目標にする座標 */
	private final Vector2 targetPosition = new Vector2();
}
