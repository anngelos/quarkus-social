package io.github.anngelos.quarkussocial.domain.repository;

import io.github.anngelos.quarkussocial.domain.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
