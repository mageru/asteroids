package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MCallBacks.EvadeCallback;
import reuze.aifiles.messaging.dg_MCallBacks.IdleCallback;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;


public class dg_MStateGetPowerup extends dg_MessState {
	//callbacks for handling messages
	public EvadeCallback m_evadeCallback = new dg_MCallBacks.EvadeCallback();
	public IdleCallback m_idleCallback = new dg_MCallBacks.IdleCallback();
	//constructor/functions
    public dg_MStateGetPowerup(dg_Control parent) {
    	super(States.MFSM_STATE_GETPOWERUP.ordinal(),parent);
    }
	public void Update(float dt)
	{
		dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;
	    dg_GameObject powerup = parent.m_nearestPowerup;
	    dg_Ship    ship     = parent.m_ship;

	    if(powerup==null || ship==null)
	        return;
	    
	    //find future position of powerup
	    gb_Vector3 futurePowPosition = powerup.m_position.tmp2();
	    gb_Vector3 deltaPos = futurePowPosition.tmp().sub(ship.m_position);
	    float dist  = deltaPos.len();
	    float speed = parent.m_maxSpeed;
	    float time = dist/speed;
	    futurePowPosition.add(powerup.m_velocity.tmp3().sub(ship.m_velocity).mul(time));
		z_app.game.Clip(futurePowPosition);
		gb_Vector3 deltaFPos = futurePowPosition.sub(ship.m_position);
	    deltaFPos.nor();
	    
	    //add braking vec if you're going too fast
	    speed = ship.m_velocity.len();
	    if(speed > parent.m_maxSpeed)
	        deltaFPos.sub(ship.UnitVectorVelocity());

	    //DOT out my velocity
	    gb_Vector3 shpUnitVel = ship.UnitVectorVelocity();
	    float dotVel = shpUnitVel.dot(deltaFPos);
	    float proj = 1-dotVel;
	    deltaFPos.sub(shpUnitVel.mul(proj));
	    deltaFPos.nor();

	  //find direction, and head to it
		float newDir	 = -m_MathUtils.directionXangle(deltaFPos);
		//ship nose angle down screen 0 to 180 clkwise, else 0 to 180 counter-clkwise
		float angDelta	 = m_MathUtils.clamp180(ship.m_angle - newDir);
		String turn="";
		if ( Math.abs(angDelta) <3 )
		{
			//thrust
			ship.StopTurn(); turn="STOP";
			if (parent.m_nearestAsteroid==null || (parent.m_nearestAsteroidDist > parent.m_nearestAsteroid.m_size + 19)) {
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
	    
	    parent.m_target.m_position = ship.m_velocity.cpy().mul(time).add(futurePowPosition);
	    parent.m_targetDir = newDir;
	    parent.m_debugTxt = "GetPowerup "+ship.m_angle+" "+turn;
	}

	//---------------------------------------------------------

	//---------------------------------------------------------
	void Exit()
	{
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_WILL_COLLIDE.ordinal(),GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_ASTEROID_NEAR.ordinal(),GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_NO_POWERUPS.ordinal(),GetMessageID());
	
		//send out messages to stop the ship
		dg_Message newMsg = new dg_Message(MSGStates.MESSAGE_SHIP_TOTAL_STOP.ordinal());
		newMsg.m_fromID = GetMessageID();
		dg_MessagePump.Instance().SendMessage(newMsg);
	}
	@Override
	void Enter() {
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_WILL_COLLIDE.ordinal(),this,GetMessageID(),m_evadeCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_ASTEROID_NEAR.ordinal(),this,GetMessageID(),m_idleCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_NO_POWERUPS.ordinal(),this,GetMessageID(),m_idleCallback);
	}

	@Override
	void Init() {
		// TODO Auto-generated method stub

	}

}
