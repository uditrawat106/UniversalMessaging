import com.pcbsys.nirvana.client.*;

public class MessageConsumer {

    public static void main(String[] args) {
        String realmServerUrl = "nsp://localhost:9000"; // URL of the Universal Messaging realm server
        String channelName = "MyTopic"; // Name of the channel to consume messages from

        try {
            // Create session attributes
            nSessionAttributes sessionAttributes = new nSessionAttributes(realmServerUrl);

            // Create session
            nSession session = nSessionFactory.create(sessionAttributes);
            session.init();

            // Create channel attributes
            nChannelAttributes channelAttributes = new nChannelAttributes();
            channelAttributes.setName(channelName);

            // Find or create the channel
            nChannel channel = session.findChannel(channelAttributes);

            // Create event listener
            nEventListener listener = new nEventListener() {
                @Override
                public void go(nConsumeEvent event) {
                    String messageText = new String(event.getEventData());
                    System.out.println("Received message: " + messageText);
                }
            };

            // Subscribe to the channel
            channel.addSubscriber(listener);

            System.out.println("Subscribed to channel: " + channelName);


            // Keep the application running to continue receiving messages
            while (true) {
                Thread.sleep(1000); // Sleep for some time before checking for new messages
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
