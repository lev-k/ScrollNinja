package org.genshin.scrollninja.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.genshin.engine.system.Disposable;
import org.genshin.scrollninja.collision.CollisionDef.BodyEditorFixtureDef;
import org.genshin.scrollninja.collision.CollisionDef.FixtureDefPair;
import org.genshin.scrollninja.utils.debug.Debug;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 衝突判定オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CollisionObject implements Disposable
{
	/**
	 * コンストラクタ
	 * @param collisionFilePath		衝突判定の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 * @param collisionCallback		衝突処理の呼び出しに使用するコールバックオブジェクト
	 */
	public CollisionObject(String collisionFilePath, World world, AbstractCollisionCallback collisionCallback)
	{
		collisionDef = CollisionDefFactory.getInstance().get(collisionFilePath);
		
		//---- Body生成
		body = world.createBody(collisionDef.bodyDef);
		body.setUserData(collisionCallback);
		
		//---- Fixture生成
		// 通常
		for(Entry<String, FixtureDef> entry : collisionDef.fixtureDefs.entrySet())
		{
			fixtures.put(entry.getKey(), body.createFixture(entry.getValue()));
		}
		
		// BodyEditor
		for(BodyEditorFixtureDef def : collisionDef.bodyEditorFixtureDefs)
		{
			final BodyEditorLoader loader = new BodyEditorLoader(def.jsonString);
			for(FixtureDefPair pair : def.fixtureDefPairs)
			{
				loader.attachFixture(body, pair.name, pair.fixtureDef, pair.scale);
			}
		}
	}
	
	@Override
	public void dispose()
	{
		//---- Fixtureオブジェクトを破棄する。
		if( !fixtures.isEmpty() )
		{
			for(Fixture fixture : fixtures.values())
			{
				fixture.getBody().destroyFixture(fixture);
			}
			fixtures.clear();
		}
		
		//---- Bodyオブジェクトを破棄する。
		if(body != null)
		{
			final ArrayList<Fixture> tmp = new ArrayList<Fixture>(body.getFixtureList());
			for(Fixture fixture : tmp)
			{
				body.destroyFixture(fixture);
			}
			body.getWorld().destroyBody(body);
			body = null;
		}
	}
	
	public void flipX(boolean flipX)
	{
		for(Entry<String, FixtureDef> entry : collisionDef.fixtureDefs.entrySet())
		{
			final String name = entry.getKey();
			final FixtureDef fd = entry.getValue();
			
			if( fd.shape instanceof PolygonShape )
			{
				final PolygonShape srcShape = (PolygonShape)fd.shape;
				final PolygonShape destShape = (PolygonShape)getFixture(name).getShape();

				final Vector2[] vertices = { Vector2.tmp, Vector2.tmp2, Vector2.tmp3 };
				for(int i = 0;  i < vertices.length;  ++i)
					srcShape.getVertex(i, vertices[i]);

				final float halfWidth	= (vertices[1].x - vertices[0].x) * 0.5f;;
				final float halfHeight	= (vertices[2].y - vertices[0].y) * 0.5f;;
				final float centerX	= (vertices[1].x + vertices[0].x) * 0.5f * (flipX ? -1.0f : 1.0f);
				final float centerY	= (vertices[2].y + vertices[0].y) * 0.5f;
				
				destShape.setAsBox(halfWidth, halfHeight, Vector2.tmp.set(centerX, centerY), 0.0f);
				
				final Vector2 tmp = Vector2.tmp;
				Debug.logToScreen("");
				for(int i = 0;  i < destShape.getVertexCount();  ++i)
				{
					destShape.getVertex(i, tmp);
					Debug.logToScreen("vertex " + i + ": " + tmp);
				}
				Debug.logToScreen("Half Width : " + halfWidth);
				Debug.logToScreen("Half Height: " + halfHeight);
				Debug.logToScreen("Center X   : " + centerX);
				Debug.logToScreen("Center Y   : " + centerY);
			}
		}
	}
	
	/**
	 * Bodyオブジェクトを取得する。
	 * @return		Bodyオブジェクト
	 */
	public Body getBody()
	{
		return body;
	}
	
	/**
	 * Fixtureオブジェクトを取得する。
	 * @param name		取得するFixtureオブジェクトの名前
	 * @return			Fixtureオブジェクト（指定された名前のFixtureオブジェクトを所持していない場合はnull）
	 */
	public Fixture getFixture(String name)
	{
		return fixtures.get(name);
	}
	
	
	/** 衝突判定の定義 */
	private final CollisionDef collisionDef;
	
	/** Bodyオブジェクト */
	private Body body;
	
	/** Fixtureオブジェクトのマップ */
	private final Map<String, Fixture> fixtures = new HashMap<String, Fixture>();
}
