package org.genshin.scrollninja.object.terrain;

import org.genshin.engine.system.Disposable;
import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionDef;
import org.genshin.scrollninja.collision.CollisionObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 地形オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Terrain implements Disposable
{
	/**
	 * コンストラクタ
	 * @param collisionFilePath		衝突判定の初期化用定義ファイルへのパス
	 * @param world					所属する世界オブジェクト
	 */
	public Terrain(String collisionFilePath, World world)
	{
		collisionObject = new CollisionObject(collisionFilePath, world, new TerrainCollisionCallback());
	}
	
	/**
	 * コンストラクタ
	 * @param collisionFilePath		衝突判定の初期化用ファイルへのパス
	 * @param world					所属する世界オブジェクト
	 * @param position				座標
	 */
	public Terrain(String collisionFilePath, World world, Vector2 position)
	{
		this(collisionFilePath, world);
		
		final Body body = collisionObject.getBody();
		final Vector2 oldPosition = body.getPosition();
		body.setTransform(oldPosition.add(position), body.getAngle());
	}
	
	public Terrain(CollisionDef collisionDef, World world, Vector2 position)
	{
		collisionObject = new CollisionObject(collisionDef, world, new TerrainCollisionCallback());

		final Body body = collisionObject.getBody();
		final Vector2 oldPosition = body.getPosition();
		body.setTransform(oldPosition.add(position), body.getAngle());
	}
	
	@Override
	public void dispose()
	{
		collisionObject.dispose();
	}


	/** 衝突オブジェクト */
	private final CollisionObject collisionObject;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	protected class TerrainCollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(Terrain.this, contact);
		}
	}
}
