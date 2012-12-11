package reuze.aifiles;

import java.awt.geom.Point2D;

import com.software.reuze.gb_Vector3;

public class dg_MStateApproach extends dg_MessState
{
	//constructor/functions
	public dg_MStateApproach(RefObject<dg_Control> parent)
	{
		super(AnonymousEnum.MFSM_STATE_APPROACH,parent.argvalue);
	}

	//---------------------------------------------------------
	public void Update(float dt)
	{
		//turn and then thrust towards closest asteroid
		MessAIControl parent = (MessAIControl)m_parent;
		dg_GameObject asteroid = parent.m_nearestAsteroid;
		dg_Ship    ship     = parent.m_ship;
		
		if (asteroid == null)
			return;
	
		gb_Vector3 deltaPos = asteroid.m_position.sub(ship.m_position);
		gb_Vector3 astVelNormalized = asteroid.m_velocity;
		astVelNormalized.norTo(len);
	
		//use braking vector if you're going too fast
		boolean needToBrake = false;
		float speed = ship.m_velocity.len();
		if(speed > parent.m_maxSpeed)
		{
			needToBrake = true;
			deltaPos = -ship.m_velocity.
		}
		else
		{
			float dotVelocity = DOT(ship.UnitVectorVelocity(),asteroid.UnitVectorVelocity());
	
			//if the other guy is "to my front" and we're moving towards each other...
			Point3<Float> targetPos = asteroid.m_position;
			if ((DOT(deltaPos,ship.UnitVectorVelocity()) < 0) || (dotVelocity > -0.93)) //magic number == about 21 degrees
			{
				Point3<Float> shipVel = ship.m_velocity;
				shipVel = shipVel.Normalize() parent.m_maxSpeed;
				float combinedSpeed = (shipVel + asteroid.m_velocity).Length();
				float predictionTime = deltaPos.Length() / combinedSpeed;
				targetPos = asteroid.m_position + (asteroid.m_velocity *predictionTime);
				//			Game.Clip(targetPos);
				deltaPos = targetPos - ship.m_position;
			}
		}
		//sub off our current velocity, to get direction of wanted velocity
		deltaPos -= ship.m_velocity;
	
		//find new direction, and head to it
		float newDir = CALCDIR(deltaPos);
		float angDelta = CLAMPDIR180(newDir - ship.m_angle);
		boolean canApproachInReverse = needToBrake || ship.GetShotLevel()!=0;
	
		if(fabsf(angDelta) <3 || (fabsf(angDelta)> 177 && canApproachInReverse))
		{
			//thrust
			ship.StopTurn();
			if(parent.m_nearestAsteroidDist > parent.m_nearestAsteroid.m_size + 20)
				fabsf(angDelta)<3? ship.ThrustOn() : ship.ThrustReverse();
			else
				ship.ThrustOff();
		}
		else if(fabsf(angDelta)<=90 || !canApproachInReverse)
		{
			//turn when facing forwards
			if(angDelta<0)
				ship.TurnRight();
			else if(angDelta>0)
				ship.TurnLeft();
		}
		else
		{
			//turn when facing rear
			if(angDelta>0)
				ship.TurnRight();
			else if(angDelta<0)
				ship.TurnLeft();
		}
	
		parent.m_target.m_position = asteroid.m_position;
		parent.m_targetDir = newDir;
		parent.m_debugTxt = "Approach";
	}

	//---------------------------------------------------------
	public void Enter()
	{
		MessagePump.Instance().RegisterForMessage(AnonymousEnum.MESSAGE_WILL_COLLIDE,this,GetMessageID(),m_evadeCallback);
		MessagePump.Instance().RegisterForMessage(AnonymousEnum.MESSAGE_POWERUP_NEAR,this,GetMessageID(),m_getPowerupCallback);
		MessagePump.Instance().RegisterForMessage(AnonymousEnum.MESSAGE_NO_ASTEROIDS,this,GetMessageID(),m_idleCallback);
		MessagePump.Instance().RegisterForMessage(AnonymousEnum.MESSAGE_ASTEROID_NEAR,this,GetMessageID(),m_attackCallback);
	}

	//---------------------------------------------------------
	public void exit()
	{
		MessagePump.Instance().UnRegisterForMessage(AnonymousEnum.MESSAGE_WILL_COLLIDE,GetMessageID());
		MessagePump.Instance().UnRegisterForMessage(AnonymousEnum.MESSAGE_POWERUP_NEAR,GetMessageID());
		MessagePump.Instance().UnRegisterForMessage(AnonymousEnum.MESSAGE_NO_ASTEROIDS,GetMessageID());
		MessagePump.Instance().UnRegisterForMessage(AnonymousEnum.MESSAGE_ASTEROID_NEAR,GetMessageID());
	
		//send out messages to stop the ship
		Message newMsg = new Message(AnonymousEnum.MESSAGE_SHIP_TOTAL_STOP);
		newMsg.m_fromID = GetMessageID();
		MessagePump.Instance().SendMessage(newMsg);
	}

	//callbacks for handling messages
	public EvadeCallback m_evadeCallback;
	public IdleCallback m_idleCallback;
	public AttackCallback m_attackCallback;
	public GetPowerupCallback m_getPowerupCallback;
}

final class DefineConstantsMStateApproach
{
	public static final int NO_LIFE_TIMER = 99999;
	public static final int MAX_SHOT_LEVEL = 3;
	public static final int MAX_SHIP_SPEED = 120;
	public static final int MAX_AG_SHIP_SPEED = 120;
	public static final int MAX_TRACTOR_DIST = 180;
	public static final int MAX_TRACTOR_POWER = 300;
}
