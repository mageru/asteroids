package reuze.aifiles.messaging;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

import reuze.aifiles.messaging.dg_MessState.States;

public class dg_MStateEvade extends dg_MessState {
	//constructor/functions
    public dg_MStateEvade(dg_Control parent) {
    	super(States.MFSM_STATE_EVADE,parent);
    }
    @Override
	public void Update(float dt)
	{
	    //evade by going to the quad opposite as the asteroid
	    //is moving, add in a deflection, and cancel out your movement
	    dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;
	    dg_GameObject asteroid = parent.m_nearestAsteroid;
	    dg_Ship    ship     = parent.m_ship;
	    gb_Vector3 vecSteer = ship.m_position.tmp().crs(asteroid.m_position);
	    gb_Vector3 vecBrake = ship.m_position.tmp2().sub(asteroid.m_position);
	    vecSteer.add(vecBrake);
	    
	    float newDir = -m_MathUtils.directionXangle(vecSteer);
	    float angDelta = m_MathUtils.clamp180(ship.m_angle - newDir);
	    float dangerAdjAngle = ((1.0f - parent.m_nearestAsteroidDist/ z_app.game.APPROACH_DIST)*10.0f) + 1.0f;
	    if(Math.abs(angDelta) <dangerAdjAngle || Math.abs(angDelta)> 180-dangerAdjAngle)//thrust
	    {
	        ship.StopTurn();
	        if(ship.m_velocity.len() < parent.m_maxSpeed || parent.m_nearestAsteroidDist< 19+asteroid.m_size) {
	            if (Math.abs(angDelta)<dangerAdjAngle) ship.ThrustOn(); else ship.ThrustReverse();
	        } else ship.ThrustOff();

	        //if I'm pointed right at the asteroid, shoot
	        ship.Shoot();
	    }
	    else if(Math.abs(angDelta)<=90)//turn front
	    {
	        if(angDelta >0)
	            ship.TurnRight();
	        else
	            ship.TurnLeft();
	    }
	    else//turn rear
	    {
	        if(angDelta<0)
	            ship.TurnRight();
	        else
	            ship.TurnLeft();
	    }
	    
	    
	    parent.m_target.m_position = parent.m_nearestAsteroid.m_position;
	    parent.m_targetDir = newDir;
	    parent.m_debugTxt = "Evade "+newDir;
	}

	//---------------------------------------------------------
	public States CheckTransitions()
	{
		dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;

	    if(!parent.m_willCollide)
	        return States.MFSM_STATE_IDLE;

	    return States.MFSM_STATE_EVADE;
	}

	//---------------------------------------------------------
	public void Exit()
	{
		/**
		MessagePump.Instance().UnRegisterForMessage(AnonymousEnum.MESSAGE_WONT_COLLIDE,GetMessageID());
		
		//send out messages to stop the ship
		Message newMsg = new Message(AnonymousEnum.MESSAGE_SHIP_TOTAL_STOP);
		newMsg.m_fromID = GetMessageID();
		MessagePump.Instance().SendMessage(newMsg);
		**/
	}
	@Override
	void Enter() {
		//MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_WONT_COLLIDE,this,GetMessageID(),m_idleCallback);

	}

	@Override
	void Init() {
		// TODO Auto-generated method stub

	}

}
final class DefineConstantsMStateEvade
{
	public static final int NO_LIFE_TIMER = 99999;
	public static final int MAX_SHOT_LEVEL = 3;
	public static final int MAX_SHIP_SPEED = 120;
	public static final int MAX_AG_SHIP_SPEED = 120;
	public static final int MAX_TRACTOR_DIST = 180;
	public static final int MAX_TRACTOR_POWER = 300;
}
