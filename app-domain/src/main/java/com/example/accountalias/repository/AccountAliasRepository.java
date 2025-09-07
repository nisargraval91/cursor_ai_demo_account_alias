package com.example.accountalias.repository;

import com.example.accountalias.domain.AccountAlias;
import com.example.accountalias.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountAliasRepository extends JpaRepository<AccountAlias, Long> {
    Optional<AccountAlias> findByAlias(String alias);
    boolean existsByAlias(String alias);
    List<AccountAlias> findAllByOwner(User owner);
    Optional<AccountAlias> findByIdAndOwner(Long id, User owner);
}

