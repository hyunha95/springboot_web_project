package org.zerock.ex2_practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2_practice.entity.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long> {

}
