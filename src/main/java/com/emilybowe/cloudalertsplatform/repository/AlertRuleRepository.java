package com.emilybowe.cloudalertsplatform.repository;

import com.emilybowe.cloudalertsplatform.domain.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertRuleRepository extends JpaRepository<AlertRule, UUID> {
}
