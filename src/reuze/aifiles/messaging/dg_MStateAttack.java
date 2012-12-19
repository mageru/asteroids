package reuze.aifiles.messaging;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

import reuze.aifiles.messaging.dg_MessState.States;
import reuze.aifiles.messaging.dg_MCallBacks.AttackCallback;
import reuze.aifiles.messaging.dg_MCallBacks.EvadeCallback;
import reuze.aifiles.messaging.dg_MCallBacks.GetPowerupCallback;
import reuze.aifiles.messaging.dg_MCallBacks.IdleCallback;
import reuze.aifiles.messaging.dg_MCallBacks.ApproachCallback;

public class dg_MStateAttack extends dg_MessState {
	public EvadeCallback m_evadeCallback;
	public IdleCallback m_idleCallback;
	public AttackCallback m_attackCallback;
	public GetPowerupCallback m_getPowerupCallback;
	public ApproachCallback m_approachCallback;
	
	//constructor/functions
    public dg_MStateAttack(dg_Control parent) {
    	super(States.MFSM_STATE_ATTACK,parent);
    }
    @Override
	public void Update(float dt)
	{
	    //turn towards closest asteroid's future position, and then fire
    	dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;
	    dg_GameObject asteroid = parent.m_nearestAsteroid;
	    dg_Ship    ship     = parent.m_ship;
	    
	    if(asteroid==null)
	        return;
	    
	    gb_Vector3 futureAstPosition;
	    gb_Vector3 deltaPos = asteroid.m_position.tmp().sub(ship.m_position);
	    float dist  = deltaPos.len();
	    float time = dist/z_app.game.BULLET_SPEED;
	    futureAstPosition = asteroid.m_velocity.tmp().sub(ship.m_velocity).mul(time).add(asteroid.m_position);
	    gb_Vector3 deltaFPos = futureAstPosition.tmp2().sub(ship.m_position);
	    deltaFPos.nor();

	    float newDir     = -m_MathUtils.directionXangle(deltaFPos);
	    float gun = ship.GetClosestGunAngle(newDir);
	    float angDelta   = m_MathUtils.clamp180(gun - newDir);
	    //float dangerAdjAngle = ((1.0f - parent.m_nearestAsteroidDist/ z_app.game.APPROACH_DIST)*5.0f) + 1.0f;
	    ship.Shoot();
	    if(angDelta >=1) {//dangerAdjAngle)
	        ship.ThrustOn(); ship.TurnRight();
	    } else if(angDelta <= -1) {//-dangerAdjAngle)
	        ship.ThrustOn(); ship.TurnLeft();
	    } else {
	    	ship.ThrustOff(); ship.StopTurn();
	    }
	    
	    parent.m_target.m_position = futureAstPosition;
	    parent.m_targetDir = newDir;
	    parent.m_debugTxt = "Attack "+deltaFPos+" "+asteroid.m_id;
	}

	//---------------------------------------------------------
	States CheckTransitions()
	{
		dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;
	    if(parent.m_willCollide)
	        return States.MFSM_STATE_EVADE;

	    if(parent.m_powerupNear && parent.m_nearestAsteroidDist > 
	       parent.m_nearestPowerupDist && parent.m_ship.GetShotLevel() < z_app.game.MAX_SHOT_LEVEL)
	        return States.MFSM_STATE_GETPOWERUP;
	    
	    if(parent.m_nearestAsteroid==null || parent.m_nearestAsteroidDist > z_app.game.APPROACH_DIST)
	        return States.MFSM_STATE_IDLE;

	    return States.MFSM_STATE_ATTACK;    
	}

	@Override
	public void Exit()
	{
		/**
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_WILL_COLLIDE,GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_POWERUP_NEAR,GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_NO_ASTEROIDS,GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_ASTEROID_FAR,GetMessageID());
	
		//send out messages to stop the ship
		Message newMsg = new Message(AnonymousEnum.MESSAGE_SHIP_TOTAL_STOP);
		newMsg.m_fromID = GetMessageID();
		MessagePump.Instance().SendMessage(newMsg);
		**/
	}
	@Override
	void Enter() {
		/**
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_WILL_COLLIDE,this,GetMessageID(),m_evadeCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_POWERUP_NEAR,this,GetMessageID(),m_getPowerupCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_NO_ASTEROIDS,this,GetMessageID(),m_idleCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_ASTEROID_FAR,this,GetMessageID(),m_approachCallback);
		 **/
	}

	@Override
	void Init() {
		// TODO Auto-generated method stub

	}

}
final class DefineConstantsMStateAttack
{
	public static final int NO_LIFE_TIMER = 99999;
	public static final int MAX_SHOT_LEVEL = 3;
	public static final int MAX_SHIP_SPEED = 120;
	public static final int MAX_AG_SHIP_SPEED = 120;
	public static final int MAX_TRACTOR_DIST = 180;
	public static final int MAX_TRACTOR_POWER = 300;
}