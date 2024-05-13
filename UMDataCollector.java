import com.pcbsys.nirvana.client.*;

public class UMDataCollector {

    private nSession session;
    private nChannel myChannel;

    // Constructor
    public UMDataCollector(String rname, String channelName) throws Exception {
        connect(rname);
        subscribeToChannel(channelName);
    }

    // Connect to UM Realm
    private void connect(String rname) throws nIllegalArgumentException, nRealmUnreachableException, nSessionNotConnectedException, nSecurityException, nSessionAlreadyInitialisedException {
        nSessionAttributes nsa = new nSessionAttributes(rname);
        session = nSessionFactory.create(nsa);
        session.init();
        System.out.println("Connected to the realm successfully.");
    }

    // Subscribe to a channel
    private void subscribeToChannel(String channelName) throws nIllegalArgumentException, nChannelNotFoundException, nSecurityException, nSessionNotConnectedException, nUnexpectedResponseException, nIllegalChannelMode, nSessionPausedException, nUnknownRemoteRealmException, nRequestTimedOutException, nChannelAlreadySubscribedException {
        nChannelAttributes nca = new nChannelAttributes();
        nca.setName(channelName);
        myChannel = session.findChannel(nca);

        nEventListener myListener = new nEventListener() {
            @Override
            public void go(nConsumeEvent event) {
                System.out.println("Message received: " + new String(event.getEventData()));
            }
        };

        myChannel.addSubscriber(myListener);
        System.out.println("Subscribed to channel: " + channelName);
    }

    //send a message to the channel
    public void sendMessage(String message) throws nChannelNotFoundException, nSessionNotConnectedException, nSessionPausedException, nSecurityException, nIllegalArgumentException, nMaxBufferSizeExceededException {
        nEventProperties props = new nEventProperties();
        props.put("bondname", "bond1");
        props.put("price", 100.00);
        nConsumeEvent evt = new nConsumeEvent( "atag", props, "Messagefunction()".getBytes());
        myChannel.publish(evt);
    }

    // Clean up resources
    public void close() throws nSessionNotConnectedException {
        if (session != null) {
            session.close();
            System.out.println("Disconnected from the realm.");
        }
    }

   /* public static void main(String[] args) {
        try {
            UMDataCollector collector = new UMDataCollector("nsp://localhost:9000", "MyTopic");

            // Send multiple sample messages
            for (int i = 0; i < 5; i++) {
                collector.sendMessage("Message " + (i + 1)); // Custom message content
                System.out.println("Message " + (i + 1) + " published successfully.");
            }

            System.out.println("Press enter to exit...");
            System.in.read();
            collector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public static void main(String[] args) {
        try {
            UMDataCollector collector = new UMDataCollector("nsp://localhost:9000", "MyTopic");
            // Send a sample message
            collector.sendMessage("Hello, Universal Messaging!");
            System.out.println("Message published successfully.");
            System.out.println("Press enter to exit...");
            System.in.read();
            collector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}