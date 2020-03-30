package com.atlp.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@AllArgsConstructor // 标注全参构造器
@NoArgsConstructor // 标注无参构造器
@Data // 标注Setter、Getter方法
@Accessors(chain = true) // 标注链式风格
public class Employee {

	private Integer id;
    private String lastName;
    private String email;
    private Integer gender;
    private Department department;
    private Date birth;

}
