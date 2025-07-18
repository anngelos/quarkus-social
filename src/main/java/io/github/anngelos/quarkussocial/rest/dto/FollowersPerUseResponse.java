package io.github.anngelos.quarkussocial.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FollowersPerUseResponse {
  private Integer followerCount;
  private List<FollowerResponse> content;
}
