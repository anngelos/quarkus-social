package io.github.anngelos.quarkussocial.rest;

import io.github.anngelos.quarkussocial.domain.model.Follower;
import io.github.anngelos.quarkussocial.domain.model.User;
import io.github.anngelos.quarkussocial.domain.repository.FollowerRepository;
import io.github.anngelos.quarkussocial.domain.repository.UserRepository;
import io.github.anngelos.quarkussocial.rest.dto.FollowerRequest;
import io.github.anngelos.quarkussocial.rest.dto.FollowerResponse;
import io.github.anngelos.quarkussocial.rest.dto.FollowersPerUseResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

  private final FollowerRepository followerRepository;
  private final UserRepository userRepository;

  @Inject
  public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
    this.followerRepository = followerRepository;
    this.userRepository = userRepository;
  }

  @PUT
  @Transactional
  public Response followUser(FollowerRequest request, @PathParam("userId") Long userId) {
    if (userId.equals(request.getFollowerId())) {
      return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself.").build();
    }
    User user = userRepository.findById(userId);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    User follower = userRepository.findById(request.getFollowerId());
    boolean follows = followerRepository.follows(follower, user);
    if (!follows) {
      var entity = new Follower();
      entity.setUser(user);
      entity.setFollower(follower);
      followerRepository.persist(entity);
    }
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @GET
  public Response listFollowers(@PathParam("userId") Long userId) {
    User user = userRepository.findById(userId);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<Follower> list = followerRepository.findByUser(userId);
    FollowersPerUseResponse responseObject = new FollowersPerUseResponse();
    responseObject.setFollowerCount(list.size());
    List<FollowerResponse> followerList = list.stream().map(FollowerResponse::new).collect(Collectors.toList());
    responseObject.setContent(followerList);
    return Response.ok(responseObject).build();
  }

  @DELETE
  @Transactional
  public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
    User user = userRepository.findById(userId);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    followerRepository.deleteByFollowerAndUser(followerId, userId);
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
