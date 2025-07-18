package io.github.anngelos.quarkussocial.rest;

import io.github.anngelos.quarkussocial.domain.model.Follower;
import io.github.anngelos.quarkussocial.domain.model.User;
import io.github.anngelos.quarkussocial.domain.repository.FollowerRepository;
import io.github.anngelos.quarkussocial.domain.repository.UserRepository;
import io.github.anngelos.quarkussocial.rest.dto.FollowerRequest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
}
