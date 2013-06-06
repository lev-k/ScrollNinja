package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * 円の衝突判定を生成する
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class CircleShapeGenerator implements ShapeGeneratorInterface
{
	@Override
	public Shape generate()
	{
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		shape.setPosition(position);
		return shape;
	}
	
	@Override
	public void setScale(float scale)
	{
		radius *= scale;
		position.mul(scale);
	}


	/** 半径 */
	public float radius;
	
	/** 座標 */
	public Vector2 position;
}
