package ru.cft.team2.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cft.team2.chat.model.Attach;

@Repository
public interface AttachRepository extends JpaRepository<Attach, Integer> {
}
