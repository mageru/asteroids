package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.MSGStates;
import reuze.aifiles.messaging.dg_MessagePump.MessageReciever;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_InterpolateLerp;

public class dg_ControlAIMess extends dg_ControlAI {
	public static final float POWERUP_SCAN_DIST=180.0f;
		//constructor/functions
		public dg_ControlAIMess() {this(null);}
		
		//perception data 
		dg_GameObject	m_nearestAsteroid;
		dg_GameObject	m_nearestPowerup;
		float       m_nearestAsteroidDist;
		float       m_nearestPowerupDist;
		boolean        m_willCollide;
		boolean        m_powerupNear;
		float       m_safetyRadius;
	    float       m_maxSpeed;
		//data
		dg_MessMachine m_machine;
		MessageReciever m_messReceiver;
		public dg_ControlAIMess(dg_Ship ship) {
			super(ship);
			dg_MessagePump thePump = new dg_MessagePump();
			dg_MessagePump.setInstance(thePump);
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_WONT_COLLIDE.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_WILL_COLLIDE.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_NO_ASTEROIDS.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_NO_POWERUPS.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_ASTEROID_NEAR.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_ASTEROID_FAR.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_POWERUP_NEAR.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_POWERUP_FAR.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_SHIP_TOTAL_STOP.ordinal());
			dg_MessagePump.Instance().AddMessageToSystem(MSGStates.MESSAGE_CHANGE_STATE.ordinal());
			    //construct the state machine and add the necessary states
			    m_machine = new dg_MessMachine(dg_MessState.Types.MFSM_MACH_MAINSHIP.ordinal(),this);
			    dg_MStateApproach approach = new dg_MStateApproach(this);
			    m_machine.AddState(approach);
			    m_machine.AddState(new dg_MStateAttack(this));
			    m_machine.AddState(new dg_MStateEvade(this));
			    m_machine.AddState(new dg_MStateGetPowerup(this));
			    dg_MStateIdle idle = new dg_MStateIdle(this);
			    m_machine.AddState(idle);
			    //m_machine.SetDefaultState(idle);
			    m_machine.SetDefaultState(approach);
			    m_machine.Reset();
			}

			//---------------------------------------------------------
			public void Init()
			{
			    m_messReceiver = new MessageReciever();
			    m_willCollide  = false;
			    m_powerupNear  = false;
			    m_nearestAsteroid = null;
			    m_nearestPowerup  = null;
			    m_safetyRadius    = 18.0f;
			    m_maxSpeed        = z_app.game.AI_MAX_SPEED_TRY;///Game.m_timeScale;
			    
			    if(m_target==null)
			    {
			        m_target = new dg_Target(1);
			        z_app.game.AddGameObj(m_target);
			    }
			}

			//---------------------------------------------------------
			public void Update(float dt)
			{
			    if(m_ship==null)
			    {
			        m_machine.Reset();
			        return;
			    }
			    
			    UpdatePerceptions(dt);
			    m_machine.Update(dt);
			}

			//---------------------------------------------------------
			public void UpdatePerceptions(float dt)
			{
			    if(m_willCollide)
			        m_safetyRadius = 30.0f;
			    else
			        m_safetyRadius = 15.0f;
			    
			    //store closest asteroid and powerup
			    m_nearestAsteroid = z_app.game.GetClosestGameObj(m_ship,m_ship.OBJ_ASTEROID);
			    m_nearestPowerup  = z_app.game.GetClosestGameObj(m_ship,m_ship.OBJ_POWERUP);
			    
			    //asteroid collision determination
			    m_willCollide = false;
			    if(m_nearestAsteroid!=null)
			    {
			        float speed = m_ship.m_velocity.len();
			        m_nearestAsteroidDist = m_nearestAsteroid.m_position.dst(m_ship.m_position);
			        gb_Vector3 normDelta = m_nearestAsteroid.m_position.tmp().sub(m_ship.m_position);
			        normDelta.nor();
			        float astSpeed = m_nearestAsteroid.m_velocity.len();
			        float shpSpeedAdj = m_ship.UnitVectorVelocity().dot(normDelta)*speed;
			        normDelta.inv();
			        float astSpeedAdj = m_nearestAsteroid.UnitVectorVelocity().dot(normDelta)*astSpeed;
			        speed = shpSpeedAdj+astSpeedAdj;

//			        if(speed > astSpeed)
//			            dotVel  = DOT(m_ship.UnitVectorVelocity(),normDelta);
//			        else 
//			        {
//			            speed = astSpeed;
//			            dotVel = DOT(m_nearestAsteroid.UnitVectorVelocity(),-normDelta);
//			        }
			        float spdAdj = m_InterpolateLerp.lerp(speed/m_maxSpeed,0.0f,90.0f);
			        float adjSafetyRadius = m_safetyRadius+spdAdj+m_nearestAsteroid.m_size;
			        
			        //if you're too close, and I'm heading somewhat towards you,
			        //flag a collision
			        if(m_nearestAsteroidDist <= adjSafetyRadius && speed > 0) {
			            m_willCollide = true;
			            dg_Message msg = new dg_Message(MSGStates.MESSAGE_WILL_COLLIDE.ordinal());
			            dg_MessagePump.Instance().SendMessage(msg);
			        } else {
			        	dg_Message msg = new dg_Message(MSGStates.MESSAGE_WONT_COLLIDE.ordinal());
			            dg_MessagePump.Instance().SendMessage(msg);
			        }
			        
			        if(m_nearestAsteroidDist > 180.0f) {
			        	dg_Message msg = new dg_Message(MSGStates.MESSAGE_ASTEROID_FAR.ordinal());
			            dg_MessagePump.Instance().SendMessage(msg);
			        } else if (m_nearestAsteroidDist < (180.0f/2)) {
			        	dg_Message msg = new dg_Message(MSGStates.MESSAGE_ASTEROID_NEAR.ordinal());
			            dg_MessagePump.Instance().SendMessage(msg);
			        }
			    }
			    else {
		        	dg_Message msg = new dg_Message(MSGStates.MESSAGE_NO_ASTEROIDS.ordinal());
		            dg_MessagePump.Instance().SendMessage(msg);
			    }

			    //powerup near determination
			    m_powerupNear = false;
			    if(m_nearestPowerup!=null)
			    {
			        m_nearestPowerupDist = m_nearestPowerup.m_position.dst(m_ship.m_position);
			        if(m_nearestPowerupDist <= POWERUP_SCAN_DIST)
			        {
			            m_powerupNear     = true;
			            dg_Message msg = new dg_Message(MSGStates.MESSAGE_POWERUP_NEAR.ordinal());
			            dg_MessagePump.Instance().SendMessage(msg);
			        }
			    } else {
			    	dg_Message msg = new dg_Message(MSGStates.MESSAGE_NO_POWERUPS.ordinal());
		            dg_MessagePump.Instance().SendMessage(msg);
			    }
			    
			}

}
