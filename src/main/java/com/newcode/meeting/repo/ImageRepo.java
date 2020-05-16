package com.newcode.meeting.repo;

import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {

    Set<Image> findImageByUserId(Long id);

    Page<Image> findByTested(Boolean tested, Pageable pageable);

    List<Image> findImageByUser(User user);

    Image findByMainAndUser_Id(boolean main, Long id);
}
