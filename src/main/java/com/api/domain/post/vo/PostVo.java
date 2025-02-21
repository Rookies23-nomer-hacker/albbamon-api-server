package com.api.domain.post.vo;

import java.time.LocalDateTime;

public class PostVo {

    private Long id;
    private String title;
    private String contents;
    private String file;
    private LocalDateTime createDate;
    private String userName;

    // 📌 생성자 추가 (JPQL의 new 구문을 위한 생성자)
    public PostVo(Long id, String title, String contents, String file, LocalDateTime createDate, String userName) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.file = file;
        this.createDate = createDate;
        this.userName = userName;
    }

    // 📌 Getter 메서드
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getFile() {
        return file;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getUserName() {
        return userName;
    }
}
