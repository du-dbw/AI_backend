package com.example.backend.entity.Works.Tags;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long workId;

    // 默认构造方法
    public Tags() {
    }

    // 全参构造方法
    public Tags(String name, long workId) {
        this.name = name;
        this.workId = workId;
    }

    // Getter 和 Setter 方法
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }


    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workId=" + workId +
                "}\n";
    }
}