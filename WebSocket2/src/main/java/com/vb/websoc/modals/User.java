package com.vb.websoc.modals;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@Data
public class User {
    @Id
    private String nickName;
    private String fullName;
    private Status status;
	
}
