package reuze.aifiles.messaging;

import java.awt.geom.Point2D;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

public class dg_MStateApproach extends dg_MessState
{
	//constructor/functions
	public dg_MStateApproach(RefObject<dg_Control> parent)
	{
		super(States.MFSM_STATE_APPROACH,parent.argvalue);
	}

	//---------------------------------------------------------
	public void Update(float dt)
	{
		//turn and then thrust towards closest asteroid
		dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;
		dg_GameObject asteroid = parent.m_nearestAsteroid;
		dg_Ship    ship     = parent.m_ship;
		
		if (asteroid == null)
			return;
	
		gb_Vector3 deltaPos = asteroid.m_position.tmp().sub(ship.m_position);
		//use braking vector if you're going too fast
		boolean needToBrake = false;
		float speed		 = ship.m_velocity.len();
	
		//use braking vector if you're going too fast
		if(speed > parent.m_maxSpeed)
		{
			needToBrake = true;
			deltaPos	= ship.m_velocity.tmp().inv();
		}
		else
		{
			float dotVelocity = ship.UnitVectorVelocity().dot(asteroid.UnitVectorVelocity());
	
			//if the other guy is "to my front" and we're moving towards each other...
			if ((deltaPos.dot(ship.UnitVectorVelocity()) < 0) || (dotVelocity > -0.93))//magic number == about 21 degrees
			{
				gb_Vector3 shipVel = ship.m_velocity.tmp2();
				shipVel.nor().mul(parent.m_maxSpeed);
				float combinedSpeed	  = shipVel.add(asteroid.m_velocity).len();
				if (Math.abs(combinedSpeed)>.01) {
					float predictionTime  = deltaPos.len() / combinedSpeed;
					gb_Vector3 targetPos = asteroid.m_position.tmp().add(asteroid.m_velocity.tmp2().mul(predictionTime));
					deltaPos  = targetPos.sub(ship.m_position);
				}
			}
		}
		//sub off our current velocity, to get direction of wanted velocity
		deltaPos.sub(ship.m_velocity);
	
		//find new direction, and head to it
		float newDir	 = -m_MathUtils.directionXangle(deltaPos);
		//ship nose angle down screen 0 to 180 clkwise, else 0 to 180 counter-clkwise
		float angDelta	 = m_MathUtils.clamp180(ship.m_angle - newDir);
		boolean canApproachInReverse = needToBrake || ship.GetShotLevel()!=0;
	
		String turn="";
		if(Math.abs(angDelta) <3 || (Math.abs(angDelta)> 177 && canApproachInReverse) )
		{
			//thrust
			ship.StopTurn(); turn="STOP";
			if(parent.m_nearestAsteroidDist > parent.m_nearestAsteroid.m_size + 19) {
	                if (Math.abs(angDelta)<3) ship.ThrustOn(); else ship.ThrustReverse();
	            } else ship.ThrustOff();
		}
		else if(newDir<0 && ship.m_angle<0) {
			if (newDir<ship.m_angle) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		} else if(newDir>=0 && ship.m_angle>=0) {
				if (newDir<ship.m_angle) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		} else if(newDir<0 && ship.m_angle>=0) {
				if ((360+newDir-ship.m_angle)>180) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		} else if(newDir>=0 && ship.m_angle<0) {
			if ((360-newDir+ship.m_angle)<180) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		}
		parent.m_target.m_position = asteroid.m_position;
		parent.m_targetDir = newDir;
		parent.m_debugTxt = "Approach "+ship.m_angle+" "+turn+" "+asteroid.m_id;
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