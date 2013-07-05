package org.genshin.scrollninja.stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionDef;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.collision.box2d.AbstractFixtureGenerator;
import org.genshin.scrollninja.collision.box2d.BodyEditorFixtureGenerator;
import org.genshin.scrollninja.collision.box2d.EdgeShapeGenerator;
import org.genshin.scrollninja.collision.box2d.FixtureGenerator;
import org.genshin.scrollninja.object.background.BackgroundGeneratorInterface;
import org.genshin.scrollninja.object.background.BackgroundLayer;
import org.genshin.scrollninja.object.background.BackgroundLayerDef;
import org.genshin.scrollninja.object.background.DirectoryBackgroundGenerator;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.enemy.TestEnemy;
import org.genshin.scrollninja.object.terrain.Terrain;
import org.genshin.scrollninja.object.utils.RespawnManager;
import org.genshin.scrollninja.object.utils.RespawnManager.RespawnPositionGetterInterface;
import org.genshin.scrollninja.utils.JsonUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;


/**
 * ステージ
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Stage implements StageInterface
{
	/**
	 * コンストラクタ
	 * @param world			所属先となるWorldオブジェクト
	 * @param stageDir		ステージディレクトリのパス
	 */
	public Stage(World world, String stageDir)
	{
		final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
		
		//---- ステージの初期化用定義をファイルから読み込む
		final StageDef stageDef = JsonUtils.read(stageDir + "/define.json", StageDef.class);
		
		// エラーチェック
		if(stageDef == null)
		{
			area = null;
			startPosition = null;
			return;
		}
				
		//---- フィールドに値を設定する。
		isNight = stageDef.isNight;
		area = new Rectangle(0.0f, 0.0f, 0.0f, 0.0f);
		startPosition = stageDef.startPosition.mul(worldScale);
		
		//---- 描画オブジェクトを生成する。
		createBackgroundLayers(stageDef.backgroundLayers);
			
		//---- 地形オブジェクトを生成する。
		for(TerrainDef terrainDef : stageDef.terrains)
			createTerrain(world, stageDir, terrainDef);
		
		//---- レイヤーにステージサイズを伝える
		for(BackgroundLayer layer : backgroundLayers.values())
		{
			layer.setScrollArea(area);
		}
		
		//---- 世界の果てをつくろう
		createEndOfWorld(world);
		
		//---- 敵オブジェクトを生成する。
		for(EnemyDef def : stageDef.enemies)
		{
			final AbstractCharacter character = new TestEnemy(world, def.position.mul(worldScale), isNight);
			
			// FIXME せっかくなのでrespawnさせてみる。最終的には消す。
			class RespawnPositionGetter implements RespawnPositionGetterInterface
			{
				public RespawnPositionGetter(Vector2 position) { this.position = position; }
				@Override public Vector2 getPosition() { return position; }
				private final Vector2 position;
			}
			new RespawnManager(character, new RespawnPositionGetter(def.position));
		}
	}

	@Override
	public void dispose()
	{
		//---- 地形オブジェクトを破棄する。
		for(Terrain terrain : terrains)
		{
			terrain.dispose();
		}
		terrains.clear();
		
		//---- 背景レイヤーを破棄する。
		for(BackgroundLayer backgroundLayer : backgroundLayers.values())
		{
			backgroundLayer.dispose();
		}
		backgroundLayers.clear();
	}
	
	@Override
	public boolean isNight()
	{
		return isNight;
	}

	@Override
	public Rectangle getArea()
	{
		return area;
	}

	@Override
	public Vector2 getStartPosition()
	{
		return startPosition;
	}
	
	/**
	 * 背景レイヤーオブジェクトを生成する。
	 * @param backgroundLayerDefs		背景レイヤーオブジェクトの初期化用定義の配列
	 */
	private void createBackgroundLayers(BackgroundLayerDef[] backgroundLayerDefs)
	{
		//---- エラーチェック
		if(backgroundLayerDefs == null)
			return;
		
		//---- 背景レイヤー、背景オブジェクトを生成する
		int renderDepth = GlobalDefine.RenderDepth.FAR_BACKGROUND;
		for(int i = 0;  i < backgroundLayerDefs.length;  ++i)
		{
			final BackgroundLayerDef backgroundLayerDef = backgroundLayerDefs[i];
			
			// レイヤー名の自動命名
			if(backgroundLayerDef.name == null || backgroundLayerDef.name.isEmpty())
			{
				backgroundLayerDef.name = "layer" + i;
			}
			// プレイヤーのレイヤー以降は手前に描画する
			else if(backgroundLayerDef.name.equals("player"))
			{
				renderDepth = GlobalDefine.RenderDepth.NEAR_BACKGROUND;
				backgroundLayerDef.scale = 1.0f;
			}
			
			// 世界補正
			if(backgroundLayerDef.backgrounds != null)
			{
				for(BackgroundGeneratorInterface generator : backgroundLayerDef.backgrounds)
				{
					generator.setWorldScale(GlobalDefine.INSTANCE.WORLD_SCALE);
				}
			}
			
			// 背景レイヤー生成
			final BackgroundLayer backgroundLayer = new BackgroundLayer(backgroundLayerDef, renderDepth);
			backgroundLayers.put(backgroundLayerDef.name, backgroundLayer);
		}
	}
	
	/**
	 * 地形を生成する
	 * @param world			所属する世界オブジェクト
	 * @param stageDir		ステージディレクトリ
	 * @param terrainDef	地形の初期化用定義
	 */
	private void createTerrain(World world, String stageDir, TerrainDef terrainDef)
	{
		final BackgroundLayer terrainLayer = backgroundLayers.get(terrainDef.layerName);
		final String terrainDir = stageDir + "/" + terrainDef.layerName + "/";
		
		//---- 地形情報ファイルを集める
		// 対象ディレクトリを開く
		final FileHandle dir = Gdx.files.local(terrainDir);
		if(!dir.isDirectory())
			return;
		
		// jsonファイルのリストを作成する
		final FileHandle[] files = dir.list(".json");
		
		//---- 地形を生成する
		final CollisionDef collisionDef = new CollisionDef();
		final BodyEditorFixtureGenerator terrainFixtureGenerator = (BodyEditorFixtureGenerator)terrainDef.fixture;
		collisionDef.bodyDef = terrainDef.bodyDef;
		collisionDef.fixtures = new AbstractFixtureGenerator[] { terrainFixtureGenerator };
		terrains.ensureCapacity(files.length);
		terrainFixtureGenerator.scale *= GlobalDefine.INSTANCE.WORLD_SCALE;
		final Rectangle currentArea = new Rectangle(0.0f, 0.0f, terrainFixtureGenerator.scale, terrainFixtureGenerator.scale);
		for(FileHandle file : files)
		{
			//---- 地形のオフセット値を算出する
			final String[] position = file.nameWithoutExtension().split("_");
			final float center = 500.0f;
			currentArea.x = (Float.parseFloat(position[1]) - center) * terrainFixtureGenerator.scale;
			currentArea.y = (center - 1.0f - Float.parseFloat(position[0])) * terrainFixtureGenerator.scale;
			
			//---- ステージの範囲を拡大
			area.merge(currentArea);
			
			//---- 地形の衝突判定を生成する
			terrainFixtureGenerator.filePath = file.path();
			final Terrain terrain = new Terrain(collisionDef, world, Vector2.tmp.set(currentArea.x, currentArea.y));
			terrains.add(terrain);
		}

		//---- 地形の描画オブジェクトを生成する
		final DirectoryBackgroundGenerator generator = new DirectoryBackgroundGenerator();
		generator.directoryPath = terrainDir;
		generator.scale = terrainFixtureGenerator.scale;
		generator.generate(terrainLayer);
	}
	
	/**
	 * 世界の果てをつくろう
	 * @param world		世界オブジェクト
	 */
	private void createEndOfWorld(World world)
	{
		final CollisionDef def = JsonUtils.read("data/jsons/collision/eow.json", CollisionDef.class);
		
		final Vector2 directions[] = {
			new Vector2(area.width, 0.0f),
			new Vector2(0.0f, area.height),
			new Vector2(area.width, 0.0f),
			new Vector2(0.0f, area.height)
		};
		final Vector2 offsets[] = {
			new Vector2(area.x, area.y),
			new Vector2(area.x, area.y),
			new Vector2(area.x, area.y + area.height),
			new Vector2(area.x + area.width, area.y)
		};
		
		for(int i = 0;  i < 4;  ++i)
		{
			final EdgeShapeGenerator shape = new EdgeShapeGenerator();
			shape.direction = directions[i];
			shape.offset = offsets[i];
			((FixtureGenerator)def.fixtures[i]).shape = shape;
		}
		
		new CollisionObject(def, world, new StageCollisionCallback());
	}
	
	/** 夜フラグ */
	private boolean isNight;

	/** ステージの大きさ */
	private final Rectangle area;
	
	/** プレイヤーの初期座標 */
	private final Vector2 startPosition;
	
	/** 地形オブジェクト */
	private final ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	
	/** 背景レイヤーのマップ */
	private final Map<String, BackgroundLayer> backgroundLayers = new HashMap<String, BackgroundLayer>();
	
	
	/** 衝突判定のコールバック */
	private class StageCollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(Stage.this, contact);
		}
	}
}
