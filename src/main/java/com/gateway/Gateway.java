package com.gateway;

import com.google.gson.Gson;
import com.models.Node;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("gateway")
public class Gateway {

    final Gson gson = new Gson();

    @POST
    @Path("/nodes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putNode(String message) {

        Node toAdd = gson.fromJson(message, Node.class);
        if (toAdd != null) {
            if (Nodes.getInstance().add(toAdd)) {
                String response = "{ \"status\" : \"done\", \"newNode\" : \"" + toAdd.getId() + "\", \"Nodes\" : " + Nodes.getInstance().getNodesList().toString() + " }";
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.PRECONDITION_FAILED).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/nodes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteNode(@HeaderParam("id") String id) {

        if (Nodes.getInstance().remove(id)) {
            String response = "{ \"status\" : \"done\", \"removed\" : \"" + id + "\",\"Nodes\" : \"" + Nodes.getInstance().getNodesList().toString() + "\"  }";
            return Response.ok(response).build();
        } else
            return Response.status(Response.Status.BAD_REQUEST).build();

    }


    @GET
    @Path("/nodes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNode(@HeaderParam("id") String id) {
        String response = "";
        Node n = Nodes.getInstance().getById(id);
        if (n != null)
            response = gson.toJson(n);
        if (response != "") {
            return Response.ok(response).build();
        } else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/analist/nodes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response totalNodes() {

          String  response = "{ \"status\" : \"done\",\"totalNodes\" : \"" + Nodes.getInstance().getNodesList().size() + "\"  }";
            return Response.ok(response).build();

    }

    @GET
    @Path("/analist/laststats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lastStats(@HeaderParam("n") int n) {

        String  response = "{ \"status\" : \"done\",\"totalNodes\" : \"" + Stats.getInstance().lastStats(n).toString() + "\"  }";
        return Response.ok(response).build();

    }
}
