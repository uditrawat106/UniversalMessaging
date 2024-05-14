import com.pcbsys.nirvana.client.*;
import com.pcbsys.nirvana.nAdminAPI.*;

public class UMAdminConnector {

    private nSession session;
    private nRealmNode realm;

    public UMAdminConnector(String rname) throws Exception {
        nSessionAttributes nsa = new nSessionAttributes(rname);
        this.session = nSessionFactory.create(nsa);
        this.session.init();

        this.realm = new nRealmNode(this.session.getAttributes());
    }

    public void fetchMetrics() throws Exception {
        // Retrieve and print some metrics
        //nChannelAttributes[] channels = realm.getChannels();
        int num = realm.getNoOfChannels();
        System.out.println("Number of channels: " + num);
        String name = realm.getName();
        System.out.println("Name of channels: " + name);
        int id = realm.getNoOfQueues();
        System.out.println("Id: " + id);

        /*for (nChannelAttributes channel : channels) {
            System.out.println("Channel Name: " + channel.getName());
           //  System.out.println("Channel ID: " + channel.getChannelID());
          //  System.out.println("Message Count: " + realm.getChannelStatistics(channel.getChannelID()).getMessageCount());
        }*/
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
            adminConnector.fetchMetrics();
            adminConnector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
