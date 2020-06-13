package grpc;

import com.node.Token;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import com.grpc.MessageHandlerGrpc;
import com.grpc.MessageHandlerGrpc.*;
import com.grpc.TokenMessageOuterClass.*;

public class MessageHandlerGrpcClient {

    public static void sendToken(){

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(Token.getInstance().getNextIp()+":"+Token.getInstance().getNextPort()).usePlaintext(true).build();

        MessageHandlerStub stub = MessageHandlerGrpc.newStub(channel);

        TokenMessage request = TokenMessage.newBuilder().putAllMeasurements(Token.getInstance().getMeasurementMessage()).build();

        stub.handleToken(request, new StreamObserver<TokenResponse>() {
            @Override
            public void onNext(TokenResponse tokenResponse) {
                System.out.println(tokenResponse.getStatus());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error on token" + throwable.getMessage());
                //TODO: se il nodo a cui mando ritorna errore, passo al nodo successivo
            }

            @Override
            public void onCompleted() {
                channel.shutdownNow();
            }
        });

    }

    public static void addNode(String id, String IP, int port){

        //TODO: targets??
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("").usePlaintext(true).build();

        MessageHandlerStub stub = MessageHandlerGrpc.newStub(channel);

        NewNodeMessage request = NewNodeMessage.newBuilder().setId(id).setIP(IP).setPort(port).build();

        stub.addNode(request, new StreamObserver<NewNodeResponse>() {
            @Override
            public void onNext(NewNodeResponse newNodeResponse) {
                System.out.println(newNodeResponse.getStatus());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error on token" + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                channel.shutdownNow();
            }
        });

    }

    public static void removeNode(String id){

        //TODO: targets??
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("").usePlaintext(true).build();

        MessageHandlerStub stub = MessageHandlerGrpc.newStub(channel);

        RemoveNodeMessage request = RemoveNodeMessage.newBuilder().setId(id).build();

        stub.removeNode(request, new StreamObserver<RemoveNodeResponse>() {

            @Override
            public void onNext(RemoveNodeResponse removeNodeResponse) {
                System.out.println(removeNodeResponse.getStatus());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error on token" + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                channel.shutdownNow();
            }
        });

    }
}
