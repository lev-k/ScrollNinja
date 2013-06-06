package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * 長方形の衝突判定を生成する
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public final class RectangleShapeGenerator implements ShapeGeneratorInterface
{
	@Override
	public Shape generate()
	{
		if(size == null)
		{
			return null;
		}
		if(offset == null)
		{
			offset = Vector2.Zero;
		}
		final PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.x * 0.5f, size.y * 0.5f, offset, degrees * MathUtils.degRad);
		return shape;
	}
	
	@Override
	public void setScale(float scale)
	{
		size.mul(scale);
		offset.mul(scale);
	}


	/** 大きさ */
	public Vector2 size;
	
	/** 中心座標のオフセット値（0.0f = 中心） */
	public Vector2 offset;
	
	/** 角度 */
	public float degrees;
}
