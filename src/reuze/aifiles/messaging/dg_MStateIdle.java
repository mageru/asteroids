package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.States;

public class dg_MStateIdle extends dg_MessState {
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
		// TODO Auto-generated method stub
		
	}
	@Override
	void Exit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	void Init() {
		// TODO Auto-generated method stub
		
	}
}
