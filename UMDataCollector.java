import com.pcbsys.nirvana.client.*;
import com.pcbsys.nirvana.nAdminAPI.*;

import java.util.Scanner;

public class UMDataCollector {

    private nSession session;
    private nChannel myChannel;
    private nRealmNode realmNode;

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
       /* nca.setName(String.valueOf(myQueue));
        myQueue = session.findQueue(nca);*/
/*        nEventListener myListener = new nEventListener() {
            @Override
            public void go(nConsumeEvent event) {
                System.out.println("Message received: " + new String(event.getEventData()));
            }
        };

        myChannel.addSubscriber(myListener);
        System.out.println("Subscribed to channel: " + channelName);*/
    }

    // Method to create a queue for a given topic
    /*public void createQueueForTopic(String channelName, String queueName) {
        try {
            // Find the topic channel
            nChannelAttributes topicChannelAttributes = new nChannelAttributes();
            topicChannelAttributes.setName(channelName);
            nChannel topicChannel = session.findChannel(topicChannelAttributes);

            // Create queue attributes

            // Create the queue
            nQueue queue = adminRealm.createQueue(queueAttributes);
            System.out.println("Queue created successfully: " + queue.getName());
        } catch (nChannelAlreadyExistsException e) {
            System.err.println("Queue already exists: " + e.getMessage());
        } catch (nSecurityException | nSessionNotConnectedException | nChannelNotFoundException | nUnexpectedResponseException e) {
            System.err.println("Error while creating queue: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

    //send a message to the channel
    public void sendMessage(String message) throws nChannelNotFoundException, nSessionNotConnectedException, nSessionPausedException, nSecurityException, nIllegalArgumentException, nMaxBufferSizeExceededException {
        nEventProperties props = new nEventProperties();
        props.put("bondname", "bond1");
        props.put("price", 100.00);
        nConsumeEvent evt = new nConsumeEvent( "atag", props, message.getBytes());
        myChannel.publish(evt);
    }

    // Clean up resources
    public void close() throws nSessionNotConnectedException {
        if (session != null) {
            session.close();
            System.out.println("Disconnected from the realm.");
        }
    }

    public static void main(String[] args) {
        try {
            UMDataCollector collector = new UMDataCollector("nsp://localhost:9000", "MyTopic");

            Scanner scanner = new Scanner(System.in);
            String userInput;
            do {
                System.out.print("Enter message to publish (type 'exit' to quit): ");
                userInput = scanner.nextLine();

                if (!userInput.equalsIgnoreCase("exit")) {
                    collector.sendMessage(userInput);
                    System.out.println("Message published successfully.");
                }
            } while (!userInput.equalsIgnoreCase("exit"));

            collector.close();
            scanner.close();
            System.out.println("Closed the collector.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}