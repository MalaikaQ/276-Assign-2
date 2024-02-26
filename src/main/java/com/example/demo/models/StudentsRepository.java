package com.example.demo.models;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface StudentsRepository extends JpaRepository<Students,Long> {
    List<Students> findByUid(Long uid);
    List<Students> deleteByUid(Long uid);

}


