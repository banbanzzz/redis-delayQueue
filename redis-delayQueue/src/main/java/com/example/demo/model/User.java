package com.example.demo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wxj
 * @date : 2019/11/5 17:09
 **/
@Data
public class User implements Serializable {

    private Long id;

    private String namme;
}
