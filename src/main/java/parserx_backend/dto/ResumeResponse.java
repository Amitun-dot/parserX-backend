package parserx_backend.dto;

public class ResumeResponse {


    private Long id;
    private String fileName;
    private String fileType;

    public ResumeResponse() {
    }

    public ResumeResponse(
            Long id,
            String fileName,
            String fileType
    ) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }


}
