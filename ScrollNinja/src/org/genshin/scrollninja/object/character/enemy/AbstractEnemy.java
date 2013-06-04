package org.genshin.scrollninja.object.character.enemy;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.engine.utils.AbstractFSM;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.attack.AbstractAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.effect.CopyEffect;
import org.genshin.scrollninja.object.effect.EffectDef;
import org.genshin.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.render.AnimationRenderObject;
import org.genshin.scrollninja.render.RenderObject;
import org.genshin.scrollninja.utils.JsonUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 敵の基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractEnemy extends AbstractCharacter
{
	/**
	 * コンストラクタ
	 * @param enemyFilePath			敵の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 */
	public AbstractEnemy(String enemyFilePath, World world)
	{
		super((enemyDefTmp = EnemyDefFactory.getInstance().get(enemyFilePath)).collisionFilePath, world);
		
		enemyDef = enemyDefTmp;
		enemyDefTmp = null;
		
		weapon = createWeapon();
		
		stateFactory = createStateFactory();
		state = stateFactory.get("Patrol");
		
		//---- 残像エフェクトの定義
		if(afterimageEffectDef == null)
		{
			afterimageEffectDef = JsonUtils.read("data/jsons/effect/enemy_afterimage.json", EffectDef.class);
			afterimageEffectDef.startVelocity.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
			afterimageEffectDef.endVelocity.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		}
	}
	
	@Override
	public void dispose()
	{
		//---- いろいろ破棄する。
		weapon.dispose();
		stateFactory.clear();
		
		//---- 基本クラスを破棄する。
		super.dispose();
	}

	@Override
	protected void updateCharacter(float deltaTime)
	{
		//---- 状態別更新処理
		state = (AbstractEnemyState)state.update(deltaTime);
		
		//---- 向きの設定
		final boolean flip = direction > 0.0f;
		flipX(flip);
		getCollisionObject().flipX(flip);
		
		//---- 追跡対象は毎フレームクリアしておく。
		chaseTarget = null;
		
		//---- 残像エフェクト
		// TODO 残像は常に出すワケではない？
		for(RenderObject ro : getRenderObjects())
		{
			final AnimationRenderObject aro = (AnimationRenderObject)ro;
			
			//---- コピーエフェクトを生成する前にアニメーションを一時停止しておく。
			final boolean oldPaused = aro.isAnimationPaused();
			aro.pauseAnimation();
			
			//---- コピーエフェクトを生成する。
			new CopyEffect(aro, afterimageEffectDef, aro.getDepth()-1);
			
			//---- アニメーションの一時停止状態を元に戻す
			if(!oldPaused)
			{
				aro.resumeAnimation();
			}
		}
		
		//---- お前はもう死んでいる。
		if(isDead())
		{
// FIXME 敵をrespawnさせて遊ぶために、一時的にコメントアウト。最終的には戻す。
//			dispose();
		}
	}

	@Override
	protected AbstractCharacterCollisionCallback createCollisionCallback()
	{
		return new EnemyCollisionCallback();
	}
	
	/**
	 * 武器を生成する。
	 * @return		武器
	 */
	protected abstract AbstractWeapon createWeapon();
	
	/**
	 * 状態の生成を管理するオブジェクトを生成する。
	 * @return		状態の生成を管理するオブジェクト
	 */
	protected EnemyStateFactory createStateFactory()
	{
		return new EnemyStateFactory();
	}
	
	/**
	 * 移動する。
	 * @param accel				加速度
	 * @param maxVelocity		最大速度
	 */
//	private void move(float accel, float maxVelocity)
//	{
//		final Body body = getCollisionObject().getBody();
//		
//		// 最高速度に達していなければ加速する
//		if( Math.abs(body.getLinearVelocity().x) < maxVelocity )
//		{
//			body.applyLinearImpulse(accel * direction, 0.0f, body.getPosition().x, body.getPosition().y);
//			
//			// 最高速度を越えている場合は丸める
//			final Vector2 velocity = Vector2.tmp.set(body.getLinearVelocity());
//			if( Math.abs(velocity.x) > maxVelocity )
//			{
//				body.setLinearVelocity(Math.signum(velocity.x) * maxVelocity, velocity.y);
//			}
//		}
//	}
	
	
	/** 敵の定義（作業用の一時領域） */
	private static EnemyDef enemyDefTmp;
	
	/** 敵の定義 */
	private final EnemyDef enemyDef;
	
	/** 武器 */
	private final AbstractWeapon weapon;
	
	/** 状態の生成を管理するオブジェクト */
	private final EnemyStateFactory stateFactory;
	
	/** 敵の状態 */
	private AbstractEnemyState state;
	
	/** 敵の向き */
	private float direction = 1.0f;
	
	/** 追跡する対象となるキャラクター */
	private AbstractCharacter chaseTarget = null;
	
	/** 残像エフェクトの定義 */
	private static EffectDef afterimageEffectDef = null;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	private class EnemyCollisionCallback extends AbstractCharacterCollisionCallback
	{
		@Override
		public void collision(AbstractCharacter obj, Contact contact)
		{
			final Fixture visionFixture = AbstractEnemy.this.getCollisionObject().getFixture("Vision");
			if(contact.getFixtureA() == visionFixture || contact.getFixtureB() == visionFixture)
			{
				AbstractEnemy.this.chaseTarget = obj;
			}
		}

		@Override
		public void collision(AbstractAttack obj, Contact contact)
		{
			//---- ダメージを受ける。
			AbstractEnemy.this.damage(obj.getPower());
		}
	}
	
	
	/**
	 * 敵の状態の基本クラス
	 */
	protected abstract class AbstractEnemyState extends AbstractFSM<Float> { /* 何も実装しない */ }
	
	
	/**
	 * 一定の区間を巡回する状態
	 */
	protected class PatrolEnemyState extends AbstractEnemyState
	{
		@Override
		protected void entryAction(Float deltaTime)
		{
			direction = -direction;
			turnTimer = enemyDef.patrolTurnInterval;
		}

		@Override
		public AbstractEnemyState inputAction(Float deltaTime)
		{
			//---- 追跡対象が存在する場合は追跡状態へ
			if(chaseTarget != null)
			{
				return stateFactory.get("Chase");
			}
			
			//---- 巡回中
			// 移動
			move(enemyDef.patrolAccel * deltaTime * direction, enemyDef.patrolMaxVelocity);
			
			// 移動方向の折り返し
			if((turnTimer -= deltaTime) < 0.0f)
			{
				turnTimer = enemyDef.patrolTurnInterval;
				direction = -direction;
			}
			
			return this;
		}
		
		
		/** 移動方向の折り返し判定用のタイマー */
		private float turnTimer;
	}
	
	
	/**
	 * プレイヤーを追跡する状態
	 */
	protected class ChaseEnemyState extends AbstractEnemyState
	{
		@Override
		public void entryAction(Float deltaTime)
		{
		}
		
		@Override
		protected AbstractEnemyState inputAction(Float deltaTime)
		{
			//---- 追跡対象が存在しない場合は巡回状態へ
			if(chaseTarget == null)
			{
				return stateFactory.get("Patrol");
			}
			
			//---- 追跡中
			// 追跡対象に向かって移動する。
			direction = Math.signum( chaseTarget.getPositionX() - getPositionX() );
			move(enemyDef.chaseAccel * deltaTime * direction, enemyDef.chaseMaxVelocity);
			
			// 目標物を追跡対象に設定する。
			setLookAtDirection(chaseTarget.getPositionX() - getPositionX(), chaseTarget.getPositionY() - getPositionY());
			
			// ぶんぶん丸
			weapon.attack();
			
			return this;
		}
	}
	
	
	/**
	 * 状態の生成を管理するファクトリクラス
	 */
	protected class EnemyStateFactory extends AbstractFlyweightFactory<String, AbstractEnemyState>
	{
		@Override
		protected AbstractEnemyState create(String key)
		{
			if(key.equals("Patrol"))	return new PatrolEnemyState();
			if(key.equals("Chase"))		return new ChaseEnemyState();
			
			return null;
		}
	}
}
