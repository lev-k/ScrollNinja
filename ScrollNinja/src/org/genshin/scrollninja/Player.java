package org.genshin.scrollninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;

// 制作メモ
// 10/2 制作開始
// 10/3 変数と空の関数を実装
// 		ジャンプと移動だけ先に明日実装！
// 10/4 ジャンプと移動は実装完了だけど実行してない
//		明日アニメーション関連進行。今週までに表示までいきたい
// 10/8 移動だけ動作確認。段差のところで空中移行になってるのを直そう
//		重力弱いから要調整。ジャンプできてねー＾ｑ＾

// *メモ*
// 攻撃はダッシュしながら攻撃可能（足は止まらない）
// 右クリック押しっぱなしで伸び続ける
// 壁とかに付いた後も押しっぱでそっちに移動
// 壁とかに付いた状態で離すとブラーン
// もう一回右クリックで離す

//========================================
// クラス宣言
//========================================
public class Player extends CharacterBase {
	// 定数宣言
	private static final float RUN_MAX_VEL		= 30.0f;	// 走りの最高速度
	private static final float DASH_MAX_VEL	= 30.0f;	// ダッシュの最高速度
	private static final float RUN_ACCEL		= 10.0f;	// 走りの加速度
	private static final float DASH_ACCEL		= 10.0f;	// ダッシュの加速度
	private static final float JUMP_POWER	=  100.0f;		// ジャンプ加速度

	private static final int FOOT	= 0;
	private static final int BODY	= 0;

	private static final int RIGHT			=  1;
	private static final int LEFT			= -1;
	private static final int STAND			=  0;
	private static final int WALK			=  1;
	private static final int DASH			=  2;
	private static final int JUMP			=  3;
	private static final int ATTACK			=  4;

	// 変数宣言
	private String			name;					// 名前
	private int				charge;				// チャージゲージ
	private int				direction;				// 向いてる方向
	private int				currentState;			// 現在の状態
	private int				count;					// カウント用変数
	private float			stateTime;
	private Weapon			weapon;				// 武器のポインタ
	private boolean			jump;					// ジャンプフラグ

	private Animation		standAnimation;		// 立ちアニメーション
	private Animation		walkAnimation;		// 歩きアニメーション
	private Animation		dashAnimation;		// ダッシュアニメーション
	private Animation		jumpAnimation;		// ジャンプアニメーション
	private Animation		attackAnimation;		// 攻撃アニメーション
	private Animation		footWalkAnimation;	// 下半身・歩きアニメーション
	private TextureRegion[]	frame;					// アニメーションのコマ
	private TextureRegion	nowFrame;				// 現在のコマ
	private TextureRegion	nowFootFrame;			// 下半身用の現在のコマ

	private Sprite			footSprite;			// 下半身用のスプライト

	// おそらく別のクラスに吐き出す変数
	private int				money;					// お金

	// おそらく使わなくなる変数
	private static final float FIRST_SPEED	=  30f;		// 初速度
	private int				nowAttack;				// 現在の攻撃方法
	private float			fall;					// 落下量


	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public String GetName(){ return name; }
	public int GetDirection(){ return direction; }
	public Sprite GetSprite(String type) {
		if (type.equals("BODY"))
			return sprite.get(BODY);
		else
			return sprite.get(FOOT);
	}
	public int GetMaxHP() { return MAX_HP;}
	public int GetHP() { return hp; };

	/**
	 * コンストラクタ
	 * @param Name		名前
	 */
	public Player(String Name) {
		World world = GameMain.world;

		// body生成
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		body = world.createBody(bd);
		body.setBullet(true);					// すり抜けない
		body.setFixedRotation(true);			// 回転しない
		body.setTransform(0.0f, 3.0f, 0.0f);	// TODO プレイヤーの初期座標はクラス外から指定するハズ。

		// fixture生成
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(1.6f, 2.4f);

		FixtureDef fd = new FixtureDef();
		fd.density			= 0.0f;	// 密度
		fd.friction		= 0.0f;	// 摩擦
		fd.restitution	= 0.0f;	// 反発
		fd.shape			= poly;	// 形状

		sensor.add( body.createFixture(fd) );

		// テクスチャの読み込み
		Texture texture = new Texture(Gdx.files.internal("data/player.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// アニメーション
		TextureRegion[][] tmp = TextureRegion.split(texture, 64, 64);

		// 下半身・歩き １行目６フレーム
		frame = new TextureRegion[6];
		int index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[0][i];
		footWalkAnimation = new Animation(5.0f, frame);

		// 上半身・歩き　２行目６フレーム
		frame = new TextureRegion[6];
		index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[1][i];
		walkAnimation = new Animation(5.0f, frame);

		// スプライトに反映 最初は立ちの第１フレーム
		// （※現在は用意されていないので歩きの第１フレームで代用）
		Sprite bodySprite = new Sprite(walkAnimation.getKeyFrame(0, true));
		bodySprite.setOrigin(bodySprite.getWidth() * 0.5f, bodySprite.getHeight() * 0.5f);
		bodySprite.setScale(0.1f);
		sprite.add(BODY, bodySprite);

		Sprite footSprite = new Sprite(footWalkAnimation.getKeyFrame(0, true));
		footSprite.setOrigin(footSprite.getWidth() * 0.5f, footSprite.getHeight() * 0.5f);
		footSprite.setScale(0.1f);
		sprite.add(FOOT, bodySprite);

		// 一番最初の表示　現在は歩きで代用
		nowFrame = walkAnimation.getKeyFrame(0, true);
		nowFootFrame = footWalkAnimation.getKeyFrame(0, true);

		name		 = Name;
		charge		 = 0;
		money		 = 0;
		direction	 = 1;
		currentState = STAND;
		fall		 = 0;
		count		 = 0;
//		weapon		 = WeaponManager.GetInstace().GetWeapon("");
		jump		 = false;

		EffectManager.CreateEffect(Effect.FIRE_2, this);
		nowAttack = Effect.FIRE_2;
	}

	//************************************************************
	// Update
	// 更新処理はここにまとめる
	//************************************************************
	public void Update() {
		sprite.get(BODY).setRegion(nowFrame);
		sprite.get(FOOT).setRegion(nowFootFrame);

		Stand();		// 立ち処理
		Move();		// 移動処理
		Jump();		// ジャンプ処理
		Attack();
		animation();		// アニメーション処理

		{
			Vector2 velocity = body.getLinearVelocity();
			System.out.printf("Velocity: %7.2f, %7.2f\n", velocity.x, velocity.y);
		}
	}

	//************************************************************
	// Stand
	// 立ち処理。
	//************************************************************
	private void Stand() {
		if( GetGroundJudge() ) {
			currentState = STAND;
		}
	}
	//************************************************************
	// Jump
	// ジャンプ処理。上押すとジャンプ！
	//************************************************************
	private void Jump() {


		// 地面に接触しているならジャンプ可能
		if( /*GetGroundJudge(world)*/ !jump ) {
			// 上押したらジャンプ！
			if (Gdx.input.isKeyPressed(Keys.W)) {
				jump = true;
				currentState = JUMP;
				body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
				body.applyLinearImpulse(0.0f, JUMP_POWER, position.x, position.y);
			}
		}

		// ジャンプ中の処理
		if( jump ) {
//			body.setLinearVelocity(velocity.x, velocity.y);
//			velocity.y -= 1;
		}
	}

	//************************************************************
	// Move
	// 移動処理。左右押すと移動します
	// 状態遷移は空中にいなければ歩きに！
	//************************************************************
	private void Move() {
		// 速度制限
		Vector2 vel = body.getLinearVelocity();
		if( Math.abs(vel.x) > RUN_MAX_VEL )
		{
			body.setLinearVelocity(Math.signum(vel.x)*RUN_MAX_VEL, vel.y);
		}

		// 右が押された
		if (Gdx.input.isKeyPressed(Keys.D)) {
			direction = RIGHT;				// プレイヤーの向きを変更。
			body.applyLinearImpulse(RUN_ACCEL*direction, 0.0f, position.x, position.y);
			int count = sprite.size();
			flip(true, false);

			if( GetGroundJudge() ) {	// もし地面なら歩くモーションにするので現在の状態を歩きに。
				currentState = WALK;
			}
		}
		// 左が押された
		if (Gdx.input.isKeyPressed(Keys.A)) {
			direction = LEFT;
			body.applyLinearImpulse(RUN_ACCEL*direction, 0.0f, position.x, position.y);
			flip(false, false);

			if( GetGroundJudge() ) {
				currentState = WALK;
			}
		}
		// 移動キーが押されていない時は少しずつ減速
		if (!Gdx.input.isKeyPressed(Keys.D) && !Gdx.input.isKeyPressed(Keys.A)) {
//			velocity.x *= 0.9;
//			if (velocity.x < 5)
//				velocity.x = 0;
//			body.setLinearVelocity(velocity.x, GRAVITY);
		}
	}

	//************************************************************
	// Attack
	// 攻撃処理。左クリックで攻撃
	//************************************************************
	private void Attack() {
		if(Gdx.input.isKeyPressed(Keys.Z)) {
			currentState = ATTACK;

			switch(nowAttack) {
			case Effect.FIRE_1:
				break;
			case Effect.FIRE_2:
				EffectManager.GetEffect(Effect.FIRE_2).SetUseFlag(true);
				break;
			}
		}
	}

	// カギ縄
	private void Kaginawa(){}

	//************************************************************
	// animation
	// 現在の状態を参照して画像を更新
	//************************************************************
	private void animation() {
		switch(currentState) {
		case STAND:		// 立ち
			break;
		case WALK:		// 歩き
			nowFrame = walkAnimation.getKeyFrame(stateTime, true);
			nowFootFrame = footWalkAnimation.getKeyFrame(stateTime, true);
			stateTime ++;
			break;
		case DASH:		// 走り
			break;
		case JUMP:		// ジャンプ
			break;
		case ATTACK:
			count ++;
			break;
		}
	}

	// 武器変更
	private void changeWeapon() {
	}

	//************************************************************
	// GetGroundJudge
	// 戻り値： true:地面接地		false:空中
	// 接触判定。長いのでここで関数化
	//************************************************************
	private boolean GetGroundJudge() {
		// この処理はStageあたりに任せる予定。
		/*
		List<Contact> contactList = GameMain.world.getContactList();
		Fixture sensor = super.sensor.get(0);

		for(int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);

			// 地面に当たったよ
			for( int j = 0; j < Background.GetBody().getFixtureList().size(); j ++) {
				if(contact.isTouching() &&
						(( contact.getFixtureA() == sensor && contact.getFixtureB() == Background.GetSensor(j) ) ||
						( contact.getFixtureA() == Background.GetSensor(j) && contact.getFixtureB() == sensor ))) {
					jump = false;
					fall = 0;
					//System.out.println("地面！");
					return true;
				}
			}
		}
		*/
		return false;
	}

	@Override
	public void collisionDispatch(ObJectBase obj, Contact contact)
	{
		obj.collisionDispatch(this, contact);

	}
	@Override
	protected void collisionNotify(Background obj, Contact contact)
	{
		// まだ作ってる途中なんだよ、こっちくんな
		return;
		/*
		// TODO プレイヤーと地形の衝突処理
		WorldManifold manifold = contact.getWorldManifold();
		int count = manifold.getNumberOfContactPoints();

		boolean below = true;
		for(int i = 0;  i < count;  ++i)
		{
			below &= (manifold.getPoints()[i].y < pos.y - 1.5f);
		}
		*/
	}
}
