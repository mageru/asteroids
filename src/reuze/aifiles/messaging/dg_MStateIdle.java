package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MCallBacks.ApproachCallback;
import reuze.aifiles.messaging.dg_MCallBacks.AttackCallback;
import reuze.aifiles.messaging.dg_MCallBacks.EvadeCallback;
import reuze.aifiles.messaging.dg_MCallBacks.GetPowerupCallback;
import reuze.aifiles.messaging.dg_MessState.States;

public class dg_MStateIdle extends dg_MessState {
	public EvadeCallback m_evadeCallback;
	public ApproachCallback m_approachCallback;
	public AttackCallback m_attackCallback;
	public GetPowerupCallback m_getPowerupCallback;
	
	public dg_MStateIdle(dg_Control parent) {
		super(States.MFSM_STATE_IDLE, parent);
	}
	public void Update(float dt)
	{
	    //Do nothing
	    dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;
	    parent.m_debugTxt = "Idle";
	}

	//---------------------------------------------------------
	public States CheckTransitions()
	{
		dg_ControlAIMess parent = (dg_ControlAIMess)m_parent;

	    if(parent.m_willCollide)
	        return States.MFSM_STATE_EVADE;

	    if(parent.m_nearestAsteroid!=null)
	    {
	        if(parent.m_nearestAsteroidDist > z_app.game.APPROACH_DIST)
	            return States.MFSM_STATE_APPROACH;
	        else
	            return States.MFSM_STATE_ATTACK;
	    }

	    if(parent.m_nearestPowerup!=null && 
		   parent.m_ship.GetShotLevel() < z_app.game.MAX_SHOT_LEVEL)
	        return States.MFSM_STATE_GETPOWERUP;

	    return States.MFSM_STATE_IDLE;
	}
	@Override
	void Enter() {
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_WILL_COLLIDE.ordinal(),this,GetMessageID(),m_evadeCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_ASTEROID_FAR.ordinal(),this,GetMessageID(),m_approachCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_ASTEROID_NEAR.ordinal(),this,GetMessageID(),m_attackCallback);
		dg_MessagePump.Instance().RegisterForMessage(MSGStates.MESSAGE_POWERUP_NEAR.ordinal(),this,GetMessageID(),m_getPowerupCallback);

	}
	@Override
	void Exit() {
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_WILL_COLLIDE.ordinal(),GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_ASTEROID_FAR.ordinal(),GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_ASTEROID_NEAR.ordinal(),GetMessageID());
		dg_MessagePump.Instance().UnRegisterForMessage(MSGStates.MESSAGE_POWERUP_NEAR.ordinal(),GetMessageID());
		
	}
	@Override
	void Init() {
		// TODO Auto-generated method stub
		
	}
}
