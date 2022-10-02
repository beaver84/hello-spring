package hello.hellospring.repository.jpa;

import hello.hellospring.domain.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<TbUser, Long> {
}
