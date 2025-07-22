package io.github.anngelos.quarkussocial.rest;

import io.github.anngelos.quarkussocial.domain.model.User;
import io.github.anngelos.quarkussocial.domain.repository.UserRepository;
import io.github.anngelos.quarkussocial.rest.dto.CreatePostRequest;
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
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

  @Inject
  UserRepository userRepository;
  Long userId;

  @BeforeEach
  @Transactional
  public void setUp() {
    var user = new User();
    user.setName("Carlos Villagrán");
    user.setAge(81);
    userRepository.persist(user);
    userId = user.getId();
  }

  @Test
  @DisplayName("should create a post for an user")
  public void createPostUser() {
    var post = new CreatePostRequest();
    post.setText("Anything!");

    given().contentType(ContentType.JSON).body(post).pathParam("userId", userId)
            .when().post()
            .then().statusCode(201);
  }

  @Test
  @DisplayName("should return 404 when trying to make a post for an inexistent user")
  public void postForAnInexistentUserTest() {
    var post = new CreatePostRequest();
    post.setText("Anything!");

    var inexistentUserId = 999;

    given().contentType(ContentType.JSON).body(post).pathParam("userId", inexistentUserId)
            .when().post()
            .then().statusCode(404);
  }

  @Test
  @DisplayName("should return 404 when user doesn't exist")
  public void listPostUserNotFoundTest() {
    var inexistentUserId = 999;
    given().contentType(ContentType.JSON).pathParam("userId", inexistentUserId)
            .when().get()
            .then().statusCode(404);
  }

  @Test
  @DisplayName("should return 400 when followerId header is not present")
  public void listPostFollowerHeaderNotSentTest() {
    given().pathParam("userId", userId)
            .when().get()
            .then().statusCode(400)
            .body(Matchers.is("You forgot the header: followerId."));
  }

  @Test
  @DisplayName("should return 400 when follower doesn't exist")
  public void listPostFollowerHeaderNotFoundTest() {
    var inexistentFollowerId = 999;
    given().pathParam("userId", userId).header("followerId", inexistentFollowerId)
            .when().get()
            .then().statusCode(400)
            .body(Matchers.is("Inexistent followerId."));
  }

  @Test
  @DisplayName("should return 403 when follower isn't follower")
  public void listPostNotAFollowerHeaderNotFoundTest() {}

  @Test
  @DisplayName("should return posts")
  public void listPostTest() {}
}