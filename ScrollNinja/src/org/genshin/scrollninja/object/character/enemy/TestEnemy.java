package org.genshin.scrollninja.object.character.enemy;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.effect.CopyEffect;
import org.genshin.scrollninja.object.effect.EffectDef;
import org.genshin.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.object.weapon.KatanaWeapon;
import org.genshin.scrollninja.render.AnimationRenderObject;
import org.genshin.scrollninja.render.RenderObject;
import org.genshin.scrollninja.utils.JsonUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 実験用の敵オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class TestEnemy extends AbstractEnemy
{
	public TestEnemy(World world, Vector2 position, boolean isNight)
	{
		super("data/jsons/enemy/test_enemy.json", world);
		
		//---- 描画オブジェクトを生成する。
		final String spritePath = "data/jsons/render/enemy_sprite.json";
		final String animationPath = "data/jsons/render/" + (isNight?"night_":"") + "enemy_animation.json";
		final AnimationRenderObject ro = new AnimationRenderObject(spritePath, animationPath, this, GlobalDefine.RenderDepth.ENEMY);
		ro.setAnimation("Walk");
		addRenderObject(ro);
		
		//---- 初期座標を設定する。
		setTransform(position, 0.0f);
		
		//---- ざざざざんぞう
		enableAfterimage = isNight;
		if(isNight && afterimageEffectDef == null)
		{
			final String effectPath = "data/jsons/effect/" + (isNight ? "night_" : "") + "enemy_afterimage.json";
			afterimageEffectDef = JsonUtils.read(effectPath, EffectDef.class);
			afterimageEffectDef.startVelocity.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
			afterimageEffectDef.endVelocity.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		}
	}

	@Override
	protected void updateCharacter(float deltaTime)
	{
		super.updateCharacter(deltaTime);
		
		//---- 残像エフェクト
		// TODO 残像は常に出すワケではない？
		if(enableAfterimage)
		{
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
		}
	}

	@Override
	protected AbstractWeapon createWeapon()
	{
		return new KatanaWeapon(getCollisionObject().getBody().getWorld(), this);
	}
	
	
	/** 残像フラグ */
	private boolean enableAfterimage;
	
	/** 残像エフェクトの定義 */
	private static EffectDef afterimageEffectDef = null;
}
