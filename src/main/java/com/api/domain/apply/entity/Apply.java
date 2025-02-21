
package com.api.domain.apply.entity;

import com.api.domain.apply.type.ApplyStatus;
import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.resume.entity.Resume;
import com.api.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "apply")
@Entity
public class Apply extends BaseTimeEntity {
    @Id
    @Column(name = "apply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "recruitment_id", referencedColumnName = "recruitment_id")
    private Recruitment recruitment;
    
    @ManyToOne
    @JoinColumn(name = "resume_id", referencedColumnName = "resume_id")
    private Resume resume;

    @Builder.Default
    @Column(name = "status")
    private ApplyStatus status = ApplyStatus.WAITING;

    public static Apply createApply(Recruitment recruitment, Resume resume) {
        return Apply.builder()
                .recruitment(recruitment)
                .resume(resume)
                .build();
    }
}
