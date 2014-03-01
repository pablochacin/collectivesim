package collectivesim.network;

import collectivesim.events.AbstractEvent;
import collectivesim.model.Model;
import collectivesim.model.ModelAgent;

/**
 * An Event related to the maintenance of a Topology
 * 
 * @author Pablo Chacin
 *
 */
public class NetworkEvent extends AbstractEvent {

	public static String  NETWORK_JOIN= "network.join";
    public static String  NETWORK_LEAVE= "network.leave";
	public static String  NETWORK_LINK = "network.link";
    public static String  NETWORK_UNLINK = "network.unlink";
    

    protected ModelAgent target;
    /**
     * @param Model
     * @param ModeAgent
     * @param type
     * @param data
     */
    public NetworkEvent(Model model,ModelAgent agent, long timeStamp,String type,ModelAgent target) {

        super(model,agent,timeStamp,type);
    	this.target = target;
 
    }

    public NetworkEvent(Model model,ModelAgent agent, String type,long timeStamp) {
        this(model,agent,timeStamp,type,null);
    }

    public ModelAgent getTarget() {
		return target;
	}

}
