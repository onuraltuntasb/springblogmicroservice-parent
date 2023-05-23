package org.springblogmicroservice.dto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericMailTemplate {
    private String From;
    private String To;
    private String Subject;
    private String Text;
}
