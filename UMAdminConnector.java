import com.pcbsys.nirvana.client.*;
import com.pcbsys.nirvana.nAdminAPI.*;

public class UMAdminConnector {

    private nSession session;
    private nRealmNode realm;
   // private nAdminRealmNode adminRealm;

    public UMAdminConnector(String rname) throws Exception {
        nSessionAttributes nsa = new nSessionAttributes(rname);
        this.session = nSessionFactory.create(nsa);
        this.session.init();
        this.realm = new nRealmNode(this.session.getAttributes());
    }

    public void fetchChannelMetrics() throws Exception {
        // Retrieve and print some metrics
        //nChannelAttributes[] channels = realm.getChannels();
        int num = realm.getNoOfChannels();
        System.out.println("Number of channels: " + num);
        String name = realm.getName();
        System.out.println("Name of channels: " + name);
        int id = realm.getNoOfQueues();
        System.out.println("Id: " + id);
    }

    public void fetchQueueMetrics(String queueName) throws Exception {
        // Retrieve and print some metrics
        int num = realm.getNoOfChannels();
        String channelType = new String();
        try{
            nChannelAttributes cattrib = new nChannelAttributes();
            System.out.println("Name of channel: " + cattrib.getName());
            cattrib.setName(queueName);
            nQueue nq = session.findQueue(cattrib);
            nQueueDetails nqd = nq.getDetails();
            System.out.println("First Event time: " + nqd.getFirstEventTime());

            nNode found = realm.findNode(queueName);
            nLeafNode nlf = (nLeafNode)found;

            cattrib =nlf.getAttributes();

            int type = cattrib.getType();
            if(type == 1){
                channelType="Reliable";
            }else if (type == 2){
                channelType="Persistent";
            }else if (type == 3){
                channelType= "Mixed";
            }else if (type == 4){
                channelType= "Simple";
            }else if (type==5){
                channelType= "Transient";
            }else if (type==7){
                channelType= "Off Heap";
            }else if (type == 8){
                channelType= "Paged";
            }

            System.out.println("ClientID: " + queueName);
            System.out.println("QueueLength: " + nqd.getNoOfEvents());
            System.out.println(nqd.getTotalMemorySize());


        }catch (Exception e){

            System.out.println("Catch : " + e.getMessage());
        }
    }

    public void close() throws nSessionNotConnectedException {
        if (session != null) {
            session.close();
            System.out.println("Disconnected from the realm.");
        }
    }

    public static void main(String[] args) {
        try {
            UMAdminConnector adminConnector = new UMAdminConnector("nsp://localhost:9000");
            String queueName = "PurchaseOrder";
            adminConnector.fetchChannelMetrics();
            adminConnector.fetchQueueMetrics(queueName);
            adminConnector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
