package com.example.accountalias.web.service;

import com.example.accountalias.domain.AccountAlias;
import com.example.accountalias.domain.User;
import com.example.accountalias.repository.AccountAliasRepository;
import com.example.accountalias.web.dto.AliasDtos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountAliasService {
    private final AccountAliasRepository aliasRepository;

    public AccountAliasService(AccountAliasRepository aliasRepository) {
        this.aliasRepository = aliasRepository;
    }

    @Transactional
    public AliasDtos.AliasResponse createAlias(User owner, AliasDtos.CreateAliasRequest request) {
        if (aliasRepository.existsByAlias(request.alias())) {
            throw new IllegalStateException("Alias already exists");
        }
        AccountAlias alias = new AccountAlias();
        alias.setAlias(request.alias());
        alias.setOwner(owner);
        alias.setActive(true);
        AccountAlias saved = aliasRepository.save(alias);
        return new AliasDtos.AliasResponse(saved.getId(), saved.getAlias(), saved.isActive());
    }

    @Transactional(readOnly = true)
    public List<AliasDtos.AliasResponse> listAliases(User owner) {
        return aliasRepository.findAllByOwner(owner).stream()
                .map(a -> new AliasDtos.AliasResponse(a.getId(), a.getAlias(), a.isActive()))
                .toList();
    }

    @Transactional
    public AliasDtos.AliasResponse updateStatus(User owner, Long aliasId, boolean active) {
        AccountAlias alias = aliasRepository.findByIdAndOwner(aliasId, owner).orElseThrow();
        alias.setActive(active);
        return new AliasDtos.AliasResponse(alias.getId(), alias.getAlias(), alias.isActive());
    }

    @Transactional
    public void delete(User owner, Long aliasId) {
        AccountAlias alias = aliasRepository.findByIdAndOwner(aliasId, owner).orElseThrow();
        aliasRepository.delete(alias);
    }
}

