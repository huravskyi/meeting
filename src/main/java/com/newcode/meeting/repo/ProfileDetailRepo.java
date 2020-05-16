package com.newcode.meeting.repo;

import com.newcode.meeting.domain.ProfileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileDetailRepo extends JpaRepository<ProfileDetail, Long> {
}
