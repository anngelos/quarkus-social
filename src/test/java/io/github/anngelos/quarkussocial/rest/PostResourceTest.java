package io.github.anngelos.quarkussocial.rest;

import io.github.anngelos.quarkussocial.domain.model.User;
import io.github.anngelos.quarkussocial.domain.repository.UserRepository;
import io.github.anngelos.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    var userId = 1;

    given().contentType(ContentType.JSON).body(post).pathParam("userId", userId)
            .when().post()
            .then().statusCode(201);
  }
}