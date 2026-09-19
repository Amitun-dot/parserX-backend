package parserx_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resumes")
public class Resume {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Resume() {
    }

    public Resume(
            String fileName,
            String filePath,
            String fileType,
            User user
    ) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public User getUser() {
        return user;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
