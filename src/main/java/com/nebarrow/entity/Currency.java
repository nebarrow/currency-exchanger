package com.nebarrow.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Currency {
    private Long id;
    private String code;
    private String name;
    private String sign;
}
