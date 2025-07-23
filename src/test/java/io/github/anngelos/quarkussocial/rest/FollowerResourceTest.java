package io.github.anngelos.quarkussocial.rest;

import io.github.anngelos.quarkussocial.domain.model.User;
import io.github.anngelos.quarkussocial.domain.repository.UserRepository;
import io.github.anngelos.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

  @Inject
  UserRepository userRepository;
  Long userId;

  @BeforeEach
  @Transactional
  void setUp() {
    // default user
    var user = new User();
    user.setName("Carlos Villagrán");
    user.setAge(81);
    userRepository.persist(user);
    userId = user.getId();
  }

  @Test
  @DisplayName("should return 409 when follower Id is equals to user id")
  public void sameUserAsFollowerTest() {
    var body = new FollowerRequest();
    body.setFollowerId(userId);

    given()
            .contentType(ContentType.JSON)
            .body(body)
            .pathParam("userId", userId)
    .when()
            .put()
    .then()
            .statusCode(409)
            .body(Matchers.is("You can't follow yourself."));
  }

}