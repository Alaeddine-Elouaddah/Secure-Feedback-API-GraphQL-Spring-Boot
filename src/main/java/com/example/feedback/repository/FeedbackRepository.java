package com.example.feedback.repository;

import com.example.feedback.domain.Feedback;
import com.example.feedback.domain.FeedbackCategory;
import com.example.feedback.domain.FeedbackStatus;
import com.example.feedback.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByAuthor(User author, Pageable pageable);

    @Query("""
            SELECT f FROM Feedback f
            WHERE (:status IS NULL OR f.status = :status)
              AND (:category IS NULL OR f.category = :category)
              AND (
                    :searchText IS NULL
                    OR LOWER(f.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
                    OR LOWER(f.message) LIKE LOWER(CONCAT('%', :searchText, '%'))
                  )
            """)
    Page<Feedback> search(
            @Param("status") FeedbackStatus status,
            @Param("category") FeedbackCategory category,
            @Param("searchText") String searchText,
            Pageable pageable);

    @Query("""
            SELECT f FROM Feedback f
            WHERE f.author = :author
              AND (:status IS NULL OR f.status = :status)
              AND (:category IS NULL OR f.category = :category)
              AND (
                    :searchText IS NULL
                    OR LOWER(f.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
                    OR LOWER(f.message) LIKE LOWER(CONCAT('%', :searchText, '%'))
                  )
            """)
    Page<Feedback> searchByAuthor(
            @Param("author") User author,
            @Param("status") FeedbackStatus status,
            @Param("category") FeedbackCategory category,
            @Param("searchText") String searchText,
            Pageable pageable);
}

