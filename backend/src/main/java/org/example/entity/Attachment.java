package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachments")
@Setter
@Getter
@NoArgsConstructor
public class Attachment implements BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(nullable = false, length = 30)
    private String fileName;

    @Column(nullable = false, length = 10)
    private String fileType;

    @Column(nullable = false)
    private String url;

    public Attachment(Message message, String fileName, String fileType, String url) {
        this.message = message;
        this.fileName = fileName;
        this.fileType = fileType;
        this.url = url;
    }
}
