package reuze.aifiles;

import java.util.List;
import reuze.aifiles.dg_Callback;

public abstract class dg_Messages {
	public static enum RegState {
		REGISTER_ERROR_MESSAGE_NOT_IN_SYSTEM,
		REGISTER_ERROR_ALREADY_REGISTERED,
		REGISTER_MESSAGE_OK
	}
	
	public static enum Types {
		MESSAGE_DEFAULT(0), 
		MESSAGE_WILL_COLLIDE(1), 
		MESSAGE_WONT_COLLIDE(2), 
		MESSAGE_NO_ASTEROIDS(3), 
		MESSAGE_NO_POWERUPS(4), 
		MESSAGE_ASTEROID_FAR(5), 
		MESSAGE_ASTEROID_NEAR(6), 
		MESSAGE_POWERUP_NEAR(7), 
		MESSAGE_POWERUP_FAR(8), 
		MESSAGE_CHANGE_STATE(9), 
		MESSAGE_SHIP_TOTAL_STOP(10),
		MESSAGE_TOKEN_PSCAN(11), 
		MESSAGE_TOKEN_MAXSPEED(12), 
		MESSAGE_TOKEN_APDIST(13), 
		MESSAGE_TOKEN_ATDIST(14), 
		MESSAGE_TOKEN_SAFERAD(15), 
		MESSAGE_TOKEN_POWSEEK(16), 
		MESSAGE_COUNT(17);
		
		private int code;
		
		private Types(int v) {
			code = v;
		}
		
		public int getCode() {
			return code;
		}
	};
	//constructor/functions
	public enum AnonymousEnum
	{
		MESSAGE_DEFAULT,
		MESSAGE_WILL_COLLIDE,
		MESSAGE_WONT_COLLIDE,
		MESSAGE_NO_ASTEROIDS,
		MESSAGE_NO_POWERUPS,
		MESSAGE_ASTEROID_FAR,
		MESSAGE_ASTEROID_NEAR,
		MESSAGE_POWERUP_NEAR,
		MESSAGE_POWERUP_FAR,
		MESSAGE_CHANGE_STATE,
		MESSAGE_SHIP_TOTAL_STOP,
		MESSAGE_TOKEN_PSCAN,
		MESSAGE_TOKEN_MAXSPEED,
		MESSAGE_TOKEN_APDIST,
		MESSAGE_TOKEN_ATDIST,
		MESSAGE_TOKEN_SAFERAD,
		MESSAGE_TOKEN_POWSEEK,
		MESSAGE_COUNT,
		REGISTER_ERROR_MESSAGE_NOT_IN_SYSTEM,
		REGISTER_ERROR_ALREADY_REGISTERED,
		REGISTER_MESSAGE_OK,
		MFSM_STATE_NONE,
		MFSM_STATE_APPROACH,
		MFSM_STATE_ATTACK,
		MFSM_STATE_EVADE,
		MFSM_STATE_GETPOWERUP,
		MFSM_STATE_IDLE,
	    MFSM_STATE_COUNT,
		MFSM_MACH_NONE,
		MFSM_MACH_MAINSHIP,
	    MFSM_MACH_COUNT
	}
	public enum MSGState {
		MFSM_STATE_NONE(0),
		MFSM_STATE_APPROACH(1),
		MFSM_STATE_ATTACK(2),
		MFSM_STATE_EVADE(3),
		MFSM_STATE_GETPOWERUP(4),
		MFSM_STATE_IDLE(5),
	    MFSM_STATE_COUNT(6),
		MFSM_MACH_NONE(7),
		MFSM_MACH_MAINSHIP(8),
	    MFSM_MACH_COUNT(9);
	    
	    private int code;
		
		private MSGState(int v) {
			code = v;
		}
	    
		public int getCode() {
			return code;
		}
	}

	public class MessageReg
	{
		public MessageReg()
		{
			m_objectID = -1;
			m_parent = null;
			m_callBack = null;
		}
		public int m_objectID;
		public Object m_parent;
		public dg_Callback m_callBack;
	}
	public class MessageType
	{
		public int m_typeID;
		public List<MessageReg> m_messageRegistrations;

		//debug data
		public String m_name;
	}
}
