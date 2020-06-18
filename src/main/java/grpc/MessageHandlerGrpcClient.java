package grpc;

import com.gateway.Nodes;
import com.models.Node;
import com.node.NodesRing;
import com.node.LocalToken;
import com.node.StatsBuffer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;

import com.grpc.MessageHandlerGrpc;
import com.grpc.MessageHandlerGrpc.*;
import com.grpc.TokenMessageOuterClass.*;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerGrpcClient {

    public static void sendToken(String currentId, Server server) {


        final ManagedChannel channel = setTokenRequest(currentId);

        MessageHandlerStub stub = MessageHandlerGrpc.newStub(channel);

        TokenMessage request = TokenMessage.newBuilder().putAllMeasurements(LocalToken.getInstance().getMeasurementMessage()).
                setCurrentId(LocalToken.getInstance().getCurrentId()).setNextId(LocalToken.getInstance().getNextId()).build();
        //System.out.println("Sending token  --> " + LocalToken.getInstance().toString());

        stub.handleToken(request, new StreamObserver<TokenResponse>() {
            @Override
            public void onNext(TokenResponse tokenResponse) {
            //    System.out.println(tokenResponse.getStatus());
                if(!NodesRing.getInstance().getStatus().equals("exiting"))
                    NodesRing.getInstance().setStatus("elaborating");
                else{
                    MessageHandlerGrpcClient.removeNode(currentId, server);
                }
            }

            @Override
            public void onError(Throwable throwable) {
            //    System.out.println("Error on next token : " + throwable.getMessage());
                sendToken(NodesRing.getInstance().getNextNode(currentId).getId(), server);
            }

            @Override
            public void onCompleted() {
                channel.shutdownNow();
            }
        });

    }

    public static void addNode(String id, String IP, int port, Server server) {
        final List<String> responses = new ArrayList<>();
        for (Node n : NodesRing.getInstance().getNodes()) {
            if (!n.getId().equals(id)) {
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(n.getIP() + ":" + n.getPort()).usePlaintext(true).build();

                MessageHandlerStub stub = MessageHandlerGrpc.newStub(channel);

                NewNodeMessage request = NewNodeMessage.newBuilder().setId(id).setIP(IP).setPort(port).build();

                stub.addNode(request, new StreamObserver<NewNodeResponse>() {
                    @Override
                    public void onNext(NewNodeResponse newNodeResponse) {
                        System.out.println("Added on node: " + n.getId()+ " - With status: "+ newNodeResponse.getStatus());
                        responses.add(newNodeResponse.getStatus());
                        System.out.println("Nodes count: " + NodesRing.getInstance().getNodes().size());
                        if (responses.size() == NodesRing.getInstance().getNodes().size()-1 && !responses.contains("elaborating")) {
                            if(NodesRing.getInstance().getNodes().get(0).getId().equals(id)) {
                                NodesRing.getInstance().setStatus("elaborating");
                                sendToken(NodesRing.getInstance().getMyNode().getId(), server);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Impossible to add on " + n.getId());
                        NodesRing.getInstance().removeNode(n.getId());
                        System.out.println("Removed, Nodes count: " + NodesRing.getInstance().getNodes().size());
                        if(NodesRing.getInstance().getNodes().size() == 1){
                            synchronized (LocalToken.getInstance()) {
                                LocalToken.getInstance().notifyAll();
                            }
                        }
                        else if (responses.size() == NodesRing.getInstance().getNodes().size()-1 && !responses.contains("elaborating")) {
                            if(NodesRing.getInstance().getNodes().get(0).getId().equals(id)) {
                                NodesRing.getInstance().setStatus("elaborating");
                                sendToken(NodesRing.getInstance().getMyNode().getId(), server);
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {
                        channel.shutdownNow();
                    }
                });
            }
        }
    }

    public static void removeNode(String id, Server server) {
        List<Node> nodesCopy = NodesRing.getInstance().getNodes();
        final int[] responses = {0};
        if (nodesCopy.size() > 1) {
            for (Node n : nodesCopy) {
                if (!n.getId().equals(id)) {
                    System.out.println("Deleting on -->" + n.getId());
                    final ManagedChannel channel = ManagedChannelBuilder.forTarget(n.getIP() + ":" + n.getPort()).usePlaintext(true).build();

                    MessageHandlerStub stub = MessageHandlerGrpc.newStub(channel);

                    RemoveNodeMessage request = RemoveNodeMessage.newBuilder().setId(id).build();

                    stub.removeNode(request, new StreamObserver<RemoveNodeResponse>() {

                        @Override
                        public void onNext(RemoveNodeResponse removeNodeResponse) {
                            System.out.println("Done - deleted on node: " + n.getId());
                            responses[0]++;
                            if (responses[0] == nodesCopy.size() - 1) {
                                server.shutdownNow();
                                NodesRing.getInstance().setStatus("exiting");
                                System.out.println("Terminating process...");
                                synchronized (LocalToken.getInstance()) {
                                    LocalToken.getInstance().notifyAll();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            System.out.println("Impossible to remove on  " + n.getId());
                            responses[0]++;
                            if (responses[0] == nodesCopy.size() - 1) {
                                server.shutdownNow();
                                NodesRing.getInstance().setStatus("exiting");
                                System.out.println("Terminating process...");
                                synchronized (LocalToken.getInstance()) {
                                    LocalToken.getInstance().notifyAll();
                                }
                            }
                        }

                        @Override
                        public void onCompleted() {
                            channel.shutdownNow();
                        }
                    });
                }
            }
        } else {
            System.out.println("Done - deleted");
            server.shutdownNow();
            NodesRing.getInstance().setStatus("exiting");
            System.out.println("Terminating process...");
            synchronized (LocalToken.getInstance()) {
                LocalToken.getInstance().notifyAll();
            }
        }
    }

    static ManagedChannel setTokenRequest(String currentId) {
       // System.out.println("Setting next endpoint..");
        Node nextNode = NodesRing.getInstance().getNextNode(currentId);
        // System.out.println("NEXT NODE --> " +  nextNode.toString());
        LocalToken.getInstance().setNextIp(nextNode.getIP());
        LocalToken.getInstance().setNextPort(nextNode.getPort());
        LocalToken.getInstance().setNextId(nextNode.getId());
        return
        ManagedChannelBuilder.forTarget(LocalToken.getInstance().getNextIp() + ":" + LocalToken.getInstance().getNextPort()).usePlaintext(true).build();
    }


}
