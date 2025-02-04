package grpc;

import com.models.Node;
import com.node.NodesRing;
import com.node.LocalToken;
import io.grpc.stub.StreamObserver;

import com.grpc.MessageHandlerGrpc.MessageHandlerImplBase;
import com.grpc.TokenMessageOuterClass.*;

public class MessageHandlerGrpcServer extends MessageHandlerImplBase {

    @Override
    public void handleToken(TokenMessage request,
                          StreamObserver<TokenResponse> responseObserver) {

        //System.out.println("New Token arrived from -->"+ request.getCurrentId());

        LocalToken.getInstance().setMeasurementsFromMessage(request.getMeasurementsMap());
        LocalToken.getInstance().setCurrentId(request.getNextId());



      //  System.out.println("Current ID setted --> "+ request.getNextId());

        TokenResponse response = TokenResponse.newBuilder().setStatus("done").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void addNode(NewNodeMessage request,
                            StreamObserver<NewNodeResponse> responseObserver) {

        System.out.println("Node registered --> "+ request);

        addToRing(request.getId(),request.getIP(),request.getPort());

        NewNodeResponse response = NewNodeResponse.newBuilder().setStatus(NodesRing.getInstance().getStatus()).build();

        synchronized (LocalToken.getInstance()) {
            LocalToken.getInstance().notifyAll();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();


    }

    @Override
    public void removeNode(RemoveNodeMessage request,
                            StreamObserver<RemoveNodeResponse> responseObserver) {

        System.out.println("Node removed --> " + request);

        removeFromRing(request.getId());

        RemoveNodeResponse response = RemoveNodeResponse.newBuilder().setStatus("done").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        synchronized (LocalToken.getInstance()) {
            LocalToken.getInstance().notifyAll();
        }

    }

    public synchronized void removeFromRing(String id){
        NodesRing.getInstance().removeNode(id);
    }

    public synchronized void addToRing(String id, String IP, int port){
        NodesRing.getInstance().addNode(new Node(id,IP,port));
    }
}
